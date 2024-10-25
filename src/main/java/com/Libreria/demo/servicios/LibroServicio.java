package com.Libreria.demo.servicios;

import ErrorServicio.ErrorServicio;
import com.Libreria.demo.entidades.Autor;
import com.Libreria.demo.entidades.Editorial;
import com.Libreria.demo.entidades.Libro;
import com.Libreria.demo.repositorio.AutorRepositorio;
import com.Libreria.demo.repositorio.EditorialRepositorio;
import com.Libreria.demo.repositorio.LibroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static sun.net.www.http.HttpClient.New;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    //GUARDAR UN LIBRO:creación
    @Transactional(propagation = Propagation.NESTED)
    public Libro guardar(long ISBN, String titulo, Integer anio,Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {

        //creo una 
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idAutor).get();
        //VALIDACIONES   
        validar(titulo, 0, anio, ejemplares, idAutor, idEditorial);
        Libro libro = new Libro();

        //SETEO DE ATRIBUTOS    
        libro.setISBN(ISBN);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(true);

        //PERSISTENCIA DEL OBJETO
        return libroRepositorio.save(libro);

    }

    public List<Libro> listarLibros() {
        List<Libro> Libros = new ArrayList();
        Libros = libroRepositorio.findAll();
        return Libros;
    }

    //MODIFICAR DATOS
    @Transactional(propagation = Propagation.NESTED)
    public void modificar(String id, String titulo, long ISBN, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String idAutor, String idEditorial) throws ErrorServicio {

        validar(titulo, 0, anio, ejemplares, idAutor, idEditorial);
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setISBN(ISBN);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplaresRestantes);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se pueden modificar los datos");
        }

    }

    //DAR DE BAJA
    public void darDeBaja(String id) throws ErrorServicio {

        Optional<Libro> optional = libroRepositorio.findById(id);
        if (optional.isPresent()) {

            Libro libro = optional.get();
            libro.setAlta(false);
        } else {
            throw new ErrorServicio("No se pueden modificar los datos");
        }
    }

    //CONSULTA POR ID
    @Transactional(readOnly = true)
    public void buscarPorId(String id) {
        Optional<Libro> optional = libroRepositorio.findById(id);

        if (optional.isPresent()) {
            libroRepositorio.findById(id);
        }
    }

    //CONSULTA POR ISBN 
    @Transactional(readOnly = true)
    public Libro buscarPorISBN(Long ISBN) throws ErrorServicio {

        if (ISBN < 0) {
            throw new ErrorServicio("Debe indicar el ISBN correctamente");
        } else {
            Libro libro = libroRepositorio.buscarPorISBN(ISBN);
            return libro;
        }

    }

    //VALIDAR
    public void validar(String titulo, long ISBN, Integer anio,Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {

        if (ISBN < 0) {
            throw new ErrorServicio("Debe indicar el ISBN correctamente");
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("Debe indicar el título del libro");
        }
        if (ejemplares == null){
            throw new ErrorServicio ("Los ejemplares no pueden ser nulos");
        }
        if (anio == null) {
            throw new ErrorServicio("Debe indicar el año de emisión");
        }

        if (idAutor.isEmpty()|| idAutor == null) {
            throw new ErrorServicio("No se recibe el Id del autor");
        }
        if (idEditorial.isEmpty()|| idEditorial == null) {
            throw new ErrorServicio("No se recibe el Id de la editorial");
        }
    }
}
