package kr.co.sist.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.sist.login.UserRepository;
import kr.co.sist.user.dto.ResumeRequestDTO;
import kr.co.sist.user.entity.UserEntity;
import kr.co.sist.user.service.AttachmentService;
import kr.co.sist.user.service.PositionCodeService;
import kr.co.sist.user.service.ResumeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CreateTempDate {

	private final ResumeService rServ;
	private final AttachmentService aServ;
	private final PositionCodeService pcs;

	private final UserRepository userRepos;
	
	private final ObjectMapper objMapper;
	private final CipherUtil cu;

	// 이력서 저장(수정)하기
	@PostMapping("/user/tempresume/resumeSubmit")
	@ResponseBody
	public Map<String, String> resumeSubmit(@RequestParam(required = false) MultipartFile profileImage,
			@RequestParam("resumeData") String resumeDataJson) {
		
		for(int i = 1; i <= 1000; i++) {
			UserEntity user = new UserEntity();
			user.setEmail("testuser"+i+"@test.com");
			int resumeSeq = rServ.addResume(user);
			
			Map<String, String> result = new HashMap<>();
			
			try {
				ResumeRequestDTO rdd = objMapper.readValue(resumeDataJson, ResumeRequestDTO.class);
				rServ.modifyResume(rdd, profileImage, rdd.getBasicInfo().getResumeSeq());
				result.put("result", "success");
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", "error");
			}
			
			return result;
		}// resumeSubmit
	}
}
