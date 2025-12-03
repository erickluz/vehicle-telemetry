package dev.erick.domain;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
public class Veiculo extends PanacheEntity {

    private String placa;
    private String marca;
    private String modelo;
    private String ano;
    private Status status;
    private LocalDateTime dataCadastro;

    public Long getId() {
        return super.id;
    }

    public void setId(Long id) {
        super.id = id;
    }
}
