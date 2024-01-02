package com.javaguides.clothesbabies.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SftpModel {
    private String host;
    private String username;
    private String password;
    private int port;
}
