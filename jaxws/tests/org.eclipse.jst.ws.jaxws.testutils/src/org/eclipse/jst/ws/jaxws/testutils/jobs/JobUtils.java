/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.jobs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.testutils.threading.TestContext;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class JobUtils
{
	@SuppressWarnings("deprecation")
	public static void waitForJobsInterruptable() throws InterruptedException
	{
		while (Platform.getJobManager().currentJob() != null)
		{
			delay(5);
		}
	}

	public static void waitForJobs() throws JavaModelException
	{
		waitForDummyJob(Job.INTERACTIVE);
		
		while (true)
		{
			try
			{
				waitForJobsInterruptable();
				break;
			} catch (InterruptedException ie)
			{

			}
		}

		// i036509 added couse waiting for jobs is not enought
		waitForIndexer();
		
		// Force notifications
		waitForNotifications();
	}

	public static void delay(final long millis) throws InterruptedException
	{
		Display display = Display.getCurrent();
		// if this is the UI thread, then process the input
		if (display != null)
		{
			final long entTimeMillis = System.currentTimeMillis() + millis;
			while (System.currentTimeMillis() < entTimeMillis)
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}
			}
			display.update();
		} else
		{
			Thread.sleep(1000);
		}
	}
	
	public static void waitForIndexer() throws JavaModelException
	{
		new SearchEngine().searchAllTypeNames(null,SearchPattern.R_EXACT_MATCH, null, 
				SearchPattern.R_CASE_SENSITIVE, IJavaSearchConstants.CLASS, SearchEngine
																		.createJavaSearchScope(new IJavaElement[0]), new TypeNameRequestor()
										{
											@SuppressWarnings("unused")
											public void acceptClass(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
																			String path)
											{
											}
											@SuppressWarnings("unused")
											public void acceptInterface(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
																			String path)
											{
											}
										}, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
	}
	
	/**
	 * Schedule a dummy job with the priority specified and wait for it is done
	 */
	private static void waitForDummyJob(final int priority)
	{
		try
		{
			waitForDummyJobWithPrio(priority);
		}
		catch(InterruptedException e)
		{
			
		}
	}
	
	private static void waitForDummyJobWithPrio(final int prio) throws InterruptedException
	{
		Job myDummyJob = new Job("DUMMY"){

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				return Status.OK_STATUS;
			}};
			
		myDummyJob.setPriority(prio);
		myDummyJob.setUser(false);
		myDummyJob.schedule();
		
		myDummyJob.join();
	}
	
	
	/**
	 * Forces delivering of notification events. The implementation of the method is "inspired" by NotificationManager's notification job
	 * @throws InterruptedException 
	 */
	private static void waitForNotifications()
	{
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRunnable dummyRunnable = new IWorkspaceRunnable(){
			public void run(IProgressMonitor monitor) throws CoreException
			{
			}
		};
		
		final Job notifForcingJob = new Job("Delivering notifications..."){

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					workspace.run(dummyRunnable, null, IWorkspace.AVOID_UPDATE, null);
				} catch (CoreException e)
				{
					logger().logDebug( e.getMessage(), e );
				}
				return Status.OK_STATUS;
			}};
		
		syncExecJob(notifForcingJob, PlatformUI.getWorkbench().getDisplay());
	}
	
	public static void waitForJobsSlow() throws JavaModelException
	{
		waitForJobs();
		waitForDummyJob(Job.DECORATE);
		
		waitForEventLoop();
	}

	private static void waitForEventLoop()
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();
		
		final Runnable runnable = new Runnable(){

			public void run()
			{
				// Let the display process the events in its queue
				while(display.readAndDispatch()){};
			}};
		
		if(Display.getCurrent() != null)
		{
			runnable.run();
		}
		else
		{
			display.syncExec(runnable);
		}
			
	}

	/**
	 * Executes synchronously (schedules and joins) the job specified.<br>
	 * If the method is called from the UI thread, the job is scheduled and joined in a modal context thread thus letting the event loop process asynchronous events<br>
	 * If the method is called from a worker thread, the job is scheduled and joined in the caller thread.
	 * 
	 * @param job
	 * @param display
	 */
	private static void syncExecJob(final Job job, final Display display)
	{
		final IRunnableWithProgress runnable = new IRunnableWithProgress()
		{

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				job.schedule();
				try
				{
					job.join();
				} catch (InterruptedException e)
				{
					logger().logDebug(e.getMessage(), e);
				}
			}
		};
		try
		{
			TestContext.run(runnable, Display.getCurrent() != null, new NullProgressMonitor(), display);
		} catch (InvocationTargetException e)
		{
			logger().logDebug(e.getMessage(), e);
		} catch (InterruptedException e)
		{
			logger().logDebug(e.getMessage(), e);
		}
	}
	
	private static ILogger logger()
	{
		return new Logger();
	}
}
