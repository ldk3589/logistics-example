package com.dk.logistics.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String role; // USER / ADMIN_L2 / ADMIN_L1
    private String adminPassword;
}
