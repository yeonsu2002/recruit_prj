package kr.co.sist.admin.ask;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @GetMapping("/admin/inquiry_list")
    public String list(
        @RequestParam(required = false, defaultValue = "전체") String category,
        @RequestParam(required = false, defaultValue = "전체") String answerStat,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "전체") String keywordType,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        Page<AdminInquiryDTO> resultPage = adminInquiryService.getFilteredInquiries(
            category, answerStat, keyword, keywordType, page
        );

        model.addAttribute("adminInquiryList", resultPage.getContent()); // 변수명은 Thymeleaf와 맞춰야 함
        System.out.println("yang" + resultPage.getContent());
        model.addAttribute("pageable", resultPage.getPageable());
        model.addAttribute("page", resultPage);

        model.addAttribute("searchType", keywordType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("answerStat", answerStat);

        int currentPage = page;
        int totalPages = resultPage.getTotalPages();

        int pageNumberPrev = currentPage > 0 ? currentPage - 1 : 0;
        int pageNumberNext = currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1;

        model.addAttribute("pageNumberPrev", pageNumberPrev);
        model.addAttribute("pageNumberNext", pageNumberNext);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        return "admin/admin_inquiry_list";
    }
}
