package io.hhplus.architecture.member.repository;

import io.hhplus.architecture.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
