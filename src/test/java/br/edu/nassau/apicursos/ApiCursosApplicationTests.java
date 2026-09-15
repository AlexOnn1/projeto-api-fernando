package br.edu.nassau.apicursos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiCursosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoController cursoController;

    @Autowired
    private CursoService cursoService;

    @Test
    void contextLoads() {
    }

    @Test
    void testEndpointMensagemAula02() throws Exception {
        mockMvc.perform(get("/cursos/mensagem"))
                .andExpect(status().isOk())
                .andExpect(content().string(cursoService.obterMensagem()));
    }
}
