package br.com.alura.leilao.dao;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Usuario;

class UsuarioDaoTest {

	private EntityManager manager;
	private UsuarioDao dao;
	
	@Test
	void deveriaBuscarUmUsuarioPeloUsername() {
		this.dao = new UsuarioDao(this.manager);
		Usuario usuario = this.dao.buscarPorUsername("fulano");
		
		assertNotNull(usuario);		
	}

}
