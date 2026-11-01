package com.API_de_Cursos.service;

import com.API_de_Cursos.dto.CursoRequestDTO;
import com.API_de_Cursos.dto.CursoResponseDTO;
import com.API_de_Cursos.entity.Curso;
import com.API_de_Cursos.entity.Professor;
import com.API_de_Cursos.exception.DuplicateResourceException;
import com.API_de_Cursos.exception.ResourceNotFoundException;
import com.API_de_Cursos.repository.CursoRepository;
import com.API_de_Cursos.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Professor professor;
    private CursoRequestDTO cursoRequestDTO;
    private CursoResponseDTO cursoResponseDTO;

    @BeforeEach
    void setUp() {
        professor = Professor.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .especialidade("Java")
                .build();

        curso = Curso.builder()
                .id(1L)
                .nome("Java Spring Boot")
                .descricao("Curso completo de desenvolvimento com Java e Spring Boot")
                .categoria("Programação")
                .cargaHoraria(40)
                .professor(professor)
                .build();

        cursoRequestDTO = CursoRequestDTO.builder()
                .nome("Java Spring Boot")
                .descricao("Curso completo de desenvolvimento com Java e Spring Boot")
                .categoria("Programação")
                .cargaHoraria(40)
                .professorId(1L)
                .build();

        cursoResponseDTO = new CursoResponseDTO(
                1L,
                "Java Spring Boot",
                "Curso completo de desenvolvimento com Java e Spring Boot",
                "Programação",
                40,
                1L
        );
    }

    @Test
    void cadastrar_DeveRetornarCursoResponseDTO_QuandoDadosValidos() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(cursoRepository.existsByNome(anyString())).thenReturn(false);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoResponseDTO result = cursoService.cadastrar(cursoRequestDTO);

        assertNotNull(result);
        assertEquals(cursoResponseDTO.nome(), result.nome());
        assertEquals(cursoResponseDTO.categoria(), result.categoria());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).existsByNome(anyString());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void cadastrar_DeveLancarResourceNotFoundException_QuandoProfessorInexistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cursoService.cadastrar(cursoRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void cadastrar_DeveLancarDuplicateResourceException_QuandoNomeDuplicado() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(cursoRepository.existsByNome(anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> cursoService.cadastrar(cursoRequestDTO)
        );

        assertEquals("Curso", exception.getResourceName());
        assertEquals("nome", exception.getFieldName());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).existsByNome(anyString());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void buscarTodos_DeveRetornarPaginaDeCursos_QuandoChamado() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Curso> cursos = Arrays.asList(curso);
        Page<Curso> page = new PageImpl<>(cursos, pageable, cursos.size());

        when(cursoRepository.findAll((org.springframework.data.jpa.domain.Specification<Curso>) any(), any(Pageable.class))).thenReturn(page);

        Page<CursoResponseDTO> result = cursoService.buscarTodos(
                null, null, null, null, null, null, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(cursoResponseDTO.nome(), result.getContent().get(0).nome());
        verify(cursoRepository, times(1)).findAll((org.springframework.data.jpa.domain.Specification<Curso>) any(), any(Pageable.class));
    }

    @Test
    void buscarTodos_DeveRetornarPaginaVazia_QuandoNaoHaCursos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Curso> emptyPage = Page.empty(pageable);

        when(cursoRepository.findAll((org.springframework.data.jpa.domain.Specification<Curso>) any(), any(Pageable.class))).thenReturn(emptyPage);

        Page<CursoResponseDTO> result = cursoService.buscarTodos(
                null, null, null, null, null, null, pageable
        );

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(cursoRepository, times(1)).findAll((org.springframework.data.jpa.domain.Specification<Curso>) any(), any(Pageable.class));
    }

    @Test
    void buscarPorId_DeveRetornarCursoResponseDTO_QuandoIdExistente() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));

        CursoResponseDTO result = cursoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(cursoResponseDTO.nome(), result.nome());
        assertEquals(cursoResponseDTO.categoria(), result.categoria());
        verify(cursoRepository, times(1)).findById(anyLong());
    }

    @Test
    void buscarPorId_DeveLancarResourceNotFoundException_QuandoIdInexistente() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cursoService.buscarPorId(1L)
        );

        assertEquals("Curso", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(cursoRepository, times(1)).findById(anyLong());
    }

    @Test
    void atualizar_DeveRetornarCursoResponseDTO_QuandoDadosValidos() {
        CursoRequestDTO updatedDTO = CursoRequestDTO.builder()
                .nome("Java Spring Boot Avançado")
                .descricao("Curso avançado de desenvolvimento com Java e Spring Boot")
                .categoria("Programação")
                .cargaHoraria(60)
                .professorId(1L)
                .build();

        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoResponseDTO result = cursoService.atualizar(1L, updatedDTO);

        assertNotNull(result);
        verify(cursoRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).existsByNomeAndIdNot(anyString(), anyLong());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void atualizar_DeveLancarResourceNotFoundException_QuandoCursoInexistente() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cursoService.atualizar(1L, cursoRequestDTO)
        );

        assertEquals("Curso", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(cursoRepository, times(1)).findById(anyLong());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void atualizar_DeveLancarResourceNotFoundException_QuandoProfessorInexistente() {
        CursoRequestDTO dtoWithNewProfessor = CursoRequestDTO.builder()
                .nome("Java Spring Boot")
                .descricao("Curso completo de desenvolvimento com Java e Spring Boot")
                .categoria("Programação")
                .cargaHoraria(40)
                .professorId(999L)
                .build();

        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cursoService.atualizar(1L, dtoWithNewProfessor)
        );

        assertEquals("Professor", exception.getResourceName());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void atualizar_DeveLancarDuplicateResourceException_QuandoNomeDuplicado() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> cursoService.atualizar(1L, cursoRequestDTO)
        );

        assertEquals("Curso", exception.getResourceName());
        assertEquals("nome", exception.getFieldName());
        verify(cursoRepository, times(1)).existsByNomeAndIdNot(anyString(), anyLong());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void deletar_DeveDeletarCurso_QuandoIdExistente() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        doNothing().when(cursoRepository).delete(any(Curso.class));

        cursoService.deletar(1L);

        verify(cursoRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).delete(any(Curso.class));
    }

    @Test
    void deletar_DeveLancarResourceNotFoundException_QuandoIdInexistente() {
        when(cursoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cursoService.deletar(1L)
        );

        assertEquals("Curso", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(cursoRepository, times(1)).findById(anyLong());
        verify(cursoRepository, never()).delete(any(Curso.class));
    }
}
