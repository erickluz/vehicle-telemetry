package dev.erick.service;

import dev.erick.domain.Status;
import dev.erick.domain.Veiculo;
import dev.erick.json.VeiculoDTO;
import dev.erick.repository.VeiculoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoServico {

    private VeiculoRepository veiculoRepository;

    public VeiculoServico(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Veiculo getVeiculoPorId(String id) {
        return veiculoRepository.findById(Long.valueOf(id));
    }

    public Veiculo getVeiculoPorPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa);
    }

    @Transactional
    public void salvarVeiculo(VeiculoDTO veiculoDTO) {
        veiculoRepository.persist(veiculoDTO.fromVeiculoDTO());
    }

    @Transactional
    public Veiculo cadastrarVeiculo(VeiculoDTO veiculoDTO) {
        Veiculo veiculo = veiculoDTO.fromVeiculoDTO();
        veiculo.setStatus(Status.ATIVO);
        veiculoRepository.persistAndFlush(veiculo);
        return veiculoRepository.findById(veiculo.getId());
    }

    @Transactional
    public void desativarVeiculo(String id) {
        Veiculo veiculo = veiculoRepository.findById(Long.valueOf(id));
        veiculo.setStatus(Status.INATIVO);
        // como a entidade está gerenciada, apenas alterar o status dentro de @Transactional é suficiente
    }

}
