package br.loop.db.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class TableKeyMover {

	@JsonUnwrapped
	private final Table table = new Table();

	private Integer space;
	
	private Boolean ignoreMax = false;

	public Table getTable() {
		return table;
	}

	@Override
	public String toString() {
		return "\nTableKeyMover [table=" + table + ", space=" + space + "]";
	}

	public Integer getSpace() {
		return space;
	}

	public void setSpace(Integer space) {
		this.space = space;
	}

	public void validate() {
		

		table.validate();
		
	}

	public void setIgnoreMax(Boolean ignoreMax) {
		this.ignoreMax = ignoreMax;
	}
	
	public Boolean getIgnoreMax() {
		return ignoreMax;
	}
}
