package br.loop.string.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import com.google.common.base.CharMatcher;

public class StringUtils {

	private static CharMatcher desired = CharMatcher.ascii()
			  .or(CharMatcher.anyOf("ÁáÂâÀàÅåÃãÄäÆæ;ÉéÊêÈèËëÐðÍíÎîÌìÏïÓóÔôÒòØøÕõÖöÚúÛûÙùÜüÇçÑñ<>&\"®©ÝýÞþßM²M³m²m³"))
			  .precomputed(); // optional, may improve performance, YMMV

	
	
	public static String removeInvalidUTF8(String str) throws CharacterCodingException, UnsupportedEncodingException {

		return desired.retainFrom(str);
	}
}
