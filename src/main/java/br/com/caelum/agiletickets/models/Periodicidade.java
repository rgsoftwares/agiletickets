package br.com.caelum.agiletickets.models;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;


public enum Periodicidade {
	
	DIARIA {
		public CalculoPeriodoDTO calcularPeriodo(LocalDate dataInicio, LocalDate dataFim) {
			
			int qtdePeriodo = Days.daysBetween(dataInicio, dataFim).getDays();
			int qtdePeriodoIncremento = 1;
			
			CalculoPeriodoDTO calculoPeriodoDTO = new CalculoPeriodoDTO();
			calculoPeriodoDTO.setQtdePeriodo(qtdePeriodo);
			calculoPeriodoDTO.setQtdePeriodoIncremento(qtdePeriodoIncremento);
			
			return calculoPeriodoDTO;
			
		}
	},
	
	SEMANAL {
		public CalculoPeriodoDTO calcularPeriodo(LocalDate dataInicio, LocalDate dataFim) {
			
			int qtdePeriodo = Weeks.weeksBetween(dataInicio, dataFim).getWeeks() + 1;
			int qtdePeriodoIncremento = 7;
			
			CalculoPeriodoDTO calculoPeriodoDTO = new CalculoPeriodoDTO();
			calculoPeriodoDTO.setQtdePeriodo(qtdePeriodo);
			calculoPeriodoDTO.setQtdePeriodoIncremento(qtdePeriodoIncremento);
			
			return calculoPeriodoDTO;			
		}
		
	};
		
	public abstract CalculoPeriodoDTO calcularPeriodo(LocalDate dataInicio, LocalDate dataFim);
	
}
