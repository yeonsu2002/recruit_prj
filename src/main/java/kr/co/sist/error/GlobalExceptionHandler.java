package kr.co.sist.error;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
      Map<String, Object> errorBody = new LinkedHashMap<>();
      errorBody.put("timestamp", LocalDateTime.now());
      errorBody.put("status", HttpStatus.NOT_FOUND.value());
      errorBody.put("error", "Not Found");
      errorBody.put("message", ex.getMessage());

      //이거 아직 사용자쪽에 뿌리는거 안만들었어.
      return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
  }	
	
	
}
