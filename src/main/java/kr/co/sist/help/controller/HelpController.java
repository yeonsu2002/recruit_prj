package kr.co.sist.help.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {
    
    @GetMapping("/user/help/help")
    public String help() {
        return "user/help/help"; // templates 폴더 기준
    }
}