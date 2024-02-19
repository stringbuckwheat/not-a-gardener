package com.buckwheat.garden.domain.watering.controller;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.plant.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;
import com.buckwheat.garden.domain.watering.service.GardenWateringService;
import com.buckwheat.garden.global.error.ErrorResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/garden/{plantId}/watering")
@Slf4j
@Tag(name = "Garden-Waterings", description = "메인 페이지의 물주기 기록 관련 API")
public class GardenWateringController {
    private final GardenWateringService gardenWateringService;

    @Operation(summary = "(인증) 메인 페이지에서 물 준 기록 추가", description = "(메인 페이지용) 물 주기 기록 업데이트에 따라 변경된 계산 결과를 포함하여 리턴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 물 주기 기록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenWateringResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B000\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No Such Item",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PostMapping("")
    public GardenWateringResponse add(@AuthenticationPrincipal UserPrincipal user, @RequestBody WateringRequest wateringRequest) {
        return gardenWateringService.add(user.getId(), wateringRequest);
    }

    @Operation(summary = "(인증) '물이 덜 말랐어요' 기록", description = "물을 주라는 알림이 도착했지만 화분에 물이 마르지 않았을 때")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: '물이 덜 말랐어요' 기록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenWateringResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"afterWateringCode\": \"1 // 물주기가 늘어났어요\", \"recentWateringPeriod\": \"7\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B000\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No Such Item",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/not-dry")
    public WateringMessage notDry(@PathVariable Long plantId) {
        return gardenWateringService.notDry(plantId);
    }

    @Operation(summary = "(인증) '물 주기를 미룰래요' 기록", description = "그냥 귀찮아서 미룰 때. WateringCode.YOU_ARE_LAZY(1) 반환")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: '물 주기를 미룰래요' 기록 성공. WateringCode.YOU_ARE_LAZY(1) 반환",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B000\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No Such Item",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/postpone")
    public int postpone(@PathVariable Long plantId) {
        return gardenWateringService.postpone(plantId);
    }
}
