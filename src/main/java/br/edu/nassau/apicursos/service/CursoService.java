package br.edu.nassau.apicursos.service;

import br.edu.nassau.apicursos.exception.RegraNegocioException;
import br.edu.nassau.apicursos.model.Curso;
import br.edu.nassau.apicursos.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    // Injeção de dependência do Spring Data JPA Repository pelo construtor
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public String obterMensagem() {
        return "Servico de cursos ativo! Mensagem produzida pelo CursoService e gerenciada pelo Spring.";
    }

    // Operação 1: Listar todos os cursos via findAll()
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    // Operação 2: Buscar um curso pelo ID via findById()
    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    // Operação 3: Cadastrar um novo curso via save() com ID gerado pelo banco
    public Curso cadastrar(Curso curso) {
        validarRegrasCurso(curso);
        // Garante que o identificador seja gerado automaticamente pelo banco (IDENTITY)
        curso.setId(null);
        return cursoRepository.save(curso);
    }

    // Operação 4: Atualizar um curso existente via save()
    public Optional<Curso> atualizar(Long id, Curso cursoAtualizado) {
        validarRegrasCurso(cursoAtualizado);

        return cursoRepository.findById(id).map(cursoExistente -> {
            cursoExistente.setNome(cursoAtualizado.getNome());
            cursoExistente.setCargaHoraria(cursoAtualizado.getCargaHoraria());
            return cursoRepository.save(cursoExistente);
        });
    }

    // Operação 5: Remover um curso via deleteById()
    public boolean remover(Long id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validação estrita das regras de negócio exigidas na atividade
    private void validarRegrasCurso(Curso curso) {
        if (curso == null) {
            throw new RegraNegocioException("Os dados do curso sao obrigatorios.");
        }
        if (curso.getNome() == null || curso.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do curso e obrigatorio e nao pode ficar em branco.");
        }
        if (curso.getCargaHoraria() == null || curso.getCargaHoraria() <= 0) {
            throw new RegraNegocioException("A carga horaria deve ser maior que zero.");
        }
    }
}
