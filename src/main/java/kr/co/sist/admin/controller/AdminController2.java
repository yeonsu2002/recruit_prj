package kr.co.sist.admin.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.admin.dashboard.DashboardService;
import kr.co.sist.admin.email.EmailService;
import kr.co.sist.admin.resume.ResumeService;
import kr.co.sist.admin.Member.*;
import kr.co.sist.admin.ask.AdminInquiryDTO;
import kr.co.sist.admin.corp.CorpService;
import kr.co.sist.corp.dto.CorpEntity;
import kr.co.sist.user.dto.ResumeDTO;
import kr.co.sist.user.dto.UserDTO;
import kr.co.sist.user.entity.InquiryEntity;
import kr.co.sist.util.CipherUtil;

@Controller
public class AdminController2 {
	@Autowired
	private MemberService ms;
	@Autowired
	private CipherUtil cipherUtil;
	
	
	@GetMapping("/admin_member_detail")
	public String SearchMember(@RequestParam String email,
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
		return "admin/admin_member_detail";
	}

	/*
	@GetMapping("/admin_member_search")
	public String SearchOneMember(@RequestParam("content") String content,@RequestParam("type") String type, Model model) {
		List<MemberEntity> member;
		switch(type) {
			case "name": member=ms.searchNameMember(content); break;
			case "gender": member=ms.searchGenderMember(content); break;
			case "status": member=ms.searchStatusMember(content); break;
			default : member=ms.searchAll();
		}
		
		List<MemberEntity> filtered = new ArrayList();
		for (MemberEntity m : member) {
		    String decryptedName="";
		    String decryptedPhone="";
			try {
				decryptedName = cipherUtil.plainText(m.getName());
				decryptedPhone = cipherUtil.plainText(m.getPhone());
				m.setName(decryptedName); 
				m.setPhone(decryptedPhone); 
			} catch (Exception e) {
				e.printStackTrace();
			}  // 복호화
		        filtered.add(m); // 조건 맞으면 필터링된 리스트에 추가
		}
		model.addAttribute("member",member);
		return "admin/admin_member";
	}
	*/
	
	@GetMapping("/admin_member")
	public String SelectMember2(Model model) {
		List<MemberEntity> member=ms.searchAll2();
		
		List<MemberEntity> filtered = new ArrayList();
		for (MemberEntity m : member) {
			try {
       if (m.getPhone() != null) {
           m.setPhone(cipherUtil.decryptText(m.getPhone()));
       }
			} catch (Exception e) {
				e.printStackTrace();
			}  // 복호화
		        filtered.add(m); // 조건 맞으면 필터링된 리스트에 추가
		}

		// filtered 리스트를 모델에 추가해서 뷰에 전달
		model.addAttribute("member", filtered);
		return "admin/admin_member";
	}
	
	@GetMapping("/admin_member_search")
	public String SearchMember2(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String name,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String type,
      Model model) {
		
		List<MemberEntity> member=ms.searchMember(email,name,gender,status,type);
		List<MemberEntity> filtered = new ArrayList();
		for (MemberEntity m : member) {
			try {
				if (m.getPhone() != null) {
					m.setPhone(cipherUtil.decryptText(m.getPhone()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}  // 복호화
			filtered.add(m); // 조건 맞으면 필터링된 리스트에 추가
		}
		
		// filtered 리스트를 모델에 추가해서 뷰에 전달
		model.addAttribute("member", filtered);
		return "admin/admin_member";
	}
	@Autowired
	private CorpService cs;
	/*
	@GetMapping("/admin_corp")
	public String SearchCorp(Model model) {
		List<CorpEntity> corp=cs.searchAll();
		model.addAttribute("corp",corp);
		return "admin/admin_corp";
	}
	*/
/*
	@GetMapping("/admin_corp_search")
	public String SearchOneCorp(@RequestParam("content") String content,@RequestParam("type") String type, Model model) {
		List<CorpEntity> corp;
		switch(type) {
			case "num": corp=cs.searchNoMember(content); break;
			case "name": corp=cs.searchNameMember(content); break;
			default : corp=cs.searchAll();
		}
		model.addAttribute("corp",corp);
		return "admin/admin_corp";
	}
	*/
	@GetMapping("/admin_corp")
	public String SelectCorp2(Model model) {
		List<CorpEntity> corp=cs.selectCorp();
		List<String> corpInd=cs.selectCorpInd();
		model.addAttribute("corp",corp);
		model.addAttribute("corpInd",corpInd);
		return "admin/admin_corp";
	}
	
	@GetMapping("/admin_corp_search")
	public String SearchCorp2( @RequestParam(required = false) String corpNo,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) List<String> industry,
      Model model) {
		List<CorpEntity> corp=cs.searchCorp(corpNo, name, industry);
		List<String> corpInd=cs.selectCorpInd();
		model.addAttribute("corp",corp);
		model.addAttribute("corpInd",corpInd);
		return "admin/admin_corp";
	}
	
	@GetMapping("/admin_corp_detail")
	public String CorpDetail(@RequestParam String corpNo,
												Model model) {
		CorpEntity corp=cs.detailCorp(corpNo);
		model.addAttribute("corp", corp);
		return "admin/admin_corp_detail";
	}
	
	
	@Autowired
    private DashboardService dashboardService;
	 @GetMapping("/admin_dashboard")
	    public String selectUserCnt(Model model) {
	        List<Map<String, Object>> userCount = dashboardService.getUserCountByDate();
	        model.addAttribute("userCount", userCount); // "userCountData"는 템플릿에서 사용할 이름
	        

	        List<Map<String, Object>> corpCount = dashboardService.getCorpCountByResume();
	        model.addAttribute("corpCount",corpCount);
	        
	        List<Map<String, Object>> indCount = dashboardService.getCorpCountByIndustry();
	        model.addAttribute("indCount",indCount);
	        
	        List<InquiryEntity> ask = dashboardService.getAsk();
	        model.addAttribute("ask",ask);
	        
	        return "admin/admin_dashboard"; 
	    }

	 
	@Autowired
	 private ResumeService rs;
	 @GetMapping("/admin_resume")
		public String SelectResume(Model model) {
			//List<ResumeDTO> resume=rs.selectResume();
		 List<Map<String, Object>> resume=rs.selectResume2();
			model.addAttribute("resume",resume);
			return "admin/admin_resume";
		}
	 
	 @GetMapping("/admin_resume_search")
		public String SearchResume( @RequestParam(required = false) Integer resume_seq,
	      @RequestParam(required = false) String name,
	      Model model) {
			List<Map<String,Object>> resume=rs.searchResume(resume_seq, name);
			model.addAttribute("resume",resume);
			return "admin/admin_resume";
		}
	 
	 
	 
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
