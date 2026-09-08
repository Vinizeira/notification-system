package br.com.astecob.aviso_guias.controller;

import br.com.astecob.aviso_guias.application.usecase.BuscarClientePorIdUseCase;
import br.com.astecob.aviso_guias.application.usecase.CadastrarClienteUseCase;
import br.com.astecob.aviso_guias.application.usecase.ListarClientesUseCase;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.dto.ClienteRequest;
import br.com.astecob.aviso_guias.dto.ClienteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    public ClienteController(
            CadastrarClienteUseCase cadastrarClienteUseCase,
            ListarClientesUseCase listarClientesUseCase, BuscarClientePorIdUseCase buscarClientePorIdUseCase) {
        this.cadastrarClienteUseCase = cadastrarClienteUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.buscarClientePorIdUseCase = buscarClientePorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteRequest request
    ) {
        Cliente cliente = cadastrarClienteUseCase.executar(
                request.nomeEmpresa(),
                request.email(),
                request.telefoneWhatsapp()
        );

        ClienteResponse response = ClienteResponse.fromDomain(cliente);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return listarClientesUseCase.executar().stream()
                .map(ClienteResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable UUID id) {
        Cliente cliente = buscarClientePorIdUseCase.executar(id);
        return ClienteResponse.fromDomain(cliente);
    }
}