package dev.pet.mvc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Null
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @Min(0)
    @Max(200)
    @NotNull
    private Integer age;


    private List<PetDto> pets;
}
