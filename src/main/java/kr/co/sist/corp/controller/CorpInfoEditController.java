package kr.co.sist.corp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.service.CorpEditService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/corp/info/")
public class CorpInfoEditController {

    @Autowired
    private CorpEditService corpService;

    // 기업정보 수정 페이지 조회
    @GetMapping("/edit")
    public String editForm(Model model) {
        // 세션에서 기업번호 가져오기 (로그인한 기업의 번호)
        Long corpNo = getCurrentCorpNo(); // 실제 구현에서는 세션에서 가져와야 함
        
        CorpEntity corpInfo = corpService.getCorpInfo(corpNo);
        
        model.addAttribute("corpInfo", corpInfo);
        
        return "corp/corp_info_edit";
    }

    // 기업정보 수정 처리
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCorpInfo(@ModelAttribute CorpEntity corpEntity) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 현재 로그인한 기업 번호 설정
            corpEntity.setCorpNo(getCurrentCorpNo());
            
            // 기업정보 수정
            corpService.updateCorpInfo(corpEntity);
            
            response.put("success", true);
            response.put("message", "기업정보가 성공적으로 수정되었습니다.");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "수정 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }

    // 기업정보 조회 페이지
    @GetMapping("/view")
    public String viewCorpInfo(Model model) {
        Long corpNo = getCurrentCorpNo();
        CorpEntity corpInfo = corpService.getCorpInfo(corpNo);
        model.addAttribute("corpInfo", corpInfo);
        
        return "corp/corp_info_view";
    }

    // 이미지 등록 페이지
    @GetMapping("/image")
    public String imageUploadForm(Model model) {
        Long corpNo = getCurrentCorpNo();
        CorpEntity corpInfo = corpService.getCorpInfo(corpNo);
        model.addAttribute("corpInfo", corpInfo);
        
        return "corp/corp_image_upload";
    }

    // 현재 로그인한 기업번호 가져오기 (실제 구현에서는 세션에서 가져와야 함)
    private Long getCurrentCorpNo() {
        // TODO: 세션에서 로그인한 기업 번호 가져오기
        // HttpSession session = request.getSession();
        // return (Long) session.getAttribute("corpNo");
        return 105L; // 임시값
    }
}