package spring.boot.community.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    NOTICE("공지사항"),
    FREE("자유게시판");

    private final String value;
}
