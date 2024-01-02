package com.javaguides.clothesbabies.dto;
import com.javaguides.clothesbabies.dto.interfaces.IChangePasswordGroup;
import com.javaguides.clothesbabies.util.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserDto {
    private Long id;

	private String principalId;

	@Size(min = 2, message = "{min.firstName}")
	@Size(max = 20, message = "{max.firstName}")
	@NotEmpty(message = "{firstName.required}")
    private String firstName;

	@Size(min = 2, message = "{min.lastName}")
	@Size(max = 20, message = "{max.lastName}" )
	@NotEmpty(message = "{lastName.required}")
    private String lastName;

	@NotEmpty(message = "{email.required}")
	@Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "{email.invalid}")
    private String email;

	@NotEmpty(message = "{password.required}", groups = IChangePasswordGroup.class)
	private String oldPin;

	@ValidPassword(groups = IChangePasswordGroup.class)
	@NotEmpty(message = "{password.required}", groups = IChangePasswordGroup.class)
	private String newPin;

	@NotEmpty(message = "{password.required}", groups = IChangePasswordGroup.class)
	private String renterNewPin;

	private String phone;

	private String profileUrl;

	private String password;

	private String status;

	private String loginType;

	private Date createDate;

	private Date updateDate;

	private String role;

	private String globalError;

	public UserDto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getOldPin() {
		return oldPin;
	}

	public void setOldPin(String oldPin) {
		this.oldPin = oldPin;
	}

	public String getNewPin() {
		return newPin;
	}

	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}

	public String getRenterNewPin() {
		return renterNewPin;
	}

	public void setRenterNewPin(String renterNewPin) {
		this.renterNewPin = renterNewPin;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

    public String getGlobalError() {
        return globalError;
    }

    public void setGlobalError(String globalError) {
        this.globalError = globalError;
    }
}
