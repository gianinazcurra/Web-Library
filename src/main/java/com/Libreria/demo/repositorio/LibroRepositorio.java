package com.Libreria.demo.repositorio;

import com.Libreria.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT c FROM Libro c WHERE c.titulo=:titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT c FROM Libro c WHERE c.ISBN=:ISBN")
    public Libro buscarPorISBN(@Param("ISBN") Long ISBN);

    @Query("SELECT c FROM Libro c WHERE c.id=:id")
    public Libro buscarPorId(@Param("id") String id);

    @Query("SELECT c FROM Libro c WHERE c.autor.nombre = :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String nombre);
}
