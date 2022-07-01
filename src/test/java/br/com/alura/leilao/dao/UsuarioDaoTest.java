package br.com.alura.leilao.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;

class UsuarioDaoTest {

	private UsuarioDao dao;
	
	@Test
	void deveriaBuscarUmUsuarioPeloUsername() {
		EntityManager manager = JPAUtil.getEntityManager();
		this.dao = new UsuarioDao(manager);
		
		Usuario novoUsuario = new Usuario("fulano", "fulano@email.com", "12345678");
		manager.getTransaction().begin();
		manager.persist(novoUsuario);
		manager.getTransaction().commit();
		
		Usuario usuario = dao.buscarPorUsername("fulano");
		assertNotNull(usuario);
	}

}
