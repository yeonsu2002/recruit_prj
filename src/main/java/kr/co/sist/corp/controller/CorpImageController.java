package kr.co.sist.corp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/corp")
public class CorpImageController {
    
    /**
     * 이미지 등록 페이지 이동
     */
    @GetMapping("/image")
    public String imageForm(Model model) {
        // 확인용 - 실제로는 세션에서 기업 정보를 가져와야 함
    	
        return "corp/image_upload";
    }
    
    /**
     * 이미지 업로드 처리
     */
    @PostMapping("/image/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImages(
            @RequestParam("corpNo") Long corpNo,
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 확인용 - 실제 파일 처리 로직 없음
            response.put("success", true);
            response.put("message", "이미지 업로드가 완료되었습니다.");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "이미지 업로드 중 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 이미지 삭제
     */
    @PostMapping("/image/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImage(
            @RequestParam("corpNo") Long corpNo,
            @RequestParam("imageId") Long imageId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 확인용 - 실제 삭제 로직 없음
            response.put("success", true);
            response.put("message", "이미지가 삭제되었습니다.");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "이미지 삭제 중 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
}