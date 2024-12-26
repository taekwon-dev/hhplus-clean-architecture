package io.hhplus.architecture.member.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.exception.MemberNotFoundException;
import io.hhplus.architecture.util.RepositoryTest;
import io.hhplus.architecture.util.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("멤버를 저장한다.")
    @Test
    void saveMember() {
        //given
        Member member = MemberFixture.SPEAKER();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    @DisplayName("ID를 기반으로 멤버를 조회한다.")
    @Test
    void findMemberById() {
        // given
        Member member = memberRepository.save(MemberFixture.SPEAKER());

        // when
        Member findMember = memberRepository.findById(member.getId());

        // then
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getRole()).isEqualTo(member.getRole());
    }

    @DisplayName("타겟 멤버 ID가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findMemberById_WhenMemberNotExist() {
        // given
        long notExistUserId = 1L;

        // when & then
        assertThatThrownBy(() -> memberRepository.findById(notExistUserId)).isInstanceOf(MemberNotFoundException.class);
    }
}
