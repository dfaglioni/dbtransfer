package br.loop.string.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void removeInvalidUTF8() throws Exception {

		String str = "%$@#This will be my ã ção á Ẽ Ç/+-= first time visiting Seattle. ߘ";

		assertEquals("%$@#This will be my ã ção á  Ç/+-= first time visiting Seattle. ",
				StringUtils.removeInvalidUTF8(str));

		
	}

	
	@Test
	public void allAlpha() throws Exception {

		String str = "ÁáÂâÀàÅåÃãÄäÆæ;ÉéÊêÈèËëÐðÍíÎîÌìÏïÓóÔôÒòØøÕõÖöÚúÛûÙùÜüÇçÑñ<>&\"®©ÝýÞþßM²M³m²m³";

		assertEquals("ÁáÂâÀàÅåÃãÄäÆæ;ÉéÊêÈèËëÐðÍíÎîÌìÏïÓóÔôÒòØøÕõÖöÚúÛûÙùÜüÇçÑñ<>&\"®©ÝýÞþßM²M³m²m³",
				StringUtils.removeInvalidUTF8(str));

	}
}
