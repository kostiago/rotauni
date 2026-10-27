package br.com.rotauni.api.municipio;

import java.time.Instant;

public record MunicipioResposta(
        Long id,
        String nome,
        String uf,
        String codigoIbge,
        boolean ativo,
        Instant criadoEm) {
    public static MunicipioResposta de(Municipio municipio) {
        return new MunicipioResposta(
                municipio.getId(),
                municipio.getNome(),
                municipio.getUf(),
                municipio.getCodigoIbge(),
                municipio.isAtivo(),
                municipio.getCriandoEm()
        );
    }
    
}
