package io.hhplus.architecture.member.repository;

import io.hhplus.architecture.member.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findById(long id);
}
