package com.lambdaschool.starthere.repository;

import com.lambdaschool.starthere.models.Course;
import com.lambdaschool.starthere.view.CountStudentsInCourses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long>
{
    ArrayList<Course> findCoursesByCoursenameEquals(String name);

    @Modifying
    @Query(value = "DELETE FROM studcourses WHERE courseid = :courseid", nativeQuery = true)
    void deleteCourseFromStudcourses(long courseid);

    @Query(value = "SELECT s.courseid, coursename, count(studid) as countstudents FROM studcourses s INNER JOIN course c on s.courseid=c.courseid GROUP BY s.courseid, coursename", nativeQuery = true)
    ArrayList<CountStudentsInCourses> getCountStudentsInCourse();

    List<Course> findByCoursenameIgnoreCase(String name, Pageable pageable);
}

