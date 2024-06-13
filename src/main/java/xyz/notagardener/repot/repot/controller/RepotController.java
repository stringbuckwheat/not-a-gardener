package xyz.notagardener.repot.repot.controller;

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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.dto.RepotResponse;
import xyz.notagardener.repot.repot.service.RepotService;

import java.util.List;

@RequestMapping("/api/repot")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Repot", description = "분갈이 관련 API")
public class RepotController {
    private final RepotService repotService;

    @Operation(summary = "분갈이 기록 추가", description = "인증된 사용자의 분갈이 기록 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 분갈이 기록 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RepotResponse.class)
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
                                            value = "{\"code\": \"REPOTDATE_NOT_FUTURE\", \"message\": \"미래 날짜를 입력할 수 없어요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<RepotResponse> addOne(@RequestBody @Valid RepotRequest repotRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repotService.addOne(repotRequest, user.getId()));
    }

    @Operation(summary = "분갈이 기록 여러개 추가", description = "인증된 사용자의 분갈이 기록 여러개 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 분갈이 기록 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RepotResponse.class)
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
                                            value = "{\"code\": \"REPOTDATE_NOT_FUTURE\", \"message\": \"미래 날짜를 입력할 수 없어요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<RepotResponse>> addAll(@RequestBody @Valid List<RepotRequest> requests, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repotService.addAll(requests, user.getId()));
    }

    @GetMapping("")
    public ResponseEntity<List<RepotList>> getAll(@AuthenticationPrincipal UserPrincipal user, Pageable pageable) {
        return ResponseEntity.ok(repotService.getAll(user.getId(), pageable));
    }

    @Operation(summary = "분갈이 기록 수정", description = "인증된 사용자의 분갈이 기록 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 분갈이 기록 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RepotResponse.class)
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
                    description = "FORBIDDEN: 요청자의 분갈이 기록이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_REPOT\", \"title\": \"요청자의 분갈이 기록이 아님\", \"message\": \"당신의 분갈이 기록이 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 분갈이 기록 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_REPOT\", \"title\": \"해당 분갈이 기록 없음\", \"message\": \"해당 분갈이 기록을 찾을 수 없어요\"}"
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
                                            value = "{\"code\": \"REPOTDATE_NOT_FUTURE\", \"message\": \"미래 날짜를 입력할 수 없어요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<RepotResponse> update(@RequestBody @Valid RepotRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(repotService.update(request, user.getId()));
    }

    @Operation(summary = "분갈이 기록 수정", description = "인증된 사용자의 분갈이 기록 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 분갈이 기록 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RepotResponse.class)
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
                    description = "FORBIDDEN: 요청자의 분갈이 기록이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_REPOT\", \"title\": \"요청자의 분갈이 기록이 아님\", \"message\": \"당신의 분갈이 기록이 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 분갈이 기록 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_REPOT\", \"title\": \"해당 분갈이 기록 없음\", \"message\": \"해당 분갈이 기록을 찾을 수 없어요\"}"
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
                                            value = "{\"code\": \"REPOTDATE_NOT_FUTURE\", \"message\": \"미래 날짜를 입력할 수 없어요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PutMapping("/repot/bulk")
    public ResponseEntity<List<RepotResponse>> updateAll(@RequestBody @Valid List<RepotRequest> requests, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(repotService.updateAll(requests, user.getId()));
    }

    @Operation(
            summary = "분갈이 기록 삭제",
            description = "인증된 사용자의 분갈이 기록 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO_CONTENT: 분갈이 기록 삭제 성공"),
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
                            description = "FORBIDDEN: 요청자의 분갈이 기록이 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_REPOT\", \"title\": \"요청자의 분갈이 기록이 아님\", \"message\": \"당신의 분갈이 기록이 아니에요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 해당 분갈이 기록 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_REPOT\", \"title\": \"해당 분갈이 기록 없음\", \"message\": \"해당 분갈이 기록을 찾을 수 없어요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long repotId, @AuthenticationPrincipal UserPrincipal user) {
        repotService.delete(repotId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
