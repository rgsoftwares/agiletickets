package br.com.caelum.agiletickets.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.testng.Assert;

public class EspetaculoTest {

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(5));
	}

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(6));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(15));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(5, 3));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(10, 3));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(5, 3));
	}

	private Sessao sessaoComIngressosSobrando(int quantidade) {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(quantidade * 2);
		sessao.setIngressosReservados(quantidade);

		return sessao;
	}
	
	@Test
	public void deveCriarUmaSessaoQuandoDataInicioForIgualDataFimEPeriodicidadeDiaria() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate();
		LocalDate fim = new LocalDate();
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.DIARIA);

		assertTrue((listaSessao.size() == 1));
		Assert.assertSame(listaSessao.get(0).getEspetaculo(), espetaculo);
		
		Assert.assertEquals(listaSessao.get(0).getInicio(), inicio.toDateTime(horario));
		
	}
	
	@Test
	public void deveCriarUmaSessaoQuandoDataInicioForIgualDataFimEPeriodicidadeSemanal() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate();
		LocalDate fim = new LocalDate();
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.SEMANAL);

		assertTrue((listaSessao.size() == 1));
		Assert.assertSame(listaSessao.get(0).getEspetaculo(), espetaculo);
		Assert.assertEquals(listaSessao.get(0).getInicio(), inicio.toDateTime(horario));
		
	}
	
	@Test
	public void deveCriarDezSessoesQuandoDiferencaEntrePeriodosForIgualDezDiasEPeriodicidadeDiaria() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate().withDayOfMonth(1).withMonthOfYear(9).withYear(2013);
		LocalDate fim = new LocalDate().withDayOfMonth(11).withMonthOfYear(9).withYear(2013);
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.DIARIA);

		assertTrue((listaSessao.size() == 10));
		for ( int i=0; i<10; i++ ) {
			Assert.assertSame(listaSessao.get(i).getEspetaculo(), espetaculo);
			Assert.assertEquals(listaSessao.get(i).getInicio(), inicio.toDateTime(horario));
		}
		
	}
	
	@Test
	public void deveCriarDuasSessoesQuandoDiferencaEntrePeriodosForIgualDuasSemanasEPeriodicidadeSemanal() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate().withDayOfMonth(1).withMonthOfYear(9).withYear(2013);
		LocalDate fim = new LocalDate().withDayOfMonth(14).withMonthOfYear(9).withYear(2013);
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.SEMANAL);

		assertTrue((listaSessao.size() == 2));
		for ( int i=0; i<2; i++ ) {
			Assert.assertSame(listaSessao.get(i).getEspetaculo(), espetaculo);
			Assert.assertEquals(listaSessao.get(i).getInicio(), inicio.toDateTime(horario));
		}
		
	}
	
	@Test
	public void naoDeveCriarSessoesSeDataFimForMaiorQueDataInicioEPeriodicidadeDiaria() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate().withDayOfMonth(2).withMonthOfYear(9).withYear(2013);
		LocalDate fim = new LocalDate().withDayOfMonth(1).withMonthOfYear(9).withYear(2013);
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.DIARIA);

		assertTrue((listaSessao.size() == 0));
		
	}	
	
	@Test
	public void naoDeveCriarSessoesSeDataFimForMaiorQueDataInicioEPeriodicidadeSemanal() {
	
		Espetaculo espetaculo = new Espetaculo();
	
		LocalDate inicio = new LocalDate().withDayOfMonth(2).withMonthOfYear(9).withYear(2013);
		LocalDate fim = new LocalDate().withDayOfMonth(1).withMonthOfYear(9).withYear(2013);
		LocalTime horario = new LocalTime();
		
		List<Sessao> listaSessao = espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.SEMANAL);

		assertTrue((listaSessao.size() == 0));
		
	}		
	
}
