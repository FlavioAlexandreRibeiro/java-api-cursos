package com.API_de_Cursos.controller;


import com.API_de_Cursos.dto.ProfessorRequestDTO;
import com.API_de_Cursos.dto.ProfessorResponseDTO;
import com.API_de_Cursos.service.ProfessorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criar(@Valid @RequestBody ProfessorRequestDTO professorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.cadastrar(professorDTO));
    }

    @GetMapping
    public ResponseEntity<Page<ProfessorResponseDTO>> buscarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String especialidade,
            Pageable pageable) {
        return ResponseEntity.ok(professorService.buscarTodos(nome, email, telefone, especialidade, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(
            @Positive @PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(
            @Positive @PathVariable Long id,
            @Valid @RequestBody ProfessorRequestDTO professorDTO) {
        return ResponseEntity.ok(professorService.atualizar(id, professorDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Positive @PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
