package kr.co.sist.corp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.corp.repository.CorpImageRepository;

@Service
@Transactional
public class CorpImageService {

    @Autowired
    private CorpImageRepository corpRepository;

    // 파일 업로드 경로 (application.properties에서 설정)
    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;

    // 허용되는 이미지 파일 확장자
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    // 최대 파일 크기 (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 회사 정보 조회
     */
    public CorpEntity getCorpInfo(Long corpNo) {
        Optional<CorpEntity> corp = corpRepository.findById(corpNo);
        return corp.orElse(null);
    }

    /**
     * 로고 이미지 업로드/수정
     */
    public String uploadLogo(Long corpNo, MultipartFile logoFile) throws IOException {
        // 회사 존재 여부 확인
        CorpEntity corp = corpRepository.findById(corpNo)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));

        // 파일 유효성 검사
        validateImageFile(logoFile);

        // 기존 로고 파일 삭제
        if (StringUtils.hasText(corp.getCorpLogo())) {
            deleteExistingFile(corp.getCorpLogo());
        }

        // 새 파일 저장
        String savedFileName = saveFile(logoFile, "logo");
        
        // DB 업데이트
        corp.setCorpLogo(savedFileName);
        corpRepository.save(corp);

        return savedFileName;
    }

    /**
     * 회사 이미지 업로드/수정
     */
    public String uploadCompanyImage(Long corpNo, MultipartFile imageFile) throws IOException {
        // 회사 존재 여부 확인
        CorpEntity corp = corpRepository.findById(corpNo)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));

        // 파일 유효성 검사
        validateImageFile(imageFile);

        // 기존 이미지 파일 삭제
        if (StringUtils.hasText(corp.getCorpImg())) {
            deleteExistingFile(corp.getCorpImg());
        }

        // 새 파일 저장
        String savedFileName = saveFile(imageFile, "company");
        
        // DB 업데이트
        corp.setCorpImg(savedFileName);
        corpRepository.save(corp);

        return savedFileName;
    }

    /**
     * 로고 삭제
     */
    public void deleteLogo(Long corpNo) {
        CorpEntity corp = corpRepository.findById(corpNo)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));

        // 파일 삭제
        if (StringUtils.hasText(corp.getCorpLogo())) {
            deleteExistingFile(corp.getCorpLogo());
            
            // DB 업데이트
            corp.setCorpLogo(null);
            corpRepository.save(corp);
        }
    }

    /**
     * 회사 이미지 삭제
     */
    public void deleteCompanyImage(Long corpNo) {
        CorpEntity corp = corpRepository.findById(corpNo)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));

        // 파일 삭제
        if (StringUtils.hasText(corp.getCorpImg())) {
            deleteExistingFile(corp.getCorpImg());
            
            // DB 업데이트
            corp.setCorpImg(null);
            corpRepository.save(corp);
        }
    }

    /**
     * 이미지 파일 유효성 검사
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 선택되지 않았습니다.");
        }

        // 파일 크기 검사
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 5MB 이하여야 합니다.");
        }

        // 파일 확장자 검사
        String originalFileName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFileName)) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }

        String extension = getFileExtension(originalFileName).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (허용: jpg, jpeg, png, gif, bmp, webp)");
        }

        // Content Type 검사
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    /**
     * 파일 저장
     */
    private String saveFile(MultipartFile file, String prefix) throws IOException {
        // 업로드 디렉토리 생성
        createUploadDirectory();

        // 고유한 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String savedFileName = prefix + "_" + UUID.randomUUID().toString() + "." + extension;

        // 파일 저장
        Path filePath = Paths.get(uploadPath, savedFileName);
        Files.copy(file.getInputStream(), filePath);

        return savedFileName;
    }

    /**
     * 기존 파일 삭제
     */
    private void deleteExistingFile(String fileName) {
        if (StringUtils.hasText(fileName)) {
            try {
                Path filePath = Paths.get(uploadPath, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 파일 삭제 실패해도 계속 진행
                System.err.println("파일 삭제 실패: " + fileName);
            }
        }
    }

    /**
     * 업로드 디렉토리 생성
     */
    private void createUploadDirectory() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String fileName) {
        if (StringUtils.hasText(fileName) && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
}