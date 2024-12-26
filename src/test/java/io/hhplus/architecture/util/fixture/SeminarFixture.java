package io.hhplus.architecture.util.fixture;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.seminar.domain.Seminar;

public class SeminarFixture {

    public static Seminar create(Member speaker) {
        return new Seminar(speaker, "description");
    }
}
