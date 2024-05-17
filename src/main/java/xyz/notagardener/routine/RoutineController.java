package xyz.notagardener.routine;

import xyz.notagardener.common.auth.UserPrincipal;
import xyz.notagardener.routine.dto.RoutineComplete;
import xyz.notagardener.routine.dto.RoutineMain;
import xyz.notagardener.routine.dto.RoutineRequest;
import xyz.notagardener.routine.dto.RoutineResponse;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
@Tag(name = "Routines", description = "루틴 관련 API")
public class RoutineController {
    private final RoutineService routineService;

    @Operation(summary = "(인증) 한 회원의 전체 루틴 리스트")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 한 회원의 전체 루틴 리스트",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RoutineMain.class)))
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
    public RoutineMain getAll(@AuthenticationPrincipal UserPrincipal user) {
        return routineService.getAll(user.getId());
    }

    @Operation(summary = "(인증) 루틴 추가", description = "인증된 사용자의 루틴 추가")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 루틴 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RoutineResponse.class)
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
    public RoutineResponse add(@AuthenticationPrincipal UserPrincipal user, @RequestBody RoutineRequest routineRequest) {
        return routineService.add(routineRequest, user.getId());
    }

    @Operation(summary = "(인증) 루틴 완료", description = "인증된 사용자의 루틴 완료 처리")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 루틴 완료 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RoutineResponse.class)
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
    @PutMapping("/{routineId}/complete")
    public RoutineResponse complete(@RequestBody RoutineComplete routineComplete, @AuthenticationPrincipal UserPrincipal user) {
        return routineService.complete(routineComplete, user.getId());
    }

    @Operation(summary = "(인증) 루틴 수정", description = "인증된 사용자의 루틴 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 루틴 수정 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RoutineResponse.class)
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
    @PutMapping("/{routineId}")
    public RoutineResponse modify(@RequestBody RoutineRequest routineRequest, @AuthenticationPrincipal UserPrincipal user) {
        return routineService.update(routineRequest, user.getId());
    }

    @Operation(
            summary = "(인증) 루틴 삭제",
            description = "인증된 사용자의 루틴 삭제",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success: 루틴 삭제 성공"),
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
    @DeleteMapping("/{routineId}")
    public void deleteRoutine(@PathVariable Long routineId, @AuthenticationPrincipal UserPrincipal user) {
        routineService.delete(routineId, user.getId());
    }
}
