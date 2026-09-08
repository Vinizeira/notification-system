    package br.com.astecob.aviso_guias.domain.repository;

    import br.com.astecob.aviso_guias.domain.model.Guia;
    import java.util.Optional;
    import java.util.UUID;

    public interface GuiaRepository {
        Guia salvar(Guia guia);
        Optional<Guia> buscarPorChaveNatural(String tipoGuia, String nomeEmpresaNormalizado, Integer mes, Integer ano);
        Optional<Guia> encontrarPorId(UUID id);
    }