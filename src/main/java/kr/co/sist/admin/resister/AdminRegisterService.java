package kr.co.sist.admin.resister;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.admin.AdminEntity;
import kr.co.sist.admin.AdminRepository;

@Service
public class AdminRegisterService {
	@Autowired
	private AdminRepository ar;
	
	/**
	 * 관리자의 모든 정보를 조회
	 * @return
	 */
	public List<AdminEntity> searchAllAdmin(){
		List<AdminEntity> list = ar.findAll();
		
		return list;
	}
	
	
	
	
}	
