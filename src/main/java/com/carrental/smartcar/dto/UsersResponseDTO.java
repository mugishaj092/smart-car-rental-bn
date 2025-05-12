package com.carrental.smartcar.dto;

import com.carrental.smartcar.model.User;
import java.util.List;

public class UsersResponseDTO {
    private String message;
    private List<User> users;

    public UsersResponseDTO(String message, List<User> users) {
        this.message = message;
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
