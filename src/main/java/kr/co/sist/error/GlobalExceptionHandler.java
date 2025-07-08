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
	
	
	
}
