package kr.co.sist.admin.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FaqEntity> getAllFaqs() {
        return faqRepository.findAll();
    }

    public Optional<FaqEntity> getFaqById(int faqSeq) {
        return faqRepository.findById(faqSeq);
    }

    @Transactional
    public FaqEntity createFaq(FaqEntity faq) {
        return faqRepository.save(faq);
    }

    @Transactional
    public FaqEntity updateFaq(FaqEntity faq) {
        return faqRepository.save(faq);
    }

    @Transactional
    public void deleteFaq(int faqSeq) {
        faqRepository.deleteById(faqSeq);
    }
}
