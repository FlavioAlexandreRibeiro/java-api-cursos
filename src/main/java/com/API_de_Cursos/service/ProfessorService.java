package com.API_de_Cursos.service;

import com.API_de_Cursos.dto.ProfessorRequestDTO;
import com.API_de_Cursos.dto.ProfessorResponseDTO;
import com.API_de_Cursos.entity.Professor;
import com.API_de_Cursos.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Transactional
    public ProfessorResponseDTO cadastrar(ProfessorRequestDTO professorDTO) {
        verificarNomeDuplicado(professorDTO.getNome());
        verificarEmailDuplicado(professorDTO.getEmail());

        Professor professor = new Professor();
        professor.setNome(professorDTO.getNome());
        professor.setEmail(professorDTO.getEmail());
        professor.setTelefone(professorDTO.getTelefone());
        professor.setEspecialidade(professorDTO.getEspecialidade());

        return ProfessorResponseDTO.fromEntity(professorRepository.save(professor));
    }


    @Transactional
    public Page<ProfessorResponseDTO> buscarTodos(
            String nome,
            String email,
            String telefone,
            String especialidade,
            Pageable pageable) {

        Specification<Professor> spec = ((root, query,
                                          criteriaBuilder) ->
                criteriaBuilder.conjunction());

        if (nome != null && !nome.isBlank()) {
            spec = spec.and((r, q, cb) ->
                    cb.like(
                            cb.lower(r.get("nome")),
                            "%" + nome.toLowerCase() + "%"
                    ));
        }

        if (email != null && !email.isBlank()) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("email"), email));
        }

        if (telefone != null && !telefone.isBlank()) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("telefone"), telefone));
        }

        if (especialidade != null && !especialidade.isBlank()) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("especialidade"), especialidade));
        }

        return professorRepository.findAll(spec, pageable)
                .map(ProfessorResponseDTO::fromEntity);
    }


    @Transactional
    public ProfessorResponseDTO atualizar(Long id, ProfessorRequestDTO professorDTO) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));

        verificarNomeDuplicadoAtualizar(professorDTO.getNome(), id);
        verificarEmailDuplicadoAtualizar(professorDTO.getEmail(), id);

        professor.setNome(professorDTO.getNome());
        professor.setEmail(professorDTO.getEmail());
        professor.setTelefone(professorDTO.getTelefone());
        professor.setEspecialidade(professorDTO.getEspecialidade());

        return ProfessorResponseDTO.fromEntity(professor);
    }


    @Transactional(readOnly = true)
    public ProfessorResponseDTO buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));
        return ProfessorResponseDTO.fromEntity(professor);
    }


    private void verificarNomeDuplicado(String nome) {
        if (professorRepository.existsByNome(nome)) {
            throw new RuntimeException("Nome já cadastrado");
        }
    }


    private void verificarEmailDuplicado(String email) {
        if (professorRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já cadastrado");
        }
    }


    private void verificarNomeDuplicadoAtualizar(String nome, Long id) {
        if (professorRepository.existsByNomeAndIdNot(nome, id)) {
            throw new RuntimeException("Nome já cadastrado");
        }
    }


    private void verificarEmailDuplicadoAtualizar(String email, Long id) {
        if (professorRepository.existsByEmailAndIdNot(email, id)) {
            throw new RuntimeException("Email já cadastrado");
        }
    }


    @Transactional
    public void deletar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));
        professorRepository.delete(professor);
    }
}
