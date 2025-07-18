package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.corp.dto.CorpDTO;
import kr.co.sist.corp.dto.JobPostingDTO;
import kr.co.sist.corp.service.JobPostingCorpService;
import kr.co.sist.jwt.CustomUser;
import kr.co.sist.jwt.JWTUtil;
import kr.co.sist.user.dto.PositionCodeDTO;
import kr.co.sist.user.dto.TechStackDTO;
import kr.co.sist.user.dto.UserDTO;

@Controller
public class JobPostController {
	
	private final JWTUtil jwtUtil;
	private final JobPostingCorpService jpcService;
	
	public JobPostController(JWTUtil jwtUtil, JobPostingCorpService jpcService) {
	  this.jpcService = jpcService;
		this.jwtUtil = jwtUtil;
	}

  /**
   * 공고등록 form으로 가기 : 권한체크 
   */
  @GetMapping("/corp/jobPostingForm")
  public String getJobPostingForm(Model model, HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
  	
  	if(user == null) {
  		return "redirect:/accessDenied";
  	}
  	boolean hasCorpAuth = user.getAuthorities().stream()
  													.anyMatch(auth -> "ROLE_CORP".equals(auth.getAuthority()));
  	
  	if( !hasCorpAuth) {
  		return "redirect:/accessDenied";
  	}
  	
  	//db조작시 user활용  
  	//user.getEmail();
  	//user.getCorpNo();
  	
	 return "corp/jobPosting/jobPostingForm";
  }
  
  /**
   * 나의 공고 리스트 화면으로 가기 : 권한체크 
   */
  @GetMapping("/corp/myJobPostingListPage")
  public String getMyJobPostingListPage(@AuthenticationPrincipal CustomUser user) {
  	if(user == null) {
  		return "redirect:/accessDenied";
  	}
  	boolean hasCorpAuth = user.getAuthorities().stream()
  													.anyMatch(auth -> "ROLE_CORP".equals(auth.getAuthority()));
  	
  	if( !hasCorpAuth) {
  		return "redirect:/accessDenied";
  	}
  	
  	return "corp/jobPosting/myJobPostingListPage";
  }
  
  
  /**
   * 새로운 공고 등록 (JSON)
   */
  @PostMapping("/corp/uploadJobPosting")
  public ResponseEntity<?> registerJobPost(@RequestBody JobPostingDTO jpDTO) {
  	
  	//form 2차 검증(if)
  	
  	
  	
  	
  	try {
  		//공고 등록 호출
  		jpcService.uploadJobPost(jpDTO);
  		return ResponseEntity.ok("공고 등록 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공고 등록 실패: " + e.getMessage());
		}
    
  }
  
  //예외처리 어떻게 하지? https://chatgpt.com/s/t_686fe64224988191838976d9bfa9aea0
  /**
   * 컨트롤러에서 catch 안 하면, 예외는 스프링 기본 예외 처리기에 의해 처리됨
			→ 콘솔에 에러 로그만 출력되고, 브라우저에 500 Internal Server Error + 흰 화면 or HTML 에러 페이지 나올 수 있음
   */
  
  /**
   * 공고form 페이지에서 포지션 검색 비동기 수신 
   */
  @GetMapping("/corp/searchPositionByKeyword") //Get은 @Requestbody를 가질수 없음
  public ResponseEntity<?> searchPositionByKeyword(@RequestParam String keyword){
  	List<PositionCodeDTO> resultList = jpcService.pDTOList(keyword);
  	
  	return ResponseEntity.ok(resultList); // → JSON 배열로 자동 직렬화됨
  }
  
  /**
   * 공고form 페이지에서 기술스택 검색 비동기 수신 
   */
  @GetMapping("/corp/searchStackByKeyword")
  public ResponseEntity<?> searchTechStackBykeyword(@RequestParam String keyword){
  	List<TechStackDTO> resultList = jpcService.tsDTO(keyword);
  	
  	return ResponseEntity.ok(resultList);
  }
  
  /**
   * 나의 공고 리스트 페이지에 뿌릴 자료 호출 (/corp/myJobPostingListPage 에 통합할지, 비동기로 호출할지 미정)
   * 필요 변수: corpNO, postSts (total, ing, end), orderBy(start, end, viewCnt) 
   */
  @GetMapping("/corp/getMyAllPosting") //PostMapping 해야 @RequestBody 오류 안남 
  public ResponseEntity<?> getMyAllPosting(@RequestParam Long corpNo, @RequestParam String postSts, @RequestParam String orderBy){
  	
  	JobPostingDTO jpDTO = new JobPostingDTO();
  	jpDTO.setCorpNo(corpNo);
  	jpDTO.setPostSts(postSts);
  	jpDTO.setOrderBy(orderBy);
  	
  	try {
  		List <Map<String, Integer>> postCntMapList =  jpcService.selectMyJobPostingCnt(jpDTO.getCorpNo());
  		List <JobPostingDTO> postList = jpcService.selectMyJobPosting(jpDTO);
  		
  		Map<String, Object> result = new HashMap<String, Object>();
  		result.put("postCnt", postCntMapList);
  		result.put("postList", postList);
  		
  		return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공고 리스트 호출 실패: " + e.getMessage());
		} 
  	
  }
  
  
}
