package com.boot3.myrestapi.common.runner;

import com.boot3.myrestapi.lectures.Lecture;
import com.boot3.myrestapi.lectures.LectureRepository;
import com.boot3.myrestapi.lectures.LectureStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Component
@Profile("prod")
public class LectureInsertRunner implements ApplicationRunner {
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 데이터를 삭제
//        lectureRepository.deleteAll();
        IntStream.range(0, 2).forEach(this::generateLecture);
        //IntStream.range(0, 15).forEach(index -> generateLecture(index));
    }
    private void generateLecture(int index) {
        Lecture lecture = buildLecture(index);
        this.lectureRepository.save(lecture);
    }
    private Lecture buildLecture(int index) {
        return Lecture.builder()
                .name(index + 1 + " Lecture ")
                .description("Test Lecture")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 11, 24, 14, 21))
                .beginLectureDateTime(LocalDateTime.of(2022, 11, 25, 14, 21))
                .endLectureDateTime(LocalDateTime.of(2022, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location(index + 1 + " 강의장")
                .free(false)
                .offline(true)
                .lectureStatus(LectureStatus.DRAFT)
                .build();
    }
}