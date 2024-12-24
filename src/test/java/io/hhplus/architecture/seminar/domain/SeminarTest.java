package io.hhplus.architecture.seminar.domain;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.util.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SeminarTest {

    @DisplayName("특강을 생성한다.")
    @Test
    void createSeminar() {
        // given
        Member speaker = MemberFixture.SPEAKER;

        // when & then
        assertDoesNotThrow(() -> new Seminar(speaker, "description"));
    }
}
