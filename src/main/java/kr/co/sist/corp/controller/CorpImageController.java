package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.service.CorpImageService;

@Controller
@RequestMapping("/corp")
public class CorpImageController {

    @Autowired
    private CorpImageService corpImageService;

    /**
     * 이미지 등록/수정 페이지 이동
     */
    @GetMapping("/image/upload/{corpNo}")
    public String imageForm(@PathVariable("corpNo") String corpNoStr, Model model) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (corpNoStr == null || corpNoStr.trim().isEmpty()) {
                model.addAttribute("error", "회사 번호가 필요합니다.");
                return "corp/image_upload";
            }
            
            Long corpNo = Long.valueOf(corpNoStr.trim());
            
            // 기존 회사 정보 조회
            CorpEntity corp = corpImageService.getCorpInfo(corpNo);
            if (corp == null) {
                model.addAttribute("error", "존재하지 않는 회사입니다.");
                return "corp/image_upload";
            }
            
            model.addAttribute("corp", corp);
            model.addAttribute("corpNo", corpNo);
            
        } catch (NumberFormatException e) {
            model.addAttribute("error", "유효한 회사 번호를 입력해주세요.");
        } catch (Exception e) {
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
        }
        
        return "corp/image_upload";
    }

    /**
     * 로고 이미지 업로드/수정
     */
    @PostMapping("/image/logo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadLogo(
            @RequestParam("corpNo") String corpNoStr,
            @RequestParam("logoFile") MultipartFile logoFile) {

        Map<String, Object> response = new HashMap<>();

        try {
            // corpNo 검증
            if (corpNoStr == null || corpNoStr.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "회사 번호가 필요합니다.");
                return ResponseEntity.ok(response);
            }

            Long corpNo;
            try {
                corpNo = Long.valueOf(corpNoStr.trim());
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "유효한 회사 번호를 입력해주세요.");
                return ResponseEntity.ok(response);
            }

            // 파일 검증
            if (logoFile == null || logoFile.isEmpty()) {
                response.put("success", false);
                response.put("message", "로고 파일을 선택해주세요.");
                return ResponseEntity.ok(response);
            }

            // 로고 업로드/수정
            String logoPath = corpImageService.uploadLogo(corpNo, logoFile);
            
            response.put("success", true);
            response.put("message", "로고가 성공적으로 업로드되었습니다.");
            response.put("logoPath", logoPath);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "로고 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 회사 이미지 업로드/수정
     */
    @PostMapping("/image/corp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadCompanyImage(
            @RequestParam("corpNo") String corpNoStr,
            @RequestParam("imageFile") MultipartFile imageFile) {

        Map<String, Object> response = new HashMap<>();

        try {
            // corpNo 검증
            if (corpNoStr == null || corpNoStr.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "회사 번호가 필요합니다.");
                return ResponseEntity.ok(response);
            }

            Long corpNo;
            try {
                corpNo = Long.valueOf(corpNoStr.trim());
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "유효한 회사 번호를 입력해주세요.");
                return ResponseEntity.ok(response);
            }

            // 파일 검증
            if (imageFile == null || imageFile.isEmpty()) {
                response.put("success", false);
                response.put("message", "이미지 파일을 선택해주세요.");
                return ResponseEntity.ok(response);
            }

            // 회사 이미지 업로드/수정
            String imagePath = corpImageService.uploadCompanyImage(corpNo, imageFile);
            
            response.put("success", true);
            response.put("message", "회사 이미지가 성공적으로 업로드되었습니다.");
            response.put("imagePath", imagePath);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 로고 삭제
     */
    @PostMapping("/image/logo/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteLogo(
            @RequestParam("corpNo") String corpNoStr) {

        Map<String, Object> response = new HashMap<>();

        try {
            // corpNo 검증
            Long corpNo = validateCorpNo(corpNoStr, response);
            if (corpNo == null) {
                return ResponseEntity.ok(response);
            }

            // 로고 삭제
            corpImageService.deleteLogo(corpNo);
            
            response.put("success", true);
            response.put("message", "로고가 삭제되었습니다.");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "로고 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 회사 이미지 삭제
     */
    @PostMapping("/image/corp/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCompanyImage(
            @RequestParam("corpNo") String corpNoStr) {

        Map<String, Object> response = new HashMap<>();

        try {
            // corpNo 검증
            Long corpNo = validateCorpNo(corpNoStr, response);
            if (corpNo == null) {
                return ResponseEntity.ok(response);
            }

            // 회사 이미지 삭제
            corpImageService.deleteCompanyImage(corpNo);
            
            response.put("success", true);
            response.put("message", "회사 이미지가 삭제되었습니다.");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "이미지 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * corpNo 검증 공통 메서드
     */
    private Long validateCorpNo(String corpNoStr, Map<String, Object> response) {
        if (corpNoStr == null || corpNoStr.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "회사 번호가 필요합니다.");
            return null;
        }

        try {
            return Long.valueOf(corpNoStr.trim());
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "유효한 회사 번호를 입력해주세요.");
            return null;
        }
    }
}