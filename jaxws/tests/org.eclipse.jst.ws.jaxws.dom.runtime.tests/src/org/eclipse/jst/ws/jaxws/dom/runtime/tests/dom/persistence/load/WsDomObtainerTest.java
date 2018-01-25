package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.load;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.load.IWsDomCallback;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.load.WsDomObtainer;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.operation.IOperationRunner;
import org.jmock.core.Constraint;

public class WsDomObtainerTest extends MockObjectTestCase
{
	private static final String LOAD_CANCELLED_MSG = "Load_Cancelled";
	private IOperationRunner operationRunner;
	private WsDomObtainer domObtainer;
	private Mock<IWsDOMRuntimeExtension> domRuntime;
	private Mock<IDOM> dom;
	private Mock<IWsDomCallback> domCallback;
	private Mock<IExceptionHandler> excHandler;

	private interface IExceptionHandler
	{
		public void handleException(final Exception e);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		setupOperationRunner();
		domRuntime = mock(IWsDOMRuntimeExtension.class);
		dom = mock(IDOM.class);
		domObtainer = new WsDomObtainer(domRuntime.proxy(), operationRunner);
		domCallback = mock(IWsDomCallback.class);
	}

	private void setupOperationRunner()
	{
		excHandler = mock(IExceptionHandler.class);
		operationRunner = new IOperationRunner()
		{
			public void run(IRunnableWithProgress runnable)
			{
				try
				{
					runnable.run(new NullProgressMonitor());
				}
				catch (InvocationTargetException e)
				{
					excHandler.proxy().handleException(e);
				}
				catch (InterruptedException e)
				{
					excHandler.proxy().handleException(e);
				}
			}
		};
	}

	public void testDomNotLoaded()
	{
		expectDomLoad();
		performLoad();
	}

	public void testDomAlreadyLoaded()
	{
		expectDomAlreadyLoaded();
		performLoad();
	}

	public void testCancelDomLoad()
	{
		expectCancelWhileLoading();
		performLoad();
	}

	public void testDomLoadPreviouslyCancelled()
	{
		expectLoadCancelledPreviously();
		performLoad();
	}

	public void testDomLoadFails()
	{
		expectLoadFails();
		performLoad();
	}
	
	public void testDomLoadedPriorLoadOperationStarts()
	{
		expectDomLoadedPriorLoadOperation();
		performLoad();
	}

	private void expectDomLoadedPriorLoadOperation()
	{
		domRuntime.expects(once()).method("getDOM").will(returnValue(null)).id("first_get");
		domRuntime.stubs().method("getDOM").after("first_get").will(returnValue(dom.proxy()));
		
		domCallback.expects(once()).method("dom").with(same(dom.proxy()));
	}

	private void expectLoadFails()
	{
		final IOException ioExc = new IOException("dom load failure");
		domRuntime.stubs().method("getDOM").will(returnValue(null));
		domRuntime.expects(once()).method("createDOM").with(isA(IProgressMonitor.class)).after("getDOM").will(throwException(ioExc));

		domCallback.expects(once()).method("domLoadStarting").withNoArguments();
		domCallback.expects(once()).method("domLoadFailed").after("domLoadStarting");

		excHandler.expects(once()).method("handleException").with(invTargetExcMatcher(ioExc));

	}

	private Constraint invTargetExcMatcher(final Exception cause)
	{
		return new Constraint()
		{

			public StringBuffer describeTo(StringBuffer buffer)
			{
				buffer.append("unexpected exception");
				return buffer;
			}

			public boolean eval(Object o)
			{
				if (!(o instanceof InvocationTargetException))
				{
					return false;
				}
				final InvocationTargetException invTargetE = (InvocationTargetException) o;
				return invTargetE.getCause() == cause;
			}
		};
	}

	private void expectLoadCancelledPreviously()
	{
		domRuntime.stubs().method("getDOM").will(throwException(loadCancelledExc())).id("first_getDOM");
		domRuntime.expects(once()).method("createDOM").with(isA(IProgressMonitor.class)).after("first_getDOM").isVoid();
		domRuntime.stubs().method("getDOM").after("createDOM").will(returnValue(dom.proxy()));

		domCallback.expects(once()).method("domLoadStarting").withNoArguments();
		domCallback.expects(once()).method("dom").with(same(dom.proxy())).after("domLoadStarting");
	}

	private void expectCancelWhileLoading()
	{
		domRuntime.stubs().method("getDOM").will(returnValue(null));
		domRuntime.expects(once()).method("createDOM").with(isA(IProgressMonitor.class)).after("getDOM").will(throwException(loadCancelledExc()));

		domCallback.expects(once()).method("domLoadStarting").withNoArguments();
		domCallback.expects(once()).method("domLoadCancelled");

		excHandler.expects(once()).method("handleException").with(isA(InterruptedException.class));
	}

	private void expectDomLoad()
	{
		domRuntime.stubs().method("getDOM").will(returnValue(null));
		domRuntime.expects(once()).method("createDOM").with(isA(IProgressMonitor.class)).after("getDOM").isVoid();
		domRuntime.stubs().method("getDOM").after("createDOM").will(returnValue(dom.proxy()));

		domCallback.expects(once()).method("domLoadStarting").withNoArguments();
		domCallback.expects(once()).method("dom").with(same(dom.proxy())).after("domLoadStarting");
	}

	private void expectDomAlreadyLoaded()
	{
		domRuntime.stubs().method("getDOM").will(returnValue(dom.proxy()));
		domCallback.expects(once()).method("dom").with(same(dom.proxy()));
	}

	private void performLoad()
	{
		domObtainer.getDom(domCallback.proxy());
	}

	private WsDOMLoadCanceledException loadCancelledExc()
	{
		return new WsDOMLoadCanceledException(LOAD_CANCELLED_MSG, LOAD_CANCELLED_MSG);
	}
}
