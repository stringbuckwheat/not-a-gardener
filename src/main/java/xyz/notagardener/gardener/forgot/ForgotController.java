package xyz.notagardener.gardener.forgot;

import xyz.notagardener.gardener.authentication.dto.Login;
import xyz.notagardener.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
                    description = "Success: 이메일로 확인코드 전송, 가입한 아이디 목록 반환",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Forgot.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: 제출 이메일에 해당하는 사용자 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B007\", \"title\": \"제출 이메일에 해당하는 사용자 없음\", \"message\": \"해당 이메일로 가입한 회원이 없어요\"}"
                            )
                    )
            )
    })
    @GetMapping("/email/{email}")
    public Forgot forgotAccount(@PathVariable String email) {
        return forgotService.forgotAccount(email);
    }

    @Operation(summary = "비밀번호 재설정", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 비밀번호 재설정 완료"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: 제출 이메일에 해당하는 사용자 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B004\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/{username}/password")
    public void resetPassword(@RequestBody Login login) {
        forgotService.resetPassword(login);
    }
}
