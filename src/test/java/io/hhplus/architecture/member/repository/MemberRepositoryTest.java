package io.hhplus.architecture.member.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.domain.MemberRole;
import io.hhplus.architecture.util.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Member를 저장한다.")
    @Test
    void saveMember() {
        //given
        Member member = new Member("speaker_name", MemberRole.SPEAKER);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember.getName()).isEqualTo("speaker_name");
        assertThat(savedMember.getRole()).isEqualTo(MemberRole.SPEAKER);
    }
}
