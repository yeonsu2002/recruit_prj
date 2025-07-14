package kr.co.sist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.jwt.CustomUser;
import kr.co.sist.login.UserRepository;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.util.CipherUtil;

@Controller
public class MainPageController {

    @Autowired
    private UserRepository ur;
    
    @Autowired
    private CipherUtil cipherUtil;
    
    @GetMapping("/user/main_page")
    public String MainPage(@AuthenticationPrincipal CustomUser user, Model model) {
        // UserEntity를 데이터베이스에서 조회
        UserEntity userEntity = ur.findById(user.getEmail()).orElse(null);

        if (userEntity != null) {
            // name이 암호화된 경우 복호화
            if (userEntity.getName() != null) {
                userEntity.setName(cipherUtil.plainText(userEntity.getName()));
            }
            // 디버깅: 복호화된 이름 출력
            System.out.println(userEntity.getName() + "--------------------------------");
        }

        // Model에 userEntity 및 user 객체를 전달
        model.addAttribute("userEntity", userEntity);
        model.addAttribute("user", user);

        return "user/main_page";
    }

}