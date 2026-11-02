package br.com.rotauni.api.usuario;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import br.com.rotauni.api.municipio.Municipio;
import br.com.rotauni.api.usuario.enums.Papel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Papel papel;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "ultimo_acesso_em")
    private Instant ultimoAcessoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected Usuario() {
    }

    public Usuario(Municipio municipio, String nome, String cpf, String email,
            String telefone, String senhaHash, Papel papel) {
        if (papel.pertenceAMunicipio() != (municipio != null)) {
            throw new IllegalArgumentException(
                    "O papel " + papel + " não é compatível com o município informado.");
        }
        this.municipio = municipio;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
        this.papel = papel;
    }

    @PrePersist 
    void aoCriar(){
        this.criadoEm = agora();
        this.atualizadoEm = this.criadoEm;
    }

    @PreUpdate 
    void aoAtualizar(){
        this.atualizadoEm = agora();
    }


    private static  Instant agora(){
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    public void alterarSenha(String novoSenhaHash){
        this.ultimoAcessoEm = agora();
    }

    public void desativar(){
        this.ativo = false;
    }

    public void ativar(){
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public Papel getPapel() {
        return papel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getUltimoAcessoEm() {
        return ultimoAcessoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    
}
