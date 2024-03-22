package com.boot3.myrestapi.lectures;

import com.boot3.myrestapi.security.userinfo.UserInfo;
import com.boot3.myrestapi.security.userinfo.UserInfoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    private String description;

    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginLectureDateTime;
    private LocalDateTime endLectureDateTime;

    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @ManyToOne
    @JsonSerialize(using = UserInfoSerializer.class)
    private UserInfo userInfo;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus = LectureStatus.DRAFT;

    public void update() {
        // Update free
//        if (this.basePrice == 0 && this.maxPrice == 0) {
//            this.free = true;
//        } else {
//            this.free = false;
//        }
        this.free = this.basePrice == 0 && this.maxPrice == 0;

        // Update offline
//        if (this.location == null || this.location.isBlank()) {
//            this.offline = false;
//        } else {
//            this.offline = true;
//        }

        this.offline = this.location != null && !this.location.isBlank();
    }
}