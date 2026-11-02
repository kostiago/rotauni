package br.com.rotauni.api.seguranca;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CriptografiaTest {
    
    private final PasswordEncoder encoder = new CriptografiaConfig().passwordEncoder();


    @Test
    void naoDeveGuardarASenhaEmTexto() {
        String hash = encoder.encode("senha-secreta");

        assertNotEquals("senha-secreta", hash);
        assertTrue(hash.startsWith("$2"), "O Hash deve estar no formato BCrypt");
    }

    @Test 
    void mesmaSenhaDeveGerarHashesDiferentes(){
        assertNotEquals(encoder.encode("123456"), encoder.encode("123456"));
    }

    @Test 
    void deveReconhecerASenhaCorretaERecusarAErrada(){
        String hash = encoder.encode("senha-secreta");

        assertTrue(encoder.matches("senha-secreta", hash));
        assertFalse(encoder.matches("senha-errada", hash));
    }
}
