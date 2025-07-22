package kr.co.sist.corp.service;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.ResumeDetailDTO;
import kr.co.sist.corp.mapper.ResumeDetailMapper;
import kr.co.sist.util.CipherUtil;

@Service
public class ResumeDetailServiceImpl implements ResumeDetailService {

    private final ResumeDetailMapper resumeDetailMapper;
    private final CipherUtil cu;

    public ResumeDetailServiceImpl(ResumeDetailMapper resumeDetailMapper, CipherUtil cu) {
        this.resumeDetailMapper = resumeDetailMapper;
        this.cu = cu;
    }

    @Override
    public ResumeDetailDTO getResumeDetail(Long resumeSeq) {
        ResumeDetailDTO dto = resumeDetailMapper.selectResumeBasic(resumeSeq);

        if (dto != null) {
          dto.setPositions(resumeDetailMapper.selectPositionsByResume(resumeSeq));
          dto.setSkills(resumeDetailMapper.selectTechStacksByResume(resumeSeq));
          dto.setEducations(resumeDetailMapper.selectEducationsByResume(resumeSeq));
          dto.setCareers(resumeDetailMapper.selectCareersByResume(resumeSeq));
          dto.setProjects(resumeDetailMapper.selectProjectsByResume(resumeSeq));
          dto.setAdditionals(resumeDetailMapper.selectAdditionalsByResume(resumeSeq));
          dto.setIntroductions(resumeDetailMapper.selectIntroductionsByResume(resumeSeq));
          dto.setLinks(resumeDetailMapper.selectLinksByResume(resumeSeq));

          // 복호화 처리
          if (dto.getMember() != null) {
              String encryptedPhone = dto.getMember().getPhone();
              if (encryptedPhone != null) {
                  String decryptedPhone = cu.decryptText(encryptedPhone);
                  dto.getMember().setPhone(decryptedPhone);
              }//end if
          }//end if
          
      }//end if

      return dto;
  }
}