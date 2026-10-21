package com.example.controle_projetos.services;

import java.time.LocalDate;
import java.util.List;

import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.ProjetoDTO;

public interface ProjetoService {
    void adicionar(ProjetoDTO projetoDTO);

    DadosProjetoDTO buscarProjetoPorId(Integer id);

    List<DadosProjetoDTO> obterTodos(LocalDate inicio, LocalDate fim);

    void vincularFuncionario(Integer idProjeto, Integer idFuncionario);
}
