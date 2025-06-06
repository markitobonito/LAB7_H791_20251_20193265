package com.example.clase6gtics.Controller;

import com.example.clase6gtics.entity.Estudiante;
import com.example.clase6gtics.repository.EstudianteRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;

    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }


    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodosEstudiantes() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }
    @GetMapping("/{dni}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorDni(@PathVariable String dni) {
        Optional<Estudiante> optionalEstudiante = estudianteRepository.findByDni(dni);
        if (optionalEstudiante.isPresent()) {
            return new ResponseEntity<>(optionalEstudiante.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante con DNI " + dni + " no encontrado.");
        }
    }
    @PostMapping
    public ResponseEntity<Estudiante> registrarEstudiante(@Valid @RequestBody Estudiante estudiante) {
        // Validación de campos únicos antes de guardar para dar un mensaje más específico
        if (estudianteRepository.existsByDni(estudiante.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un estudiante con el DNI " + estudiante.getDni());
        }
        if (estudianteRepository.existsByCodigoPucp(estudiante.getCodigoPucp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un estudiante con el Código PUCP " + estudiante.getCodigoPucp());
        }
        if (estudianteRepository.existsByCorreoInstitucional(estudiante.getCorreoInstitucional())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un estudiante con el Correo Institucional " + estudiante.getCorreoInstitucional());
        }
        if (estudiante.getCorreoPersonal() != null && estudianteRepository.existsByCorreoPersonal(estudiante.getCorreoPersonal())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un estudiante con el Correo Personal " + estudiante.getCorreoPersonal());
        }

        // El estado se establece como 'Activo' y fechaRegistro se genera en @PrePersist
        // ultimaActualizacion se asigna a null por defecto y se gestionará en @PreUpdate
        estudiante.setEstado(true); // Asegurarse de que el estado sea activo por defecto en la creación
        estudiante.setUltimaActualizacion(null); // Asegurarse de que sea null al crear

        try {
            Estudiante nuevoEstudiante = estudianteRepository.save(estudiante);
            return new ResponseEntity<>(nuevoEstudiante, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // Este catch es para otras violaciones de integridad que puedan surgir (ej. de DB)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error al guardar el estudiante debido a una violación de integridad de datos.", e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable Long id, @Valid @RequestBody Estudiante estudianteDetails) {
        Optional<Estudiante> optionalEstudiante = estudianteRepository.findById(id);
        if (optionalEstudiante.isPresent()) {
            Estudiante estudianteExistente = optionalEstudiante.get();

            if (estudianteDetails.getNombres() != null) estudianteExistente.setNombres(estudianteDetails.getNombres());
            if (estudianteDetails.getApellidos() != null) estudianteExistente.setApellidos(estudianteDetails.getApellidos());
            if (estudianteDetails.getFechaNacimiento() != null) estudianteExistente.setFechaNacimiento(estudianteDetails.getFechaNacimiento());
            if (estudianteDetails.getSexo() != null) estudianteExistente.setSexo(estudianteDetails.getSexo());
            if (estudianteDetails.getCorreoPersonal() != null) {
                if (estudianteDetails.getCorreoPersonal() != null && !estudianteDetails.getCorreoPersonal().equals(estudianteExistente.getCorreoPersonal()) && estudianteRepository.existsByCorreoPersonal(estudianteDetails.getCorreoPersonal())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo personal " + estudianteDetails.getCorreoPersonal() + " ya está en uso.");
                }
                estudianteExistente.setCorreoPersonal(estudianteDetails.getCorreoPersonal());
            }
            if (estudianteDetails.getTelefono() != null) estudianteExistente.setTelefono(estudianteDetails.getTelefono());
            if (estudianteDetails.getDireccion() != null) estudianteExistente.setDireccion(estudianteDetails.getDireccion());
            if (estudianteDetails.getDepartamento() != null) estudianteExistente.setDepartamento(estudianteDetails.getDepartamento());
            if (estudianteDetails.getProvincia() != null) estudianteExistente.setProvincia(estudianteDetails.getProvincia());
            if (estudianteDetails.getCarrera() != null) estudianteExistente.setCarrera(estudianteDetails.getCarrera());

            try {
                Estudiante estudianteActualizado = estudianteRepository.save(estudianteExistente);
                return new ResponseEntity<>(estudianteActualizado, HttpStatus.OK);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Error al actualizar el estudiante debido a una violación de integridad de datos.", e);
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante con ID " + id + " no encontrado.");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarEstudianteLogico(@PathVariable Long id) {
        Optional<Estudiante> optionalEstudiante = estudianteRepository.findById(id);
        if (optionalEstudiante.isPresent()) {
            Estudiante estudiante = optionalEstudiante.get();
            estudiante.setEstado(false); // Cambiar estado a inactivo
            estudianteRepository.save(estudiante); // Guardar el cambio de estado
            Map<String, String> response = new HashMap<>();
            response.put("message", "Estudiante con ID " + id + " eliminado lógicamente (estado inactivo).");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante con ID " + id + " no encontrado.");
        }
    }


}
