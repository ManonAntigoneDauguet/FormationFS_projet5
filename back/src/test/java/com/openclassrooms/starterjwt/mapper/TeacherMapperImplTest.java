package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperImplTest {

    private TeacherMapperImpl teacherMapper;

    TeacherDto teacherDto;

    Teacher teacher;

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapperImpl();

        // A teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Buffay");
        teacher.setFirstName("Phoebe");
        teacher.setCreatedAt(localDate);
        teacher.setUpdatedAt(localDate);

        // A teacher dto
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Tribbani");
        teacherDto.setFirstName("Joe");
        teacherDto.setCreatedAt(localDate);
        teacherDto.setUpdatedAt(localDate);
    }

    /************************* toEntity() ****************************/

    @Test
    void testSuccessToEntity() {
        // When
        Teacher convertedTeacher = teacherMapper.toEntity(teacherDto);
        // Then
        assertNotNull(convertedTeacher);
        assertEquals(1L, convertedTeacher.getId());
        assertEquals("Tribbani", convertedTeacher.getLastName());
        assertEquals("Joe", convertedTeacher.getFirstName());
        assertEquals(localDate, convertedTeacher.getCreatedAt());
        assertEquals(localDate, convertedTeacher.getUpdatedAt());
    }

    @Test
    void testNUllToEntity() {
        // When
        Teacher convertedTeacher = teacherMapper.toEntity((TeacherDto) null);
        // Then
        assertNull(convertedTeacher);
    }

    @Test
    void testSuccessListToEntity() {
        // When
        List<Teacher> convertedTeachers = teacherMapper.toEntity(Arrays.asList(teacherDto, teacherDto));
        // Then
        assertNotNull(convertedTeachers);
        assertEquals(2, convertedTeachers.size());
        assertEquals("Joe", convertedTeachers.get(0).getFirstName());
    }

    @Test
    void testNullListToEntity() {
        // When
        List<Teacher> convertedTeachers = teacherMapper.toEntity((List<TeacherDto>) null);
        // Then
        assertNull(convertedTeachers);
    }

    /************************* toDto() ****************************/

    @Test
    void testSuccessToDto() {
        // When
        TeacherDto convertedTeacher = teacherMapper.toDto(teacher);
        // Then
        assertNotNull(convertedTeacher);
        assertEquals(1L, convertedTeacher.getId());
        assertEquals("Buffay", convertedTeacher.getLastName());
        assertEquals("Phoebe", convertedTeacher.getFirstName());
        assertEquals(localDate, convertedTeacher.getCreatedAt());
        assertEquals(localDate, convertedTeacher.getUpdatedAt());
    }

    @Test
    void testNullToDto() {
        // When
        TeacherDto convertedTeacher = teacherMapper.toDto((Teacher) null);
        // Then
        assertNull(convertedTeacher);
    }

    @Test
    void testSuccessListToDto() {
        // When
        List<TeacherDto> convertedTeachers = teacherMapper.toDto(Arrays.asList(teacher, teacher));
        // Then
        assertNotNull(convertedTeachers);
        assertEquals(2, convertedTeachers.size());
        assertEquals(1L, convertedTeachers.get(0).getId());
    }

    @Test
    void testNullListToDto() {
        // When
        List<TeacherDto> convertedTeachers = teacherMapper.toDto((List<Teacher>) null);
        // Then
        assertNull(convertedTeachers);
    }
}