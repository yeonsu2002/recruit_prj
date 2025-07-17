package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.service.TalentPoolService;
import kr.co.sist.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequiredArgsConstructor
public class TalentPoolController {

    private final TalentPoolService talentPoolService;

    // 이력서 상세 페이지
    @GetMapping("/corp/talentPool/resume_detail")
    public String resumeDetailPage() {
        return "corp/talentPool/resume_detail";
    }

    // 인터뷰 제안 모달 fragment
    @GetMapping("/corp/talentPool/interviewOffer")
    public String interviewOfferFragment() {
        return "fragments/interviewOffer::modalContent";
    }

    // 전체 인재 목록
    @GetMapping("/corp/talentPool/talent_pool")
    public String showTalentPool(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "latest") String sortBy,
                                 @RequestParam(defaultValue = "desc") String order,
                                 Model model,
                                 @AuthenticationPrincipal CustomUser corpInfo) {
      if (corpInfo == null) {
        return "redirect:/login";
      }
    		
        Long corpNo = corpInfo.getCorpNo();
        int offset = (page - 1) * size;

        List<TalentPoolDTO> talents = talentPoolService.getPaginatedTalents(sortBy, order, offset, size, corpNo);
        int totalCount = talentPoolService.selectTalentTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("talentPool", talents);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        
        return "corp/talentPool/talent_pool";
    }

 // AJAX로 정렬, 페이징 처리할 때 부분 HTML 반환
    @GetMapping("/sort")
    public String sortTalentList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "latest") String sortBy,
                                 @RequestParam(defaultValue = "desc") String order,
                                 Model model,
                                 @AuthenticationPrincipal CustomUser corpInfo) {

        if (corpInfo == null) {
            return "redirect:/login";
        }

        Long corpNo = corpInfo.getCorpNo();
        int offset = (page - 1) * size;

        List<TalentPoolDTO> talentPool = talentPoolService.getPaginatedTalents(sortBy, order, offset, size, corpNo);

        model.addAttribute("talentPool", talentPool);
        return "fragments/talentList :: talentList";
    }

    
    // 스크랩된 인재
    @GetMapping("/corp/talentPool/scrap")
    public String showScrappedTalents(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model,
                                      @AuthenticationPrincipal CustomUser corpInfo) {
    	 if (corpInfo == null) {
         return "redirect:/login";
       }
        Long corpNo = corpInfo.getCorpNo();
        int offset = (page - 1) * size;

        List<TalentPoolDTO> scrappedTalents = talentPoolService.getScrappedTalents(corpNo, offset, size);
        int totalCount = talentPoolService.getScrappedTalentsCount(corpNo);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("talentPool", scrappedTalents);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);

        return "corp/talentPool/scrap";
    }
    

    // 스크랩 요청 (AJAX)
    @PostMapping("/corp/talentPool/scrap")
    @ResponseBody
    public ResponseEntity<Map<String, String>> scrapResume(@RequestBody Map<String, Long> payload,
                                                            @AuthenticationPrincipal CustomUser corpInfo) {
        
    		Long resumeSeq = payload.get("resumeSeq");
        Long corpNo = corpInfo.getCorpNo();

        String status = talentPoolService.scrapResume(resumeSeq, corpNo);

        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    // 면접제안한 인재
    @GetMapping("/corp/talentPool/offer")
    public String showOfferTalents() {
        return "corp/talentPool/offer";
    }

    // 최근 열람한 인재
    @GetMapping("/corp/talentPool/recently_viewed")
    public String showRecentlyViewedTalents() {
        return "corp/talentPool/recently_viewed";
    }
    


    
    
    

}
