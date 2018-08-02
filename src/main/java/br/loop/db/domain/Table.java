package br.loop.db.domain;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String table;

	private String column;
	
	private final List<Table> dependencies = new ArrayList<>();

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}


	
	@Override
	public String toString() {
		return "Table [table=" + table + ", column=" + column + ", dependencies=" + dependencies + "]";
	}

	public List<Table> getDependencies() {
		return dependencies;
	}
	

}
