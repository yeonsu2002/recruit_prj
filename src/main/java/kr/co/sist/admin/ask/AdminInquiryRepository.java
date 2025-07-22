package kr.co.sist.admin.ask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import kr.co.sist.user.entity.InquiryEntity;

public interface AdminInquiryRepository extends JpaRepository<InquiryEntity, Long>, JpaSpecificationExecutor<InquiryEntity> {
}
