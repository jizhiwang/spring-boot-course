package com.example.learning.courses;

import java.util.List;

import com.example.learning.web.PageResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@Profile({"mysql", "test-db"})
class CourseController {

    private final CourseService service;

    CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    PageResult<Course> list(@RequestParam(defaultValue = "") String name,
                            @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        return service.list(name, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    CourseService.CourseDetail find(@PathVariable String id) {
        return service.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CourseService.CourseDetail create(@Valid @RequestBody CourseRequest request) {
        return service.create(request.name(), request.labels());
    }

    @PutMapping("/{id}")
    CourseService.CourseDetail update(@PathVariable String id, @Valid @RequestBody CourseRequest request) {
        return service.update(id, request.name(), request.labels());
    }

    record CourseRequest(@NotBlank @Size(max = 100) String name,
                         @NotNull @Size(max = 10) List<@NotBlank @Size(max = 50) String> labels) {
    }
}
