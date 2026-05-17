package br.com.senai.api_cursos.api_curso.controller;

import br.com.senai.api_cursos.api_curso.curso.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "Gerenciamento de cursos")

public class CursoController {
    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar um novo curso", description = "Salva um curso no Banco de Dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DadosDetalhamentoCurso.class)
                    )

            }),
            @ApiResponse(responseCode = "409", description = "Nome duplicado",
            content = {
                    @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Já existe um curso com esse nome")
                    )
            })
    })

    public ResponseEntity<?> cadastrarCurso(
            @RequestBody @Valid DadosCadastroCurso dados
    ) {
        // verifica nome duplicado
        if (repository.existsByNome(dados.nome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um curso com esse nome");
        }
        var curso = repository.save(new Curso(dados));

        return ResponseEntity
                .created(URI.create("/cursos/" + curso.getId()))
                .body(new DadosDetalhamentoCurso(curso));
    }


    @GetMapping
    @Operation(summary = "Listar cursos", description = "Lista todos os cursos que estão cadastrados no Banco de Dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cursos",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DadosListagemCurso.class)
                            )
                    }),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = {@Content})
    })
    public ResponseEntity<Page<DadosListagemCurso>> listarCursos(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao
    ) {
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso pelo ID", description = "Busca os detalhes do curso especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class)
                            )
                    }),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = {@Content})
    })
    public ResponseEntity<DadosDetalhamentoCurso> detalharCurso(
            @PathVariable Long id
    ) {
        var curso = repository.findByIdAndAtivoTrue(id);
        if (curso.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                new DadosDetalhamentoCurso(curso.get())
        );
    }


    @GetMapping("/periodos")
    @Operation(summary = "Listar periodos", description = "Lista todos os periodos disponiveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de periodos",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    })
    })
    public ResponseEntity<List<Curso.Periodo>> listarPeriodos() {

        List<Curso.Periodo> periodos =
                Arrays.asList(Curso.Periodo.values());

        return ResponseEntity.ok(periodos);
    }


    @PutMapping
    @Operation(summary = "Atualizar curso", description = "Atualiza curso especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DadosDetalhamentoCurso.class)
                            )
                    }),
            @ApiResponse(responseCode = "409", description = "Nome duplicado",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(value = "Já existe um curso com esse nome")
                            )
                    })
    })
    @Transactional
    public ResponseEntity<?> atualizarCurso(
            @RequestBody @Valid DadosAtualizarCurso dados
    ) {

        var optionalCurso = repository.findByIdAndAtivoTrue(dados.id());

        // curso não encontrado
        if (optionalCurso.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var curso = optionalCurso.get();

        // nome duplicado
        if (dados.nome() != null
                && repository.existsByNome(dados.nome())
                && !curso.getNome().equals(dados.nome())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um curso com esse nome");
        }

        curso.atualizarCurso(dados);

        return ResponseEntity.ok(
                new DadosDetalhamentoCurso(curso)
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir curso", description = "Excluir curso especificado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso excluido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @Transactional
    public ResponseEntity<Void> excluirCurso(
            @PathVariable Long id
    ) {
        var optionalCurso = repository.findByIdAndAtivoTrue(id);

        // curso não encontrado
        if (optionalCurso.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var curso = optionalCurso.get();
        curso.excluirCurso();

        return ResponseEntity.noContent().build();
    }
}