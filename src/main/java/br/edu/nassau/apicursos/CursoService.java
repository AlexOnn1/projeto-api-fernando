package br.edu.nassau.apicursos;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CursoService {

    // Armazenamento temporário em memória para a Aula 03
    private final List<Curso> cursos = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong sequenceId = new AtomicLong(1);

    public String obterMensagem() {
        return "Servico de cursos ativo! Mensagem produzida pelo CursoService e gerenciada pelo Spring.";
    }

    public Curso cadastrar(Curso curso) {
        curso.setId(sequenceId.getAndIncrement());
        cursos.add(curso);
        return curso;
    }

    public List<Curso> listar() {
        return new ArrayList<>(cursos);
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Optional<Curso> atualizar(Long id, Curso cursoAtualizado) {
        return buscarPorId(id).map(cursoExistente -> {
            cursoExistente.setNome(cursoAtualizado.getNome());
            cursoExistente.setCargaHoraria(cursoAtualizado.getCargaHoraria());
            return cursoExistente;
        });
    }

    public boolean remover(Long id) {
        return cursos.removeIf(c -> c.getId().equals(id));
    }
}
