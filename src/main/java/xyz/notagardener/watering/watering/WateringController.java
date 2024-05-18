package xyz.notagardener.watering.watering;

import xyz.notagardener.common.auth.UserPrincipal;
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
                    description = "Success: 캘린더 단위 물주기 기록 정보",
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
                    description = "Unauthorized: 인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B001\", \"title\": \"인증 실패\", \"message\": \"토큰이 유효하지 않습니다\"}"
                            )
                    )
            )
    })
    @GetMapping("/date/{date}")
    public Map<LocalDate, List<WateringByDate>> getAll(@AuthenticationPrincipal UserPrincipal user, @PathVariable String date) {
        return wateringService.getAll(user.getId(), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
    }

    @Operation(summary = "(인증) 물 주기 기록 추가", description = "인증된 사용자의 물 주기 기록 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 물 주기 기록 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WateringByDate.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"7\", \"name\": \"메네델\", \"type\": \"미량 원소 비료\", \"period\": \"30\"}"
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
    @PostMapping("")
    public WateringByDate add(@RequestBody WateringRequest wateringRequest, @AuthenticationPrincipal UserPrincipal user) {
        return wateringService.add(wateringRequest, user.getId());
    }

    @Operation(
            summary = "(인증) 물 주기 기록 삭제",
            description = "인증된 사용자의 물 주기 기록 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success: 물 주기 기록 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B000\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{wateringId}")
    public void delete(@PathVariable Long wateringId, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody Map<String, Long> request) {
        wateringService.delete(wateringId, request.get("plantId"), userPrincipal.getId());
    }
}
