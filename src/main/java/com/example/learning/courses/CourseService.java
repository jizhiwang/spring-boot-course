package com.example.learning.courses;

import java.util.List;
import java.util.UUID;

import com.example.learning.web.PageResult;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Profile({"mysql", "test-db"})
class CourseService {

    private final CourseMapper mapper;

    CourseService(CourseMapper mapper) {
        this.mapper = mapper;
    }

    PageResult<Course> list(String name, int pageNum, int pageSize) {
        return new PageResult<>(mapper.findPage(name, PageResult.offset(pageNum, pageSize), pageSize),
                mapper.count(name));
    }

    CourseDetail find(String id) {
        Course course = mapper.find(id);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        return new CourseDetail(course.id(), course.name(), mapper.findLabels(id));
    }

    @Transactional
    public CourseDetail create(String name, List<String> labels) {
        String id = UUID.randomUUID().toString();
        mapper.insert(new Course(id, name));
        insertLabels(id, labels);
        return find(id);
    }

    @Transactional
    public CourseDetail update(String id, String name, List<String> labels) {
        if (mapper.update(new Course(id, name)) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        mapper.deleteLabels(id);
        insertLabels(id, labels);
        return find(id);
    }

    private void insertLabels(String id, List<String> labels) {
        for (String label : labels) {
            mapper.insertLabel(id, label);
        }
    }

    record CourseDetail(String id, String name, List<String> labels) {
    }
}
