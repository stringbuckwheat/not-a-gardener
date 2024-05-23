package xyz.notagardener.goal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.goal.dto.GoalDto;
import xyz.notagardener.goal.service.GoalService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/goal")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "목표 관련 API")
public class GoalController {
    private final GoalService goalService;

    @Operation(summary = " 한 회원의 전체 목표 리스트")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 회원의 전체 목표 리스트",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GoalDto.class)))
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
    @GetMapping("")
    public ResponseEntity<List<GoalDto>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(goalService.getAll(user.getId()));
    }


    @Operation(summary = "목표 추가", description = "인증된 사용자의 목표 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 목표 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class)
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
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 식물이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_PLANT\", \"title\": \"요청자의 식물이 아님\", \"message\": \"당신의 식물이 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 식물 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_PLANT\", \"title\": \"해당 식물 없음\", \"message\": \"해당 식물을 찾을 수 없어요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": \"CONTENT_NOT_BLANK\", \"message\": \"목표 내용을 입력해주세요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<GoalDto> add(@RequestBody @Valid GoalDto goalRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.add(user.getId(), goalRequest));
    }

    @Operation(summary = "목표 수정", description = "인증된 사용자의 목표 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 목표 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class)
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
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 목표, 식물이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_GOAL\", \"title\": \"요청자의 목표가 아님\", \"message\": \"당신의 목표가 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 식물 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_PLANT\", \"title\": \"해당 식물 없음\", \"message\": \"해당 식물을 찾을 수 없어요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST: 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": \"CONTENT_NOT_BLANK\", \"message\": \"목표 내용을 입력해주세요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<GoalDto> modify(@RequestBody @Valid GoalDto goalRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(goalService.update(user.getId(), goalRequest));
    }

    @Operation(summary = "목표 달성", description = "인증된 사용자의 목표 달성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 목표 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class)
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
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 목표가 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_GOAL\", \"title\": \"요청자의 목표가 아님\", \"message\": \"당신의 목표가 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 목표 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_GOAL\", \"title\": \"해당 목표 없음\", \"message\": \"해당 목표를 찾을 수 없어요\"}"
                            )
                    )
            ),
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<GoalDto> complete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(goalService.complete(id, user.getId()));
    }

    @Operation(
            summary = "약품 삭제",
            description = "인증된 사용자의 약품 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO_CONTENT: 약품 삭제 성공"),

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
                            responseCode = "403",
                            description = "FORBIDDEN: 요청자의 목표가 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_GOAL\", \"title\": \"요청자의 목표가 아님\", \"message\": \"당신의 목표가 아니에요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 해당 목표 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_GOAL\", \"title\": \"해당 목표 없음\", \"message\": \"해당 목표를 찾을 수 없어요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        goalService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
