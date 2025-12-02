package com.simpay.repository;

import com.simpay.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByEmailAndActivoTrue(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.activo = true")
    Optional<Usuario> findActiveUserByEmail(@Param("email") String email);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    long countByRolAndActivoTrue(@Param("rol") Usuario.Rol rol);
}