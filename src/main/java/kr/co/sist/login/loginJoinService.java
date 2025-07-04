package kr.co.sist.login;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

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

    @Transactional
    public UserEntity registerUser(UserDTO uDTO) {
        UserEntity ue = new UserEntity();
        
        try {
            // 기본 정보 설정
            ue.setEmail(uDTO.getEmail());
            ue.setCorpEntity(null);
            ue.setPassword(cu.hashText(uDTO.getPassword()));
            ue.setName(cu.cipherText(uDTO.getName()));
            ue.setRole("ROLE_USER");
            ue.setPhone(cu.cipherText(uDTO.getPhone()));
            
            // 현재 시간 설정
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ue.setRegDt(now.format(dtf));
            
            // 로그인 관련 초기값 설정
            ue.setLoginFailCnt(0);
            ue.setIsLocked(0);
            
            // lockEndDt (Date -> String 변환, null 체크)
            if (uDTO.getLockEndDt() != null) {
                ue.setLockEndDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uDTO.getLockEndDt()));
            } else {
                ue.setLockEndDt(null);
            }
            
            // pwChangeDt (Date -> String 변환, null 체크)
            if (uDTO.getPwChangeDt() != null) {
                ue.setPwChangeDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uDTO.getPwChangeDt()));
            } else {
                ue.setPwChangeDt(null);
            }
            
            // lastLoginDt (Date -> String 변환, null 체크)
            if (uDTO.getLastLoginDt() != null) {
                ue.setLastLoginDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uDTO.getLastLoginDt()));
            } else {
                ue.setLastLoginDt(null);
            }
            
            ue.setLastLoginIp(null);
            
            // 주소 정보 설정
            ue.setZipcode(uDTO.getZipcode());
            ue.setRoadAddress(uDTO.getRoadAddress());
            ue.setDetailAddress(uDTO.getDetailAddress());
            
            // 생년월일 변환
            if (uDTO.getBirth() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String birthStr = sdf.format(uDTO.getBirth());
                ue.setBirth(birthStr);
            } else {
              ue.setBirth(null);
            }
            
            // 기타 정보 설정
            ue.setGender(uDTO.getGender());
            ue.setPwResetRequired(0);
            ue.setProfileImage(null);
            ue.setActiveStatus(0);
            
            System.out.println("저장 전 UserEntity: " + ue);
            
            // 저장 전 검증
            System.out.println("UserRepository 존재 여부: " + (ur != null));
            System.out.println("Email 값: " + ue.getEmail());
            
            // DB 저장 시도
            System.out.println("저장 시도 시작...");
            UserEntity savedEntity = ur.save(ue);
            System.out.println("저장 완료!");
            
            if (savedEntity != null) {
                System.out.println("저장 후 UserEntity: " + savedEntity);
                return savedEntity;
            } else {
                System.err.println("저장 후 반환된 Entity가 null입니다!");
                throw new RuntimeException("저장 후 Entity가 null로 반환됨");
            }
            
        } catch (Exception e) {
            System.err.println("=== 회원가입 오류 상세 정보 ===");
            System.err.println("오류 메시지: " + e.getMessage());
            System.err.println("오류 타입: " + e.getClass().getSimpleName());
            
            if (e.getCause() != null) {
                System.err.println("원인: " + e.getCause().getMessage());
            }
            
            e.printStackTrace();
            
            // 더 구체적인 오류 정보 출력
            if (e.getMessage() != null) {
                if (e.getMessage().contains("constraint")) {
                    System.err.println("제약조건 위반 가능성이 있습니다.");
                } else if (e.getMessage().contains("duplicate")) {
                    System.err.println("중복 데이터 오류 가능성이 있습니다.");
                } else if (e.getMessage().contains("null")) {
                    System.err.println("NULL 값 오류 가능성이 있습니다.");
                }
            }
            
            throw new RuntimeException("회원 저장 실패: " + e.getMessage(), e);
        }
    }
}