package com.example.clase6gtics.Controller;

import com.example.clase6gtics.entity.Estudiante;
import com.example.clase6gtics.repository.EstudianteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;

    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Obtiene una lista de todos los estudiantes registrados.
     * Los campos 'fechaRegistro' y 'ultimaActualizacion' son ignorados en la respuesta JSON
     * y el 'estado' se devuelve como String "Activo" o "Inactivo" gracias a la entidad.
     *
     * @return ResponseEntity con la lista de Estudiantes.
     */
    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodosEstudiantes() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }
}
