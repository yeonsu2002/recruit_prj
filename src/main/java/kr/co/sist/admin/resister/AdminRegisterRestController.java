package kr.co.sist.admin.resister;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.admin.AdminEntity;
import kr.co.sist.admin.AdminService;
import kr.co.sist.admin.email.EmailService;
import kr.co.sist.corp.controller.CorpProductController;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@RestController
public class AdminRegisterRestController {


	
	private  Map<String, AdminEntity> adminMap=new HashMap<String, AdminEntity>();
	
	@Autowired(required=false)
    private AdminService as;

   
	
	@PostMapping("/sendEmail")
	public Map<String, Object> mailConfirm(@RequestBody Map<String,String> request) {
		String email = request.get("email");
		Map<String, Object> mailConfirm = new HashMap<String, Object>();
		mailConfirm = as.sendEmail(email);
	    
	    
	    
	    
	    
	    return mailConfirm;
	}
	
	
	
	
    
}
