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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.VerifyResponse;
import xyz.notagardener.gardener.service.GardenerService;

@Slf4j
@RestController
@RequestMapping("/api/gardener")
@RequiredArgsConstructor
@Tag(name = "Gardeners", description = "회원 관련 API")
public class GardenerController {
    private final GardenerService gardenerService;

    @Operation(summary = "본인의 회원 정보")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 본인의 회원 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenerDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: PLEASE_LOGIN",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            )
    })
    @GetMapping("/{gardenerId}")
    public ResponseEntity<GardenerDetail> getOne(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(gardenerService.getOne(user.getId()));
    }

    @Operation(summary = "비밀번호 변경 전 한 번 입력받아서 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 비밀번호 일치 여부",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: PLEASE_LOGIN",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 계정 정보 없음 ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PostMapping("/password")
    public ResponseEntity<VerifyResponse> identify(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        VerifyResponse result = gardenerService.identify(user.getId(), login);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT: 비밀번호 변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: 비밀번호 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"INVALID_PASSWORD\", \"title\": \"비밀번호 유효성 검사 실패\", \"message\": \"숫자, 특수문자를 포함하여 8자리 이상이어야 해요.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: PLEASE_LOGIN",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 계정 정보 없음 ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        gardenerService.updatePassword(user.getId(), login);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 성공 시 회원 정보 리턴",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenerDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: PLEASE_LOGIN",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NAME_NOT_BLANK\", \"message\": \"이름은 비워둘 수 없어요.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 계정 정보 없음 ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/{gardenerId}")
    public ResponseEntity<GardenerDetail> update(@RequestBody @Valid GardenerDetail gardenerDetail, @PathVariable long gardenerId) {
        GardenerDetail gardenerDetail1 = gardenerService.update(gardenerDetail);
        return ResponseEntity.ok().body(gardenerDetail1);
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 탈퇴 완료"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: PLEASE_LOGIN",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 계정 정보 없음 ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{gardenerId}")
    public void delete(@PathVariable("gardenerId") long gardenerId) {
        gardenerService.delete(gardenerId);
    }
}
