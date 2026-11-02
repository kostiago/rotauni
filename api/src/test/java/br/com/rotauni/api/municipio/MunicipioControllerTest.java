package br.com.rotauni.api.municipio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class MunicipioControllerTest {

    @Autowired
    private WebApplicationContext contexto;

    @Autowired
    private JdbcTemplate jdbc;

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        jdbc.execute("TRUNCATE municipio RESTART IDENTITY CASCADE");
    }

    private void cadastrarPombal() throws Exception {
        mvc.perform(post("/api/municipios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Pombal", "uf": "PB", "codigoIbge": "2512002" }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void deveCadastrarMunicipioENormalizarUf() throws Exception {
        mvc.perform(post("/api/municipios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Pombal", "uf": "pb", "codigoIbge": "2512002" }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/municipios/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uf").value("PB"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRecusarCodigoIbgeDuplicado() throws Exception {
        cadastrarPombal();

        mvc.perform(post("/api/municipios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Outro", "uf": "PB", "codigoIbge": "2512002" }
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail", containsString("2512002")));
    }

    @Test
    void deveListarCamposInvalidos() throws Exception {
        mvc.perform(post("/api/municipios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "", "uf": "Paraíba", "codigoIbge": "123" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Dados inválidos"))
                .andExpect(jsonPath("$.erros", hasSize(3)));
    }

    @Test
    void deveBuscarMunicipioPorId() throws Exception {
        cadastrarPombal();

        mvc.perform(get("/api/municipios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pombal"));
    }

    @Test
    void deveResponder404ParaIdInexistente() throws Exception {
        mvc.perform(get("/api/municipios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }

    @Test
    void deveListarMunicipiosEmOrdemAlfabetica() throws Exception {
        cadastrarPombal();
        mvc.perform(post("/api/municipios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nome": "Cajazeiras", "uf": "PB", "codigoIbge": "2503704" }
                                """))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/municipios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Cajazeiras"))
                .andExpect(jsonPath("$[1].nome").value("Pombal"));
    }
}