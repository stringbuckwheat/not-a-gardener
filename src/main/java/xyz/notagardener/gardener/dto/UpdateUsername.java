package xyz.notagardener.gardener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import xyz.notagardener.common.validation.UsernameConstraints;

@Getter
public class UpdateUsername {
    private Long id;

    @NotBlank(message = "아이디는 비워둘 수 없어요")
    @UsernameConstraints
    private String username;
}
