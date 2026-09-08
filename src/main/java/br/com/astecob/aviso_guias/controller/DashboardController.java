package br.com.astecob.aviso_guias.controller;

import br.com.astecob.aviso_guias.application.usecase.AtualizarClienteUseCase;
import br.com.astecob.aviso_guias.application.usecase.DeletarClienteUseCase;
import br.com.astecob.aviso_guias.application.usecase.ReenviarNotificacaoUseCase;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.domain.repository.GuiaRepository;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import br.com.astecob.aviso_guias.domain.repository.PendenciaCadastroClienteRepository;
import br.com.astecob.aviso_guias.infrastructure.web.ClienteForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ClienteRepository clienteRepository;
    private final HistoricoNotificacaoRepository historicoRepository;
    private final PendenciaCadastroClienteRepository pendenciaRepository;
    private final GuiaRepository guiaRepository;
    private final DeletarClienteUseCase deletarClienteUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final ReenviarNotificacaoUseCase reenviarNotificacaoUseCase;

    @Value("${astecob.guias.diretorio:guias}")
    private String diretorioGuias;

    public DashboardController(ClienteRepository clienteRepository,
                               HistoricoNotificacaoRepository historicoRepository,
                               PendenciaCadastroClienteRepository pendenciaRepository,
                               GuiaRepository guiaRepository,
                               DeletarClienteUseCase deletarClienteUseCase,
                               AtualizarClienteUseCase atualizarClienteUseCase,
                               ReenviarNotificacaoUseCase reenviarNotificacaoUseCase) {
        this.clienteRepository = clienteRepository;
        this.historicoRepository = historicoRepository;
        this.pendenciaRepository = pendenciaRepository;
        this.guiaRepository = guiaRepository;
        this.deletarClienteUseCase = deletarClienteUseCase;
        this.atualizarClienteUseCase = atualizarClienteUseCase;
        this.reenviarNotificacaoUseCase = reenviarNotificacaoUseCase;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("novoCliente", new ClienteForm());
        model.addAttribute("listaClientes", clienteRepository.listarTodos());
        model.addAttribute("pendencias", pendenciaRepository.listarTodas());
        model.addAttribute("historicoNotificacoes", historicoRepository.listarTodos());
        return "dashboard";
    }

    @PostMapping("/clientes")
    public String salvarCliente(@ModelAttribute("novoCliente") ClienteForm form, RedirectAttributes redirectAttributes) {
        try {
            String nomeNormalizado = form.getNomeEmpresa().trim().toLowerCase();

            if (clienteRepository.existePorNomeEmpresaNormalizado(nomeNormalizado)) {
                redirectAttributes.addFlashAttribute("erro", "Já existe um cliente cadastrado com esta empresa.");
                return "redirect:/dashboard";
            }

            Cliente cliente = Cliente.novo(
                    form.getNomeEmpresa(),
                    form.getEmail(),
                    form.getTelefoneWhatsapp()
            );
            clienteRepository.salvar(cliente);
            redirectAttributes.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            // Trata o erro técnico do banco e exibe uma mensagem limpa
            String mensagemErro = "Erro ao salvar cliente.";
            if (e.getMessage() != null && e.getMessage().contains("clientes_nome_empresa_normalizado_key")) {
                mensagemErro = "Já existe uma empresa cadastrada com esse nome.";
            }
            redirectAttributes.addFlashAttribute("erro", mensagemErro);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/clientes/{id}/deletar")
    public String deletarCliente(@PathVariable UUID id) {
        deletarClienteUseCase.executar(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/clientes/{id}/editar")
    public String editarCliente(
            @PathVariable UUID id,
            @RequestParam String nomeEmpresa,
            @RequestParam String email,
            @RequestParam String telefoneWhatsapp) {
        atualizarClienteUseCase.executar(id, nomeEmpresa, email, telefoneWhatsapp);
        return "redirect:/dashboard";
    }

    @PostMapping("/guias/{id}/reenviar")
    public String reenviarGuia(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            Guia guia = guiaRepository.encontrarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Guia não encontrada"));

            Cliente cliente = clienteRepository.buscarPorId(guia.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado para esta guia"));

            File arquivoPdf = new File(diretorioGuias, guia.getTipoGuia() + "-" + guia.getNomeEmpresaNormalizado() + "-" + String.format("%02d", guia.getMes()) + "-" + guia.getAno() + ".pdf");
            if (!arquivoPdf.exists()) {
                throw new RuntimeException("Arquivo PDF físico não encontrado no diretório: " + arquivoPdf.getPath());
            }

            reenviarNotificacaoUseCase.executar(guia, cliente, arquivoPdf);

            redirectAttributes.addFlashAttribute("sucesso", "Notificação reenviada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao reenviar: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
