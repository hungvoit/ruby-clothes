package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

     Page<User> getCustomerUserByList(int pageNumber, int size);

     User findById(Long id);

     User findByEmail(String email);

     void createUser(UserDto userDto);

     void updateUser(UserDto userDto);

     void deleteUser(Long id);

     void resetPinUser(Long id);

     void changePinUser(UserDto userDto);

     void forgotPasswordUser(UserDto userDto);

     String uploadImageUser(MultipartFile uploadFile);
}
