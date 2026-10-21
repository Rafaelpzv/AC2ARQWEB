package com.example.controle_projetos.services;

import java.util.List;

import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;

public interface FuncionarioService {
    void adicionar(FuncionarioDTO funcionarioDTO);

    List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario);
}
