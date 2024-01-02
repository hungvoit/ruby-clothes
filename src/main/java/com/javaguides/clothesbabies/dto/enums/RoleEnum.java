package com.javaguides.clothesbabies.dto.enums;

public enum RoleEnum {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER");

    private String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RoleEnum fromName(String name) {
        for (RoleEnum e : RoleEnum.values()) {
            if (name.equals(e.name)) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(RoleEnum.class, name);
    }
}


