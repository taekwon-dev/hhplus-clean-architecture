package io.hhplus.architecture.seminar.domain;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.util.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class SeminarTest {

    @DisplayName("특강을 생성한다.")
    @Test
    void createSeminar() {
        // given
        Member speaker = MemberFixture.SPEAKER();

        // when & then
        assertThatCode(() -> new Seminar(speaker, "description")).doesNotThrowAnyException();
    }
}
