package br.com.senai.api_cursos.api_curso.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository {
    Page<Curso> findAllByAtivoTrue(Pageable pageable);
    Page<Curso> findAllByAtivoTrue(Long id);
}
