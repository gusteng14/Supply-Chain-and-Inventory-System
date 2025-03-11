package com.barkstore.Barkstore.Email;

public interface EmailSender {
    void send(String to, String email, String subject);

}
