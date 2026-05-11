package br.com.senai.api_cursos.api_curso.cliente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroCurso(

        @NotNull
        @Size(min = 3, max = 100)
        String nome,

        @NotNull
        Curso.Periodo periodo

) {
}