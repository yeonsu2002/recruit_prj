/*
 * package kr.co.sist.user.controller;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.security.core.annotation.AuthenticationPrincipal; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import kr.co.sist.jwt.CustomUser; import
 * kr.co.sist.user.service.RecentViewPostingService; import
 * lombok.RequiredArgsConstructor;
 * 
 * @Controller
 * 
 * @RequiredArgsConstructor public class RecentViewPostingController {
 * 
 * private final RecentViewPostingService rvp;
 * 
 * @PostMapping("/recent_view/save") public ResponseEntity<String>
 * saveRecentViewed(@RequestParam("jobPostingSeq") Integer
 * jobPostingSeq, @AuthenticationPrincipal CustomUser userInfo){
 * 
 * if (userInfo != null) { String email = userInfo.getEmail();
 * rvp.insertRecentViewed(email, jobPostingSeq); return
 * ResponseEntity.ok("최근 본 공고 저장 완료"); } return
 * ResponseEntity.badRequest().body("로그인이 필요합니다"); } }
 */