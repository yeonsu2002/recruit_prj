package kr.co.sist.corp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		@Autowired
    private final TalentPoolService talentPoolService;
    private final ResumeDetailService resumeDetailService;

    // 전체 인재 목록
    @GetMapping("/corp/talentPool/talent_pool")
    public String showTalentPool(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "latest") String sortBy,
                                 @RequestParam(defaultValue = "desc") String order,
                                 Model model, @AuthenticationPrincipal CustomUser corpInfo) {
    	//로그인 안 했으면 로그인창으로
      if (corpInfo == null) {
        return "redirect:/login";
      }
    		
        Long corpNo = corpInfo.getCorpNo();
        int offset = (page - 1) * size;
        
        List<TalentPoolDTO> talents = talentPoolService.getPaginatedTalents(sortBy, order, offset, size, corpNo);
        int totalCount = talentPoolService.selectTalentTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        System.out.println("corpNo in controller: " + corpNo);
        
        model.addAttribute("corpNo", corpNo);
        model.addAttribute("talentPool", talents);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        
        return "corp/talentPool/talent_pool";
    }//showTalentPool
    
    // 면접제안한 인재
    @GetMapping("/corp/talentPool/offer")
    public String showOfferTalents(@RequestParam(defaultValue = "1") int page,
													    		 @RequestParam(defaultValue = "5") int size,
													    		 @RequestParam(defaultValue = "latest") String sortBy,
													    		 @RequestParam(defaultValue = "desc") String order,
													    		 Model model, @AuthenticationPrincipal CustomUser corpInfo) {
	    	//로그인 안 했으면 로그인창으로
	      if (corpInfo == null) {
	        return "redirect:/login";
	      }
	      
	    	return "corp/talentPool/offer";
    }//showOfferTalents
    
    // 최근 열람한 인재 전체 페이지
    @GetMapping("/corp/talentPool/recently_viewed")
    public String showRecentlyViewedTalents(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(defaultValue = "latest") String sortBy,
                                            @RequestParam(defaultValue = "desc") String order,
                                            Model model, @AuthenticationPrincipal CustomUser corpInfo) {
        if (corpInfo == null) {
            return "redirect:/login";
        }

        Long corpNo = corpInfo.getCorpNo();
        int startRow = (page - 1) * size + 1;
        int endRow = startRow + size - 1;
        
        // 이력서 seq 목록 가져오기
        List<Integer> resumeSeqs = talentPoolService.getRecentlyViewedResumes(corpNo, startRow, endRow);//
        
        List<TalentPoolDTO> recentlyViewedResumes = new ArrayList<>();//
        if (!resumeSeqs.isEmpty()) {//
            recentlyViewedResumes = talentPoolService.getResumeDetailsBySeqs(resumeSeqs);//
        }

        int totalCount = talentPoolService.getRecentlyViewedResumesCount(corpNo);
        int totalPages = (int) Math.ceil((double) totalCount / size);//

        model.addAttribute("talentPool", recentlyViewedResumes);//
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);

        return "corp/talentPool/recently_viewed";
    }//showRecentlyViewedTalents

    // 최근 열람한 인재 리스트 프래그먼트만 반환 AJAX
    @GetMapping("/corp/talentPool/recently_viewed-fragment")
    public String getRecentlyViewedFragment(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(defaultValue = "latest") String sortBy,
                                            @RequestParam(defaultValue = "desc") String order,
                                            @AuthenticationPrincipal CustomUser corpInfo,
                                            Model model) {
        Long corpNo = corpInfo.getCorpNo();
        int startRow = (page - 1) * size + 1;
        int endRow = startRow + size - 1;

        // 이력서 seq 목록 가져오기
        List<Integer> resumeSeqs = talentPoolService.getRecentlyViewedResumes(corpNo, startRow, endRow);

        List<TalentPoolDTO> recentlyViewedResumes = new ArrayList<>();
        if (!resumeSeqs.isEmpty()) {
            recentlyViewedResumes = talentPoolService.getResumeDetailsBySeqs(resumeSeqs);
        }

        int totalCount = talentPoolService.getRecentlyViewedResumesCount(corpNo);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("talentPool", recentlyViewedResumes);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);

        return "fragments/talentList :: talentList";
    }//getRecentlyViewedFragment
    
    
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
    }//showScrappedTalents

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
    
    //면접 제안 모달 fragment불러오기========================================
    @GetMapping("/corp/talentPool/interviewOffer")
    public String interviewOfferFragment(@AuthenticationPrincipal CustomUser corpInfo, Model model) {
        Long corpNo = corpInfo.getCorpNo();

        
        // corpNo에 해당하는 기업 정보 조회
        InterviewOfferDTO corpInfoDTO = talentPoolService.getCorpInfoByCorpNo(corpNo);
        
        // corpInfoDTO가 null일 경우 기본값 설정
        if (corpInfoDTO == null) {
            corpInfoDTO = new InterviewOfferDTO();
            corpInfoDTO.setCorpName("DefaultCorpName");  // 기본값 설정
        }
        
        model.addAttribute("corpNo", corpNo);
        model.addAttribute("corpInfo", corpInfoDTO);


        return "fragments/interviewOffer::modalContent"; // modalContent 프래그먼트 반환
    }//interviewOfferFragment

    //면접 제안 기능
    @PostMapping("/corp/talentPool/interviewOffer/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendInterviewOffer(@AuthenticationPrincipal CustomUser corpInfo,
    																															@RequestBody InterviewOfferDTO ioDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 면접 제안 상태 초기화
        		ioDTO.setCorpNo(corpInfo.getCorpNo());
            ioDTO.setIsRead("N");
            ioDTO.setReadedAt("0");
            ioDTO.setIsOffered("Y");

            // 면접 제안 전송
            talentPoolService.sendInterviewOffer(ioDTO);

            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            e.printStackTrace(); // 예외 로그 출력
        }
        return ResponseEntity.ok(response);
    }//sendInterviewOffer

    //이력서 상세페이지 및 이력서 확인버튼 클릭시 열람 메세지 보내기 ===========================
    @GetMapping("/corp/talentPool/resume_detail")
    public String resumeDetail(@RequestParam("resumeNo") int resumeNo, 
												        @AuthenticationPrincipal CustomUser corpInfo, 
												        Model model) {  
      if (corpInfo == null) {
        // 로그인되지 않은 경우, 로그인 페이지로 리디렉션
        return "redirect:/login";
    }
    	
    		Long corpNo = corpInfo.getCorpNo(); // CustomUser에서 corpNo를 추출
    		
    		InterviewOfferDTO corpInfoDTO = talentPoolService.getCorpInfoByCorpNo(corpNo);
    		
    		talentPoolService.viewResume((long) resumeNo, corpNo); 
    		ResumeDetailDTO rdDTO = resumeDetailService.getResumeDetail((long) resumeNo); // 서비스 통해 호출

        if (rdDTO.getImage() == null || rdDTO.getImage().trim().isEmpty()) {
            rdDTO.setImage("default_img.png");
        }
        System.out.println("tp con corpNo: " + corpNo + ", tp con resumeNo: " + resumeNo);
        model.addAttribute("corpInfo", corpInfoDTO);
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
