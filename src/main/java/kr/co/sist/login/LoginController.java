package kr.co.sist.login;

import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.util.CipherUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

//@RequiredArgsConstructor -> 생성자주입방식 생성자 코드 대신 어노테이션
@Controller
public class LoginController {

	private final loginJoinService ljs; 
	
	//생성자주입방식
	public LoginController(loginJoinService ljs) {
		this.ljs = ljs;
	}
	
  /**
   * 기업회원 로그인 페이지로 이동
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
		// loginJoinService.registerUser(userDTO);
  	// uDTO.setIP(request.getRemoteAddr());
  	
  	// redirectAttr.addFlashAttribute("msg", "회원가입이 완료되었습니다. 로그인 해주세요.");
  	
  	return "redirect:/user/login";
  }
  
  /**
   * 기업회원 가입 프로세스
   */
  @PostMapping("/corp/joinProcess")
	public String corpJoinProcess(/* @ModelAttribute TotalCorpDTO totalCorpDTO */) {
  	//통합DTO로 받고, Service에서 분리시켜 각 Entity생성
		/* loginJoinService.registerCorp(totalCorpDTO); */
  	
  	return "redirect:/user/login";
  }
  
  /**
   * 이메일 중복 체크
   */
  
  
  
}
