package xyz.notagardener.plant.garden.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.common.error.ErrorResponse;
import xyz.notagardener.plant.garden.dto.GardenMain;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.service.GardenService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/garden")
@Tag(name = "Garden", description = "계산을 필요로 하는 식물 API")
public class GardenController {
    private final GardenService gardenService;

    @Operation(summary = "오늘 할 일이 있는 식물, 물 주기 정보가 없는 식물, 나의 루틴",
            description = "메인 페이지 용\n"
                    + "<div style=\"font-size: larger;\">"
                    + "<ul>"
                    + "   <li>오늘 할 일이 있는 식물</li>"
                    + "     <ul>"
                    + "         <li>식물 상세 정보</li>"
                    + "         <li>물 주기 코드</li>"
                    + "             <ul>"
                    + "                 <li>-1: 물주기 놓침</li>"
                    + "                 <li>0: 물주기 정보 부족</li>"
                    + "                 <li>1: 오늘 물 줘야함</li>"
                    + "                 <li>2: 체크하기(물주기 하루 전)</li>"
                    + "                 <li>3: 물주기 늘어나는 중</li>"
                    + "                 <li>4: 놔두세요(할 일 없음)</li>"
                    + "                 <li>5: 오늘 물 줌</li>"
                    + "                 <li>6: 유저가 물주기를 미룸</li>"
                    + "             </ul>"
                    + "         <li>약품 정보: 오늘 물을 준다면 이 약품을 함께 주세요</li>"
                    + "             <ul>"
                    + "                 <li>chemicalId: 약품 id</li>"
                    + "                 <li>chemicalName: 약품 이름</li>"
                    + "             </ul>"
                    + "     </ul>"
                    + "   <li>물 주기 정보가 없는 식물: 한 번도 물 주지 않은 식물</li>"
                    + "   <li>나의 루틴</li>"
                    + "</ul>"
                    + "</div>"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 오늘 할 일이 있는 식물, 물 주기 정보가 없는 식물, 나의 루틴",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GardenMain.class)))
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
    public ResponseEntity<GardenMain> getGardenMain(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(gardenService.getGarden(user.getId()));
    }

    @Operation(summary = "전체 식물의 계산 정보, 상세 정보 리스트",
            description = "<div style=\"font-size: larger;\">"
                    + "     <ul style=\"font-size: larger;\">"
                    + "         <li>식물 상세 정보</li>"
                    + "         <li>물 주기 코드</li>"
                    + "             <ul>"
                    + "                 <li>-1: 물주기 놓침</li>"
                    + "                 <li>0: 물주기 정보 부족</li>"
                    + "                 <li>1: 오늘 물 줘야함</li>"
                    + "                 <li>2: 체크하기(물주기 하루 전)</li>"
                    + "                 <li>3: 물주기 늘어나는 중</li>"
                    + "                 <li>4: 놔두세요(할 일 없음)</li>"
                    + "                 <li>5: 오늘 물 줌</li>"
                    + "                 <li>6: 유저가 물주기를 미룸</li>"
                    + "             </ul>"
                    + "         <li>약품 정보: 오늘 물을 준다면 이 약품을 함께 주세요</li>"
                    + "             <ul>"
                    + "                 <li>chemicalId: 약품 id</li>"
                    + "                 <li>chemicalName: 약품 이름</li>"
                    + "             </ul>"
                    + "     </ul>"
                    + "</div>"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: 오늘 할 일이 있는 식물, 물 주기 정보가 없는 식물, 나의 루틴",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GardenResponse.class)))
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
    @GetMapping("/plants")
    public ResponseEntity<List<GardenResponse>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(gardenService.getAll(user.getId()));
    }
}
