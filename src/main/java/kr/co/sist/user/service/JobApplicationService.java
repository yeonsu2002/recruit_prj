package kr.co.sist.user.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.JobApplicationDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.mapper.JobApplicationMapper;

@Service
public class JobApplicationService {
	
	@Autowired
	private JobApplicationMapper jmm;

	/**
	 * 지원하기
	 * @param jad
	 */
	public void applyForJob(JobApplicationDTO jad) {
		
		UserDTO user=new UserDTO();
		
		jad.setApplicationStatus("지원완료");
		
		String applicationDate=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		jad.setApplicationDate(applicationDate);
		
		
		user.setEmail("testuser@domain.com");
		jad.setIsRead("N");
		jmm.insertJobApplication(jad);
	}


	
}
