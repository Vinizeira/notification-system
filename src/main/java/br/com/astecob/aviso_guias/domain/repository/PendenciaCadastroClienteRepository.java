package br.com.astecob.aviso_guias.domain.repository;

import br.com.astecob.aviso_guias.domain.model.PendenciaCadastroCliente;
import java.util.List;

public interface PendenciaCadastroClienteRepository {
    PendenciaCadastroCliente salvar(PendenciaCadastroCliente pendencia);
    List<PendenciaCadastroCliente> listarTodas();
}