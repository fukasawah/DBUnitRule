package ryzzy.dbunitrule.util;

public class StringUtil {
	public static String toLower(String str) {
		return str == null ? null : str.toLowerCase();
	}

	public static String getExtention(String filename)
	{
		int i = filename.lastIndexOf('.');
		if (i == -1)
		{
			throw new IllegalArgumentException("no extension. filename=" + filename);
		}
		return filename.substring(i + 1);
	}

	public static String getFilename(String filePath)
	{
		String tmp = filePath.replaceFirst("[\\\\\\/]+$", "");
		tmp = tmp.replaceFirst("^.+[\\\\\\/]", "");
		return tmp;
	}

	public static boolean wildcardMatch(String str, String match)
	{
		if (str == null || match == null) {
			return str == match;
		}
		return wildcardMatch(str, 0, match, 0);
	}

	private static boolean wildcardMatch(String str, int strptr, String match, int matchptr)
	{
		boolean x = strptr == str.length();
		boolean y = matchptr == match.length();
		if (y)
		{
			return x;
		}
		switch (match.charAt(matchptr))
		{
		case '*':
			return wildcardMatch(str, strptr, match, matchptr + 1) || !x && wildcardMatch(str, strptr + 1, match, matchptr);
		default:
			return !x && !y && str.charAt(strptr) == match.charAt(matchptr) && wildcardMatch(str, strptr + 1, match, matchptr + 1);
		}
	}

}
