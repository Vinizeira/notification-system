package br.com.astecob.aviso_guias.controller;

import br.com.astecob.aviso_guias.application.usecase.BuscarClientePorIdUseCase;
import br.com.astecob.aviso_guias.application.usecase.CadastrarClienteUseCase;
import br.com.astecob.aviso_guias.application.usecase.ListarClientesUseCase;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.exception.ClienteDuplicadoException;
import br.com.astecob.aviso_guias.exception.ClienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarClienteUseCase cadastrarClienteUseCase;

    @MockitoBean
    private ListarClientesUseCase listarClientesUseCase;

    @MockitoBean
    private BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @Test
    void deveCadastrarClienteERetornar201() throws Exception {
        Cliente cliente = Cliente.novo(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        );

        when(cadastrarClienteUseCase.executar(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        )).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeEmpresa": "Astecob",
                                  "email": "contato@astecob.com",
                                  "telefoneWhatsapp": "11999999999"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cliente.getId().toString()))
                .andExpect(jsonPath("$.nomeEmpresa").value("Astecob"));
    }

    @Test
    void deveRetornar400QuandoRequestForInvalido() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeEmpresa": " ",
                                  "email": "contato@astecob.com",
                                  "telefoneWhatsapp": "11999999999"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem")
                        .value("O nome da empresa é obrigatório."));
    }

    @Test
    void deveRetornar409QuandoEmpresaForDuplicada() throws Exception {
        when(cadastrarClienteUseCase.executar(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        )).thenThrow(new ClienteDuplicadoException("Astecob"));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeEmpresa": "Astecob",
                                  "email": "contato@astecob.com",
                                  "telefoneWhatsapp": "11999999999"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem")
                        .value("Já existe um cliente cadastrado para a empresa: Astecob"));
    }

    @Test
    void deveListarClientesERetornar200() throws Exception {
        Cliente cliente = Cliente.novo(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        );

        when(listarClientesUseCase.executar()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(cliente.getId().toString()))
                .andExpect(jsonPath("$[0].nomeEmpresa").value("Astecob"));
    }

    @Test
    void deveBuscarClientePorIdERetornar200() throws Exception {
        Cliente cliente = Cliente.novo(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        );

        when(buscarClientePorIdUseCase.executar(cliente.getId()))
                .thenReturn(cliente);

        mockMvc.perform(get("/clientes/{id}", cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId().toString()))
                .andExpect(jsonPath("$.email").value("contato@astecob.com"));
    }

    @Test
    void deveRetornar404QuandoClienteNaoExiste() throws Exception {
        UUID id = UUID.randomUUID();

        when(buscarClientePorIdUseCase.executar(id))
                .thenThrow(new ClienteNaoEncontradoException(id));

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem")
                        .value("Cliente não encontrado: " + id));
    }
}