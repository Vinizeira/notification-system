package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.model.PendenciaCadastroCliente;
import br.com.astecob.aviso_guias.domain.repository.PendenciaCadastroClienteRepository;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.PendenciaCadastroClienteEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PendenciaCadastroClienteRepositoryJpaAdapter implements PendenciaCadastroClienteRepository {

    private final SpringDataPendenciaCadastroClienteRepository springDataRepository;

    public PendenciaCadastroClienteRepositoryJpaAdapter(SpringDataPendenciaCadastroClienteRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public PendenciaCadastroCliente salvar(PendenciaCadastroCliente pendencia) {
        PendenciaCadastroClienteEntity salva = springDataRepository.save(toEntity(pendencia));
        return toDomain(salva);
    }

    @Override
    public List<PendenciaCadastroCliente> listarTodas() {
        return springDataRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private PendenciaCadastroClienteEntity toEntity(PendenciaCadastroCliente pendencia) {
        return new PendenciaCadastroClienteEntity(
                pendencia.getId(), pendencia.getTipoGuia(), pendencia.getNomeEmpresa(),
                pendencia.getMes(), pendencia.getAno(), pendencia.getCriadoEm()
        );
    }

    private PendenciaCadastroCliente toDomain(PendenciaCadastroClienteEntity entity) {
        return PendenciaCadastroCliente.restaurar(
                entity.getId(), entity.getTipoGuia(), entity.getNomeEmpresa(),
                entity.getMes(), entity.getAno(), entity.getCriadoEm()
        );
    }
}