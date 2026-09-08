package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.port.AvisoContadoraSender;
import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import br.com.astecob.aviso_guias.domain.model.PendenciaCadastroCliente;
import br.com.astecob.aviso_guias.domain.repository.PendenciaCadastroClienteRepository;
import org.springframework.stereotype.Component;

@Component
public class AvisarContadoraUseCase {

    private final PendenciaCadastroClienteRepository pendenciaCadastroClienteRepository;
    private final AvisoContadoraSender avisoContadoraSender;

    public AvisarContadoraUseCase(PendenciaCadastroClienteRepository pendenciaCadastroClienteRepository,
                                  AvisoContadoraSender avisoContadoraSender) {
        this.pendenciaCadastroClienteRepository = pendenciaCadastroClienteRepository;
        this.avisoContadoraSender = avisoContadoraSender;
    }

    public void avisarClienteNaoEncontrado(String tipoGuia, String nomeEmpresa, Integer mes, Integer ano) {
        pendenciaCadastroClienteRepository.salvar(
                new PendenciaCadastroCliente(tipoGuia, nomeEmpresa, mes, ano));

        avisoContadoraSender.avisar(new AvisoContadora(
                "Cliente não encontrado para a guia " + tipoGuia + " de " + nomeEmpresa
                        + " (" + mes + "/" + ano + "). Pendência de cadastro criada.",
                null));
    }
}