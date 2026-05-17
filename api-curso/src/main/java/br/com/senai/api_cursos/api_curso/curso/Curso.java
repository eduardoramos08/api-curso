package br.com.senai.api_cursos.api_curso.curso;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "curso")
@Entity(name = "curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Periodo periodo;

    private Boolean ativo;



    public enum Periodo {
        MATUTINO,
        VESPERTINO,
        NOTURNO,
        INTEGRAL
    }

    public Curso(DadosCadastroCurso dados) {
        this.nome = dados.nome();
        this.periodo = dados.periodo();
        this.ativo = true;
    }
    public void atualizarCurso(@Valid DadosAtualizarCurso dados) {
        if (dados.nome() != null && !dados.nome().isBlank())
            this.nome = dados.nome();
        if (dados.periodo() != null) {
            this.periodo = dados.periodo();
        }
    }

    public void excluirCurso(){this.ativo = false;}

}



