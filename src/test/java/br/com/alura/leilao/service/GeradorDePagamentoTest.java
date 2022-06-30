package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento service;
	
	@Mock
	private PagamentoDao dao;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;//Captura um objeto criado em um metodo sem retorno
	
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
		
		Mockito.verify(dao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		assertEquals(vencedor.getValor(), pagamento.getValor());
		assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		assertEquals(leilao, pagamento.getLeilao());
		assertFalse(pagamento.getPago());
	}

	private Leilao leilao() {		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance lance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);
		
		return leilao;
	}
	
}
