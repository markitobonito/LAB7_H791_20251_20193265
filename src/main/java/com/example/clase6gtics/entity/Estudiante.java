package com.example.clase6gtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Size(max = 255, message = "Los nombres no pueden exceder los 255 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Size(max = 255, message = "Los apellidos no pueden exceder los 255 caracteres")
    private String apellidos;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Column(unique = true)
    private String dni;

    @NotBlank(message = "El Código PUCP no puede estar vacío")
    @Column(name = "codigo_pucp", unique = true)
    private String codigoPucp;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")

    private LocalDate fechaNacimiento;

    @NotBlank(message = "El sexo no puede estar vacío")
    private String sexo;

    @NotBlank(message = "El correo institucional no puede estar vacío")
    @Email(message = "El formato del correo institucional es inválido")

    @Column(name = "correo_institucional", unique = true)
    private String correoInstitucional;

    @Email(message = "El formato del correo personal es inválido")
    @Size(max = 255, message = "El correo personal no puede exceder los 255 caracteres")
    @Column(name = "correo_personal", unique = true)
    private String correoPersonal; // Puede ser null, pero si se envía, debe ser válido y único

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres")
    private String direccion;

    @NotBlank(message = "El departamento no puede estar vacío")
    @Size(max = 255, message = "El departamento no puede exceder los 255 caracteres")
    private String departamento;

    @NotBlank(message = "La provincia no puede estar vacía")
    @Size(max = 255, message = "La provincia no puede exceder los 255 caracteres")
    private String provincia;

    @NotBlank(message = "La carrera no puede estar vacía")
    @Size(max = 255, message = "La carrera no puede exceder los 255 caracteres")
    private String carrera;

    @Column(name = "fecha_registro", updatable = false) // No se actualiza después de la creación
    private LocalDateTime fechaRegistro;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

    // Campos no persistidos, solo para validación y lógica de negocio
    @Transient // Indica que este campo no se mapea a la base de datos
    private Integer edad;

    @PrePersist // Se ejecuta antes de persistir la entidad por primera vez
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = true; // Por defecto Activo
        }
    }

    @PreUpdate // Se ejecuta antes de actualizar la entidad
    protected void onUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}




