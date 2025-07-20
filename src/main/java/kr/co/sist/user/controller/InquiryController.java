package kr.co.sist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

import kr.co.sist.user.dto.InquiryRequestDTO;
import kr.co.sist.user.dto.InquiryResponseDTO;
import kr.co.sist.user.service.InquiryService;

@Controller
@RequestMapping("/user/help")
public class InquiryController {

    @Autowired
    private InquiryService helpService;

    // 고객센터 메인 페이지
    @GetMapping("/help")
    public String helpCenter() {
        return "user/help/help";
    }
    @GetMapping("/test/setSession")
    @ResponseBody
    public String setTestSession(HttpSession session) {
        session.setAttribute("userEmail", "testuser@example.com");
        session.setAttribute("adminId", "acs0705@naver.com");
        return "세션에 임시 이메일과 adminId가 설정되었습니다.";
    }

    // 문의 등록 처리
    @PostMapping("/inquiry")
    @ResponseBody
    public ResponseEntity<?> submitInquiry(
            @RequestParam("inquiryType") String category,
            @RequestParam("inquiryTitle") String title,
            @RequestParam("inquiryContent") String content,
            @RequestParam(value = "attachFile", required = false) MultipartFile file,
            HttpSession session) {   // HttpSession 직접 주입

        try {
            String userEmail = (String) session.getAttribute("userEmail"); // 로그인한 사용자 이메일

            System.out.println("=== 디버깅 정보 ===");
            System.out.println("userEmail: " + userEmail);
            System.out.println("category: " + category);
            System.out.println("title: " + title);
            System.out.println("content: " + content);
            System.out.println("file: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("==================");

            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }

            String adminId = (String) session.getAttribute("adminId");
            if (adminId == null || adminId.trim().isEmpty()) {
                adminId = "acs0705@naver.com";
            }

            InquiryRequestDTO inquiryRequest = new InquiryRequestDTO();
            inquiryRequest.setEmail(userEmail.trim());
            inquiryRequest.setAdminId(adminId.trim());
            inquiryRequest.setTitle(title != null ? title.trim() : "");
            inquiryRequest.setContent(content != null ? content.trim() : "");
            inquiryRequest.setCategory(category != null ? category.trim() : "");
            inquiryRequest.setAnswerStat("N");

            if (file != null && !file.isEmpty()) {
                try {
                    String fileName = helpService.saveFile(file);
                    inquiryRequest.setAttachFile(fileName);
                    System.out.println("파일 업로드 성공: " + fileName);
                } catch (Exception e) {
                    System.out.println("파일 업로드 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("=== InquiryRequestDTO 최종 값 확인 ===");
            System.out.println("Email: " + inquiryRequest.getEmail());
            System.out.println("AdminId: " + inquiryRequest.getAdminId());
            System.out.println("Title: " + inquiryRequest.getTitle());
            System.out.println("Content: " + inquiryRequest.getContent());
            System.out.println("Category: " + inquiryRequest.getCategory());
            System.out.println("AttachFile: " + inquiryRequest.getAttachFile());
            System.out.println("AnswerStat: " + inquiryRequest.getAnswerStat());
            System.out.println("=====================================");

            helpService.saveInquiry(inquiryRequest);

            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "문의가 성공적으로 접수되었습니다."));

        } catch (Exception e) {
            System.out.println("문의 등록 중 오류 발생:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "문의 접수 중 오류 발생: " + e.getMessage()));
        }
    }
}