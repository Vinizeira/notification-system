package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.repository.GuiaRepository;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.GuiaEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class GuiaRepositoryJpaAdapter implements GuiaRepository {

    private final SpringDataGuiaRepository springDataGuiaRepository;

    public GuiaRepositoryJpaAdapter(SpringDataGuiaRepository springDataGuiaRepository) {
        this.springDataGuiaRepository = springDataGuiaRepository;
    }

    @Override
    public Guia salvar(Guia guia) {
        GuiaEntity salva = springDataGuiaRepository.save(toEntity(guia));
        return toDomain(salva);
    }

    @Override
    public Optional<Guia> buscarPorChaveNatural(String tipoGuia, String nomeEmpresaNormalizado,
                                                Integer mes, Integer ano) {
        return springDataGuiaRepository
                .findByTipoGuiaAndNomeEmpresaNormalizadoAndMesAndAno(tipoGuia, nomeEmpresaNormalizado, mes, ano)
                .map(this::toDomain);
    }

    private GuiaEntity toEntity(Guia guia) {
        return new GuiaEntity(
                guia.getId(), guia.getTipoGuia(), guia.getNomeEmpresaNormalizado(),
                guia.getMes(), guia.getAno(), guia.getVencimento(), guia.getClienteId(),
                guia.getStatus().name(), guia.getCriadoEm()
        );
    }

    private Guia toDomain(GuiaEntity entity) {
        return Guia.restaurar(
                entity.getId(), entity.getTipoGuia(), entity.getNomeEmpresaNormalizado(),
                entity.getMes(), entity.getAno(), entity.getVencimento(), entity.getClienteId(),
                StatusGuia.valueOf(entity.getStatus()), entity.getCriadoEm()
        );
    }
}