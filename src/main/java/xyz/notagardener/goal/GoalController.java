package xyz.notagardener.goal;

import xyz.notagardener.common.auth.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/goal")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "목표 관련 API")
public class GoalController {
    private final GoalService goalService;

    @Operation(summary = "(인증) 한 회원의 전체 목표 리스트")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 한 회원의 전체 목표 리스트",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GoalDto.class)))
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
            )
    })
    @GetMapping("")
    public List<GoalDto> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return goalService.getAll(user.getId());
    }

    @Operation(summary = "(인증) 목표 추가", description = "인증된 사용자의 목표 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 목표 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class)
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
            )
    })
    @PostMapping("")
    public GoalDto add(@RequestBody GoalDto goalRequest, @AuthenticationPrincipal UserPrincipal user) {
        return goalService.add(user.getId(), goalRequest);
    }

    @Operation(summary = "(인증) 목표 수정", description = "인증된 사용자의 목표 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 목표 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class)
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
                    description = "No Such Item: 선택한 목표, 식물이 없을 시",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"
                            )
                    )
            )
    })
    @PutMapping("/{goalId}")
    public GoalDto modify(@RequestBody GoalDto goalRequest, @AuthenticationPrincipal UserPrincipal user) {
        return goalService.update(user.getId(), goalRequest);
    }

    @Operation(summary = "(인증) 목표 달성", description = "인증된 사용자의 목표 달성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 목표 달성 기록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoalDto.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": \"8\", \"content\": \"사라왁 꽃 보기\", \"complete\": \"Y\", \"plantId\": \"8\", \"plantName\": \"베고니아 사라왁\"}"
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
    @PutMapping("/{goalId}/complete")
    public GoalDto complete(@PathVariable Long goalId, @AuthenticationPrincipal UserPrincipal user) {
        return goalService.complete(goalId, user.getId());
    }

    @Operation(
            summary = "(인증) 약품 삭제",
            description = "인증된 사용자의 약품 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success: 약품 삭제 성공"),
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
    @DeleteMapping("/{goalId}")
    public void delete(@PathVariable Long goalId, @AuthenticationPrincipal UserPrincipal user) {
        goalService.delete(goalId, user.getId());
    }
}
