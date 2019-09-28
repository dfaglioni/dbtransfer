package br.loop.db.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		return "Table [table=" + table + ", column=" + column + ", " + "dependencies="
				+ dependencies.stream().map(d -> d.getTable()).collect(Collectors.toList()) + "]";
	}

	public List<Table> getDependencies() {
		return dependencies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	public void validate() {

		if (getDependencies().contains(this)) {
			
		
			throw new IllegalStateException("Same table on dependencies: "+ this.getTable());
		}

		Map<Table, Long> tablesCounts =
                getDependencies().stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );
		
		tablesCounts.entrySet().forEach( e -> {
			
			if ( e.getValue() > 1) {
				
				
				throw new IllegalStateException("Repeated table on dependencies:" + e.getKey().getTable());

			}
			
			
		});
		
	}

}
