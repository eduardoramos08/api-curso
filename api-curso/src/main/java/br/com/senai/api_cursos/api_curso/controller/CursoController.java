package br.com.senai.api_cursos.api_curso.controller;
import br.com.senai.api_cursos.api_curso.cliente.DadosCadastroCurso;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController//uni @controller e @responseBody faz com que a api retorne json ou xml
    @RequestMapping("/curso")//url de base

    public class CursoController {
        @Autowired
        private br.com.senai.api_cursos.api_curso.cliente.CursoRepository cursoRepository;

        @PostMapping
        @Transactional
        public void cadastrarCurso(@RequestBody @Valid DadosCadastroCurso dadosCadastro) {
            repository.save(new Curso(dados));
        }
    }

