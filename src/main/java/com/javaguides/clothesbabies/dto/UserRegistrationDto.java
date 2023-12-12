package com.javaguides.clothesbabies.dto;

import com.javaguides.clothesbabies.util.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegistrationDto {
    @Size(min = 2, message = "{min.firstName}")
    @Size(max = 20, message = "{max.firstName}")
    @NotEmpty(message = "{firstName.required}")
    private String firstName;

    @Size(min = 2, message = "{min.lastName}")
    @Size(max = 20, message = "{max.lastName}" )
    @NotEmpty(message = "{lastName.required}")
    private String lastName;

    @ValidPassword
    @NotEmpty(message = "{password.required}")
    private String password;

    @NotEmpty(message = "{email.required}")
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "{email.invalid}")
    private String email;

    public UserRegistrationDto(){

    }

    public UserRegistrationDto(String firstName, String lastName, String password, String email) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
