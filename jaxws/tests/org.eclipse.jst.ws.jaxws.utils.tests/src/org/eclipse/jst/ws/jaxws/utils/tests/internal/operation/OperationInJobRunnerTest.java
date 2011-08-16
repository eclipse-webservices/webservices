/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.tests.internal.operation;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.utils.operation.OperationInJobRunner;

public class OperationInJobRunnerTest extends TestCase
{
	private static final String TEST_JOB_NAME = "OperationInJobRunnerTestJob" + System.currentTimeMillis();
	private OperationInJobRunner opRunner;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		opRunner = new OperationInJobRunner(TEST_JOB_NAME, null);
	}

	public void testOperationExecutedSuccessfully()
	{
		runOperation(new TestOperation(), IStatus.OK);
	}

	public void testOperationInterrupted()
	{
		runOperation(interruptedOperation(), IStatus.CANCEL);
	}

	public void testOperationFailed()
	{
		runOperation(failingOperation(), IStatus.ERROR);
	}

	private void runOperation(final TestOperation op, final int expectedExecutionStatusSeverity)
	{
		final IStatus status = runOperation(op);
		assertOperationExecuted(op);
		assertEquals("Unexpected job status severity", expectedExecutionStatusSeverity, status.getSeverity());
	}

	private TestOperation interruptedOperation()
	{
		return new TestOperation()
		{
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				super.run(monitor);
				throw new InterruptedException();
			}
		};
	}

	private TestOperation failingOperation()
	{
		return new TestOperation()
		{
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				super.run(monitor);
				throw new InvocationTargetException(new Exception());
			}
		};
	}

	private void assertOperationExecuted(final TestOperation op)
	{
		assertTrue("Operation not executed", op.isExecuted);
	}

	private IStatus runOperation(final IRunnableWithProgress runnable)
	{
		final IStatus[] jobStatus = new IStatus[1];
		final IJobChangeListener jobListener = new TestJobChangeListener(jobStatus);
		Job.getJobManager().addJobChangeListener(jobListener);
		try
		{
			opRunner.run(runnable);
			waitForStatus(jobStatus);
		}
		finally
		{
			Job.getJobManager().removeJobChangeListener(jobListener);
		}
		return jobStatus[0];
	}

	private void waitForStatus(final IStatus[] statusHolder)
	{
		Assertions.waitAssert(new IWaitCondition()
		{

			public boolean checkCondition() throws ConditionCheckException
			{
				return statusHolder[0] != null;
			}
		}, "test job did not finish");
	}

	private class TestOperation implements IRunnableWithProgress
	{
		public boolean isExecuted = false;

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
		{
			isExecuted = true;
		}
	}

	private class TestJobChangeListener extends JobChangeAdapter
	{
		private IStatus[] statusHolder;

		public TestJobChangeListener(final IStatus[] statusHolder)
		{
			this.statusHolder = statusHolder;
		}

		@Override
		public void done(IJobChangeEvent event)
		{
			if (isTheTestJob(event))
			{
				statusHolder[0] = event.getResult();
			}
		}

		private boolean isTheTestJob(final IJobChangeEvent event)
		{
			return event.getJob().getName() == TEST_JOB_NAME;
		}
	}
}
