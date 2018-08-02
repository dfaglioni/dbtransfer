package br.loop.db.repository;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import br.loop.db.domain.TableJoin;
import br.loop.db.domain.TableKeyMover;

public class TableRepositoryTest {

	private TableRepository repository = new TableRepository();

	
	private String resourceToPathname(String resourcePath) {

		return new File(getClass().getClassLoader().getResource(resourcePath).getFile()).getAbsolutePath();

	}
	
	@Test(expected = FileNotFoundException.class)
	public void readTableKeyMoverFileNotExists() throws IOException {

		 repository
				.readListTableKeyMoverFromPathname("/any.txt");

		
	}

	@Test
	public void readTableKeyMover() throws IOException {

		List<TableKeyMover> listTables = repository
				.readListTableKeyMoverFromPathname(resourceToPathname("./tablekeymover-1.json"));

		assertThat(listTables, not(nullValue()));
		
		assertThat(listTables.size(), equalTo(1));
		
		TableKeyMover tableKeyMover = listTables.get(0);
		
		assertThat(tableKeyMover.getTable().getColumn(), equalTo("bairro"));
		assertThat(tableKeyMover.getTable().getTable(), equalTo("bairro"));
		assertThat(tableKeyMover.getSpace(), equalTo(100));
		
		assertThat(tableKeyMover.getTable().getDependencies().size(), equalTo(2));
		
		assertThat(tableKeyMover.getTable().getDependencies().get(0).getTable(), equalTo("cep"));
		assertThat(tableKeyMover.getTable().getDependencies().get(0).getColumn(), equalTo("bairro"));
		
	}
	
	@Test
	public void readTableJoin() throws IOException {
		
		List<TableJoin> listTables = repository
				.readListTableJoinFromPathname(resourceToPathname("./tablejoin-1.json"));

		assertThat(listTables, not(nullValue()));
		
		assertThat(listTables.size(), equalTo(1));
		
		assertThat(listTables.get(0).getColumns(), hasItems("cidade","descricao"));
		
	}


}
