package br.edu.nassau.apicursos;

import br.edu.nassau.apicursos.controller.CursoController;
import br.edu.nassau.apicursos.model.Curso;
import br.edu.nassau.apicursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiCursosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CursoController cursoController;

    @Autowired
    private CursoService cursoService;

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void testEndpointMensagemAula02() throws Exception {
        mockMvc.perform(get("/cursos/mensagem"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CursoService")));
    }

    @Test
    @Order(3)
    void testCadastrarCursoValido() throws Exception {
        Curso curso = new Curso("Back-End Frameworks", 60);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Back-End Frameworks"))
                .andExpect(jsonPath("$.cargaHoraria").value(60));
    }

    @Test
    @Order(4)
    void testRegraNomeObrigatorioNaoPodeSerVazio() throws Exception {
        Curso curso = new Curso("", 40);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(containsString("obrigatorio")));
    }

    @Test
    @Order(5)
    void testRegraNomeObrigatorioNaoPodeSerNulo() throws Exception {
        Curso curso = new Curso(null, 40);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(containsString("obrigatorio")));
    }

    @Test
    @Order(6)
    void testRegraCargaHorariaDeveSerMaiorQueZero() throws Exception {
        Curso curso = new Curso("Estrutura de Dados", 0);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(containsString("maior que zero")));
    }

    @Test
    @Order(7)
    void testListarCursos() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(8)
    void testBuscarCursoPorId() throws Exception {
        mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(9)
    void testBuscarCursoPorIdInexistente() throws Exception {
        mockMvc.perform(get("/cursos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void testAtualizarCurso() throws Exception {
        Curso cursoAtualizado = new Curso("Back-End Frameworks Avancado", 80);

        mockMvc.perform(put("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Back-End Frameworks Avancado"))
                .andExpect(jsonPath("$.cargaHoraria").value(80));
    }

    @Test
    @Order(11)
    void testRemoverCurso() throws Exception {
        mockMvc.perform(delete("/cursos/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isNotFound());
    }
}
