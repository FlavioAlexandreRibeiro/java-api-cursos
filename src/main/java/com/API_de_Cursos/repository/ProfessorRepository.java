package com.API_de_Cursos.repository;

import com.API_de_Cursos.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProfessorRepository extends JpaRepository<Professor, Long>,
        JpaSpecificationExecutor<Professor> {

    boolean existsByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByNomeAndIdNot(String nome, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
