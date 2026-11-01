package br.com.rotauni.api.municipio;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    private final MunicipioService service;

    public MunicipioController(MunicipioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MunicipioResposta> cadastrar(@RequestBody @Valid MunicipioRequisicao requisicao,
            UriComponentsBuilder uriBuilder) {
        var criado = service.cadastrar(requisicao);
        var location = uriBuilder.path("/api/municipios/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<MunicipioResposta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MunicipioResposta buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
    
    

}
