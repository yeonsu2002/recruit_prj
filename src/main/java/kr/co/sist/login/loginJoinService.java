package kr.co.sist.login;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.dto.UserEntity;
import kr.co.sist.util.CipherUtil;

@Service
public class loginJoinService {
	
	private final UserRepository ur;
	private final CorpRepository cr;
	private final CipherUtil cu;
	
	public loginJoinService(UserRepository ur, CorpRepository cr, CipherUtil cu) {
		this.ur = ur;
		this.cr = cr;
		this.cu = cu;
	}
	
	public String saveUser(UserDTO uDTO) {
		UserEntity ue = new UserEntity();
		ue.setEmail(uDTO.getEmail());
		ue.setCorpEntity(null);
		ue.setPassword( cu.hashText(uDTO.getPassword()));
		//이어서
		ur.save(uDTO);
		
		return "";
	}

	
	//private ??Mapper ??; 
	
	//생성자 주입방식
	public loginJoinService(CipherUtil cu) {
		this.cu = cu;
	}

	/**
	 * mapper를 쓰든 repository를 쓰든 암호화는 과정은 똑같다.
	 * mapper는 DTO와 자도연동되고, repository는 Entity와 자동연동되기 때문에 set하는 자료형만 다를뿐.
	 * @return
	 */
	@Transactional
	public boolean registerUser(/* UserDTO uDTO */) {
		boolean flag = false;
		//이메일 = 이메일 + 도메인
		//uDTO.setEmail = (uDTO.getEmail() + "@" + uDTO.getDomain()); 

		//일방향 hash : 비밀번호
		//uDTO.setPassword(cu.hashText(uDTO.getPassword));
		
		//암호문 : 이메일, 이름, 전화번호, 주소(ISMS) 등 식별가능한 모든 개인정보
		//uDTO.setName(cu.cipherText(uDTO.getName()));
		// ....
		
		//flag = ??mapper.insertMember(uDTO) == 1;
		
		//-------------Entity라면?????
		//UserEntity userEntity = new UserEntity();
		//userEntity.setName(cu.cipherText(uDTO.getName()));
		//...
		// userRepository.save(userEntity);
		
		return flag;
	}
	
	
	
	
}
