package xyz.notagardener.watering.watering.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.watering.watering.dto.WateringByDate;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.service.WateringService;
import xyz.notagardener.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watering")
@Tag(name = "Waterings", description = "물주기 관련 API")
public class WateringController {
    private final WateringService wateringService;

    @Operation(
            summary = "캘린더 단위 물주기 기록 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 캘린더 단위 물주기 기록 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value =
                                    "{\"2023-10-31\": [{\"id\": 166, \"plantId\": 31, \"plantName\": \"구구토끼공주\", \"chemicalId\": null, \"chemicalName\": null, \"wateringDate\": \"2023-10-31\"}], " +
                                            "\"2023-10-30\": [{\"id\": 4, \"plantId\": 7, \"plantName\": \"무싱\", \"chemicalId\": null, \"chemicalName\": null, \"wateringDate\": \"2023-10-30\"}]}"
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
    })
    @GetMapping("/date/{date}")
    public Map<LocalDate, List<WateringByDate>> getAll(@AuthenticationPrincipal UserPrincipal user, @PathVariable String date) {
        return wateringService.getAll(user.getId(), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
    }

    @Operation(summary = "물 주기 기록 추가", description = "인증된 사용자의 물 주기 기록 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 물 주기 기록 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WateringByDate.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"7\", \"name\": \"메네델\", \"type\": \"미량 원소 비료\", \"period\": \"30\"}"
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
                    description = "NOT_FOUND: 해당 약품, 식물 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_PLANT\", \"title\": \"해당 식물 없음\", \"message\": \"해당 식물을 찾을 수 없어요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 약품, 식물이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_PLANT\", \"title\": \"요청자의 식물이 아님\", \"message\": \"당신의 식물이 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CONFLICT: 이미 물 준 날짜",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"ALREADY_WATERED\", \"title\": \"이미 물 준 날짜\", \"message\": \"이 날짜엔 이미 물을 줬어요\"}"
                            )
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<WateringByDate> add(@RequestBody @Valid WateringRequest wateringRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wateringService.add(wateringRequest, user.getId()));
    }

    @Operation(
            summary = "물 주기 기록 삭제",
            description = "인증된 사용자의 물 주기 기록 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO_CONTENT: 물 주기 기록 삭제 성공"),
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
                            description = "NOT_FOUND: 해당 약품, 식물 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_PLANT\", \"title\": \"해당 식물 없음\", \"message\": \"해당 식물을 찾을 수 없어요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "FORBIDDEN: 요청자의 물주기 기록이 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_WATERING\", \"title\": \"요청자의 물주기 기록이 아님\", \"message\": \"당신의 물주기 기록이 아니에요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody Map<String, Long> request) {
        wateringService.delete(id, request.get("plantId"), userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }
}
