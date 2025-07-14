package kr.co.sist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.login.UserRepository;
import kr.co.sist.login.loginJoinService;
import kr.co.sist.user.entity.UserEntity;



@Controller
public class MainPageController {
	
	@Autowired
	private  UserRepository ur;
	

    @GetMapping("/")
    public String MainPage(@AuthenticationPrincipal CustomUser user, Model model){
    	
    	UserEntity userEntity=ur.findById(user.getEmail()).orElse(null);
    	
    	model.addAttribute("userEntity",userEntity);
    
        return "main_page"; 
    }
}