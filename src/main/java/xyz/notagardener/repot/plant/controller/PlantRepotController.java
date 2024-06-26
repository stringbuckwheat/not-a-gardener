package xyz.notagardener.repot.plant.controller;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.plant.service.PlantRepotService;
import xyz.notagardener.repot.repot.dto.RepotRequest;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/repot")
@Tag(name = "Plant-Repot", description = "한 식물의 분갈이")
public class PlantRepotController {
    private final PlantRepotService plantRepotService;

    @Operation(summary = "한 식물의 분갈이 기록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 식물의 분갈이 기록(10개 단위 페이징)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PlantResponse.class)))
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
    public ResponseEntity<List<RepotList>> get(@PathVariable Long plantId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(plantRepotService.getAllRepotForOnePlant(plantId, pageable));
    }

    @Operation(summary = "한 식물의 분갈이 기록 추가", description = "인증된 사용자의 분갈이 기록 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 분갈이 기록 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RepotList.class)
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
    public ResponseEntity<RepotList> add(@RequestBody @Valid RepotRequest repotRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantRepotService.add(repotRequest, user.getId()));
    }
}
