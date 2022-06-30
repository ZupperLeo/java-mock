package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
	
	@Mock
	private Clock clock;

	@Captor
	private ArgumentCaptor<Pagamento> captor;//Captura um objeto criado em um metodo sem retorno
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);//Inicia todos os atributos anotados com @Mock
		this.service = new GeradorDePagamento(dao, clock); 
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoEmDiaDasmena() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		LocalDate data = LocalDate.of(2022, 7, 4);		
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

		service.gerarPagamento(vencedor);
		Mockito.verify(dao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
		assertEquals(vencedor.getValor(), pagamento.getValor());
		assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		assertEquals(leilao, pagamento.getLeilao());
		assertFalse(pagamento.getPago());
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaSegundaQuandoOPagamentoCairNoSabado() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		LocalDate data = LocalDate.of(2022, 6, 1);		
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		service.gerarPagamento(vencedor);
		Mockito.verify(dao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilaoNaSegundaQuandoOPagamentoCairNoDomingo() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		LocalDate data = LocalDate.of(2022, 6, 2);		
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		service.gerarPagamento(vencedor);
		Mockito.verify(dao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
	}

	private Leilao leilao() {		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance lance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);
		
		return leilao;
	}
	
}
