package br.loop.db.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class TableJoin {

	@JsonUnwrapped
	private final Table table = new Table();
	
	private final Set<String> columns = new HashSet<>();

	public Table getTable() {
		return table;
	}

	public Set<String> getColumns() {
		return columns;
	}
	
	@Override
	public String toString() {
		return "TableJoin [table=" + table + ", columns=" + columns + "]";
	}

	

}
