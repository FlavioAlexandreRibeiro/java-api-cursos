package com.API_de_Cursos.controller;

import com.API_de_Cursos.dto.CursoRequestDTO;
import com.API_de_Cursos.dto.CursoResponseDTO;
import com.API_de_Cursos.service.CursoService;
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

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CursoService cursoService;

    private CursoRequestDTO cursoRequestDTO;
    private CursoResponseDTO cursoResponseDTO;

    @BeforeEach
    void setUp() {
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
    void criar_DeveRetornarCreated_QuandoDadosValidos() throws Exception {
        when(cursoService.cadastrar(any(CursoRequestDTO.class))).thenReturn(cursoResponseDTO);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Java Spring Boot"))
                .andExpect(jsonPath("$.categoria").value("Programação"))
                .andExpect(jsonPath("$.cargaHoraria").value(40));

        verify(cursoService, times(1)).cadastrar(any(CursoRequestDTO.class));
    }

    @Test
    void buscarTodos_DeveRetornarOk_QuandoChamado() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CursoResponseDTO> cursos = Arrays.asList(cursoResponseDTO);
        Page<CursoResponseDTO> page = new PageImpl<>(cursos, pageable, cursos.size());

        when(cursoService.buscarTodos(
                any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/cursos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Java Spring Boot"));

        verify(cursoService, times(1)).buscarTodos(
                any(), any(), any(), any(), any(), any(), any(Pageable.class)
        );
    }

    @Test
    void buscarTodos_DeveRetornarOk_QuandoComFiltros() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CursoResponseDTO> cursos = Arrays.asList(cursoResponseDTO);
        Page<CursoResponseDTO> page = new PageImpl<>(cursos, pageable, cursos.size());

        when(cursoService.buscarTodos(
                eq("Java"), any(), eq("Programação"), any(), any(), anyLong(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/cursos")
                        .param("nome", "Java")
                        .param("categoria", "Programação")
                        .param("professorId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(cursoService, times(1)).buscarTodos(
                eq("Java"), any(), eq("Programação"), any(), any(), anyLong(), any(Pageable.class)
        );
    }

    @Test
    void buscarPorId_DeveRetornarOk_QuandoIdExistente() throws Exception {
        when(cursoService.buscarPorId(anyLong())).thenReturn(cursoResponseDTO);

        mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Java Spring Boot"))
                .andExpect(jsonPath("$.categoria").value("Programação"));

        verify(cursoService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void atualizar_DeveRetornarOk_QuandoDadosValidos() throws Exception {
        CursoRequestDTO updatedDTO = CursoRequestDTO.builder()
                .nome("Java Spring Boot Avançado")
                .descricao("Curso avançado de desenvolvimento com Java e Spring Boot")
                .categoria("Programação")
                .cargaHoraria(60)
                .professorId(1L)
                .build();

        CursoResponseDTO updatedResponse = new CursoResponseDTO(
                1L,
                "Java Spring Boot Avançado",
                "Curso avançado de desenvolvimento com Java e Spring Boot",
                "Programação",
                60,
                1L
        );

        when(cursoService.atualizar(anyLong(), any(CursoRequestDTO.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Java Spring Boot Avançado"))
                .andExpect(jsonPath("$.cargaHoraria").value(60));

        verify(cursoService, times(1)).atualizar(anyLong(), any(CursoRequestDTO.class));
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoIdExistente() throws Exception {
        doNothing().when(cursoService).deletar(anyLong());

        mockMvc.perform(delete("/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService, times(1)).deletar(anyLong());
    }
}
