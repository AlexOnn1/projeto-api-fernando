package br.edu.nassau.apicursos;

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
    void testListarCursosInicialmenteVazio() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(4)
    void testCadastrarCursoSucesso() throws Exception {
        Curso curso = new Curso("Back-End Frameworks", 60);

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Back-End Frameworks"))
                .andExpect(jsonPath("$.cargaHoraria").value(60));
    }

    @Test
    @Order(5)
    void testBuscarCursoPorIdExistente() throws Exception {
        mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Back-End Frameworks"))
                .andExpect(jsonPath("$.cargaHoraria").value(60));
    }

    @Test
    @Order(6)
    void testBuscarCursoPorIdInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/cursos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void testAtualizarCursoExistente() throws Exception {
        Curso cursoAtualizado = new Curso("Back-End Frameworks Avancado", 80);

        mockMvc.perform(put("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Back-End Frameworks Avancado"))
                .andExpect(jsonPath("$.cargaHoraria").value(80));
    }

    @Test
    @Order(8)
    void testAtualizarCursoInexistenteRetorna404() throws Exception {
        Curso curso = new Curso("Inexistente", 20);

        mockMvc.perform(put("/cursos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void testRemoverCursoExistenteRetorna204() throws Exception {
        mockMvc.perform(delete("/cursos/1"))
                .andExpect(status().isNoContent());

        // Confirma que foi removido
        mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void testRemoverCursoInexistenteRetorna404() throws Exception {
        mockMvc.perform(delete("/cursos/999"))
                .andExpect(status().isNotFound());
    }
}
