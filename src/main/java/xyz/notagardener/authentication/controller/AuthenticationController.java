package xyz.notagardener.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.dto.Refresh;
import xyz.notagardener.authentication.dto.Token;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.authentication.service.AuthenticationService;
import xyz.notagardener.common.error.ErrorResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Authentications", description = "인증 관련 API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "로그인",
            description = "사용자 로그인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 로그인 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: 로그인 실패",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"WRONG_ACCOUNT\", \"title\": \"로그인 실패\", \"message\": \"아이디 또는 비밀번호를 다시 확인해주세요.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Info> login(@RequestBody @Valid Login login) {
        Info info = authenticationService.login(login);
        return ResponseEntity.ok(info);
    }

    @Operation(
            summary = "액세스/리프레쉬 토큰 갱신",
            description = "리프레쉬 토큰을 사용하여 액세스 토큰, 리프레쉬 토큰 모두 갱신",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 액세스 토큰 갱신 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Token.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: 리프레시 토큰 만료",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"REFRESH_TOKEN_EXPIRED\", \"title\": \"리프레쉬 토큰 만료\", \"message\": \"로그인 시간이 만료되었습니다\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 레디스에 사용자 정보 없음",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_TOKEN_IN_REDIS\", \"title\": \"레디스에 사용자 없음\", \"message\": \"로그인 시간이 만료되었습니다. 다시 로그인해주세요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: 유효하지 않은 리프레쉬 토큰",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"INVALID_REFRESH_TOKEN\", \"title\": \"유효하지 않은 리프레쉬 토큰\", \"message\": \"비정상적인 움직임이 발생했어요. 다시 로그인해주세요.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/token")
    public ResponseEntity<Token> refreshAccessToken(@RequestBody @Valid Refresh refreshToken) {
        Token token = authenticationService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok().body(token);
    }

    @Operation(
            summary = "로그아웃",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "NO_CONTENT: 로그아웃 성공"
                    ),

            }
    )
    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logOut(@PathVariable Long id) {
        authenticationService.logOut(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "인증된 유저의 정보 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 사용자 정보 조회 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: 로그인 실패",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"로그인 실패\", \"message\": \"아이디 또는 비밀번호를 다시 확인해주세요.\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 로그인 실패",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"WRONG_ACCOUNT\", \"title\": \"로그인 실패\", \"message\": \"아이디 또는 비밀번호를 다시 확인해주세요.\"}"
                                    )
                            )
                    )

            }
    )
    @GetMapping("/info")
    public ResponseEntity<Info> getGardenerInfo(@AuthenticationPrincipal UserPrincipal user) {
        Info info = authenticationService.getGardenerInfo(user.getId());
        return ResponseEntity.ok().body(info);
    }
}
