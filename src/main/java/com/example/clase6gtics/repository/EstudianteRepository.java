package com.example.clase6gtics.repository;

import com.example.clase6gtics.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByDni(String dni);
    // Puedes añadir más métodos de búsqueda si son necesarios (ej: por codigoPucp, correoInstitucional, etc.)
    boolean existsByDni(String dni);
    boolean existsByCodigoPucp(String codigoPucp);
    boolean existsByCorreoInstitucional(String correoInstitucional);
    boolean existsByCorreoPersonal(String correoPersonal);
}
