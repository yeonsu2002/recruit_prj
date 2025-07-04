package kr.co.sist.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.sist.user.dto.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {


}
