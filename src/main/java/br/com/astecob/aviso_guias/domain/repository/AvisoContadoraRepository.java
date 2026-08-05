package br.com.astecob.aviso_guias.domain.repository;

import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import java.util.List;

public interface AvisoContadoraRepository {
    AvisoContadora salvar(AvisoContadora aviso);
    List<AvisoContadora> listarTodas();
}