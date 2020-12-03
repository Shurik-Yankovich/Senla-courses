package com.expexchangeservice.service.impl;

import com.expexchangeservice.model.dto.CourseDto;
import com.expexchangeservice.model.dto.LessonDto;
import com.expexchangeservice.model.dto.ProfileDto;
import com.expexchangeservice.model.entities.Course;
import com.expexchangeservice.model.entities.Lesson;
import com.expexchangeservice.model.entities.User;
import com.expexchangeservice.model.entities.UserProfile;
import com.expexchangeservice.repository.interfaces.IUserProfileRepository;
import com.expexchangeservice.service.converter.DtoConverter;
import com.expexchangeservice.service.interfaces.IUserService;
import com.expexchangeservice.service.interfaces.IUserProfileService;
import com.expexchangeservice.utils.comparators.CourseStartDateComparator;
import com.expexchangeservice.utils.comparators.LessonDateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService implements IUserProfileService {

    private IUserProfileRepository profileRepository;
    private IUserService userService;
    private DtoConverter converter;

    @Autowired
    public UserProfileService(IUserProfileRepository profileRepository, IUserService userService,
                              DtoConverter converter) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.converter = converter;
    }

    @Override
    public void addUserProfile(ProfileDto profileDto) {
        UserProfile userProfile = converter.convertDtoToUserProfile(new UserProfile(), profileDto);
        profileRepository.create(userProfile);
    }

    @Override
    public boolean changeUserProfile(Long profileId, ProfileDto profileDto) {
        UserProfile userProfile = converter.convertDtoToUserProfile(profileRepository.read(profileId), profileDto);
        if (userProfile == null) {
            return false;
        }
        profileRepository.update(converter.convertDtoToUserProfile(userProfile, profileDto));
        return true;
    }

    @Override
    public boolean deleteUserProfile(Long profileId) {
        UserProfile userProfile = profileRepository.read(profileId);
        if (userProfile == null) {
            return false;
        }
        profileRepository.delete(userProfile);
        return true;
    }

    @Override
    public ProfileDto getProfileDtoByUsername(String username) {
        UserProfile userProfile = findProfileByUsername(username);
        return converter.convertUserProfileToDto(userProfile);
    }

    @Override
    public UserProfile findProfileByUsername(String username) {
        User user = userService.loadUserByUsername(username);
        return user == null ? null : profileRepository.findByUser(user);
    }

    @Override
    public boolean signUpForTheLesson(String username, Lesson lesson) {
        UserProfile profile = findProfileByUsername(username);
        if (profile == null) {
            return false;
        }
        profile.getLessons().add(lesson);
        profileRepository.update(profile);
        return true;
    }

    @Override
    public boolean signUpForTheCourse(String username, Course course) {
        UserProfile profile = findProfileByUsername(username);
        if (profile == null) {
            return false;
        }
        profile.getCourses().add(course);
        profileRepository.update(profile);
        return true;
    }

    @Override
    public ProfileDto getUserProfileById(Long profileId) {
        UserProfile profile = profileRepository.read(profileId);
        return converter.convertUserProfileToDto(profile);
    }

    @Override
    public List<LessonDto> getLessonListForUser(String username) {
        UserProfile profile = findProfileByUsername(username);
        if (profile == null) {
            return null;
        }
        List<Lesson> lessons = profile.getLessons();
        if (lessons.size() > 0) {
            lessons.sort(new LessonDateComparator());
        }
        return converter.convertLessonListToDtoList(lessons);
    }

    @Override
    public List<CourseDto> getCourseListForUser(String username) {
        UserProfile profile = findProfileByUsername(username);
        if (profile == null) {
            return null;
        }
        List<Course> courses = profile.getCourses();
        if (courses.size() > 0) {
            courses.sort(new CourseStartDateComparator());
        }
        return converter.convertCourseListToDtoList(courses);
    }

    @Override
    public boolean changeUserRole(String username, boolean isAdmin) {
        UserProfile profile = findProfileByUsername(username);
        User user = userService.changeUserRole(username, isAdmin);
        if (user == null) {
            return false;
        }
        profile.setUser(user);
        profileRepository.update(profile);
        return true;
    }
}
