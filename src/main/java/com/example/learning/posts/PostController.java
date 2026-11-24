package com.example.learning.posts;

import com.example.learning.web.PageResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
class PostController {

    private final PostService service;

    PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    PageResult<Post> list(@RequestParam(required = false) String name,
                          @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                          @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        return service.list(name, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    Post find(@PathVariable long id) {
        return service.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Post create(@Valid @RequestBody PostRequest request) {
        return service.create(request.name());
    }

    record PostRequest(@NotBlank @Size(max = 100) String name) {
    }
}
