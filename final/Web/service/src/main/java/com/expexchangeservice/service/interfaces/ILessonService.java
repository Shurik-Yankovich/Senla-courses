package com.expexchangeservice.service.interfaces;

import com.expexchangeservice.model.dto.LessonDto;
import com.expexchangeservice.model.entities.Lesson;
import com.expexchangeservice.model.entities.Review;
import com.expexchangeservice.model.entities.Theme;
import com.expexchangeservice.model.entities.UserProfile;
import com.expexchangeservice.model.enums.Type;
import com.expexchangeservice.model.exception.DBException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ILessonService {

    void addLesson(LessonDto lessonDto);
    void changeLesson(int lessonId, LessonDto lessonDto);
    void deleteLesson(Integer lessonId);
    LessonDto getLessonById(Integer lessonId);
    List<LessonDto> getAll();
    List<LessonDto> getLessonsOnTheDate(LocalDate date);
    List<LessonDto> getLessonsAfterDate(LocalDate date);
    List<LessonDto> getLessonsOnTheTheme(Theme theme);
    List<LessonDto> getLessonsForTheProfessor(UserProfile professor);
    List<LessonDto> getLessonsByType(Type lessonType);
    void addReview(Integer lessonId, Review review);
    Set<Review> getReviewOnTheLesson(Integer lessonId);
}
