package io.hhplus.architecture.seminar.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.domain.MemberRole;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.util.RepositoryTest;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SeminarRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @DisplayName("특강을 저장한다.")
    @Test
    void saveSeminar() {
        //given
        Member speaker = MemberFixture.SPEAKER;
        Member savedSpeaker = memberRepository.save(speaker);
        Seminar seminar = SeminarFixture.create(savedSpeaker);

        //when
        Seminar savedSeminar = seminarRepository.save(seminar);

        //then
        assertThat(savedSeminar.getSpeaker()).isEqualTo(seminar.getSpeaker());
        assertThat(savedSeminar.getDescription()).isEqualTo(seminar.getDescription());
    }
}
