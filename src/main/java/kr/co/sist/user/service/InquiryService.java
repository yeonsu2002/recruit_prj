package kr.co.sist.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.sist.user.dto.InquiryRequestDTO;
import kr.co.sist.user.dto.InquiryResponseDTO;
import kr.co.sist.user.entity.InquiryEntity;
import kr.co.sist.user.repository.InquiryRepository;

@Service
@Transactional
public class InquiryService {
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;
    
    // 문의 저장 (파일 포함)
    public void saveInquiry(InquiryRequestDTO request, MultipartFile file) {
        InquiryEntity inquiry = new InquiryEntity();
        inquiry.setEmail(request.getEmail());
        
        // adminId가 null이면 기본값 설정 (또는 null 허용)
        if (request.getAdminId() == null) {
            inquiry.setAdminId(null); // 나중에 관리자 배정
        } else {
            inquiry.setAdminId(request.getAdminId());
        }
        
        inquiry.setTitle(request.getTitle());
        inquiry.setContent(request.getContent());
        inquiry.setCategory(request.getCategory());
        inquiry.setAnswerStat("N"); // 답변 대기 상태
        inquiry.setRegsDate(LocalDateTime.now());
        
        // 파일 처리
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file);
            inquiry.setFilePath(fileName);
        }
        
        inquiryRepository.save(inquiry);
    }
    
    // 문의 저장 (파일 없이)
    public void saveInquiry(InquiryRequestDTO request) {
        saveInquiry(request, null);
    }
    
    // 문의 생성 (createInquiry 메서드 수정)
    public void createInquiry(InquiryRequestDTO dto) {
        InquiryEntity inquiry = new InquiryEntity();
        inquiry.setEmail(dto.getEmail());
        
        // adminId가 null이면 기본값 설정 또는 null 허용
        if (dto.getAdminId() == null) {
            inquiry.setAdminId(getDefaultAdminId()); // 기본 관리자 ID 설정
            // 또는 inquiry.setAdminId(null); // null 허용하려면 이렇게
        } else {
            inquiry.setAdminId(dto.getAdminId());
        }
        
        inquiry.setTitle(dto.getTitle());
        inquiry.setContent(dto.getContent());
        inquiry.setCategory(dto.getCategory());
        inquiry.setAnswerStat("N");
        inquiry.setRegsDate(LocalDateTime.now());
        
        // 파일 처리
        if (dto.getAttachFile() != null && !dto.getAttachFile().isEmpty()) {
            inquiry.setFilePath(dto.getAttachFile());
        }
        
        inquiryRepository.save(inquiry);
    }
    
    // 문의 목록 조회 (페이징, 필터링)
    @Transactional(readOnly = true)
    public Page<InquiryResponseDTO> getInquiries(PageRequest pageRequest, String category, String answerStat) {
        Specification<InquiryEntity> spec = Specification.where(null);
        
        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        
        if (answerStat != null && !answerStat.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("answerStat"), answerStat));
        }
        
        Page<InquiryEntity> inquiries = inquiryRepository.findAll(spec, pageRequest);
        return inquiries.map(this::convertToResponse);
    }
    
    // 문의 상세 조회
    @Transactional(readOnly = true)
    public InquiryResponseDTO getInquiryById(Long askSeq) {
        InquiryEntity inquiry = inquiryRepository.findById(askSeq)
                .orElseThrow(() -> new IllegalArgumentException("문의를 찾을 수 없습니다."));
        return convertToResponse(inquiry);
    }
    
    // 문의 답변 저장
    public void saveAnswer(Long askSeq, String answer, String adminId) {
        InquiryEntity inquiry = inquiryRepository.findById(askSeq)
                .orElseThrow(() -> new IllegalArgumentException("문의를 찾을 수 없습니다."));
        
        inquiry.setAnswer(answer);
        inquiry.setAnswerStat("Y"); // 답변 완료 상태
        inquiry.setAdminId(adminId); // 답변한 관리자 ID
        
        inquiryRepository.save(inquiry);
    }
    
    // 파일 저장
    public String saveFile(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath + fileName);
            
            // 디렉토리 생성
            Files.createDirectories(filePath.getParent());
            
            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }
    
    // 사용자별 문의 목록 조회
    @Transactional(readOnly = true)
    public Page<InquiryResponseDTO> getInquiriesByUser(String userEmail, PageRequest pageRequest) {
        Page<InquiryEntity> inquiries = inquiryRepository.findByEmailOrderByRegsDateDesc(userEmail, pageRequest);
        return inquiries.map(this::convertToResponse);
    }
    
    // 카테고리별 문의 통계
    @Transactional(readOnly = true)
    public Map<String, Long> getInquiryStatsByCategory() {
        List<Object[]> stats = inquiryRepository.getInquiryStatsByCategory();
        return stats.stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> (Long) row[1]
                ));
    }
    
    // 답변 상태별 문의 통계
    @Transactional(readOnly = true)
    public Map<String, Long> getInquiryStatsByAnswerStatus() {
        List<Object[]> stats = inquiryRepository.getInquiryStatsByAnswerStatus();
        return stats.stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> (Long) row[1]
                ));
    }
    
    // Entity to Response DTO 변환
    private InquiryResponseDTO convertToResponse(InquiryEntity inquiry) {
        InquiryResponseDTO response = new InquiryResponseDTO();
        response.setAskSeq(inquiry.getAskSeq());
        response.setEmail(inquiry.getEmail());
        response.setAdminId(inquiry.getAdminId());
        response.setTitle(inquiry.getTitle());
        response.setContent(inquiry.getContent());
        response.setAnswer(inquiry.getAnswer());
        response.setRegsDate(inquiry.getRegsDate());
        response.setCategory(inquiry.getCategory());
        response.setAnswerStat(inquiry.getAnswerStat());
        return response;
    }
    
    // 기본 관리자 ID 반환
    private String getDefaultAdminId() {
        // 기본 관리자 ID 반환 또는 사용 가능한 관리자 찾기
        return "1"; // 예시 - 실제 존재하는 관리자 ID로 변경
    }
}