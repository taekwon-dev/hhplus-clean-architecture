package io.hhplus.architecture.seminar.domain;

import io.hhplus.architecture.BaseEntity;
import io.hhplus.architecture.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seminar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Member speaker;

    private String description;

    public Seminar(Member speaker, String description) {
        this.speaker = speaker;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seminar seminar)) {
            return false;
        }
        return Objects.equals(id, seminar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
