package com.boot3.myrestapi.lectures.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureReqDto {

    @NotBlank(message = "Name 은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginLectureDateTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endLectureDateTime;

    private String location;

    @Min(0)
    private int basePrice;

    @Min(0)
    private int maxPrice;

    @Min(5) @Max(20)
    private int limitOfEnrollment;
}
