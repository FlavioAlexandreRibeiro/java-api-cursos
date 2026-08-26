package com.API_de_Cursos.dto;

import com.API_de_Cursos.entity.Professor;

public record ProfessorResponseDTO(

        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade
) {

    public static ProfessorResponseDTO fromEntity(Professor professor) {
        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getTelefone(),
                professor.getEspecialidade()
        );
    }
}
