package com.javaguides.clothesbabies.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class GlobalConfigurationDto {
    @NotEmpty(message = "{code.required}")
    private String code;

    @NotEmpty(message = "{value.required}")
    private String value;

    private String description;

    private Date createDate;

    private Date updateDate;

    private boolean flagChange;

    public GlobalConfigurationDto(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean getFlagChange() {
        return flagChange;
    }

    public void setFlagChange(boolean flagChange) {
        this.flagChange = flagChange;
    }
}
