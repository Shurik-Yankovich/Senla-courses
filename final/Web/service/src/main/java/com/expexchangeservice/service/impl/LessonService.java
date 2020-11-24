package com.expexchangeservice.service.impl;

import com.expexchangeservice.model.dto.LessonDto;
import com.expexchangeservice.model.entities.Lesson;
import com.expexchangeservice.model.entities.Review;
import com.expexchangeservice.model.entities.Theme;
import com.expexchangeservice.model.entities.UserProfile;
import com.expexchangeservice.model.enums.Type;
import com.expexchangeservice.model.exception.DBException;
import com.expexchangeservice.repository.interfaces.ILessonRepository;
import com.expexchangeservice.service.interfaces.ILessonService;
import com.expexchangeservice.service.interfaces.IReviewService;
import com.expexchangeservice.utils.HibernateSessionFactoryUtil;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LessonService implements ILessonService {

    private ILessonRepository lessonRepository;
    private IReviewService reviewService;

    @Autowired
    public LessonService(ILessonRepository lessonRepository, IReviewService reviewService) {
        this.lessonRepository = lessonRepository;
        this.reviewService = reviewService;
    }

    @Override
    public void addLesson(LessonDto lessonDto) {
        Lesson lesson = convertDtoToLesson(new Lesson(), lessonDto);
        lesson.setPrice(100);
        lessonRepository.create(lesson);
    }

    @Override
    public void changeLesson(int lessonId, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.read(lessonId);
        lesson = convertDtoToLesson(lesson, lessonDto);
        lessonRepository.update(lesson);
    }

    @Override
    public void deleteLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.read(lessonId);
        lessonRepository.delete(lesson);
    }

    @Override
    public LessonDto getLessonById(Integer lessonId) {
        Lesson lesson = lessonRepository.read(lessonId);
        return convertLessonToDto(lesson);
    }

    @Override
    public List<LessonDto> getAll() {
        List<Lesson> lessons = lessonRepository.readAll();
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public List<LessonDto> getLessonsOnTheDate(LocalDate date) {
        List<Lesson> lessons = lessonRepository.findByDate(date);
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public List<LessonDto> getLessonsAfterDate(LocalDate date) {
        List<Lesson> lessons = lessonRepository.findAfterDate(date);
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public List<LessonDto> getLessonsOnTheTheme(Theme theme) {
        List<Lesson> lessons = lessonRepository.findByTheme(theme);
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public List<LessonDto> getLessonsForTheProfessor(UserProfile professor) {
        List<Lesson> lessons = lessonRepository.findByProfessor(professor);
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public List<LessonDto> getLessonsByType(Type lessonType) {
        List<Lesson> lessons = lessonRepository.findByType(lessonType);
        return convertLessonListToDtoList(lessons);
    }

    @Override
    public void addReview(Integer lessonId, Review review) {
        reviewService.addReview(review);
        Lesson lesson = lessonRepository.read(lessonId);
        if (lesson.getReviews() == null) {
            lesson.setReviews(new HashSet<Review>());
        }
        lesson.getReviews().add(review);
        lessonRepository.update(lesson);
    }

    @Override
    public Set<Review> getReviewOnTheLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.read(lessonId);
        return lesson.getReviews();
    }

    private Lesson convertDtoToLesson(Lesson lesson, LessonDto lessonDto) {
        lesson.setId(lessonDto.getId());
        lesson.setTheme(lessonDto.getTheme());
        lesson.setProfessor(lessonDto.getProfessor());
        lesson.setType(lessonDto.getType());
        lesson.setDate(lessonDto.getDate());
        return lesson;
    }

    private LessonDto convertLessonToDto(Lesson lesson) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(lesson.getId());
        lessonDto.setTheme(lesson.getTheme());
        lessonDto.setProfessor(lesson.getProfessor());
        lessonDto.setType(lesson.getType());
        lessonDto.setDate(lesson.getDate());
        return lessonDto;
    }

    private List<LessonDto> convertLessonListToDtoList(List<Lesson> lessons) {
        List<LessonDto> lessonDtoList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonDtoList.add(convertLessonToDto(lesson));
        }
        return lessonDtoList;
    }
}
