package kr.co.sist.mail;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class EmailVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailVerifiSeq;

    private String email;
    
    private String ip;

    private String verificationCode;
    
    private int count = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    //인증 유지시간은 5분으로 고정 
    private LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

    private boolean isVerified = false;
}