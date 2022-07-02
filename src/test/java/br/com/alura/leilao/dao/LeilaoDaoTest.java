package br.com.alura.leilao.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.LeilaoBuilder;
import br.com.alura.leilao.util.builder.UsuarioBuilder;

class LeilaoDaoTest {

	private LeilaoDao dao;
	private EntityManager manager;
	
	@BeforeEach
	public void beforeEach() {
		this.manager = JPAUtil.getEntityManager();
		this.dao = new LeilaoDao(manager);
		manager.getTransaction().begin();
	}
	
	@AfterEach
	public void afterEach() {
		manager.getTransaction().rollback();
	}
	
	@Test
	void deveriaSalvarUmNovoLeilao() {
		Leilao leilao = builder();
		
		leilao = dao.salvar(leilao);
		
		leilao.setNome("Ford Ka 1998");
		leilao.setValorInicial(new BigDecimal("2358"));
		
		Leilao atualizado = dao.buscarPorId(leilao.getId());
		
		assertEquals("Ford Ka 1998", atualizado.getNome());
		assertEquals(new BigDecimal("2358"), atualizado.getValorInicial());
	}

	@Test
	void deveriaAtualizarUmLeilaoExistente() {
		Leilao leilao = builder();
		
		leilao = dao.salvar(leilao);
		
		Leilao salvo = dao.buscarPorId(leilao.getId());
		
		assertNotNull(salvo);
	}
	
	private Leilao builder() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		manager.persist(usuario);
		
		Leilao leilao = new LeilaoBuilder()
				.comNome("Ford Ka 2009")
				.comValorInicial("5600")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.criar();
		
		return leilao;
	}

}
