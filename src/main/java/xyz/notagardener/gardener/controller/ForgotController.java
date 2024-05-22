package xyz.notagardener.gardener.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.gardener.dto.Forgot;
import xyz.notagardener.gardener.dto.VerifyRequest;
import xyz.notagardener.gardener.dto.VerifyResponse;
import xyz.notagardener.gardener.service.ForgotService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forgot")
@Tag(name = "Forgots", description = "아이디/비밀번호 찾기")
@Hidden
public class ForgotController {
    private final ForgotService forgotService;

    @Operation(summary = "아이디 찾기", description = "가입 시 제출한 이메일로 확인코드 전송, 대조 후 아이디 찾기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 이메일로 확인코드 전송, 가입한 아이디 목록 반환",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Forgot.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 제출 이메일에 해당하는 사용자 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT_FOR_EMAIL\", \"title\": \"제출 이메일에 해당하는 사용자 없음\", \"message\": \"해당 이메일로 가입한 회원이 없어요\"}"
                            )
                    )
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Forgot> forgotAccount(@PathVariable String email) {
        Forgot forgot = forgotService.forgotAccount(email);
        return ResponseEntity.ok().body(forgot);
    }

    @Operation(summary = "본인 확인 코드 검증", description = "이메일로 보낸 본인 확인 코드와 서버 저장값 대조")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 검증 여부",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 제출 확인 코드 해당 내역 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_IDENTIFICATION_INFO_IN_REDIS\", \"title\": \"레디스에 해당 확인코드 정보 없음\", \"message\": \"본인 확인 코드를 확인해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 본인 확인 코드 검증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_IDENTIFICATION_CODE\", \"title\": \"본인 확인 코드 불일치\", \"message\": \"본인 확인 코드를 확인해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: DTO 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"IDENTIFICATIONCODE_NOT_BLANK\", \"message\": \"본인 확인 코드를 확인해주세요\"}"
                            )
                    )
            )
    })
    public ResponseEntity<VerifyResponse> verifyIdentificationCode(@RequestBody @Valid VerifyRequest verifyRequest) {
        VerifyResponse result = forgotService.verifyIdentificationCode(verifyRequest);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "비밀번호 재설정", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT: 비밀번호 재설정 완료"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 제출 아이디에 해당하는 사용자 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_ACCOUNT\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/{username}/password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid Login login) {
        forgotService.resetPassword(login);
        return ResponseEntity.noContent().build();
    }
}
