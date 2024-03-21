package com.boot3.myrestapi.lectures;

import com.boot3.myrestapi.common.errors.ErrorsResource;
import com.boot3.myrestapi.lectures.dto.LectureReqDto;
import com.boot3.myrestapi.lectures.dto.LectureResDto;
import com.boot3.myrestapi.lectures.dto.LectureResource;
import com.boot3.myrestapi.lectures.validator.LectureValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/lectures", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class LectureController {
    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;
    private final LectureValidator lectureValidator;

//    @Autowired 쓰는대신 아래처럼 직접 초기화 하면 좋은점은 Test할때 유용
//    public LectureController(LectureRepository lectureRepository) {
//        this.lectureRepository = lectureRepository;
//    }

    @PostMapping
    public ResponseEntity<?> createLecture(@RequestBody @Valid LectureReqDto lectureReqDto, Errors errors) {
        // 입력 항목 검증
//        if(errors.hasErrors()) {
//            return badRequest(errors);
//        }

        // Biz로직의 입력 항목 체크
        this.lectureValidator.validate(lectureReqDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        //ReqDto => Entity 매핑
        Lecture lecture = modelMapper.map(lectureReqDto, Lecture.class);

        // free, offline 정보 업데이트
        lecture.update();


        Lecture addLecture = this.lectureRepository.save(lecture);

        LectureResDto lectureResDto = modelMapper.map(addLecture, LectureResDto.class);

        WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder.linkTo(LectureController.class).slash(addLecture.getId());
        URI createUri = selfLinkBuilder.toUri();

        LectureResource lectureResource = new LectureResource(lectureResDto);
        //relation 이름이 query-lectures 인 link
        lectureResource.add(linkTo(LectureController.class).withRel("query-lectures"));
        //relation 이름이 update-lecture 인 link
        lectureResource.add(selfLinkBuilder.withRel("update-lecture"));

        return ResponseEntity.created(createUri).body(lectureResource);

//        DB환경 없을경우 테스트 목업 데이터
//        lecture.setId(10);
//        WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder.linkTo(LectureController.class).slash(lecture.getId());
//        URI createUri = selfLinkBuilder.toUri();
//        return ResponseEntity.created(createUri).body(lecture);
    }

    private static ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
