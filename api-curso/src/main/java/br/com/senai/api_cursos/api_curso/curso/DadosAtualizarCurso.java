package br.com.senai.api_cursos.api_curso.curso;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosAtualizarCurso(
        Long id,

        @Size(min = 3, max = 100)
        String nome


) {
}