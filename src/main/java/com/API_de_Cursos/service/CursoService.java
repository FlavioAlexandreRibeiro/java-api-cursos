package com.API_de_Cursos.service;


import com.API_de_Cursos.dto.CursoRequestDTO;
import com.API_de_Cursos.dto.CursoResponseDTO;
import com.API_de_Cursos.entity.Curso;
import com.API_de_Cursos.entity.Professor;
import com.API_de_Cursos.exception.DuplicateResourceException;
import com.API_de_Cursos.exception.ResourceNotFoundException;
import com.API_de_Cursos.repository.CursoRepository;
import com.API_de_Cursos.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public CursoResponseDTO cadastrar(CursoRequestDTO cursoDTO) {
        Professor professor = professorRepository.findById(cursoDTO.getProfessorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", cursoDTO.getProfessorId().toString()));

        verificarNomeDuplicado(cursoDTO.getNome());

        Curso curso = new Curso();
        curso.setProfessor(professor);
        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        curso.setCategoria(cursoDTO.getCategoria());
        curso.setCargaHoraria(cursoDTO.getCargaHoraria());

        return CursoResponseDTO.fromEntity(cursoRepository.save(curso));
    }

    @Transactional
    public Page<CursoResponseDTO> buscarTodos(
            String nome,
            String descricao,
            String categoria,
            Integer cargaHoraria,
            Long professorId,
            Pageable pageable) {

        Specification<Curso> spec = ((root, query,
                                      criteriaBuilder) ->
                criteriaBuilder.conjunction());

        if (nome != null) {
            spec = spec.and((r, q, cb) ->
                    cb.like(
                            cb.lower(r.get("nome")),
                            "%" + nome.toLowerCase() + "%"));
        }

        if (descricao != null) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("descricao"), descricao));
        }

        if (categoria != null) {
            spec = spec.and((root, query,
                             cb) ->
                    cb.equal(root.get("categoria"), categoria));
        }

        if (cargaHoraria != null) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("cargaHoraria"), cargaHoraria));
        }

        if (professorId != null) {
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("professor"), professorId));
        }

        return cursoRepository.findAll(spec, pageable)
                .map(CursoResponseDTO::fromEntity);
    }


    @Transactional
    public CursoResponseDTO buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        return CursoResponseDTO.fromEntity(curso);
    }


    @Transactional
    public CursoResponseDTO atualizar(Long id, CursoRequestDTO cursoDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        verificarNomeDuplicadoAtualizar(cursoDTO.getNome(), id);

        if (cursoDTO.getProfessorId() != null) {
            Professor professor = professorRepository.findById(cursoDTO.getProfessorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", cursoDTO.getProfessorId().toString()));
            curso.setProfessor(professor);
        }

        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        curso.setCategoria(cursoDTO.getCategoria());
        curso.setCargaHoraria(cursoDTO.getCargaHoraria());

        return CursoResponseDTO.fromEntity(cursoRepository.save(curso));
    }


    private void verificarNomeDuplicado(String nome) {
        if (cursoRepository.existsByNome(nome)) {
            throw new DuplicateResourceException("Curso", "nome", nome);
        }
    }

    private void verificarNomeDuplicadoAtualizar(String nome, Long id) {
        if (cursoRepository.existsByNomeAndIdNot(nome, id)) {
            throw new DuplicateResourceException("Curso", "nome", nome);
        }
    }


    @Transactional
    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        cursoRepository.delete(curso);
    }
}
