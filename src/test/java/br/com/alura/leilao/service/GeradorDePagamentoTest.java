package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento service;
	
	@Mock
	private PagamentoDao dao;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmail;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);//Inicia todos os atributos anotados com @Mock
		this.service = new GeradorDePagamento(dao); 
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		service.gerarPagamento(vencedor);
		
		
	}

	private Leilao leilao() {		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance lance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);
		
		return leilao;
	}
	
}
