package br.edu.nassau.apicursos;

import org.springframework.stereotype.Service;

@Service
public class CursoService {

    public String obterMensagem() {
        return "Servico de cursos ativo! Mensagem produzida pelo CursoService e gerenciada pelo Spring.";
    }
}
