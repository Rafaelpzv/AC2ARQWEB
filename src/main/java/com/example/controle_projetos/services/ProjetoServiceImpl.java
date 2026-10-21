package com.example.controle_projetos.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;
import com.example.controle_projetos.dtos.DadosFuncionarioDTO;
import com.example.controle_projetos.dtos.DadosProjetoDTO;
import com.example.controle_projetos.dtos.ProjetoDTO;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.ProjetoRepository;

@Service
public class ProjetoServiceImpl implements ProjetoService {
    private ProjetoRepository projetoRepository;
    private FuncionarioRepository funcionarioRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository,
            FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void adicionar(ProjetoDTO dto) {
        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RegraNegocioException("Descrição do projeto é obrigatória");
        }
        if (dto.getDataInicio() == null) {
            throw new RegraNegocioException("Data de início é obrigatória");
        }
        if (dto.getDataFim() != null && dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new RegraNegocioException(
                    "Data de fim não pode ser anterior à data de início");
        }
        Projeto p = Projeto.builder()
                .descricao(dto.getDescricao().trim())
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .build();
        projetoRepository.save(p);
    }

    @Override
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        Projeto p = projetoRepository.obterProjetoComFuncionarios(id)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));
        return paraDados(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> obterTodos(LocalDate inicio, LocalDate fim) {
        if ((inicio == null) != (fim == null)) {
            throw new RegraNegocioException(
                    "Informe as duas datas (inicio e fim) para filtrar por período");
        }
        List<Projeto> projetos;
        if (inicio == null) {
            projetos = projetoRepository.findAll();
        } else {
            if (fim.isBefore(inicio)) {
                throw new RegraNegocioException(
                        "O fim do período não pode ser anterior ao início");
            }
            projetos = projetoRepository.obterProjetosPorPeriodoInicio(inicio, fim);
        }
        return projetos.stream().map(ProjetoServiceImpl::paraDados).toList();
    }

    @Override
    @Transactional
    public void vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        Projeto p = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));
        Funcionario f = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));
        boolean jaVinculado = p.getFuncionarios().stream()
                .anyMatch(x -> x.getId().equals(idFuncionario));
        if (jaVinculado) {
            throw new RegraNegocioException("Funcionário já está vinculado ao projeto");
        }
        p.getFuncionarios().add(f);
        projetoRepository.save(p);
    }

    static DadosProjetoDTO paraDados(Projeto p) {
        List<DadosFuncionarioDTO> funcionarios = p.getFuncionarios().stream()
                .map(f -> new DadosFuncionarioDTO(f.getId(), f.getNome()))
                .toList();
        return new DadosProjetoDTO(p.getId(), p.getDescricao(),
                p.getDataInicio(), p.getDataFim(), funcionarios);
    }
}
