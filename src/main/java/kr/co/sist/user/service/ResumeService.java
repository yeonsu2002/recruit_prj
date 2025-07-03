package kr.co.sist.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.mapper.ResumeMapper;

@Service
public class ResumeService {

	private final ResumeMapper rm;
	
	//생성자 주입
	public ResumeService(ResumeMapper rm) {
		this.rm= rm;
	}
	
	public List<PositionCodeDTO> searchAllPositionCode(){
		return rm.selectAllPositionCode();
	}
}
