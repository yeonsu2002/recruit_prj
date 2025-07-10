package kr.co.sist.corp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.corp.dto.TalentPoolDTO;
import kr.co.sist.corp.mapper.TalentPoolMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TalentPoolServiceImpl implements TalentPoolService {
	private final TalentPoolMapper talentPoolMapper;;

	@Override
	public List<TalentPoolDTO> getAllTalents() {
		return talentPoolMapper.selectAllTalents();
		
	}
}
