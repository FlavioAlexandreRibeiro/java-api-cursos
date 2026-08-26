package com.API_de_Cursos.specification;

import com.API_de_Cursos.entity.Curso;
import org.springframework.data.jpa.domain.Specification;

public class CursoSpecification {

    public static Specification<Curso> porNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<Curso> porCategoria(String categoria) {
        return (root, query, cb) -> {
            if (categoria == null || categoria.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%");
        };
    }

    public static Specification<Curso> porDescricao(String descricao) {
        return (root, query, cb) -> {
            if (descricao == null || descricao.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
        };
    }

    public static Specification<Curso> porCargaHorariaMinima(Integer cargaHorariaMinima) {
        return (root, query, cb) -> {
            if (cargaHorariaMinima == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("cargaHoraria"), cargaHorariaMinima);
        };
    }

    public static Specification<Curso> porCargaHorariaMaxima(Integer cargaHorariaMaxima) {
        return (root, query, cb) -> {
            if (cargaHorariaMaxima == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("cargaHoraria"), cargaHorariaMaxima);
        };
    }

    public static Specification<Curso> porProfessorId(Long professorId) {
        return (root, query, cb) -> {
            if (professorId == null) return null;
            return cb.equal(root.get("professor").get("id"), professorId);
        };
    }
}
