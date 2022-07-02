package br.com.alura.leilao.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;

class UsuarioDaoTest {

	private UsuarioDao dao;
	private EntityManager manager;
	
	@BeforeEach
	public void beforeEach() {
		this.manager = JPAUtil.getEntityManager();
		this.dao = new UsuarioDao(manager);
		manager.getTransaction().begin();
	}
	
	@AfterEach
	public void afterEach() {
		manager.getTransaction().rollback();
	}
	
	
	@Test
	void deveriaBuscarUmUsuarioCadastradoPeloUsername() {		
		Usuario usuario = criarUsuario();
		
		Usuario encontrado = dao.buscarPorUsername(usuario.getNome());
		assertNotNull(encontrado);
	}

	@Test
	void naoDeveriaBuscarUmUsuarioNaoCadastradoPeloUsername() {
		criarUsuario();
		assertThrows(NoResultException.class, () -> this.dao.buscarPorUsername("beltrano"));
	}
	
	@Test
	void deveriaDeletarUmUsuario() {
		Usuario usuario = criarUsuario();
		dao.deletar(usuario);
		
		assertThrows(NoResultException.class, () -> this.dao.buscarPorUsername(usuario.getNome()));
	}
	
	private Usuario criarUsuario() {
		Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
		manager.persist(usuario);
		return usuario;
	}

}
