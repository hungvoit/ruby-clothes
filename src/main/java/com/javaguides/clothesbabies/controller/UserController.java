package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.Rest;
import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.dto.enums.RoleEnum;
import com.javaguides.clothesbabies.dto.interfaces.IChangePasswordGroup;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.service.PropertyService;
import com.javaguides.clothesbabies.service.UserService;
import com.javaguides.clothesbabies.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PropertyService propertyService;

    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;

    @GetMapping(value= URI.USERS + "/showChangePassword")
    public String showChangePassword(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/";
        }
        UserDto userDto = new UserDto();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String email = userDetail.getUsername();
        User user = this.userService.findByEmail(email);
        if (user != null) {
            userDto.setId(user.getId());
            userDto.setRole(user.getRole().getName());
        }
        model.addAttribute("user", userDto);
        return "user/change_password";
    }

    @GetMapping(value= URI.USERS + "/showForgotPassword")
    public String showForgotPassword(Model model){
        model.addAttribute("user", new UserDto());
        return "user/forgot_password";
    }

    @PostMapping(value= URI.API + URI.USERS + "/changePassword")
    public String changePassword(@Validated(IChangePasswordGroup.class) @ModelAttribute(name = "user") UserDto userDto,
                                 BindingResult result) {
        String redirectUrl;
        result = this.validationService.validateUser(userDto, result, "user", false);
        if (result.hasErrors()) {
            return "user/change_password";
        }
        this.userService.changePinUser(userDto);
        if (RoleEnum.ADMIN.getName().equalsIgnoreCase(userDto.getRole())) {
            redirectUrl = URI.REDIRECT + "/dashboard";
        } else {
            redirectUrl = URI.REDIRECT + "/home";
        }
        return redirectUrl;
    }

    @PostMapping(value= URI.API + URI.USERS + "/forgetPassword")
    public String forgetPassword(@ModelAttribute(name = "user") UserDto userDto,
                                 BindingResult result) {
        result = this.validationService.validateForgotPassword(userDto, result);
        if (result.hasErrors()) {
            return "user/forgot_password";
        }
        this.userService.forgotPasswordUser(userDto);
        return URI.REDIRECT + URI.USERS + "/showForgotPassword?success";
    }

    @GetMapping(value= URI.ADMIN + URI.USERS)
    public String listUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return findCustomerUsersPaginated(1,  model);
    }

    @GetMapping(value = URI.ADMIN + URI.USERS + "/page/{pageNo}")
    public String findCustomerUsersPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        List<User> lstUsers = null;
        Page<User> pageUsers = this.userService.getCustomerUserByList(pageNo, pageSize);
        if (pageUsers != null && pageUsers.getContent().size() > 0) {
            lstUsers = pageUsers.getContent();
            model.addAttribute("totalPages", pageUsers.getTotalPages());
            model.addAttribute("totalItems", pageUsers.getTotalElements());
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("lstCustomerUsers", lstUsers);
        return "user" + URI.LIST;
    }

    @GetMapping(value = URI.ADMIN + URI.USERS + "/showUserForm")
    public String showUserForm(@RequestParam(required = false, defaultValue = "0") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        UserDto userDto =  new UserDto();
        if (id != null && id > 0L) {
            User user = this.userService.findById(id);
            BeanUtils.copyProperties(user, userDto);
            userDto.setRole(user.getRole().getName());
        }
        model.addAttribute("user", userDto);
        return "user/create";
    }

    @PostMapping(URI.API + URI.ADMIN + URI.USERS)
    public String saveUser(@ModelAttribute(name = "user") @Valid UserDto userDto,
                           BindingResult result, RedirectAttributes redirectAttributes) {
        String content = "";
        result = this.validationService.validateUser(userDto, result, "user", false);
        if (result.hasErrors()) {
            return "user/create";
        }
        if (userDto.getId() != null) {
            this.userService.updateUser(userDto);
            content = "update user";
        } else {
            this.userService.createUser(userDto);
            content = "create new user";
        }
        redirectAttributes.addFlashAttribute("message",
                this.propertyService.getMessage("message.successfully").replace("${content}", content));
        return URI.REDIRECT + URI.ADMIN + URI.USERS;
    }

    @PostMapping(value= URI.API + URI.ADMIN + URI.USERS + "/resetPassword" + URI.ID)
    public String resetPassword(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        this.userService.resetPinUser(id);
        redirectAttributes.addFlashAttribute("message",
                this.propertyService.getMessage("message.successfully").replace("${content}", "reset pin user"));
        return URI.REDIRECT + URI.ADMIN + URI.USERS;
    }

    @PostMapping(value = URI.API + URI.USERS + "/user-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody String uploadUserImage(@RequestParam("uploadFile")  MultipartFile uploadFile) {
        return this.userService.uploadImageUser(uploadFile);
    }

    @PostMapping(value=URI.API + URI.ADMIN + URI.USERS + URI.ID)
    public String deleteUser(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {
        this.userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message",
                this.propertyService.getMessage("message.successfully").replace("${content}", "deleted user"));
        return URI.REDIRECT + URI.ADMIN + URI.USERS;
    }

}
