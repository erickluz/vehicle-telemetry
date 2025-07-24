package dev.erick.domain;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
public class Veiculo extends PanacheEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private String ano;
    private Status status;
    private LocalDateTime dataCadastro;
    
}
