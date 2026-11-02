package br.com.rotauni.api.usuario.enums;

public enum Papel {
    SUPERADMIN("Superadministrador"),
    EDITOR("Administrador"),
    LEITOR("Leitor"),
    ESTUDANTE("Estudante"),
    MOTORISTA("Motorista");

    private final String descricao;

    Papel(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return  descricao;
    }

    public boolean pertenceAMunicipio(){
        return this != SUPERADMIN;
    }
}
