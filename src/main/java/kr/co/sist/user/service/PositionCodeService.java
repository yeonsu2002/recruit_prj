package kr.co.sist.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.entity.PositionCodeEntity;
import kr.co.sist.user.repository.PositionCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class PositionCodeService {

	private final PositionCodeRepository pcr;
	
	public List<PositionCodeEntity> searchAllPositionCode(){
		return pcr.findAll();
	}
	
}
