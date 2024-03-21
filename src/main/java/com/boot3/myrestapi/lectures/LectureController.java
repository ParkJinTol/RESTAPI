package com.boot3.myrestapi.lectures;

import com.boot3.myrestapi.common.errors.ErrorsResource;
import com.boot3.myrestapi.common.exception.BusinessException;
import com.boot3.myrestapi.common.exception.advice.DefaultExceptionAdvice;
import com.boot3.myrestapi.lectures.dto.LectureReqDto;
import com.boot3.myrestapi.lectures.dto.LectureResDto;
import com.boot3.myrestapi.lectures.dto.LectureResource;
import com.boot3.myrestapi.lectures.validator.LectureValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

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
    @GetMapping(value = "/all") // 전체 조회
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> queryLectures(Pageable pageable, PagedResourcesAssembler<LectureResDto> assembler) {
        Page<Lecture> lecturePage = this.lectureRepository.findAll(pageable);
        // Page<Lecture> => Page<LectureResDto>
        Page<LectureResDto> lectureResDtoPage = lecturePage
                .map(lecture -> modelMapper.map(lecture, LectureResDto.class));
        // Page<LectureResDto> => PagedModel<EntityModel<LectureResDto>>
        //PagedModel<EntityModel<LectureResDto>> pagedModel = assembler.toModel(lectureResDtoPage);

//        PagedModel<LectureResource> pagedModel = assembler
//                .toModel(lectureResDtoPage, lectureResDto -> new LectureResource(lectureResDto));
        PagedModel<LectureResource> pagedModel = assembler.toModel(lectureResDtoPage, LectureResource::new);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}") // 개별 조회
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getLecture(@PathVariable Integer id) {
       Lecture lecture = getExitLecture(id);

        LectureResDto lectureResDto = modelMapper.map(lecture, LectureResDto.class);
        LectureResource lectureResource = new LectureResource(lectureResDto);
        return ResponseEntity.ok(lectureResource);
    }

    @PostMapping // 데이터 추가
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
    @PutMapping("/{id}") // 데이터 업데이트
    public ResponseEntity<?> updateLecture(@PathVariable Integer id,
                                        @RequestBody @Valid LectureReqDto lectureReqDto,
                                        Errors errors) {
        Lecture existingLecture = getExitLecture(id);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        lectureValidator.validate(lectureReqDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        this.modelMapper.map(lectureReqDto, existingLecture);
        existingLecture.update();

        Lecture savedLecture = this.lectureRepository.save(existingLecture);
        LectureResDto lectureResDto = modelMapper.map(savedLecture, LectureResDto.class);

        LectureResource lectureResource = new LectureResource(lectureResDto);
        return ResponseEntity.ok(lectureResource);
    }

    private Lecture getExitLecture(Integer id) {
        Optional<Lecture> optionalLecture = this.lectureRepository.findById(id);

        String errMsg = String.format("id = %d Lecture Not Found", id);
        return optionalLecture
                .orElseThrow(() -> new BusinessException(errMsg, HttpStatus.NOT_FOUND));
    }

    private static ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
