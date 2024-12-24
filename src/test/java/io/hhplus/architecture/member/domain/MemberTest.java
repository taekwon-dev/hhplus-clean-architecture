package io.hhplus.architecture.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class MemberTest {

    @DisplayName("Member를 생성한다.")
    @ParameterizedTest
    @CsvSource({
            "speaker_name, SPEAKER",
            "audience_name, AUDIENCE"
    })
    void createMember(String name, MemberRole role) {
        // when & then
        assertThatCode(() -> new Member(name, role)).doesNotThrowAnyException();
    }
}
