package br.com.alura.leilao.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;

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
		Usuario usuario = criarUsuario();
		Leilao leilao = new Leilao("Ford Ka 2009", new BigDecimal("5600"), LocalDate.now(), usuario);
		
		leilao = dao.salvar(leilao);
		
		leilao.setNome("Ford Ka 1998");
		leilao.setValorInicial(new BigDecimal("2358"));
		
		Leilao atualizado = dao.buscarPorId(leilao.getId());
		
		assertEquals("Ford Ka 1998", atualizado.getNome());
		assertEquals(new BigDecimal("2358"), atualizado.getValorInicial());
	}

	@Test
	void deveriaAtualizarUmLeilaoExistente() {
		Usuario usuario = criarUsuario();
		Leilao leilao = new Leilao("Ford Ka 2009", new BigDecimal("5600"), LocalDate.now(), usuario);
		
		leilao = dao.salvar(leilao);
		
		Leilao salvo = dao.buscarPorId(leilao.getId());
		
		assertNotNull(salvo);
	}
	
	private Usuario criarUsuario() {
		Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
		manager.persist(usuario);
		return usuario;
	}

}
