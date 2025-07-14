package kr.co.sist.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.JobApplicationDTO;

@Mapper
public interface JobApplicationMapper {

    /**
     * 지원 정보를 DB에 삽입
     * @param jobApplicationDTO - 지원 정보 DTO
     */
   public void insertJobApplication(JobApplicationDTO jobApplicationDTO);


}
