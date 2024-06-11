package xyz.notagardener.plant.place.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.plant.place.service.PlacePlantService;
import xyz.notagardener.plant.plant.dto.PlantInPlace;
import xyz.notagardener.plant.plant.dto.PlantRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/place/{id}/plant")
@Tag(name = "Place-Plants", description = "장소 페이지 내 식물 관련 API")
public class PlacePlantController {
    private final PlacePlantService placePlantService;

    @Operation(summary = "해당 장소에 식물 추가", description = "해당 장소에 인증된 사용자의 식물 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 식물 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PlantInPlace.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"7\", \"name\": \"메네델\", \"type\": \"미량 원소 비료\", \"period\": \"30\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 장소가 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_PLACE\", \"title\": \"요청자의 장소가 아님\", \"message\": \"당신의 장소가 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 장소 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_PLACE\", \"title\": \"해당 장소 없음\", \"message\": \"해당 장소를 찾을 수 없어요\"}"
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
    public ResponseEntity<PlantInPlace> addPlantInPlace(@RequestBody @Valid PlantRequest plantRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(placePlantService.addPlantInPlace(user.getId(), plantRequest));
    }
}
