package br.com.rotauni.api.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmailIgnoreCase (String email);
    boolean existsByEmailIgnoreCase (String email);
    boolean existsByMunicipioIdAndCpf (Long municipioId, String cpf);
}
