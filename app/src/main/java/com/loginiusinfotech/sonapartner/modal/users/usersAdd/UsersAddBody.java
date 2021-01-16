package com.loginiusinfotech.sonapartner.modal.users.usersAdd;

public class UsersAddBody {
    String email;
    String password;
    String status;
    String role_id;

    public UsersAddBody(String email, String password, String status, String role_id) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.role_id = role_id;
    }
}
