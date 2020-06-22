package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable <Evento> {
	
	public enum TipoEvento {
		CRIMINE,
		ARRIVA_AGENTE,
		GESTITO
	}
	
	private TipoEvento type;
	private LocalDateTime data; 
	private Event crimine;
	/**
	 * @param type
	 * @param data
	 * @param crimine
	 */
	public Evento(TipoEvento type, LocalDateTime data, Event crimine) {
		super();
		this.type = type;
		this.data = data;
		this.crimine = crimine;
	}
	public TipoEvento getType() {
		return type;
	}
	public void setType(TipoEvento type) {
		this.type = type;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public Event getCrimine() {
		return crimine;
	}
	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}
	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.data.compareTo(o.data);
	}
	
	
	

}
