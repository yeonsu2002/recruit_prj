package kr.co.sist.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.PositionCodeDTO;

@Mapper
public interface ResumeMapper {

	public List<PositionCodeDTO> selectAllPositionCode();
}
