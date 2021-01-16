package com.loginiusinfotech.sonapartner.modal.users.changeStatus;

public class ChangeStatusBody {
    String userId;
    String status;

    public ChangeStatusBody(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }
}
