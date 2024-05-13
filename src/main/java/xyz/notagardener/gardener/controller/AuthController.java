package xyz.notagardener.domain.gardener.controller;

import xyz.notagardener.domain.gardener.dto.Info;
import xyz.notagardener.domain.gardener.dto.Login;
import xyz.notagardener.domain.gardener.dto.Refresh;
import xyz.notagardener.domain.gardener.dto.Token;
import xyz.notagardener.domain.gardener.service.AuthenticationService;
import xyz.notagardener.domain.gardener.token.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Authentications", description = "인증 관련 API")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "로그인",
            description = "사용자 로그인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success: 로그인 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: 로그인 실패",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B003\", \"title\": \"로그인 실패\", \"message\": \"아이디 또는 비밀번호가 잘못되었습니다\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public Info login(@RequestBody Login login) {
        return authenticationService.login(login);
    }

    @Operation(
            summary = "액세스 토큰 갱신",
            description = "리프레시 토큰을 사용하여 액세스 토큰 갱신",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success: 액세스 토큰 갱신 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Token.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: 리프레시 토큰 만료",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B002\", \"title\": \"리프레쉬 토큰 만료\", \"message\": \"로그인 시간이 만료되었습니다\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: NO_TOKEN_IN_REDIS",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B009\", \"title\": \"레디스에 사용자 없음\", \"message\": \"로그인 시간이 만료되었습니다. 다시 로그인해주세요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: INVALID_REFRESH_TOKEN",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B011\", \"title\": \"유효하지 않은 리프레쉬 토큰\", \"message\": \"비정상적인 움직임이 발생했어요. 다시 로그인해주세요.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/token")
    public Token refreshToken(@RequestBody Refresh refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }

    @Operation(
            summary = "로그아웃",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success: 로그아웃 성공"
                    )
            }
    )
    @PostMapping("/logout/{gardenerId}")
    public void logOut(@PathVariable Long gardenerId) {
        authenticationService.logOut(gardenerId);
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "인증된 유저의 정보 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success: 사용자 정보 조회 성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    )
            }
    )
    @GetMapping("/info")
    public Info getGardenerInfo(@AuthenticationPrincipal UserPrincipal user) {
        return authenticationService.getGardenerInfo(user.getId());
    }
}
