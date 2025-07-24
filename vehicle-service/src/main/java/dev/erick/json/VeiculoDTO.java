package dev.erick.json;

import java.time.format.DateTimeFormatter;

import dev.erick.domain.Status;
import dev.erick.domain.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VeiculoDTO {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private String ano;
    private String status;
    private String dataCadastro;

    public VeiculoDTO() {

    }

    public VeiculoDTO(Veiculo veiculo) {
        this.id = veiculo.getId();
        this.placa = veiculo.getPlaca();
        this.marca = veiculo.getMarca();
        this.modelo = veiculo.getModelo();
        this.ano = veiculo.getAno();
        this.status = veiculo.getStatus().toString();
        this.dataCadastro = veiculo.getDataCadastro().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public Veiculo fromVeiculoDTO() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(id);
        veiculo.setPlaca(placa);
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setAno(ano);
        veiculo.setStatus(Status.fromString(this.status));
        return veiculo;
    }

}
