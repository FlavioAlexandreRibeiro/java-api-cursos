package com.API_de_Cursos.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoRequestDTO {


    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Descrição do curso é obrigatória")
    @Size(max = 500, message = "Descrição do curso deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "Categoria do curso é obrigatória")
    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    private String categoria;

    @NotNull(message = "Carga horária do curso é obrigatória")
    @Positive(message = "Carga horária deve ser positiva")
    private Integer cargaHoraria;

    @NotNull(message = "ID do Professor do curso é obrigatório")
    private Long professorId;
}
