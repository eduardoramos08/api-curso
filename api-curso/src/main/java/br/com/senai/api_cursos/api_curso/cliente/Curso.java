package br.com.senai.api_cursos.api_curso.cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cursos")
@Entity(name = "cursos")
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
}

