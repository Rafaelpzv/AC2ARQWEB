package com.example.controle_projetos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.FuncionarioDTO;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.SetorRepository;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
    private FuncionarioRepository funcionarioRepository;
    private SetorRepository setorRepository;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository,
            SetorRepository setorRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
    }

    @Override
    public void adicionar(FuncionarioDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("Nome do funcionário é obrigatório");
        }
        if (dto.getSetorId() == null) {
            throw new RegraNegocioException("Setor do funcionário é obrigatório");
        }
        Setor setor = setorRepository.findById(dto.getSetorId())
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado"));
        Funcionario f = Funcionario.builder()
                .nome(dto.getNome().trim())
                .setor(setor)
                .build();
        funcionarioRepository.save(f);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new RegraNegocioException("Funcionário não encontrado");
        }
        return funcionarioRepository.obterProjetosDoFuncionario(idFuncionario).stream()
                .map(ProjetoServiceImpl::paraDados)
                .toList();
    }
}
