package com.barkstore.Barkstore.registration.token;

import com.barkstore.Barkstore.appuser.MyUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> getUserId(Long id) {
        return confirmationTokenRepository.findByUserId(id);
    }

    public void deleteByUserId(Long user_id) {
        System.out.print("ID IS: " + user_id);
        ConfirmationToken token = confirmationTokenRepository.findByUserId(user_id).get();
        confirmationTokenRepository.delete(token);
    }



    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
