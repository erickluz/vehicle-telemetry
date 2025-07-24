package dev.erick.repository;

import dev.erick.domain.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, Long>{

    public Veiculo findByPlaca(String placa) {
        return findByPlaca(placa);
    }
}
