package com.example.controle_projetos.services;

import java.util.List;

import com.example.controle_projetos.dtos.DadosSetorDTO;
import com.example.controle_projetos.dtos.SetorDTO;

public interface SetorService {
    void adicionar(SetorDTO setorDTO);

    DadosSetorDTO buscarSetorPorId(Integer idSetor);

    List<DadosSetorDTO> obterTodos();
}
