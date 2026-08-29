package com.API_de_Cursos.controller;


import com.API_de_Cursos.dto.CursoRequestDTO;
import com.API_de_Cursos.dto.CursoResponseDTO;
import com.API_de_Cursos.service.CursoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoResponseDTO> criar(@Valid @RequestBody CursoRequestDTO cursoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cursoService.cadastrar(cursoDTO));
    }


    @GetMapping
    public ResponseEntity<Page<CursoResponseDTO>> buscarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer cargaHorariaMinima,
            @RequestParam(required = false) Integer cargaHorariaMaxima,
            @RequestParam(required = false) Long professorId,
            Pageable pageable) {
        return ResponseEntity.ok(cursoService.buscarTodos(
                nome, descricao, categoria,
                cargaHorariaMinima, cargaHorariaMaxima, professorId, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(
            @Positive @PathVariable Long id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> atualizar(
            @Positive @PathVariable Long id,
            @Valid @RequestBody CursoRequestDTO cursoDTO) {
        return ResponseEntity.ok(cursoService.atualizar(id, cursoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Positive @PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
