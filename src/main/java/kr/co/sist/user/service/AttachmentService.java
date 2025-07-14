package kr.co.sist.user.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.AttachmentEntity;
import kr.co.sist.user.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService {

	@Value("${upload.fileDir}")
	private String fileDir;

	private final AttachmentRepository attachmentRepos;

	/**
	 * 첨부파일 업로드
	 * @param file
	 * @param user
	 * @return
	 */
	public AttachmentEntity addAttachment(MultipartFile file, UserDTO user) {

		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		// 1.유저 정보 추출
		String email = user.getEmail();

		// 2. 파일 정보 추출
		String originalName = file.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf('.') + 1);
		long fileSize = file.getSize();

		// 3. 첨부파일 엔티티 생성 및 저장
		AttachmentEntity attachment = new AttachmentEntity();
		attachment.setEmail(email);
		attachment.setFileName(originalName);
		attachment.setFileSize(fileSize);
		attachment.setFileType(extension);
		attachment.setCreatedAt(now.toString());

		attachmentRepos.save(attachment);
		attachment.setCreatedAt(attachment.getCreatedAt().substring(0,10));

		// 4. 파일 저장 경로 설정 및 실제 파일 저장
		File uploadDir = new File(fileDir);
		if (!uploadDir.exists())
			uploadDir.mkdir(); // 하위 폴더까지 생성

		File destination = new File(uploadDir, originalName);
		try {
			file.transferTo(destination);
			return attachment;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}//searchAllAttachment
	
	/**
	 * 첨부파일seq로 첨부파일 찾기
	 * @param attachmentSeq
	 * @return
	 */
	public AttachmentEntity searchOneAttachment(int attachmentSeq) {
		
		return attachmentRepos.findById(attachmentSeq).orElse(null);
	}
	
	/**
	 * 해당 첨부파일 삭제하기
	 * @param attachmentSeq
	 */
	public void removeAttachment(int attachmentSeq) {
		
		attachmentRepos.deleteById(attachmentSeq);
	}//removeAttachment
	
}//class
