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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.SerializerAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.InitialModelSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.OnEventModelSynchronizer;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.facets.FacetUtils;
import org.eclipse.jst.ws.jaxws.utils.facets.IFacetUtils;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Represents the resource for JAX-WS web service DOM model. This class is responsible for loading the 
 * model. It is capable to use {@link IProgressMonitor} to track progress and cancel requests.
 * 
 * @author 
 */
public class JaxWsWorkspaceResource extends NonStreamResource
{
	private final IJavaModel javaModel;
	private final ServiceModelData serviceData;
	private final OnEventModelSynchronizer modelSynchronizer;
	private final SerializerAdapterFactory serializerFactory; 

	private IProgressMonitor monitor;
	
	private boolean saveEnabled;
	private boolean loadCnaceled;

	/**
	 * Constructor
	 * @param javaModel
	 */
	public JaxWsWorkspaceResource(final IJavaModel javaModel)
	{
		this.javaModel = javaModel;
		serviceData = new ServiceModelData();
		modelSynchronizer = new OnEventModelSynchronizer(this, serviceData);
		saveEnabled = true;
		serializerFactory = new SerializerAdapterFactory(this);
	}

	/**
	 * Set progress monitor in case the processing progress should be tracked and
	 * also in case it is expected that the processing can be canceled 
	 * @param pm the monitor - can be <code>null</code>
	 */
	public void setProgressMonitor(IProgressMonitor pm)
	{
		this.monitor = pm;
	}

	/**
	 * @return the loaded DOM in case it is successfully loaded. This method will return <code>null</code>
	 * in case loading has not been started at all or in case loading has been canceled. To check if the
	 * loading has been canceled call isLoadingCanceled().
	 */
	public IDOM getDOM()
	{
		if (getContents().size() == 0) {
			return null;
		}
		
		return (IDOM) getContents().get(0);
	}

	/**
	 * Adds listener for changes to Java elements in order to keep the model in sync
	 * with these changes
	 */
	public void startSynchronizing()
	{
		JavaCore.addElementChangedListener(modelSynchronizer, ElementChangedEvent.POST_RECONCILE | ElementChangedEvent.POST_CHANGE);
	}

	/**
	 * Removes listener for changes to Java elements. Keep in mind that after calling
	 * this method changes in java elements will not be reflected in DOM model
	 */
	public void stopSynchronizing()
	{
		JavaCore.removeElementChangedListener(modelSynchronizer);
	}

	/**
	 * @return the logger to use when some event needs to be logged
	 */
	public ILogger logger()
	{
		return new Logger();
	}
	
	/**
	 * @return the java model
	 */
	public IJavaModel javaModel()
	{
		return javaModel;
	}	

	@Override
	protected void doLoad(final Map<?, ?> options) throws IOException
	{
		loadCnaceled = false;
		getContents().clear();

		final IWorkspaceRunnable runnable = new IWorkspaceRunnable()
		{
			public void run(final IProgressMonitor subMonitor) throws CoreException
			{
				try 
				{		
					new InitialModelSynchronizer(JaxWsWorkspaceResource.this, serviceData).load(options, subMonitor);
				} 
				catch (WsDOMLoadCanceledException e) {
					loadCnaceled = true;
					logger().logError("JAX WS Web Services DOM loading has been canceled by the user, some JAX WS Web Services functionalities won't be available", e); //$NON-NLS-1$
				}
			}
		};
		
		try {
			javaModel().getWorkspace().run(runnable, monitor);
		} 
		catch (CoreException ce) {
			throw new IOWrappedException(ce);
		}
	}
	
	@Override
	protected void doSave(Map<?, ?> options) throws IOException	{
		// no processing needed
	}

	protected IElementChangedListener getSynchronizingListener() {
		return modelSynchronizer;
	}
	
	/**
	 * Creates compilation unit finder that crawls through javaModel and calls 
	 * compilation unit handler to process available CU's 
	 * @param jModel
	 * @param projectSelectors
	 * @return non <code>null</code> {@link ICompilationUnitFinder} instance 
	 */
	public ICompilationUnitFinder newCompilationUnitFinder(final IJavaModel jModel, final IProjectSelector[] projectSelectors)
	{
		return new WorkspaceCUFinder(jModel, projectSelectors);
	}

