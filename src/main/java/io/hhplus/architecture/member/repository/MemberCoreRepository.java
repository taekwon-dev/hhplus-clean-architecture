package io.hhplus.architecture.member.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCoreRepository implements MemberRepository {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(Member member) {
        return jpaRepository.save(member);
    }

    @Override
    public Member findById(long id) {
        return jpaRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }
}
