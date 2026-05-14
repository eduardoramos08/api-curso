package br.com.senai.api_cursos.api_curso.curso;

public record DadosListagemCurso(
        long id,
        String nome,
        Curso.Periodo periodo
) {
    public DadosListagemCurso(Curso curso){this(curso.getId(), curso.getNome(), curso.getPeriodo());}
}
