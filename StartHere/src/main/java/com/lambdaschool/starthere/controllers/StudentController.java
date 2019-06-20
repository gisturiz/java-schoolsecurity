package com.lambdaschool.starthere.controllers;

import com.lambdaschool.starthere.models.ErrorDetail;
import com.lambdaschool.starthere.models.Student;
import com.lambdaschool.starthere.services.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "returns all Students", response = Student.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integr", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    // Please note there is no way to add students to course yet!

    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(@PageableDefault(page = 0,
            size = 5)
                                                     Pageable pageable)
    {
        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    @ApiOperation(value = "Retrieves a student associated with the studentid", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student not found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/student/{studentid}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @PathVariable
                    Long studentid)
    {
        Student r = studentService.findStudentById(studentid);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


    @ApiOperation(value = "Retrieves a student with names that contain similar characters", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student with similar name Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student with similar name not found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @PathVariable
                    String name,
            @PageableDefault(page = 0,
                    size = 5)
                    Pageable pageable)
    {
        List<Student> myStudents = studentService.findStudentByNameLike(name, pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    @ApiOperation(value = "Adds new student", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New student added", response = Student.class),
            @ApiResponse(code = 404, message = "New student not added", response = ErrorDetail.class)
    })
    @PostMapping(value = "/student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody
                                                   Student newStudent) throws URISyntaxException
    {
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Updates a current student associated with given studentid", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student updated", response = Student.class),
            @ApiResponse(code = 404, message = "Student not found", response = ErrorDetail.class)
    })
    @PutMapping(value = "/student/{studentid}")
    public ResponseEntity<?> updateStudent(
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long studentid)
    {
        studentService.update(updateStudent, studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Deletes current student associated with given studentid", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student was deleled", response = Student.class),
            @ApiResponse(code = 404, message = "Student was not found", response = ErrorDetail.class)
    })
    @DeleteMapping("/student/{studentid}")
    public ResponseEntity<?> deleteStudentById(
            @PathVariable
                    long studentid)
    {
        studentService.delete(studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
