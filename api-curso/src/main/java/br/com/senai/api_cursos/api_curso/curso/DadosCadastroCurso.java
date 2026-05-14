package br.com.senai.api_cursos.api_curso.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCurso(

        @NotBlank
        String nome,

        @NotNull
        Curso.Periodo periodo

) {
}