package br.com.senai.api_cursos.api_curso.controller;

import br.com.senai.api_cursos.api_curso.curso.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrarCurso(@RequestBody @Valid DadosCadastroCurso dados) {
        repository.save(new Curso(dados));
    }

    @GetMapping
    public Page<DadosListagemCurso> listarCursos(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizarCurso(@PathVariable Long id, @RequestBody @Valid DadosAtualizarCurso dados) {
        var curso = repository.getReferenceById(id);
        curso.atualizarCurso(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirCurso(@PathVariable Long id) {
        var curso = repository.getReferenceById(id);
        curso.excluirCurso();
    }
    @GetMapping("/{id}")
    public DadosDetalhamentoCurso detalharCurso(@PathVariable Long id) {

        Curso curso = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não existe"
                ));

        return new DadosDetalhamentoCurso(curso);
    }
}