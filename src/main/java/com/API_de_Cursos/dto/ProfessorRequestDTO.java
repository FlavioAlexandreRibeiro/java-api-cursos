package com.API_de_Cursos.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorRequestDTO {

    @NotBlank(message = "Nome do professor é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do professor deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email do professor é obrigatório")
    @Email(message = "Email do professor deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Pattern(regexp = "\\d{10,15}", message = "Telefone deve conter entre 10 e 15 dígitos")
    private String telefone;

    @Size(max = 100, message = "Especialidade deve ter no máximo 100 caracteres")
    private String especialidade;
}
