package br.insper.curso.controller;

import br.insper.curso.dto.CriarCursoDTO;
import br.insper.curso.model.Curso;
import br.insper.curso.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public Curso criarCurso(@RequestBody CriarCursoDTO dto) {
        return cursoService.criarCurso(dto);
    }

    @GetMapping
    public List<Curso> listarCursos(@RequestParam(required = false) String nome) {
        return cursoService.listarCursos(nome);
    }

    @PostMapping("/{id}/alunos")
    public ResponseEntity<Void> adicionarAluno(@PathVariable Long id, @RequestParam String cpfAluno) {
        cursoService.adicionarAluno(id, cpfAluno); // Chamada sem retorno
        return ResponseEntity.ok().build(); // Retorna 200 OK
    }
}
