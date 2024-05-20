package xyz.notagardener.watering.plant;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.common.auth.UserPrincipal;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.plant.dto.PlantWateringResponse;
import xyz.notagardener.watering.plant.dto.WateringForOnePlant;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/watering")
@Tag(name = "Plant-Waterings", description = "한 식물의 물 주기 기록 관련 API")
public class PlantWateringController {
    private final PlantWateringService plantWateringService;

    @Operation(summary = "(인증) 한 식물의 물 준 내역", description = "10개 단위 페이징")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 한 식물의 물 준 내역",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = WateringForOnePlant.class)))
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
    @GetMapping("")
    public List<WateringForOnePlant> getAllWithPaging(@PathVariable long plantId, @PageableDefault(size = 10) Pageable pageable) {
        return plantWateringService.getAll(plantId, pageable);
    }

    @Operation(summary = "(인증) 특정 식물에 물 주기 기록 추가", description = "인증된 사용자의 특정 식물에 물 준 기록 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 약품 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PlantWateringResponse.class),
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
    public PlantWateringResponse add(@RequestBody WateringRequest wateringRequest, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        return plantWateringService.add(wateringRequest, pageable, user.getId());
    }

    @Operation(summary = "(인증) 한 식물의 물 주기 기록 수정", description = "인증된 사용자의 한 식물에 대한 물 주기 기록 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 한 식물의 물 주기 기록 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PlantWateringResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"8\", \"name\": \"빅카드\", \"type\": \"농약(살충)\", \"period\": \"180\"}"
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
    @PutMapping("/{wateringId}")
    public PlantWateringResponse modify(@RequestBody WateringRequest wateringRequest,
                                        @PageableDefault(size = 10) Pageable pageable,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return plantWateringService.update(wateringRequest, pageable, userPrincipal.getId());
    }

    @Operation(
            summary = "(인증) 한 식물의 물 주기 기록 개별 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success: 한 식물의 물 주기 기록 개별 삭제 성공"),
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
    public void delete(@PathVariable Long wateringId, @PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        plantWateringService.delete(wateringId, plantId, userPrincipal.getId());
    }

    @Operation(
            summary = "(인증) 한 식물의 물 주기 기록 전체 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success: 한 식물의 물 주기 기록 전체 삭제 성공"),
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
    @DeleteMapping("")
    public void deleteAll(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        plantWateringService.deleteAll(plantId, user.getId());
    }
}
