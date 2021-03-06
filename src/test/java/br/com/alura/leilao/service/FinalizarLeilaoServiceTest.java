package br.com.alura.leilao.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	
	@Mock
	private LeilaoDao dao;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmail;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);//Inicia todos os atributos anotados com @Mock
		this.service = new FinalizarLeilaoService(dao, enviadorDeEmail); 
	}
	
	@Test
	void deveriaFinalizarUmLeilaoExpirado() {
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		assertTrue(leilao.isFechado());
		assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
		
		Mockito.verify(dao).salvar(leilao);
	}

	@Test
	void deveriaEnviarEmailParaVencedorDoLeilao() {
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		Lance valor = leilao.getLanceVencedor();
		
		Mockito.verify(enviadorDeEmail).enviarEmailVencedorLeilao(valor);
	}

	@Test
	void naoDeveriaEnviarEmailParaVencedorDoLeilaoEmCasoDeErroAoEncerrarLeilao() {
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		Mockito.when(dao.salvar(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {			
			service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(enviadorDeEmail);
		} catch (Exception e) {}
		
	}
	
	private List<Leilao> leiloes() {
		List<Leilao> leiloes = new ArrayList<>();
		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		leiloes.add(leilao);
		
		return leiloes;
	}

}
