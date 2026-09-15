package br.edu.nassau.apicursos.repository;

import br.edu.nassau.apicursos.model.Curso;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CursoRepository {

    // Armazenamento dos dados na camada de Repository
    private final List<Curso> cursos = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong sequenceId = new AtomicLong(1);

    public Curso salvar(Curso curso) {
        if (curso.getId() == null) {
            curso.setId(sequenceId.getAndIncrement());
            cursos.add(curso);
            return curso;
        } else {
            // Atualização de registro existente
            for (int i = 0; i < cursos.size(); i++) {
                if (cursos.get(i).getId().equals(curso.getId())) {
                    cursos.set(i, curso);
                    return curso;
                }
            }
            cursos.add(curso);
            return curso;
        }
    }

    public List<Curso> listar() {
        return new ArrayList<>(cursos);
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public boolean remover(Long id) {
        return cursos.removeIf(c -> c.getId().equals(id));
    }
}
