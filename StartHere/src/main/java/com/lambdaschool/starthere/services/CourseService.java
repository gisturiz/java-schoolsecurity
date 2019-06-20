package com.lambdaschool.starthere.services;

import com.lambdaschool.starthere.models.Course;
import com.lambdaschool.starthere.view.CountStudentsInCourses;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface CourseService
{
    ArrayList<Course> findAll(Pageable pageable);

    ArrayList<CountStudentsInCourses> getCountStudentsInCourse();

    void delete(long id);

    List<Course> findCourseByNameLike(String name, Pageable pageable);
}