	/**
	 * Approves that <code>prj</code> is JAX-WS web service enabled 
	 * @param prj
	 * @return <code>true</code> in case this project might contain JAX-WS web services
	 */
	public boolean approveProject(final IJavaProject prj)
	{
		final IFacetUtils facetUtils = new FacetUtils();
		try {
			return facetUtils.hasFacetWithVersion(prj.getProject(), FacetUtils.EJB_30_FACET_VERSION, FacetUtils.EJB_30_FACET_ID, true)
				|| facetUtils.hasFacetWithVersion(prj.getProject(), FacetUtils.WEB_25_FACET_VERSION, FacetUtils.WEB_25_FACET_ID, true);
		} catch (CoreException ce)
		{
			logger().logError("Unable to read facet on project " + prj.getElementName() + ". Any Web Service elements in this project will not be shown in the navigation tree", ce); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
	}
	
	/**
	 * Creates new instance of {@link IAnnotationInspector} for <code>type</code>
	 * @param type
	 * @return non <code>null</code> inspector
	 */
	public IAnnotationInspector newAnnotationInspector(final IType type)
	{
		return AnnotationFactory.createAnnotationInspector(type);
	}

	/**
	 * @return non empty array of projects selectors responsible for filtering processed projects 
	 */
	public IProjectSelector[] getProjectSelectors()
	{
		return new IProjectSelector[] { new IProjectSelector()
		{
			public boolean approve(IJavaProject prj)
			{
				return approveProject(prj);
			}
		} };
	}

	/**
	 * @return the serializer factory used to adapt DOM objects with serializer adapters  
	 */
	public SerializerAdapterFactory getSerializerFactory() {
		return serializerFactory;
	}

	/**
	 * @return <code>true</code> if the saving is enabled
	 */
	public boolean isSaveEnabled() {
		return saveEnabled;
	}

	/**
	 * call this method to enable saving of model objects to underlying resources
	 */
	synchronized public void enableSaving() {
		this.saveEnabled = true;
	}
	
	/**
	 * call this method to disable saving of model objects to underlying resources
	 */
	synchronized public void disableSaving() {
		this.saveEnabled = false;
	}
	
	/**
	 * @return <code>true</code> in case the load of DOM model has been canceled
	 */
	public boolean isLoadCnaceled() {
		return loadCnaceled;
	}
	
	/**
	 * Holds model data helpful in model processing
	 */
	public class ServiceModelData
	{
		private Map<IWebService, String> ws2sei = new HashMap<IWebService, String>();

		private Map<String, Collection<IWebService>> sei2ws = new HashMap<String, Collection<IWebService>>();

		private Map<String, Collection<ICompilationUnit>> type2inherited = new HashMap<String, Collection<ICompilationUnit>>();

		public void map(IWebService ws, String seiImpl)
		{
			ws2sei.put(ws, seiImpl);
			getImplementingWs(seiImpl).add(ws);
		}

		String getSeiImpl(IWebService ws)
		{
			return ws2sei.get(ws);
		}

		public Collection<IWebService> getImplementingWs(String seiIml)
		{
			Collection<IWebService> wss = sei2ws.get(seiIml);
			if (wss == null)
			{
				wss = new ArrayList<IWebService>();
				sei2ws.put(seiIml, wss);
			}
			return wss;
		}

		public void unmap(IWebService ws)
		{
			final String sei = ws2sei.remove(ws);
			final Collection<IWebService> implementingWebServices = getImplementingWs(sei);
			implementingWebServices.remove(ws);
			if (implementingWebServices.size() == 0)
			{
				sei2ws.remove(sei);
			}
		}

		public void recordHierarchy(String fqName, Collection<ICompilationUnit> inheritedTypes)
		{
			type2inherited.put(fqName, inheritedTypes);
		}

		/**
		 * @param fqName the fully qualified class name
		 * @return collection of classes that are super for class <code>fqName</code>
		 */
		public Collection<ICompilationUnit> getHierarchy(String fqName)
		{
			return type2inherited.get(fqName);
		}

		/**
		 * Clears recorder hierarchy for class with FQName <code>fqName</code>
		 * @param fqName
		 */
		public void clearHierarchy(String fqName)
		{
			type2inherited.remove(fqName);
		}

	}
}
