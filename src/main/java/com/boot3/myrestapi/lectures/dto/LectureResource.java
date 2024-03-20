package com.boot3.myrestapi.lectures.dto;

import com.boot3.myrestapi.lectures.LectureController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LectureResource extends RepresentationModel<LectureResource> {
    @JsonUnwrapped
    private LectureResDto lectureResDto;
    
    public LectureResource(LectureResDto resDto) {
        this.lectureResDto = resDto;
        add(linkTo(LectureController.class).slash(resDto.getId()).withSelfRel());

    }
    
    public LectureResDto getLectureResDto() {
        return lectureResDto;
    }

}