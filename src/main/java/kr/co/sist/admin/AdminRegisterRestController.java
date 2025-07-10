package kr.co.sist.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminRegisterRestController {
	private final EmailService emailService;
	private  Map<String, AdminDTO> adminMap=new HashMap<String, AdminDTO>();
	
    
	@PostMapping("/sendEmail")
	public Map<String, String> mailConfirm(@RequestBody Map<String,String> request) {
	    String email = request.get("email");
	    String code = emailService.sendEmail(email);
	    
	    Map<String, String> response = new HashMap<>();
	    
	    response.put("msg", "전송이 완료되었습니다.");
	    response.put("code", code);
	    return response;
	}
    
}
