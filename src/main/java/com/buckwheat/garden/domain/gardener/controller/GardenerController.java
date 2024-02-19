package com.buckwheat.garden.domain.gardener.controller;

import com.buckwheat.garden.domain.gardener.dto.GardenerDetail;
import com.buckwheat.garden.domain.gardener.dto.Login;
import com.buckwheat.garden.domain.gardener.service.GardenerService;
import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
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

@Slf4j
@RestController
@RequestMapping("/api/gardener")
@RequiredArgsConstructor
@Tag(name = "Gardeners", description = "회원 관련 API")
public class GardenerController {
    private final GardenerService gardenerService;

    @Operation(summary = "(인증) 본인의 회원 정보")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 본인의 회원 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenerDetail.class))
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
    @GetMapping("/{gardenerId}")
    public GardenerDetail getOne(@AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.getOne(user.getId());
    }

    @Operation(summary = "(인증) 비밀번호 변경 전 한 번 입력받아서 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 비밀번호 일치 여부",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Boolean.class))
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
    @PostMapping("/password")
    public boolean identify(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.identify(user.getId(), login);
    }

    @Operation(summary = "(인증) 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 비밀번호 변경 성공"
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
    @PutMapping("/password")
    public void updatePassword(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        gardenerService.updatePassword(user.getId(), login);
    }

    @Operation(summary = "(인증) 회원 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 성공 시 회원 정보 리턴",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GardenerDetail.class))
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
    @PutMapping("/{gardenerId}")
    public GardenerDetail update(@RequestBody GardenerDetail gardenerDetail, @PathVariable long gardenerId) {
        return gardenerService.update(gardenerDetail);
    }

    @Operation(summary = "(인증) 회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success: 탈퇴 완료"
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
    @DeleteMapping("/{gardenerId}")
    public void delete(@PathVariable("gardenerId") long gardenerId) {
        gardenerService.delete(gardenerId);
    }
}
