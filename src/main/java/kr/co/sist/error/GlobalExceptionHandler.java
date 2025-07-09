package kr.co.sist.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IllegalStateException.class)
  public String handleIllegalState(IllegalStateException ex, RedirectAttributes rttr) {
    rttr.addFlashAttribute("errorMessage", ex.getMessage()); //컨트롤러에서 ex.getMessage() 내용을 정해
    return "redirect:/corp/join"; 
	}
	
	//@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes rttr) {
		rttr.addFlashAttribute("errorMessage", ex.getMessage());
		return "redirect:/login";
	}
	
	//로그인 커스텀 예외처리 
	@ExceptionHandler(LoginException.class)
	public String handleLoginException(LoginException le, RedirectAttributes rttr) {
		rttr.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
		return "redirect:/login";
	}
	
	
	
}
