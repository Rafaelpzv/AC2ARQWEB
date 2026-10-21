package com.example.controle_projetos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DadosSetorDTO {
    private Integer id;
    private String nome;
    private List<DadosFuncionarioDTO> funcionarios;
}
