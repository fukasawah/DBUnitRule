package ryzzy.dbunitrule.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

public class StringUtilTest {

	@RunWith(Theories.class)
	public static class GetExtensionTest
	{
		private static class Fixture
		{
			final String actualString;
			final String expected;

			public Fixture(String actualString, String expected)
			{
				this.actualString = actualString;
				this.expected = expected;
			}
		}

		@DataPoints
		public static Fixture[] fixture = {
				new Fixture(".xml", "xml"),
				new Fixture("a.out", "out"),
				new Fixture("xyz.zyx.bat", "bat"),
				new Fixture("xyz.zyx.", ""),
		};

		@Theory
		public void getExtensionTest(Fixture fix)
		{
			assertThat(StringUtil.getExtention(fix.actualString), is(fix.expected));
		}

		@Rule
		public ExpectedException expectedExceptionRule = ExpectedException.none();

		@Test
		public void illegalTest()
		{
			expectedExceptionRule.expect(IllegalArgumentException.class);
			StringUtil.getExtention("NO_EXTENSION_NAME");
		}
	}

	@RunWith(Theories.class)
	public static class WildcardMatchTest
	{

		private static class Fixture
		{
			final String actualString;
			final String actualPattern;
			final boolean expected;

			public Fixture(String actualString, String actualPattern, boolean expected)
			{
				this.actualString = actualString;
				this.actualPattern = actualPattern;
				this.expected = expected;
			}
		}

		@DataPoints
		public static Fixture[] fixture = {
				/* success */
				new Fixture("a", "a", true),
				new Fixture("a", "*", true),
				new Fixture("a", "*a", true),
				new Fixture("a", "a*", true),
				new Fixture("aa", "aa", true),
				new Fixture("aa", "*", true),
				new Fixture("aa", "*a", true),
				new Fixture("aa", "a*", true),
				new Fixture("aa", "*a*", true),
				new Fixture("abc", "a*c", true),
				new Fixture("abc", "*c", true),
				new Fixture("abc", "a*", true),
				new Fixture("abc", "*b*", true),
				new Fixture("abc", "*", true),
				new Fixture("abc", "a**b***************c", true),
				/* failure */
				new Fixture("a", "b", false),
				new Fixture("ab", "ba", false),
				new Fixture("abc", "a*b", false),
				new Fixture("abc", "b*c", false),
				new Fixture("abc", "*b*a*", false),
				new Fixture("abc", "b*", false),
				new Fixture("abc", "*b", false),
		};

		@Theory
		public void wildcardMatchTest(Fixture fix)
		{
			assertThat(StringUtil.wildcardMatch(fix.actualString, fix.actualPattern), is(fix.expected));
		}
	}
}
