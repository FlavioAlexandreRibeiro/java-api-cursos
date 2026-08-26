package com.API_de_Cursos.dto;

import com.API_de_Cursos.entity.Curso;

public record CursoResponseDTO(

        Long id,
        String nome,
        String descricao,
        String categoria,
        Integer cargaHoraria,
        Long professorId
) {

    public static CursoResponseDTO fromEntity(Curso curso) {
        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getCategoria(),
                curso.getCargaHoraria(),
                curso.getProfessor() != null ? curso.getProfessor().getId() : null
        );
    }
}
