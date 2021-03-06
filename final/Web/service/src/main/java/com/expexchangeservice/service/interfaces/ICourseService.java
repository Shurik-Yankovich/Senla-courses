package com.expexchangeservice.service.interfaces;

import com.expexchangeservice.model.dto.CourseDto;
import com.expexchangeservice.model.dto.ProfileDto;
import com.expexchangeservice.model.entities.*;
import com.expexchangeservice.model.enums.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ICourseService {

    void createCourse(CourseDto courseDto);
    boolean updateCourse(Long courseId, CourseDto courseDto);
    boolean deleteCourse(Long courseId);
    CourseDto getCourseById(Long courseId);
    List<CourseDto> getAll();
    List<CourseDto> getCoursesOnTheDate(LocalDate date);
    List<CourseDto> getCoursesAfterDate(LocalDate date);
    List<CourseDto> getCoursesOnTheSection(Section section);
    List<CourseDto> getCoursesForTheProfessor(ProfileDto profileDto);
    List<CourseDto> getCoursesByType(Type courseType);
    boolean addReview(Long courseId, Review review);
    Set<Review> getReviewOnTheCourse(Long courseId);
    int getRewardForCoursesByProfessor(String username);
    boolean changeRewardByCourseId(Long courseId, int reward);
    boolean signUpForTheCourse(Long courseId, String username);
}
