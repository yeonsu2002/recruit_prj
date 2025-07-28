package kr.co.sist.notice;

import kr.co.sist.notice.HelpNoticeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpNoticeDTO {
    private Long noticeSeq;
    private String adminId;
    private String title;
    private String content;
    private LocalDateTime regsDate;
    private LocalDateTime modifyDate;

    public static HelpNoticeDTO fromEntity(HelpNoticeEntity entity) {
        return HelpNoticeDTO.builder()
                .noticeSeq(entity.getNoticeSeq())
                .adminId(entity.getAdminId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .regsDate(entity.getRegsDate())
                .modifyDate(entity.getModifyDate())
                .build();
    }
}