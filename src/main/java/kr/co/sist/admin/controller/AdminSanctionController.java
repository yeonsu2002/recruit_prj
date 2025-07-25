package kr.co.sist.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.admin.Member.MemberEntity;
import kr.co.sist.admin.Member.MemberService;
import kr.co.sist.admin.email.EmailService;
import kr.co.sist.util.CipherUtil;

@Controller
public class AdminSanctionController {
	@Autowired
	private MemberService ms;
	@Autowired
	private CipherUtil cipherUtil;

	 
	 @GetMapping("/admin_sanction")
		public String SearchResume(@RequestParam String email,
				Model model) {
			MemberEntity member=ms.searchEmailMember(email);
			
				try {
	       if (member.getPhone() != null) {
	           member.setPhone(cipherUtil.decryptText(member.getPhone()));
	       }
				} catch (Exception e) {
					e.printStackTrace();
				}  // 복호화

			// filtered 리스트를 모델에 추가해서 뷰에 전달
			model.addAttribute("member", member);
			return "admin/admin_sanction";
		}
	 
	 @GetMapping("/admin_sanction_cancel")
		public String sanctionCancel(@RequestParam String email,
				Model model) {
		 ms.sanctionCancel(email);
		 MemberEntity member=ms.searchEmailMember(email);
			
				try {
	       if (member.getPhone() != null) {
	           member.setPhone(cipherUtil.decryptText(member.getPhone()));
	       }
				} catch (Exception e) {
					e.printStackTrace();
				}  // 복호화

			// filtered 리스트를 모델에 추가해서 뷰에 전달
			model.addAttribute("member", member);
			return "admin/admin_member_detail";
		}

		@Autowired
		private EmailService es;
	 @GetMapping("/send_sanction")
		public String SendSaction(@RequestParam(required = false) String email,
				@RequestParam String name,
				@RequestParam String content) {
		 ms.sanctionMember(email);
		 es.sendSanctionEmail(email,name,content);
			return "redirect:/admin_member";
		}
	 
}
