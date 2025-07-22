package kr.co.sist.admin.ask;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryRepository inquiryRepo;

    public Page<AdminInquiryDTO> getFilteredInquiries(String category, String answerStat, String keyword, String keywordType, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "askSeq"));
        System.out.println(inquiryRepo.findAll());
        return inquiryRepo.findAll(
                AdminInquirySpecification.filterBy(category, answerStat, keyword, keywordType),
                pageRequest
        ).map(AdminInquiryDTO::new);
    }
}
