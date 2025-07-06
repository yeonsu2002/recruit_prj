package kr.co.sist.user.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.sist.user.dto.PositionCodeEntity;

public interface PositionCodeRepository extends JpaRepository<PositionCodeEntity, Integer> {

}
