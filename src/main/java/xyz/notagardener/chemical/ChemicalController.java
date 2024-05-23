package xyz.notagardener.chemical;

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
import xyz.notagardener.chemical.dto.ChemicalDetail;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.dto.WateringResponseInChemical;
import xyz.notagardener.chemical.service.ChemicalService;
import xyz.notagardener.common.error.ErrorResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chemical")
@Slf4j
@Tag(name = "Chemicals", description = "약품 관련 API")
public class ChemicalController {
    private final ChemicalService chemicalService;

    @Operation(summary = "한 회원의 전체 약품 리스트")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 회원의 전체 약품 리스트",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ChemicalDto.class)))
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
            )
    })
    @GetMapping("")
    public ResponseEntity<List<ChemicalDto>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        List<ChemicalDto> chemicals = chemicalService.getAll(user.getId());
        return ResponseEntity.ok().body(chemicals);
    }

    @Operation(summary = "한 약품의 상세 정보")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 약품의 상세 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ChemicalDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 로그인 필요",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 약품 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_CHEMICAL\", \"title\": \"해당 약품 없음\", \"message\": \"해당 약품을 찾을 수 없어요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 약품이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_CHEMICAL\", \"title\": \"요청자의 약품이 아님\", \"message\": \"당신의 약품이 아니에요\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChemicalDetail> getOne(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        ChemicalDetail chemicalDetail = chemicalService.getOne(id, user.getId());
        return ResponseEntity.ok().body(chemicalDetail);
    }

    @Operation(
            summary = "한 약품의 시비 정보",
            description = "10개 단위 페이징"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 한 약품의 시비 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = WateringResponseInChemical.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 로그인 필요",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}/watering")
    public ResponseEntity<List<WateringResponseInChemical>> getWateringWithPaging(@PathVariable Long id, @PageableDefault(size = 10) Pageable pageable) {
        List<WateringResponseInChemical> result = chemicalService.getWateringsForChemical(id, pageable);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "약품 추가", description = "인증된 사용자의 약품 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: 약품 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ChemicalDto.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"7\", \"name\": \"메네델\", \"type\": \"미량 원소 비료\", \"period\": \"30\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 로그인 필요",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
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
                                            value = "{\"code\": \"CHEMICAL_TYPE_INVALID\", \"title\": \"유효한 약품 타입이 아님\", \"message\": \"약품 타입을 확인해주세요\"}"
                                    ),
                            }
                    )
            ),
    })
    @PostMapping("")
    public ResponseEntity<ChemicalDto> add(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid ChemicalDto chemicalRequest) {
        ChemicalDto chemicalDto = chemicalService.add(user.getId(), chemicalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(chemicalDto);
    }

    @Operation(summary = "약품 수정", description = "인증된 사용자의 약품 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 약품 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ChemicalDto.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"8\", \"name\": \"빅카드\", \"type\": \"농약(살충)\", \"period\": \"180\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: 로그인 필요",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: 요청자의 약품이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NOT_YOUR_CHEMICAL\", \"title\": \"요청자의 약품이 아님\", \"message\": \"당신의 약품이 아니에요\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: 해당 약품 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"NO_SUCH_CHEMICAL\", \"title\": \"해당 약품 없음\", \"message\": \"해당 약품을 찾을 수 없어요\"}"
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
                                            value = "{\"code\": \"NAME_NOT_BLANK\", \"message\": \"약품 이름을 입력해주세요.\"}"
                                    ),
                            }
                    )
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ChemicalDto> update(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid ChemicalDto chemicalRequest) {
        ChemicalDto chemicalDto = chemicalService.update(user.getId(), chemicalRequest);
        return ResponseEntity.ok().body(chemicalDto);
    }

    @Operation(
            summary = "약품 삭제",
            description = "인증된 사용자의 약품 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO_CONTENT: 약품 삭제 성공"),
                    @ApiResponse(
                            responseCode = "302",
                            description = "PLEASE_LOGIN",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"PLEASE_LOGIN\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "FORBIDDEN: 요청자의 약품이 아님",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NOT_YOUR_CHEMICAL\", \"title\": \"요청자의 약품이 아님\", \"message\": \"당신의 약품이 아니에요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 해당 약품 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"code\": \"NO_SUCH_CHEMICAL\", \"title\": \"해당 약품 없음\", \"message\": \"해당 약품을 찾을 수 없어요\"}"
                                    )
                            )
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        chemicalService.deactivate(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
