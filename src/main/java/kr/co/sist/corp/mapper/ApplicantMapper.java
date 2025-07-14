package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.ApplicantDTO;

@Mapper
public interface ApplicantMapper {

	public List<ApplicantDTO> selectAllApplicant(long corpNo);
}
