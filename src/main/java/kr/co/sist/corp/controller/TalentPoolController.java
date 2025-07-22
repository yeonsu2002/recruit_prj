package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.corp.dto.InterviewOfferDTO;
import kr.co.sist.corp.dto.ResumeDetailDTO;
import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.service.ResumeDetailService;
import kr.co.sist.corp.service.TalentPoolService;
import kr.co.sist.jwt.CustomUser;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TalentPoolController {

    private final TalentPoolService talentPoolService;
    private final ResumeDetailService resumeDetailService;

    // 전체 인재 목록
    @GetMapping("/corp/talentPool/talent_pool")
    public String showTalentPool(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "latest") String sortBy,
                                 @RequestParam(defaultValue = "desc") String order,
                                 Model model,
                                 @AuthenticationPrincipal CustomUser corpInfo) {
    	//로그인 안 했으면 로그인창으로
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
    
    // 면접제안한 인재
    @GetMapping("/corp/talentPool/offer")
    public String showOfferTalents() {
    	return "corp/talentPool/offer";
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


    // 최근 열람한 인재
    @GetMapping("/corp/talentPool/recently_viewed")
    public String showRecentlyViewedTalents() {
        return "corp/talentPool/recently_viewed";
    }
    
    //=======================기능 부분===========================
    
    //면접 제안 모달 fragment불러오기
    @GetMapping("/corp/talentPool/interviewOffer")
    public String interviewOfferFragment(  ) {
//    	 String messageTitle, String messageContent, int resumeSeq, String email, 
//    																			 @AuthenticationPrincipal CustomUser corpInfo
//    	messageServ.addMessage(email, corpEntity.getCorpNo(), messageTitle, messageContent);
    	return "fragments/interviewOffer::modalContent";
    }
    //면접 제안 기능 
    @PostMapping("/corp/talentPool/interviewOffer/send")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendInterviewProposal(
            @RequestBody InterviewOfferDTO proposalDto,
            @AuthenticationPrincipal CustomUser corpUser) {
        Map<String, String> response = new HashMap<>();
        try {
            if (corpUser == null) {
                response.put("status", "fail");
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            proposalDto.setCorpNo(corpUser.getCorpNo());
            talentPoolService.sendInterviewProposal(proposalDto);

            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    //이력서 상세페이지 및 확인 
    @GetMapping("/corp/talentPool/resume_detail")
    public String resumeDetail(@RequestParam("resumeNo") int resumeNo, Model model) {
        ResumeDetailDTO rdDTO = resumeDetailService.getResumeDetail((long) resumeNo); // 서비스 통해 호출

        if (rdDTO.getImage() == null || rdDTO.getImage().trim().isEmpty()) {
            rdDTO.setImage("default_img.png");
        }

        model.addAttribute("resume", rdDTO); 
        model.addAttribute("resumeUser", rdDTO.getMember()); 

        model.addAttribute("positions", rdDTO.getPositions());
        model.addAttribute("skills", rdDTO.getSkills());
        model.addAttribute("educations",rdDTO.getEducations());
        model.addAttribute("careers", rdDTO.getCareers());
        model.addAttribute("projects", rdDTO.getProjects());
        model.addAttribute("introductions", rdDTO.getIntroductions());
        model.addAttribute("additionals", rdDTO.getAdditionals());
        model.addAttribute("links", rdDTO.getLinks());
//        model.addAttribute("isScraped", rdDTO.isScraped());

        return "corp/talentPool/resume_detail";
    }

    // 툴바 정렬 
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
    

}
