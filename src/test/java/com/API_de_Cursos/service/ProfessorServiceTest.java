package com.API_de_Cursos.service;

import com.API_de_Cursos.dto.ProfessorRequestDTO;
import com.API_de_Cursos.dto.ProfessorResponseDTO;
import com.API_de_Cursos.entity.Professor;
import com.API_de_Cursos.exception.DuplicateResourceException;
import com.API_de_Cursos.exception.ResourceNotFoundException;
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
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private Professor professor;
    private ProfessorRequestDTO professorRequestDTO;
    private ProfessorResponseDTO professorResponseDTO;

    @BeforeEach
    void setUp() {
        professor = Professor.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .especialidade("Java")
                .build();

        professorRequestDTO = ProfessorRequestDTO.builder()
                .nome("João Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .especialidade("Java")
                .build();

        professorResponseDTO = new ProfessorResponseDTO(
                1L,
                "João Silva",
                "joao.silva@email.com",
                "11999999999",
                "Java"
        );
    }

    @Test
    void cadastrar_DeveRetornarProfessorResponseDTO_QuandoDadosValidos() {
        when(professorRepository.existsByNome(anyString())).thenReturn(false);
        when(professorRepository.existsByEmail(anyString())).thenReturn(false);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        ProfessorResponseDTO result = professorService.cadastrar(professorRequestDTO);

        assertNotNull(result);
        assertEquals(professorResponseDTO.nome(), result.nome());
        assertEquals(professorResponseDTO.email(), result.email());
        verify(professorRepository, times(1)).existsByNome(anyString());
        verify(professorRepository, times(1)).existsByEmail(anyString());
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void cadastrar_DeveLancarDuplicateResourceException_QuandoNomeDuplicado() {
        when(professorRepository.existsByNome(anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> professorService.cadastrar(professorRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals("nome", exception.getFieldName());
        verify(professorRepository, times(1)).existsByNome(anyString());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void cadastrar_DeveLancarDuplicateResourceException_QuandoEmailDuplicado() {
        when(professorRepository.existsByNome(anyString())).thenReturn(false);
        when(professorRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> professorService.cadastrar(professorRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals("email", exception.getFieldName());
        verify(professorRepository, times(1)).existsByNome(anyString());
        verify(professorRepository, times(1)).existsByEmail(anyString());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void buscarTodos_DeveRetornarPaginaDeProfessores_QuandoChamado() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Professor> professores = Arrays.asList(professor);
        Page<Professor> page = new PageImpl<>(professores, pageable, professores.size());

        when(professorRepository.findAll((org.springframework.data.jpa.domain.Specification<Professor>) any(), any(Pageable.class))).thenReturn(page);

        Page<ProfessorResponseDTO> result = professorService.buscarTodos(null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(professorResponseDTO.nome(), result.getContent().get(0).nome());
        verify(professorRepository, times(1)).findAll((org.springframework.data.jpa.domain.Specification<Professor>) any(), any(Pageable.class));
    }

    @Test
    void buscarTodos_DeveRetornarPaginaVazia_QuandoNaoHaProfessores() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Professor> emptyPage = Page.empty(pageable);

        when(professorRepository.findAll((org.springframework.data.jpa.domain.Specification<Professor>) any(), any(Pageable.class))).thenReturn(emptyPage);

        Page<ProfessorResponseDTO> result = professorService.buscarTodos(null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(professorRepository, times(1)).findAll((org.springframework.data.jpa.domain.Specification<Professor>) any(), any(Pageable.class));
    }

    @Test
    void buscarPorId_DeveRetornarProfessorResponseDTO_QuandoIdExistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));

        ProfessorResponseDTO result = professorService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(professorResponseDTO.nome(), result.nome());
        assertEquals(professorResponseDTO.email(), result.email());
        verify(professorRepository, times(1)).findById(anyLong());
    }

    @Test
    void buscarPorId_DeveLancarResourceNotFoundException_QuandoIdInexistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> professorService.buscarPorId(1L)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(professorRepository, times(1)).findById(anyLong());
    }

    @Test
    void atualizar_DeveRetornarProfessorResponseDTO_QuandoDadosValidos() {
        ProfessorRequestDTO updatedDTO = ProfessorRequestDTO.builder()
                .nome("João Silva Atualizado")
                .email("joao.atualizado@email.com")
                .telefone("11988888888")
                .especialidade("Java e Spring")
                .build();

        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(professorRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(professorRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        ProfessorResponseDTO result = professorService.atualizar(1L, updatedDTO);

        assertNotNull(result);
        verify(professorRepository, times(1)).findById(anyLong());
        verify(professorRepository, times(1)).existsByNomeAndIdNot(anyString(), anyLong());
        verify(professorRepository, times(1)).existsByEmailAndIdNot(anyString(), anyLong());
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    @Test
    void atualizar_DeveLancarResourceNotFoundException_QuandoIdInexistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> professorService.atualizar(1L, professorRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void atualizar_DeveLancarDuplicateResourceException_QuandoNomeDuplicado() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(professorRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> professorService.atualizar(1L, professorRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals("nome", exception.getFieldName());
        verify(professorRepository, times(1)).existsByNomeAndIdNot(anyString(), anyLong());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void atualizar_DeveLancarDuplicateResourceException_QuandoEmailDuplicado() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        when(professorRepository.existsByNomeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(professorRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> professorService.atualizar(1L, professorRequestDTO)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals("email", exception.getFieldName());
        verify(professorRepository, times(1)).existsByEmailAndIdNot(anyString(), anyLong());
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deletar_DeveDeletarProfessor_QuandoIdExistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
        doNothing().when(professorRepository).delete(any(Professor.class));

        professorService.deletar(1L);

        verify(professorRepository, times(1)).findById(anyLong());
        verify(professorRepository, times(1)).delete(any(Professor.class));
    }

    @Test
    void deletar_DeveLancarResourceNotFoundException_QuandoIdInexistente() {
        when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> professorService.deletar(1L)
        );

        assertEquals("Professor", exception.getResourceName());
        assertEquals(1L, exception.getId());
        verify(professorRepository, times(1)).findById(anyLong());
        verify(professorRepository, never()).delete(any(Professor.class));
    }
}
