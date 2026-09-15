package br.edu.nassau.apicursos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    // Injeção de dependência via construtor
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/mensagem")
    public String getMensagem() {
        return cursoService.obterMensagem();
    }

    // 1. Listar todos os cursos - GET /cursos (200 OK)
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    // 2. Buscar um curso pelo ID - GET /cursos/{id} (200 OK ou 404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastrar um curso - POST /cursos (201 Created)
    @PostMapping
    public ResponseEntity<Curso> cadastrar(@RequestBody Curso curso) {
        Curso novoCurso = cursoService.cadastrar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    // 4. Atualizar um curso - PUT /cursos/{id} (200 OK ou 404 Not Found)
    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso) {
        return cursoService.atualizar(id, curso)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Remover um curso - DELETE /cursos/{id} (204 No Content ou 404 Not Found)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean removido = cursoService.remover(id);
        if (removido) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
