package br.com.rotauni.api.municipio;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rotauni.api.comum.RecursoNaoEncontradoException;
import br.com.rotauni.api.comum.RegraNegocioException;

@Service
public class MunicipioService {

    private final MunicipioRepository repository;

    public MunicipioService(MunicipioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MunicipioResposta cadastrar(MunicipioRequisicao requisicao) {
        if (repository.existsByCodigoIbge(requisicao.codigoIbge())) {
            throw new RegraNegocioException("Já existe um município com o código IBGE" + requisicao.codigoIbge());
        }

        var municipio = new Municipio(
                requisicao.nome().trim(),
                requisicao.uf().toUpperCase(),
                requisicao.codigoIbge());

        return MunicipioResposta.de(repository.save(municipio));
    }

    @Transactional(readOnly = true)
    public List<MunicipioResposta> listar() {
        return repository.findAll(Sort.by("nome")).stream().map(MunicipioResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public MunicipioResposta buscar(Long id) {
        return repository.findById(id).map(MunicipioResposta::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Município não encontrado!"));
    }
}
