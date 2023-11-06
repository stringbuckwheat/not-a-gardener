package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.gardener.Info;
import com.buckwheat.garden.data.dto.gardener.Register;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final AuthenticationService authenticationService;

    @GetMapping("/username/{username}")
    public String hasSameUsername(@PathVariable String username) {
        return authenticationService.hasSameUsername(username);
    }

    @PostMapping("")
    public Info add(@RequestBody Register register) {
        return authenticationService.add(register);
    }
}
