package ryzzy.dbunitrule.matcher;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public abstract class VerifyMatcher<T> extends TypeSafeMatcher<T>
{
	private final T expectedObject;
	private T actualObject;
	private Throwable exception;

	public VerifyMatcher(T expectedObject)
	{
		this.expectedObject = expectedObject;
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendValue(expectedObject);
		if (exception == null)
		{
			return;
		}
		description.appendText("\n");
		description.appendText("  >>> ");
		description.appendText(exception.getMessage());
	}

	@Override
	public boolean matchesSafely(T actualObject)
	{
		try
		{
			this.actualObject = actualObject;
			verify();
		} catch (AssertionError e)
		{
			throw e;
		} catch (Exception e)
		{
			exception = e;
			return false;
		}
		return true;
	};

	public Throwable getException() {
		return exception;
	}

	public T getExpectedObject()
	{
		return expectedObject;
	}

	public T getActualObject()
	{
		return actualObject;
	}

	protected abstract void verify() throws Exception;
}
