package kr.co.sist.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailVerificationEntity, Long> {

	public EmailVerificationEntity findByEmail(String Email);
	
}
