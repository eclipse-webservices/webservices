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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.load;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.utils.operation.IOperationRunner;

/**
 * Implementation of the {@link IWsDomObtainer} interface which performs {@link IDOM} load if necessary. The implementation performs the load
 * operation via the {@link IOperationRunner} supplied in the contructor
 * 
 * @author Danail Branekov
 */
public class WsDomObtainer implements IWsDomObtainer
{
	private final IWsDOMRuntimeExtension domRuntime;
	private final IOperationRunner opRunner;

	public WsDomObtainer(final IWsDOMRuntimeExtension domRuntime, final IOperationRunner opRunner)
	{
		this.domRuntime = domRuntime;
		this.opRunner = opRunner;
	}

	public void getDom(final IWsDomCallback loadCallback)
	{
		if (isDomAlreadyLoaded())
		{
			loadCallback.dom(getDomAlreadyLoaded());
			return;
		}

		opRunner.run(loadRunnable(loadCallback));
	}

	private IRunnableWithProgress loadRunnable(final IWsDomCallback loadCallback)
	{
		return new LoadDomRunnable(loadCallback);
	}

	private IDOM getDomAlreadyLoaded()
	{
		try
		{
			return domRuntime.getDOM();
		}
		catch (WsDOMLoadCanceledException e)
		{
			throw new IllegalStateException(e);
		}
	}

	private boolean isDomAlreadyLoaded()
	{
		try
		{
			return domRuntime.getDOM() != null;
		}
		catch (WsDOMLoadCanceledException e)
		{
			return false;
		}
	}

	private final class LoadDomRunnable implements IRunnableWithProgress
	{
		private final IWsDomCallback loadCallback;

		private LoadDomRunnable(IWsDomCallback loadCallback)
		{
			this.loadCallback = loadCallback;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
		{
			if (isDomAlreadyLoaded())
			{
				loadCallback.dom(getDomAlreadyLoaded());
				return;
			}

			try
			{
				loadCallback.domLoadStarting();
				loadCallback.dom(performLoad(monitor));
			}
			catch (WsDOMLoadCanceledException e)
			{
				cancelled(e);
			}
			catch (IOException e)
			{
				failed(e);
			}
		}

		private IDOM performLoad(final IProgressMonitor monitor) throws IOException, WsDOMLoadCanceledException
		{
			try
			{
				domRuntime.createDOM(monitor);
			}
			catch (WsDOMLoadCanceledException e)
			{
				throw e;
			}
			return domRuntime.getDOM();
		}
		
		private void failed(IOException e) throws InvocationTargetException
		{
			loadCallback.domLoadFailed();
			throw new InvocationTargetException(e);
		}

		private void cancelled(WsDOMLoadCanceledException e) throws InterruptedException
		{
			loadCallback.domLoadCancelled();
			throw new InterruptedException(e.getMessage());
		}
	}
}
