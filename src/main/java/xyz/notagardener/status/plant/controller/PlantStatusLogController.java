package xyz.notagardener.status.plant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.dto.StatusLogResponse;
import xyz.notagardener.status.plant.service.PlantStatusLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/status/log")
@Slf4j
@Tag(name = "Plant-Status", description = "한 식물의 상태 정보")
public class PlantStatusLogController {
    private final PlantStatusLogService plantStatusLogService;

    @Operation(summary = "한 식물의 상태 정보")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 식물의 상태 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StatusLogResponse.class)))
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
    @GetMapping("")
    public ResponseEntity<List<StatusLogResponse>> getAllLog(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusLogService.getAllLog(plantId, user.getId()));
    }

    @Operation(
            summary = "식물 상태 로그 삭제",
            description = "인증된 사용자의 식물 상태 로그 삭제, 식물 상태 변경",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 식물 상태 로그 삭제, 식물 상태 변경",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PlantStatusResponse.class)
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
                            description = "FORBIDDEN: 요청자의 식물 상태 로그가 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_PLANT_STATUS_LOG\", \"title\": \"요청자의 식물 상태 로그가 아님\", \"message\": \"당신의 식물 상태 로그가 아니에요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 해당 식물 상태 로그 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_PLANT_STATUS_LOG\", \"title\": \"해당 식물 상태 로그 없음\", \"message\": \"해당 식물 상태 로그를 찾을 수 없어요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{statusLogId}")
    public ResponseEntity<PlantStatusResponse> deleteOne(@PathVariable Long statusLogId, @AuthenticationPrincipal UserPrincipal user) {
        plantStatusLogService.deleteOne(statusLogId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
