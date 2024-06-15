package xyz.notagardener.status.plant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import xyz.notagardener.status.plant.dto.AddStatusResponse;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.service.PlantStatusService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/status")
public class PlantStatusController {
    private final PlantStatusService plantStatusService;

    @Operation(summary = "한 식물의 식물 상태 설정", description = "인증된 사용자의 식물 상태 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 식물 상태 설정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddStatusResponse.class)
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
                                            value = "{\"code\": \"PLANTID_NOT_NULL\", \"message\": \"식물 정보를 확인해주세요\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<AddStatusResponse> add(@RequestBody @Valid PlantStatusRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantStatusService.add(request, user.getId()));
    }

    @Operation(
            summary = "식물 상태 삭제",
            description = "인증된 사용자의 식물 상태, 로그 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 식물 상태 설정, 로그 삭제 성공",
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
                            description = "FORBIDDEN: 요청자의 식물 상태 정보가 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_PLANT_STATUS\", \"title\": \"요청자의 식물 상태 정보가 아님\", \"message\": \"당신의 식물 상태 정보가 아니에요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 해당 식물 상태 정보 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_PLANT_STATUS\", \"title\": \"해당 식물 상태 정보 없음\", \"message\": \"해당 식물 상태 정보를 찾을 수 없어요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{statusId}")
    public ResponseEntity<PlantStatusResponse> delete(@PathVariable Long plantId, @PathVariable Long statusId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusService.delete(plantId, statusId, user.getId()));
    }
}
