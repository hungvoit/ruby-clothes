package com.javaguides.clothesbabies.util;

import com.jcraft.jsch.*;
import com.luciad.imageio.webp.WebPWriteParam;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class SFTPUtils {
    private static Logger Logger = LoggerFactory.getLogger(SFTPUtils.class.getName());
    private Session session = null;
    private SftpModel model;

    private SFTPUtils(SftpModel model) {
        this.model = model;
    }

    private void connect() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(model.getUsername(), model.getHost(), model.getPort());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(model.getPassword());
        session.connect();
    }

    private void upload(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.setFilenameEncoding("UTF-8");
        sftpChannel.put(source, destination);
        sftpChannel.exit();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    private void disconnect() {
        if (session != null) session.disconnect();
    }

    public static String uploadSingleMultipartFile(SftpModel model, MultipartFile uploadFile, String basePath, String uploadHost, String imagePath) throws Exception {
        String url = "";
        SFTPUtils utils = new SFTPUtils(model);
        try {
            if (uploadFile == null) throw new Exception("Unable to find uploaded file.");
            utils.connect();
            ResizedImage resizedImage = imageCommon(utils, uploadFile, basePath, uploadHost, imagePath);
            url = resizedImage.getOrigin();
            utils.disconnect();
        } catch (Exception e) {
            utils.disconnect();
            e.printStackTrace();
            Logger.error("UploadSingleMultipartFile-exception:" + e);
            throw new Exception(e.getMessage());
        }
        return url;
    }

    private static Boolean isJPEG(File filename) throws IOException {
        try (DataInputStream ins = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            return ins.readInt() == 0xffd8ffe0;
        }
    }

    private static ResizedImage imageCommon(SFTPUtils utils, MultipartFile uploadFile, String basePath, String uploadHost, String productListingPath) {
        ResizedImage url = new ResizedImage();
        try {
            if (uploadFile == null) throw new Exception("Unable to find uploaded file.");
            File file = convert(uploadFile);
            Boolean isJPEGImage = isJPEG(file);
            String originalFileName = uploadFile.getOriginalFilename();
            boolean isImage = StringUtils.hasText(uploadFile.getContentType()) && uploadFile.getContentType().contains("image") &&
                    ((uploadFile.getContentType().toLowerCase().contains("jpg") && isJPEGImage) ||
                            uploadFile.getContentType().toLowerCase().contains("png") ||
                            (uploadFile.getContentType().toLowerCase().contains("jpeg") && isJPEGImage));
            if (isImage) {
                String newName = generateRandom6Digits() + "_original_" + uploadFile.getOriginalFilename();
                File originalFile = new File(newName);
                FileOutputStream fos = new FileOutputStream(originalFile);
                fos.write(uploadFile.getBytes());
                fos.close();
                try {
                    newName = newName.substring(0, newName.lastIndexOf(".")) + ".webp";
                    File target = new File(newName);
                    String smallImage = newName.replaceAll("_original_", "_thumbnail_");
                    try {
                        convertToWebp(originalFile, target, uploadFile.getContentType());
                        utils.upload(target.getName(), basePath + productListingPath + newName);
                        url.setOrigin(uploadHost + productListingPath + newName);
                        Thumbnails.of(originalFile)
                                .size(500, 500)
                                .toFile(originalFile);

                        utils.upload(originalFile.getName(), basePath + productListingPath + smallImage);
                        url.setSmall(uploadHost + productListingPath + smallImage);
                    } catch (Exception e) {
                        Logger.error("Convert to webp image fail", e);
                        String newName1 = generateRandom6Digits() + uploadFile.getOriginalFilename();
                        utils.upload(originalFileName, basePath + productListingPath + newName1);
                        url.setOrigin(uploadHost + productListingPath + newName1);
                        url.setSmall(uploadHost + productListingPath + newName1);
                    }
                    target.delete();
                    originalFile.delete();
                } catch (Exception e) {
                    Logger.error("Failes:" + e);
                }
            } else {
                String newName = generateRandom6Digits() + uploadFile.getOriginalFilename();
                utils.upload(originalFileName, basePath + productListingPath + newName);
                url.setOrigin(uploadHost + productListingPath + newName);
                url.setSmall(uploadHost + productListingPath + newName);
            }

            try {
                file.delete();
            } catch (Exception e) {
                Logger.error("Failes:" + e);
            }
        } catch (Exception e) {
            Logger.error("Upload image fail!" + e);
        }
        return url;
    }

    private static File convert(MultipartFile file) {
        File writeFile = null;
        try {
            writeFile = new File(file.getOriginalFilename());
            writeFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(writeFile);
            fos.write(file.getBytes());
            fos.close();

        } catch (Exception e) {
            Logger.error("Exception :  " + e);
        }
        return writeFile;
    }

    private static void convertToWebp(File file, File target, String type) {
        if (type.toLowerCase().contains("jpg") || type.toLowerCase().contains("png") || type.toLowerCase().contains("jpeg")) {
            try {
                int size = 2200;
                Thumbnails.of(file)
                        .scale(1)
                        .addFilter(new NoScaleUpResizer(size, size))
                        .toFile(file);
                BufferedImage image = Thumbnails.of(file)
                        .scale(1)
                        .asBufferedImage();
                ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
                WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
                writer.setOutput(new FileImageOutputStream(target));
                writer.write(null, new IIOImage(image, null, null), writeParam);
            } catch (IOException e) {
                Logger.info("Convert to webp fail", e);
            }
        }
    }

    private static String generateRandom6Digits() {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return "" + n;
    }
}
