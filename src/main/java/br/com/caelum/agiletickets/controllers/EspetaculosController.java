package br.com.caelum.agiletickets.controllers;

import static br.com.caelum.vraptor.view.Results.status;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.com.caelum.agiletickets.domain.Agenda;
import br.com.caelum.agiletickets.domain.DiretorioDeEstabelecimentos;
import br.com.caelum.agiletickets.models.Espetaculo;
import br.com.caelum.agiletickets.models.Periodicidade;
import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

import com.google.common.base.Strings;

@Resource
public class EspetaculosController {

	private NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

	private final Agenda agenda;
	private Validator validator;
	private Result result;

	private final DiretorioDeEstabelecimentos estabelecimentos;

	public EspetaculosController(Agenda agenda,
			DiretorioDeEstabelecimentos estabelecimentos, Validator validator,
			Result result) {
		this.agenda = agenda;
		this.estabelecimentos = estabelecimentos;
		this.validator = validator;
		this.result = result;
	}

	@Get
	@Path("/espetaculos")
	public List<Espetaculo> lista() {
		// inclui a lista de estabelecimentos
		result.include("estabelecimentos", estabelecimentos.todos());
		return agenda.espetaculos();
	}

	@Post
	@Path("/espetaculos")
	public void adiciona(Espetaculo espetaculo) {
		this.validarEspetaculo(espetaculo);
		agenda.cadastra(espetaculo);
		result.redirectTo(this).lista();
	}

	private void validarEspetaculo(Espetaculo espetaculo) {
		// aqui eh onde fazemos as varias validacoes
		// se nao tiver nome, avisa o usuario
		// se nao tiver descricao, avisa o usuario
		if (Strings.isNullOrEmpty(espetaculo.getNome())) {
			validator.add(new ValidationMessage(
					"Nome do espetáculo nao pode estar em branco", ""));
		}
		if (Strings.isNullOrEmpty(espetaculo.getDescricao())) {
			validator.add(new ValidationMessage(
					"Descricao do espetaculo nao pode estar em branco", ""));
		}
		validator.onErrorRedirectTo(this).lista();
	}

	@Get
	@Path("/sessao/{id}")
	public void sessao(Long id) {
		Sessao sessao = agenda.sessao(id);
		if (sessao == null) {
			result.notFound();
		}

		result.include("sessao", sessao);
	}

	@Post
	@Path("/sessao/{sessaoId}/reserva")
	public void reserva(Long sessaoId, final Integer quantidade, Integer tipoDesconto) {

		Sessao sessao = agenda.sessao(sessaoId);
		if (sessao == null) {
			result.notFound();
			return;
		}

		this.validarReserva(sessao, quantidade);

		sessao.reserva(quantidade);

		BigDecimal precoTotal = sessao.getPreco().multiply(BigDecimal.valueOf(quantidade));
		
		Double valorDesconto = 0.0;
		if ( tipoDesconto == 1 ) {
			// estudante
			valorDesconto = precoTotal.multiply(new BigDecimal(0.5)).doubleValue();
		} else if ( tipoDesconto == 2 ) {
			// idoso
			valorDesconto = precoTotal.multiply(new BigDecimal(0.6)).doubleValue();
		}

		precoTotal = precoTotal.subtract(new BigDecimal(valorDesconto));
		
		result.include("message", "Sessao reservada com sucesso por " + CURRENCY.format(precoTotal) + " com o desconto de: " + CURRENCY.format(valorDesconto));

		result.redirectTo(IndexController.class).index();
		
	}

	private void validarReserva(Sessao sessao, Integer quantidade) {
	
		if (quantidade == null) {
			validator.add(new ValidationMessage("Informe a quantidade!", ""));
		}
		
		if (quantidade < 1) {
			validator.add(new ValidationMessage(
					"Voce deve escolher um lugar ou mais", ""));
		}

		if (!sessao.podeReservar(quantidade)) {
			validator.add(new ValidationMessage(
					"Nao existem ingressos dispon√≠veis", ""));
		}

		// em caso de erro, redireciona para a lista de sessao
		validator.onErrorRedirectTo(this).sessao(sessao.getId());		
		
	}
	
	@Get
	@Path("/espetaculo/{espetaculoId}/sessoes")
	public void sessoes(Long espetaculoId) {
		Espetaculo espetaculo = carregaEspetaculo(espetaculoId);
		result.include("espetaculo", espetaculo);
	}

	@Post
	@Path("/espetaculo/{espetaculoId}/sessoes")
	public void cadastraSessoes(Long espetaculoId, LocalDate inicio, LocalDate fim, LocalTime horario, Periodicidade periodicidade) {
		Espetaculo espetaculo = carregaEspetaculo(espetaculoId);

		// aqui faz a magica!
		// cria sessoes baseado no periodo de inicio e fim passados pelo usuario
		List<Sessao> sessoes = espetaculo.criaSessoes(inicio, fim, horario, periodicidade);

		agenda.agende(sessoes);

		result.include("message", sessoes.size() + " sessoes criadas com sucesso");
		result.redirectTo(this).lista();
	}

	private Espetaculo carregaEspetaculo(Long espetaculoId) {
		Espetaculo espetaculo = agenda.espetaculo(espetaculoId);
		if (espetaculo == null) {
			validator.add(new ValidationMessage("", ""));
		}
		validator.onErrorUse(status()).notFound();
		return espetaculo;
	}

}
