package kr.co.sist.corp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.corp.dto.TalentPoolDTO;

@Mapper
public interface TalentPoolMapper {
	
	List<TalentPoolDTO> selectAllTalents();
	
	
}
