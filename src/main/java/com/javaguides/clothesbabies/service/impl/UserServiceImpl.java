package com.javaguides.clothesbabies.service.impl;

import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.dto.enums.RoleEnum;
import com.javaguides.clothesbabies.dto.enums.Status;
import com.javaguides.clothesbabies.model.Role;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.repository.UserRepository;
import com.javaguides.clothesbabies.service.PropertyService;
import com.javaguides.clothesbabies.service.UserService;
import com.javaguides.clothesbabies.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    GlobalConfigurationServiceImpl globalConfigurationServiceImpl;

    @Autowired
    PropertyService propertyService;

    @Autowired
    MailUtil mailUtil;

    @Autowired
    Util util;

    @Override
    public Page<User> getCustomerUserByList(int pageNumber, int size) {
        Pageable paging = PageRequest.of(pageNumber - 1, size);
        return this.userRepository.findAll(paging);
    }

    @Override
    public User findById(Long id) {
        Optional<User> optUser = this.userRepository.findById(id);
        User user = new User();
        if (optUser.isPresent()) {
            user = optUser.get();
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void createUser(UserDto userDto) {

        String[] passwords = util.generateRandomString();
        String hashPassword = passwords[0];
        String tempPassword = passwords[1];

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setCreateDate(new Date());
        user.setStatus(Status.ACTIVE.name());
        user.setTmp_pwd_flag(1);
        if (StringUtils.hasText(userDto.getPhone())) user.setPhone(userDto.getPhone());
        user.setRole(new Role(StringUtils.hasText(userDto.getRole()) ? userDto.getRole() : RoleEnum.CUSTOMER.name()));
        user.setPassword(hashPassword);
        user = this.userRepository.save(user);
        if (user != null) {
            List<String> codes = new ArrayList<>();
            codes.add(GlobalConfigCodes.PROFILE_ACTIVATION_MES);
            codes.add(GlobalConfigCodes.PROFILE_ACTIVATION_SUBJECT);
            this.sendEmail(codes, user.getEmail(), tempPassword);
        }
    }

    @Override
    public void updateUser(UserDto userDto) {
        Optional<User> userOtp = this.userRepository.findById(userDto.getId());
        if (userOtp.isPresent()) {
            User user = userOtp.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            if (StringUtils.hasText(userDto.getPhone())) user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            user.setRole(new Role(userDto.getRole()));
            user.setUpdateDate(new Date());
            this.userRepository.save(user);
        } else {
            throw new NoSuchElementException("User with Email: " + userDto.getEmail() + " existn't");
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userOtp = this.userRepository.findById(id);
        if (!userOtp.isPresent()){
            throw new NoSuchElementException("User with id: " + id + " existn't");
        } else {
            User user = userOtp.get();
            this.userRepository.delete(user);
        }
    }

    @Override
    public void resetPinUser(Long id) {
        Optional<User> userOtp = this.userRepository.findById(id);
        if (!userOtp.isPresent()){
            throw new NoSuchElementException("User with id: " + id + " existn't");
        } else {
            User user = userOtp.get();
            String[] passwords = util.generateRandomString();
            String hashPassword = passwords[0];
            String tempPassword = passwords[1];
            user.setPassword(hashPassword);
            user.setTmp_pwd_flag(1);
            user.setUpdateDate(new Date());
            user = this.userRepository.save(user);
            if (user != null) {
                List<String> codes = new ArrayList<>();
                codes.add(GlobalConfigCodes.RESET_PASSWORD_MES);
                codes.add(GlobalConfigCodes.RESET_PASSWORD_SUBJECT);
                this.sendEmail(codes, user.getEmail(), tempPassword);
            }
        }
    }

    @Override
    public void changePinUser(UserDto userDto) {
        Optional<User> userOtp = this.userRepository.findById(userDto.getId());
        if (!userOtp.isPresent()){
            throw new NoSuchElementException("User with id: " + userDto.getId() + " existn't");
        } else {
            User user = userOtp.get();
            user.setPassword(new BCryptPasswordEncoder().encode(userDto.getNewPin()));
            user.setTmp_pwd_flag(0);
            user.setUpdateDate(new Date());
            this.userRepository.save(user);
        }
    }

    @Override
    public void forgotPasswordUser(UserDto userDto) {
        User user = this.userRepository.findByPhoneOrEmail(userDto.getPhone(), userDto.getEmail());
        if (user == null) {
            throw new NoSuchElementException("User existn't");
        } else {
            String[] passwords = util.generateRandomString();
            String hashPassword = passwords[0];
            String tempPassword = passwords[1];
            user.setPassword(hashPassword);
            user.setTmp_pwd_flag(1);
            user.setUpdateDate(new Date());
            user = this.userRepository.save(user);
            if (user != null) {
                List<String> codes = new ArrayList<>();
                codes.add(GlobalConfigCodes.RESET_PASSWORD_MES);
                codes.add(GlobalConfigCodes.RESET_PASSWORD_SUBJECT);
                this.sendEmail(codes, user.getEmail(), tempPassword);
            }
        }
    }

    @Override
    public String uploadImageUser(MultipartFile uploadFile) {
        String url = "";
        List<String> codes = new ArrayList<>();
        codes.add(GlobalConfigCodes.SFTP_HOST);
        codes.add(GlobalConfigCodes.SFTP_USER_NAME);
        codes.add(GlobalConfigCodes.SFTP_PASSWORD);
        codes.add(GlobalConfigCodes.SFTP_PORT);
        codes.add(GlobalConfigCodes.BASE_PATH);
        codes.add(GlobalConfigCodes.UPLOAD_HOST);
        codes.add(GlobalConfigCodes.IMAGE_PATH);
        String sftpHost;
        String sftpUsername;
        String sftpPassword;
        String basePath;
        String uploadHost;
        String imagePath;
        int sftpPort;
        try {
            Map<String, String> mapConfig = this.globalConfigurationServiceImpl.getConfigByListCode(codes);
            sftpHost = mapConfig.get(GlobalConfigCodes.SFTP_HOST);
            sftpUsername = mapConfig.get(GlobalConfigCodes.SFTP_USER_NAME);
            sftpPassword = mapConfig.get(GlobalConfigCodes.SFTP_PASSWORD);
            sftpPort = Integer.valueOf(mapConfig.get(GlobalConfigCodes.SFTP_PORT));
            basePath = mapConfig.get(GlobalConfigCodes.BASE_PATH);
            uploadHost = mapConfig.get(GlobalConfigCodes.UPLOAD_HOST);
            imagePath = mapConfig.get(GlobalConfigCodes.IMAGE_PATH);
            SftpModel model = new SftpModel(sftpHost, sftpUsername, sftpPassword, sftpPort);
            url = SFTPUtils.uploadSingleMultipartFile(model, uploadFile, basePath, uploadHost, imagePath);
        } catch (Exception ex) {
            LOGGER.error("Get config api error", ex);
        }
        return url;
    }

    private void sendEmail(List<String> codes, String email, String tempPassword){
        Map<String, String> mapConfig = this.globalConfigurationServiceImpl.getConfigByListCode(codes);
        String msgText = mapConfig.get(codes.get(0));
        String subText = mapConfig.get(codes.get(1));
        if (StringUtils.hasText(msgText)) {
            String paramsEmail[] = { tempPassword };
            String messageReset = Util.getMessageString(msgText, paramsEmail);
            mailUtil.sendEmail(email, subText, messageReset);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), this.mapRolesToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        Collection<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles.stream().map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
    }
}
