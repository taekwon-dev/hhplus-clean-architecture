package io.hhplus.architecture.schedule.controller;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.schedule.controller.dto.response.ScheduleResponse;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.seminar.repository.SeminarRepository;
import io.hhplus.architecture.util.ControllerTest;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.ScheduleFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleControllerTest extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("예약 가능한 스케줄을 조회한다.")
    @Test
    void findAvailableSchedules() {
        // given
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        String url = String.format("/schedules/%d", audience.getId());

        // when
        List<ScheduleResponse> response = RestAssured.given().log().all()
                .when().get(url)
                .then().log().all().statusCode(200)
                .extract().as(new TypeRef<>() {
                });

        // then
        assertThat(response.size()).isOne();
        assertThat(response.get(0).title()).isEqualTo(schedule.getTitle());
        assertThat(response.get(0).speakerName()).isEqualTo(schedule.getSpeakerName());
        assertThat(response.get(0).description()).isEqualTo(schedule.getDescription());
        assertThat(response.get(0).startDate()).isEqualTo(schedule.getStartDate());
        assertThat(response.get(0).endDate()).isEqualTo(schedule.getEndDate());
    }
}
