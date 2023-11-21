package com.buckwheat.garden.domain.todo;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    @GetMapping("")
    public List<TodoDto> getAll(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return todoService.getAll(userPrincipal.getId());
    }

    @PostMapping("")
    public TodoDto add(@RequestBody TodoDto todoDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return todoService.add(todoDto, userPrincipal.getId());
    }

    @PutMapping("/{todoId}")
    public TodoDto update(@RequestBody TodoDto todoDto){
        return todoService.update(todoDto);
    }

    @DeleteMapping("/{todoId}")
    public void delete(@PathVariable Long todoId){
        todoService.delete(todoId);
    }
}
