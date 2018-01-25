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
package org.eclipse.jst.ws.jaxws.utils.operation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.utils.StatusUtils;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Implementation of the {@link IOperationRunner} interface which runs the operation in a job which the supplied scheduling rule
 * 
 * @author Danail Branekov
 */
public class OperationInJobRunner implements IOperationRunner
{
	private final String jobName;
	private final ISchedulingRule jobRule;
	private final ILogger logger;

	/**
	 * Constructor
	 * 
	 * @param jobName
	 *            the name of the job which performs the operation
	 * @param jobRule
	 *            the scheduling rule of the job
	 */
	public OperationInJobRunner(final String jobName, final ISchedulingRule jobRule)
	{
		this.jobName = jobName;
		this.jobRule = jobRule;
		this.logger = new Logger();
	}

	public void run(final IRunnableWithProgress runnable)
	{
		final Job job = new Job(jobName)
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					runnable.run(monitor);
				}
				catch (InvocationTargetException e)
				{
					logger.logError(e.getMessage(), e);
					return StatusUtils.statusError(e.getMessage(), e);
				}
				catch (InterruptedException e)
				{
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		job.setRule(this.jobRule);
		job.schedule();
	}
}
