package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import br.com.astecob.aviso_guias.domain.repository.AvisoContadoraRepository;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.AvisoContadoraEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AvisoContadoraRepositoryJpaAdapter implements AvisoContadoraRepository {

    private final SpringDataAvisoContadoraRepository springDataRepository;

    public AvisoContadoraRepositoryJpaAdapter(SpringDataAvisoContadoraRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public AvisoContadora salvar(AvisoContadora aviso) {
        AvisoContadoraEntity salva = springDataRepository.save(toEntity(aviso));
        return toDomain(salva);
    }

    @Override
    public List<AvisoContadora> listarTodas() {
        return springDataRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private AvisoContadoraEntity toEntity(AvisoContadora aviso) {
        return new AvisoContadoraEntity(aviso.getId(), aviso.getMensagem(), aviso.getCriadoEm(), aviso.getGuiaId());
    }

    private AvisoContadora toDomain(AvisoContadoraEntity entity) {
        return AvisoContadora.restaurar(entity.getId(), entity.getMensagem(), entity.getCriadoEm(), entity.getGuiaId());
    }
}