package br.com.senai.api_cursos.api_curso.curso;

import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Page<Curso> findAllByAtivoTrue(Pageable pageable);


    Optional<Curso> findByIdAndAtivoTrue(Long id);

    boolean existsByNome(String nome);
}