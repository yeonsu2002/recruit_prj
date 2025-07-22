package kr.co.sist.admin.notice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.jwt.CustomAdmin;

@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지사항 목록 페이지
    @GetMapping("/admin/notice_list")
    public String getNoticeList(@RequestParam(value = "searchType", defaultValue = "전체") String searchType,
                                @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                Model model) {
        model.addAttribute("notices", noticeService.getNoticeList(searchType, keyword));
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "admin/notice_list";
    }

    // AJAX용 JSON 공지사항 목록 반환
    @GetMapping("/admin/api/notice_list")
    @ResponseBody
    public List<Notice> getNoticeListJson(@RequestParam(value = "searchType", defaultValue = "전체") String searchType,
                                          @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return noticeService.getNoticeList(searchType, keyword);
    }

    // 공지사항 작성 페이지
    @GetMapping("/admin/notice_write")
    public String getNoticeWritePage(Model model) {
        Notice notice = new Notice();
        notice.setNoticeSeq(0);  // 0으로 초기화해서 새 글임을 표시
        model.addAttribute("notice", notice);
        return "admin/notice_write";
    }

 // 공지사항 수정 페이지
    @GetMapping("/admin/notice_write/{noticeSeq}")
    public String getNoticeEditPage(@PathVariable("noticeSeq") int noticeSeq, Model model) {
        Notice notice = noticeService.getNoticeById(noticeSeq);
        if (notice == null) {
            return "redirect:/admin/notice_write";
        }
        model.addAttribute("notice", notice);
        return "admin/notice_write";
    }

    // 공지사항 저장 처리 (작성/수정)
    @PostMapping("/admin/notice_save")
    public String saveNotice(Notice notice) {
      // 현재 로그인한 관리자 정보 가져오기
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      CustomAdmin admin = (CustomAdmin) auth.getPrincipal();

      // Notice 객체에 관리자 ID 세팅 (CustomAdmin의 메서드명에 맞게 조정)
      notice.setAdminId(admin.getUsername());
        if (notice.getNoticeSeq() != 0) {
            noticeService.updateNotice(notice);
        } else {
            noticeService.createNotice(notice);
        }
        return "redirect:/admin/notice_list";
    }
    
    // 공지사항 다중 삭제 (새로 추가)
    @PostMapping("/admin/notice_delete")
    @ResponseBody
    public String deleteNotices(@RequestParam("noticeSeqs") List<Integer> noticeSeqs) {
        try {
            noticeService.deleteNotices(noticeSeqs);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
