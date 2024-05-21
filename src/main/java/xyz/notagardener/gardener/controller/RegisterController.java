package xyz.notagardener.gardener.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.gardener.GardenerUtils;
import xyz.notagardener.gardener.dto.Register;
import xyz.notagardener.gardener.service.GardenerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
@Tag(name = "Register", description = "회원 가입 관련 API")
public class RegisterController {
    private final GardenerService gardenerService;

    @Operation(summary = "아이디 중복 검사")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 중복 시 Exception, 중복 아닐 시 제출한 username",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<String> hasSameUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(gardenerService.hasSameUsername(username));
    }

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 회원가입 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Info.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"INVALID_PASSWORD\", \"title\": \"비밀번호 유효성 검사 실패\", \"message\": \"숫자, 특수문자를 포함하여 8자리 이상이어야 해요.\"}"
                            )
                    )
            ),
    })
    @PostMapping("")
    public Info add(@RequestBody @Valid Register register) {
        if (!GardenerUtils.isPasswordValid(register.getPassword())) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_PASSWORD.getCode());
        }

        return gardenerService.add(register);
    }
}
