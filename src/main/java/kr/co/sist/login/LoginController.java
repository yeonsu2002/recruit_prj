package kr.co.sist.login;
import kr.co.sist.admin.controller.AdminController;
import kr.co.sist.corp.dto.CorpDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.dto.UserEntity;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

//@RequiredArgsConstructor -> 생성자주입방식 생성자 코드 대신 어노테이션
@Controller
public class LoginController {

    private final AdminController adminController;

	private final loginJoinService ljs; 
	
	private final Environment env;
	
	//생성자주입방식
	public LoginController(loginJoinService ljs, Environment env, AdminController adminController) {
		this.ljs = ljs;
		this.env = env;
		this.adminController = adminController;
	}
	
  /**
   * 회원 로그인 페이지로 이동
   * @return
   */
  @GetMapping("/login")
  public String goCorpLoginForm() {
    return "login/login_form";
  }
    
  /**
   * 일반회원 로그인 처리
   * @return
   */
  @PostMapping("user/loginProcess")
  public String memberLoginProcess() {
    System.out.println("개인회원 로그인 처리");
    return "redirect:/user/main";
  }
  
  /**
   * 로그인 처리 후 유저 메인페이지 이동
   * @return
   */
  @GetMapping("user/main")
  public String goUserMainPage() {
    return "user/main_page";
  }
  
  /**
   * 기업회원 로그인 처리
   * @return
   */
  @PostMapping("corp/loginProcess")
  public String corpLoginProcess(String email, String password) {
    System.out.println("기업회원 로그인 처리");
    return "redirect:/corp/main";
  }
  
  /**
   * 기업회원 로그인 처리후 기업메인페이지 이동
   * @return
   */
  @GetMapping("corp/main")
  public String goCorpMainPage() {
    return "corp/main_page";
  }
  
  /**
   * 일반회원 가입 페이지로 이동
   * @return
   */
  @GetMapping("/user/join")
  public String goJoinUserForm() {
    return "login/joinUserForm";
  }
  
  /**
   * 기업회원 가입 페이지로 이동
   * @return
   */
  @GetMapping("/corp/join")
  public String goJoinCorpForm() {
    return "login/joinCorpForm";
  }
  
  /**
   * 일반회원 가입 프로세스
   */
  @PostMapping("/user/joinProcess")
	public String userJoinProcess(@ModelAttribute UserDTO uDTO, HttpServletRequest request, Model model, RedirectAttributes redirectAttr) {
    // model에 Entity를 담는건 아니야. DTO를 담아. Entity는 보안문제도 있고 민감한 객체야. 
    ljs.registerUser(uDTO);
    //redirect:로 리다이렉트되면 Model에 담긴 값은 유지되지 않아
    //대신 RedirectAttributes.addFlashAttribute()를 써야 유지됨. -> 이건 세션에 잠깐 값을 저장했다가, 리다이렉트 이후 자동으로 한 번만 Model에 옮겨주는 기능. 리다이렉트 전용 Model!
  	redirectAttr.addFlashAttribute("msg", "회원가입이 완료되었습니다. 로그인 해주세요.");
  	
  	return "redirect:/login";
  }
  
  /**
   * 기업회원 가입 프로세스 (파일저장은 개발시 임시로 프로젝트 내에 저장하지만, 배포시에는 C:/upload/... 같은 외부 폴더 사용할 것!)
   */
  @PostMapping("/corp/joinProcess")
	public String corpJoinProcess(@ModelAttribute UserCorpDTO ucDTO, @RequestParam("upfile") MultipartFile file, RedirectAttributes redirectAttr) {
    
    Long corpNo = ucDTO.getCorpNo();
    
    String projectPath = new File("").getAbsolutePath(); // 현재 프로젝트 루트
    String resourcePath = projectPath + "/src/main/resources/static/corp/biz_cert";
    
    // 디렉토리가 없으면 생성
    File folder = new File(resourcePath);
    if (!folder.exists()) {
        folder.mkdirs();
    }

    String newFileName = null;
    File saveFile = null;
    
    if(!file.isEmpty()) {
      String originFileName = file.getOriginalFilename();
      //저장할 파일명: cert_사업자번호_파일이름
      newFileName = "cert_" + corpNo + "_" + originFileName;
      
      // 최종 경로
      saveFile = new File(resourcePath + File.separator + newFileName);
      
      try {
        file.transferTo(saveFile); //실제 저장
        ucDTO.setUpfileName(originFileName); //파일명저장 
        
      } catch (IOException e) {
        e.printStackTrace();
        redirectAttr.addAttribute("msg", "파일 업로드 실패");
        return "redirect:/corp/join"; // 회원가입 페이지로 다시 이동
      }
    } // end if file
    
    // DB 저장 처리
    try {
      ljs.registerCorp(ucDTO, newFileName);
      redirectAttr.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
      return "redirect:/login";

    } catch (Exception e) {
      e.printStackTrace();

      // DB 저장 실패 시 업로드된 파일 삭제
      if (saveFile != null && saveFile.exists()) {
        boolean deleted = saveFile.delete();
        if (deleted) {
          System.out.println("업로드된 파일 삭제 완료: " + newFileName);
        } else {
          System.out.println("업로드된 파일 삭제 실패: " + newFileName);
        }
      }

      // 구체적인 에러 메시지 설정
      String errorMsg = "회원가입 처리 중 오류가 발생했습니다.";
      if (e instanceof IllegalArgumentException) {
        errorMsg = e.getMessage(); // 예: "기업이 존재하지 않습니다."
      } else if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
        errorMsg = "이미 존재하는 사업자번호 또는 이메일입니다.";
      }

      redirectAttr.addFlashAttribute("msg", errorMsg);
      return "redirect:/corp/join"; // 회원가입 페이지로 다시 이동
    }
}
  /** 이메일 구현시 추가할거
   * @PostMapping("/mail/sendVfCode")
      public ResponseEntity<String> sendVerificationCode(
          @RequestParam("to") String email,
          @RequestParam(value = "action", required = false) String action
      ) {
          // 이메일 발송 로직
          try {
              // 이메일 발송 처리
              return ResponseEntity.ok("success");
          } catch (Exception e) {
              return ResponseEntity.status(500).body("fail");
          }
      }
   */
  
  /**
   * 이메일 중복 체크
   */
  
  @GetMapping("/corp/testForm")
  public String testForm() {
    return "login/joinCorpFormOnlyHTML";
  }
  
}
