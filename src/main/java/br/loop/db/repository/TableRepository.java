package br.loop.db.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.loop.db.domain.TableJoin;
import br.loop.db.domain.TableKeyMover;

public class TableRepository {

	private ObjectMapper mapper = new ObjectMapper();
	
	private <T> List<T> readListFromPathname(String pathname, TypeReference<List<T>> reference) throws IOException {
		
		File file = new File(pathname);
		
		if ( !file.exists()) {
			
			throw new FileNotFoundException(pathname);
		}
	
		
		String content = FileUtils.readFileToString(file, Charset.defaultCharset());
		
	    return mapper.readValue(content, reference );
		
	}
	

	public List<TableKeyMover> readListTableKeyMoverFromPathname(String pathname) throws IOException {
		
		return readListFromPathname(pathname, new TypeReference<List<TableKeyMover>>(){});	
		
	}
	
	public List<TableJoin> readListTableJoinFromPathname(String pathname) throws IOException {
		
		return readListFromPathname(pathname, new TypeReference<List<TableJoin>>(){});
	}
		

}
