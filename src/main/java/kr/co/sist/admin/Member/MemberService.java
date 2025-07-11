package kr.co.sist.admin.Member;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	@Autowired
	private MemberRepository mer;
	
	public List<MemberEntity> searchAll() {
		return mer.findAll();
	}
	
	public List<MemberEntity> searchNameMember(String name) {
		return mer.findByNameContaining(name);
	}
	
	public List<MemberEntity> searchGenderMember(String gender) {
		return mer.findByGender(gender);
	}
	
	public List<MemberEntity> searchStatusMember(String status) {
		return mer.findByactiveStatus(Integer.parseInt(status));
	}
}
