package kr.co.sist.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.JobPostDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.JobPostingService;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    
    private final UserRepository ur;
    private final CipherUtil cu;
    private final JobPostingService jps; 
    
    @GetMapping("/user/main_page")
    public String MainPage(@AuthenticationPrincipal CustomUser userInfo, Model model) {

        if (userInfo == null) {
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        // 사용자 정보 가져오기
        UserEntity userEntity = ur.findById(userInfo.getEmail()).orElse(null);
        if(userEntity == null) { 
            return "redirect:/accessDenied"; 
        }

        // UserDTO로 변환 후 model에 추가
        UserDTO user = new UserDTO(userEntity);
        model.addAttribute("user", user);

        

        return "user/main_page";
    }
}  