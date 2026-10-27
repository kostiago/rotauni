package br.com.rotauni.api.municipio;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "municipio")
public class Municipio {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column (nullable = false, length = 120)
    private String nome;

    @Column (nullable = false, length = 2)
    private String uf;

    @Column (name= "codigo_ibge", nullable = false, unique = true, length = 7)
    private  String codigoIbge;

    @Column (nullable = false)
    private boolean ativo = true;

    @Column (name= "criado_em", nullable = false, updatable = false)
    private Instant criandoEm;

    protected Municipio() {

    }

    public Municipio(String nome, String uf, String codigoIbge) {
        this.nome = nome;
        this.uf = uf;
        this.codigoIbge = codigoIbge;
    }

    void aoCriar(){
        this.criandoEm = Instant.now();
    }

    public void ativar(){
        this.ativo = true;
    }

    public void desativar (){
        this.ativo = false;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUf() {
        return uf;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instant getCriandoEm() {
        return criandoEm;
    }
}