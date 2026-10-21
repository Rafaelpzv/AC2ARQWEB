package com.example.controle_projetos;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.controle_projetos.domains.Funcionario;
import com.example.controle_projetos.domains.Projeto;
import com.example.controle_projetos.domains.Setor;
import com.example.controle_projetos.repositories.FuncionarioRepository;
import com.example.controle_projetos.repositories.ProjetoRepository;
import com.example.controle_projetos.repositories.SetorRepository;

@SpringBootApplication
public class ControleProjetosApplication {

	@Bean
	public CommandLineRunner init(
			@Autowired SetorRepository setorRepository,
			@Autowired FuncionarioRepository funcionarioRepository,
			@Autowired ProjetoRepository projetoRepository) {
		return args -> {
			System.out.println("=== Criando setores ===");
			Setor ti = setorRepository.save(Setor.builder().nome("TI").build());
			Setor rh = setorRepository.save(Setor.builder().nome("RH").build());

			System.out.println("=== Criando funcionarios ===");
			Funcionario ana = funcionarioRepository
					.save(Funcionario.builder().nome("Ana").setor(ti).build());
			Funcionario bruno = funcionarioRepository
					.save(Funcionario.builder().nome("Bruno").setor(ti).build());
			Funcionario carla = funcionarioRepository
					.save(Funcionario.builder().nome("Carla").setor(rh).build());

			System.out.println("=== Criando projetos ===");
			Projeto p1 = Projeto.builder()
					.descricao("Migracao do ERP")
					.dataInicio(LocalDate.of(2026, 1, 10))
					.dataFim(LocalDate.of(2026, 6, 30))
					.build();
			p1.getFuncionarios().add(ana);
			p1.getFuncionarios().add(bruno);
			p1 = projetoRepository.save(p1);

			Projeto p2 = Projeto.builder()
					.descricao("Programa de onboarding")
					.dataInicio(LocalDate.of(2026, 3, 1))
					.build();
			p2.getFuncionarios().add(carla);
			p2.getFuncionarios().add(ana);
			p2 = projetoRepository.save(p2);

			System.out.println("=== (a) Projeto com funcionarios ===");
			projetoRepository.obterProjetoComFuncionarios(p1.getId())
					.ifPresent(System.out::println);

			System.out.println("=== (b) Projetos com inicio entre 01/01 e 31/03/2026 ===");
			projetoRepository
					.obterProjetosPorPeriodoInicio(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31))
					.forEach(p -> System.out.println(p.getDescricao()));

			System.out.println("=== (c) Projetos da Ana ===");
			funcionarioRepository.obterProjetosDoFuncionario(ana.getId())
					.forEach(p -> System.out.println(p.getDescricao()));

			System.out.println("=== (d) Setores e funcionarios ===");
			setorRepository.obterSetoresEFuncionarios().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ControleProjetosApplication.class, args);
	}

}
