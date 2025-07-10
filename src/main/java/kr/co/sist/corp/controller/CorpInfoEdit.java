package kr.co.sist.corp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorpInfoEdit {

    @GetMapping("/corp/corp_info_edit")
    public String showEditForm(Model model) {

        return "corp/corp_info_edit"; // templates/corp/edit.html (또는 edit.jsp)
    }
}
