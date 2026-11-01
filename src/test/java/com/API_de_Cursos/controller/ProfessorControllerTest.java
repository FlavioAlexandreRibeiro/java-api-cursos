package com.API_de_Cursos.controller;

import com.API_de_Cursos.dto.ProfessorRequestDTO;
import com.API_de_Cursos.dto.ProfessorResponseDTO;
import com.API_de_Cursos.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfessorService professorService;

    private ProfessorRequestDTO professorRequestDTO;
    private ProfessorResponseDTO professorResponseDTO;

    @BeforeEach
    void setUp() {
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
    void criar_DeveRetornarCreated_QuandoDadosValidos() throws Exception {
        when(professorService.cadastrar(any(ProfessorRequestDTO.class))).thenReturn(professorResponseDTO);

        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));

        verify(professorService, times(1)).cadastrar(any(ProfessorRequestDTO.class));
    }

    @Test
    void buscarTodos_DeveRetornarOk_QuandoChamado() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProfessorResponseDTO> professores = Arrays.asList(professorResponseDTO);
        Page<ProfessorResponseDTO> page = new PageImpl<>(professores, pageable, professores.size());

        when(professorService.buscarTodos(any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/professores")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("João Silva"));

        verify(professorService, times(1)).buscarTodos(any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void buscarTodos_DeveRetornarOk_QuandoComFiltros() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProfessorResponseDTO> professores = Arrays.asList(professorResponseDTO);
        Page<ProfessorResponseDTO> page = new PageImpl<>(professores, pageable, professores.size());

        when(professorService.buscarTodos(
                eq("João"), eq("joao.silva@email.com"), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/professores")
                        .param("nome", "João")
                        .param("email", "joao.silva@email.com")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(professorService, times(1)).buscarTodos(
                eq("João"), eq("joao.silva@email.com"), any(), any(), any(Pageable.class)
        );
    }

    @Test
    void buscarPorId_DeveRetornarOk_QuandoIdExistente() throws Exception {
        when(professorService.buscarPorId(anyLong())).thenReturn(professorResponseDTO);

        mockMvc.perform(get("/professores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));

        verify(professorService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void atualizar_DeveRetornarOk_QuandoDadosValidos() throws Exception {
        ProfessorRequestDTO updatedDTO = ProfessorRequestDTO.builder()
                .nome("João Silva Atualizado")
                .email("joao.atualizado@email.com")
                .telefone("11988888888")
                .especialidade("Java e Spring")
                .build();

        ProfessorResponseDTO updatedResponse = new ProfessorResponseDTO(
                1L,
                "João Silva Atualizado",
                "joao.atualizado@email.com",
                "11988888888",
                "Java e Spring"
        );

        when(professorService.atualizar(anyLong(), any(ProfessorRequestDTO.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/professores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@email.com"));

        verify(professorService, times(1)).atualizar(anyLong(), any(ProfessorRequestDTO.class));
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoIdExistente() throws Exception {
        doNothing().when(professorService).deletar(anyLong());

        mockMvc.perform(delete("/professores/1"))
                .andExpect(status().isNoContent());

        verify(professorService, times(1)).deletar(anyLong());
    }
}
