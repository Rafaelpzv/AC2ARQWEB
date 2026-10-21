package com.example.controle_projetos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.dtos.DadosFuncionarioDTO;
import com.example.controle_projetos.dtos.DadosSetorDTO;
import com.example.controle_projetos.dtos.SetorDTO;
import com.example.controle_projetos.repositories.SetorRepository;

@Service
public class SetorServiceImpl implements SetorService {
    private SetorRepository setorRepository;

    public SetorServiceImpl(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Override
    public void adicionar(SetorDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("Nome do setor é obrigatório");
        }
        String nome = dto.getNome().trim();
        if (setorRepository.existsByNomeIgnoreCase(nome)) {
            throw new RegraNegocioException("Já existe um setor com esse nome");
        }
        setorRepository.save(Setor.builder().nome(nome).build());
    }

    @Override
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        Setor s = setorRepository.obterSetorComFuncionarios(idSetor)
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado"));
        return paraDados(s);
    }

    @Override
    public List<DadosSetorDTO> obterTodos() {
        return setorRepository.obterSetoresEFuncionarios().stream()
                .map(this::paraDados)
                .toList();
    }

    private DadosSetorDTO paraDados(Setor s) {
        List<DadosFuncionarioDTO> funcionarios = s.getFuncionarios().stream()
                .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
                .toList();
        return new DadosSetorDTO(s.getId(), s.getNome(), funcionarios);
    }
}
