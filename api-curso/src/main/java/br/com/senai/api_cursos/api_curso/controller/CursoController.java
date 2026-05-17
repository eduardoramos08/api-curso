package br.com.senai.api_cursos.api_curso.controller;

import br.com.senai.api_cursos.api_curso.curso.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cursos")
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

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirCurso(@PathVariable Long id) {
        var curso = repository.getReferenceById(id);
        curso.excluirCurso();
    }
    @GetMapping("/{id}")
    public DadosDetalhamentoCurso detalharCurso(@PathVariable Long id) {
        Curso curso = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não existe"
                ));

        return new DadosDetalhamentoCurso(curso);
    }

    @GetMapping("/periodos")
    public ResponseEntity<List<Curso.Periodo>> listarPeriodos() {
        List<Curso.Periodo> periodos = Arrays.asList(Curso.Periodo.values());
        return ResponseEntity.ok(periodos);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCurso> atualizarCurso(
            @RequestBody @Valid DadosAtualizarCurso dados
    ) {

        var curso = repository.findByIdAndAtivoTrue(dados.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não encontrado"
                ));

        if (dados.nome() != null &&
                repository.existsByNome(dados.nome()) &&
                !curso.getNome().equals(dados.nome())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um curso com esse nome"
            );
        }

        curso.atualizarCurso(dados);

        return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
    }
}