package io.hhplus.architecture.util.fixture;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.domain.MemberRole;

public class MemberFixture {

    public static Member SPEAKER() {
        return new Member("speaker_name", MemberRole.SPEAKER);
    }

    public static Member AUDIENCE() {
        return new Member("audience_name", MemberRole.AUDIENCE);
    }
}
