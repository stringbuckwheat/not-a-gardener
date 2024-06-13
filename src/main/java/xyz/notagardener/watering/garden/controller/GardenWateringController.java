package xyz.notagardener.watering.garden.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.watering.garden.service.GardenWateringService;
import xyz.notagardener.watering.garden.dto.GardenWateringResponse;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/garden/{plantId}/watering")
@Slf4j
@Tag(name = "Garden-Waterings", description = "메인 페이지의 물주기 기록 관련 API")
public class GardenWateringController {
    private final GardenWateringService gardenWateringService;

    @Operation(summary = "메인 페이지에서 물 준 기록 추가", description = "(메인 페이지용) 물 주기 기록 업데이트에 따라 변경된 계산 결과를 포함하여 리턴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 물 주기 기록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenWateringResponse.class)
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
                    responseCode = "409",
                    description = "CONFLICT: 이미 물 준 날짜",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"ALREADY_WATERED\", \"title\": \"이미 물 준 날짜\", \"message\": \"이 날짜엔 이미 물을 줬어요\"}"
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
                    responseCode = "400",
                    description = "BAD_REQUEST: 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": \"WATERINGDATE_NOT_BLANK\", \"message\": \"물 준 날짜는 비워둘 수 없어요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<GardenWateringResponse> add(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid WateringRequest wateringRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gardenWateringService.add(user.getId(), wateringRequest));
    }

    @Operation(summary = "'물이 덜 말랐어요' 기록", description = "물을 주라는 알림이 도착했지만 화분에 물이 마르지 않았을 때")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: '물이 덜 말랐어요' 기록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenWateringResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"afterWateringCode\": \"1 // 물주기가 늘어났어요\", \"recentWateringPeriod\": \"7\"}"
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
    })
    @PutMapping("/not-dry")
    public ResponseEntity<WateringMessage> notDry(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(gardenWateringService.notDry(plantId, user.getId()));
    }

    @Operation(summary = "'물 주기를 미룰래요' 기록", description = "그냥 귀찮아서 미룰 때.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: '물 주기를 미룰래요' 기록 성공. WateringCode.YOU_ARE_LAZY(1) 반환",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")
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
    })
    @PutMapping("/postpone")
    public ResponseEntity<String> postpone(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(gardenWateringService.postpone(plantId, user.getId()));
    }
}
