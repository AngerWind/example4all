package com.gitlab.techschool.pcbook.service.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/19
 * @description
 */
public class User {
    private String name;
    private byte[] hashedPassword;
    private Set<String> role; // 一个user可以有多个role

    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User(String name, String password, Set<String> role) {
        this.name = name;
        this.hashedPassword = getHashedPassword(password);
        this.role = role;
    }

    protected User clone() {
        return new User(this.name, new String(this.hashedPassword, StandardCharsets.UTF_8), this.role);
    }

    public boolean isCorrectPassword(String password) {
        return Arrays.equals(this.hashedPassword, getHashedPassword(password));
    }

    public static byte[] getHashedPassword(String password) {
        digest.reset();
        digest.update(password.getBytes(StandardCharsets.UTF_8));
        return digest.digest();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
