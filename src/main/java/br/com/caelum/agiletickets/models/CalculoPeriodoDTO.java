package br.com.caelum.agiletickets.models;

import java.io.Serializable;

public class CalculoPeriodoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int qtdePeriodo;
	private int qtdePeriodoIncremento;
	public int getQtdePeriodo() {
		return qtdePeriodo;
	}
	public void setQtdePeriodo(int qtdePeriodo) {
		this.qtdePeriodo = qtdePeriodo;
	}
	public int getQtdePeriodoIncremento() {
		return qtdePeriodoIncremento;
	}
	public void setQtdePeriodoIncremento(int qtdePeriodoIncremento) {
		this.qtdePeriodoIncremento = qtdePeriodoIncremento;
	}
	
	
	
}
