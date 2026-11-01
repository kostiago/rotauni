package br.com.rotauni.api.municipio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MunicipioRequisicao(
    @NotBlank (message = "Informe o nome")
    @Size (max = 120, message = "O nome deve ter no máximo 120 caracteres")
    String nome,

    @NotBlank (message = "Informe a UF")
    @Pattern (regexp = "[A-Za-z]{2}", message = "a UF deve ter 2 letras")
    String uf,

    @NotBlank (message = "Informe a UF")
    @Pattern (regexp = "\\d{7}", message = "o código IBGE deve ter 7 dígitos")
    String codigoIbge
) {
    
}
