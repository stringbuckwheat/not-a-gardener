package xyz.notagardener.gardener.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.gardener.authentication.dto.Info;
import xyz.notagardener.gardener.authentication.dto.Register;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
@Tag(name = "Register", description = "회원 가입 관련 API")
public class RegisterController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "아이디 중복 검사")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 중복 시 제출한 ID, 중복이 아닐 시 null",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/username/{username}")
    public String hasSameUsername(@PathVariable String username) {
        return authenticationService.hasSameUsername(username);
    }

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 회원가입 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Info.class)
                    )
            )
    })
    @PostMapping("")
    public Info add(@RequestBody Register register) {
        return authenticationService.add(register);
    }
}
