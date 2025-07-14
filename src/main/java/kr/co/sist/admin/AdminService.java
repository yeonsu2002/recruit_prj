package kr.co.sist.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.admin.email.EmailService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {

	
	@Autowired
	private AdminRepository ar;
	private final EmailService emailService;
	/**
	 * 관리자 한명 찾아서 정보 조회(나중에 AdminService로 옮겨야함)
	 * @return
	 */
	public Map<String, Object> sendEmail(String email) {
	    boolean flag = emailChk(email);
	    Map<String, Object> response = new HashMap<>();
	    
	    if(flag) {  // 이메일이 이미 존재하는 경우
	        response.put("exists", true);
	        response.put("msg", "이미 사용 중인 이메일입니다.");
	    } else {    // 이메일이 존재하지 않는 경우 (사용 가능)
	        String code = emailService.sendEmail(email);
	        response.put("exists", false);  // 존재하지 않음 = 사용 가능
	        response.put("msg", "전송이 완료되었습니다.");
	        response.put("code", code);
	    }
	    return response;
	}
	
	public boolean emailChk(String email) {
		Optional<AdminEntity> optional = ar.findById(email);
		
		boolean flag = false;
		if (optional.isPresent()) {
			flag = true;
	    }
		
		return flag;
	}
	
	
}
