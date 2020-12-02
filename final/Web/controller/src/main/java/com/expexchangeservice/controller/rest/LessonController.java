package com.expexchangeservice.controller.rest;

import com.expexchangeservice.model.dto.DateDto;
import com.expexchangeservice.model.dto.LessonDto;
import com.expexchangeservice.model.dto.RequestError;
import com.expexchangeservice.model.entities.*;
import com.expexchangeservice.model.enums.Type;
import com.expexchangeservice.service.interfaces.ILessonService;
import com.expexchangeservice.service.interfaces.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/lessons")
public class LessonController {

    private ILessonService lessonService;
    private ITokenService tokenService;

    @Autowired
    public LessonController(ILessonService lessonService, ITokenService tokenService) {
        this.lessonService = lessonService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createLesson(@RequestBody LessonDto lessonDto,
                                          HttpServletRequest httpRequest) {
        if (!tokenService.checkUser(httpRequest, lessonDto.getProfessor().getUsername())) {
            return new ResponseEntity<>(new RequestError(403,
                    "Hasn't access",
                    "Hasn't access with this user"),
                    HttpStatus.FORBIDDEN);
        }
        lessonService.addLesson(lessonDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getLessonsList() {
        List<LessonDto> lessons = lessonService.getAll();
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons not found"),
                        HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{lessonId}")
    public ResponseEntity<?> changeLesson(@PathVariable(name = "lessonId") int lessonId,
                                          @RequestBody LessonDto lessonDto,
                                          HttpServletRequest httpRequest) {
        if (!tokenService.checkUser(httpRequest, lessonDto.getProfessor().getUsername())) {
            return new ResponseEntity<>(new RequestError(403,
                    "Hasn't access",
                    "Hasn't access with this user"),
                    HttpStatus.FORBIDDEN);
        }
        boolean isChanged = lessonService.changeLesson(lessonId, lessonDto);
        return isChanged ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lesson not found",
                        "lesson with current id not found"),
                        HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable(name = "lessonId") int lessonId,
                                          HttpServletRequest httpRequest) {
        LessonDto lessonDto = lessonService.getLessonById(lessonId);
        if (!tokenService.checkUser(httpRequest, lessonDto.getProfessor().getUsername())) {
            return new ResponseEntity<>(new RequestError(403,
                    "Hasn't access",
                    "Hasn't access with this user"),
                    HttpStatus.FORBIDDEN);
        }
        boolean isDeleted = lessonService.deleteLesson(lessonId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lesson not found",
                        "lesson with current id not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{lessonId}")
    public ResponseEntity<?> getLessonById(@PathVariable(name = "lessonId") int lessonId) {
        LessonDto lesson = lessonService.getLessonById(lessonId);
        return lesson != null ? new ResponseEntity<>(lesson, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lesson not found",
                        "lesson with current id not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/date")
//    public ResponseEntity<?> getLessonsOnTheDate(@PathVariable(name = "date") String stringDate) {
    public ResponseEntity<?> getLessonsOnTheDate(@RequestBody DateDto dateDto) {
//            LocalDate date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<LessonDto> lessons = lessonService.getLessonsOnTheDate(dateDto.getDate());
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons on the date not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/afterdate")
    public ResponseEntity<?> getLessonsAfterDate(@RequestBody DateDto dateDto) {
        List<LessonDto> lessons = lessonService.getLessonsAfterDate(dateDto.getDate());
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons after the date not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/available")
    public ResponseEntity<?> getListOfAvailableLessons() {
        List<LessonDto> lessons = lessonService.getLessonsAfterDate(LocalDate.now());
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "available lessons not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/theme")
    public ResponseEntity<?> getLessonsOnTheTheme(@RequestBody Theme theme) {
        List<LessonDto> lessons = lessonService.getLessonsOnTheTheme(theme);
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons on the theme not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/professor")
    public ResponseEntity<?> getLessonsForTheProfessor(@RequestBody UserProfile professor) {
        List<LessonDto> lessons = lessonService.getLessonsForTheProfessor(professor);
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons for the professor not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/type/{type}")
    public ResponseEntity<?> getLessonsByType(@PathVariable(name = "type") String type) {
        List<LessonDto> lessons = lessonService.getLessonsByType(Type.valueOf(type.toUpperCase()));
        return lessons != null ? new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "lessons not found",
                        "lessons by type not found"),
                        HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{lessonId}/review")
    public ResponseEntity<?> getReviewOnTheLesson(@PathVariable(name = "lessonId") int lessonId) {
        Set<Review> reviews = lessonService.getReviewOnTheLesson(lessonId);
        return reviews != null ? new ResponseEntity<>(reviews, HttpStatus.OK) :
                new ResponseEntity<>(new RequestError(404,
                        "reviews not found",
                        "reviews for lesson with current id not found"),
                        HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{lessonId}/review")
    public ResponseEntity<?> addReviewToTheLesson(@PathVariable(name = "lessonId") int lessonId,
                                                  @RequestBody Review review) {
        boolean isAdded = lessonService.addReview(lessonId, review);
        return isAdded ? new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(new RequestError(404,
                        "lesson not found",
                        "lesson with current id not found"),
                        HttpStatus.NOT_FOUND);
    }

}
