package com.example.controle_projetos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FuncionarioDTO {
    private String nome;
    private Integer setorId;
}
