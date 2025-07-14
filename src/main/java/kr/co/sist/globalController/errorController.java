package kr.co.sist.globalController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class errorController {

	@GetMapping("/accessDenied")
	public String errorPage() {
		return "error/accessDenied";
	}
}
