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
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.util.ProjectExplorerUtil;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject.ILoadingCanceled;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject.ILoadingDummy;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDomLoadListener;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.IWsDOMRuntimeInfo;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.WsDOMRuntimeRegistry;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.jst.ws.jaxws.utils.facets.FacetUtils;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

/** 
 * Content provider for DOM related objects
 * 
 * @author 
 */
public class DOMAdapterFactoryContentProvider extends AdapterFactoryContentProvider
{
	private Map<IWsDOMRuntimeExtension, IWsDomLoadListener> loadListeners = new HashMap<IWsDOMRuntimeExtension, IWsDomLoadListener>(); 
	
	/**
	 * Constructor
	 */
	public DOMAdapterFactoryContentProvider()
	{
		this(CustomDomItemProviderAdapterFactory.INSTANCE);
	}
	
	/**
	 * Constructor
	 * @param adapterFactory
	 */
	public DOMAdapterFactoryContentProvider(AdapterFactory adapterFactory)
	{
		super(adapterFactory);
	}
	
	@Override
	public Object[] getChildren(final Object context)
	{
		if (context instanceof IAdaptable)
		{
			return getWsProject((IProject)((IAdaptable) context).getAdapter(IProject.class));
		}
		else if(context instanceof ILoadingWsProject) 
		{
			return new Object[] { ((ILoadingWsProject)context).isLoadCanceled() ? new ILoadingCanceled(){} : new ILoadingDummy(){} };
		}
		else if(context instanceof IWebServiceProject)
		{
			return new Object[]{ adapterFactory.adapt(context, ISEIChildList.class),
					adapterFactory.adapt(context, IWebServiceChildList.class) };
		}
		else if(context instanceof IWebServiceChildList)
		{
			Object[] webServices = ((IWebServiceChildList)context).getWSChildList().toArray();
			
			for(int ii=0; ii<webServices.length; ii++)
			{
				adapterFactory.adapt(webServices[ii], ITreeItemContentProvider.class);
			}
			
			return webServices;
		}
		else if(context instanceof ISEIChildList)
		{
			Object[] sEIs = ((ISEIChildList)context).getSEIChildList().toArray();
			
			for(int ii=0; ii<sEIs.length; ii++)
			{
				adapterFactory.adapt(sEIs[ii], ITreeItemContentProvider.class);
			}
			
			return sEIs;
		}
		else if(context instanceof IWebService)
		{
			final IServiceEndpointInterface sei = ((IWebService)context).getServiceEndpoint();
			if (sei == null) {
				return new Object[0];
			}
			
			adapterFactory.adapt(sei, ITreeItemContentProvider.class);
			return new Object[]{ sei };
		}
		else if(context instanceof IServiceEndpointInterface) {
			return extractMethods((IServiceEndpointInterface) context);
		}
		else if (context instanceof IWebMethod) {
			return extractParams((IWebMethod)context);
		}
		
		return new Object[0];
	}
	
	protected Object[] getWsProject(final IProject project) 
	{
		try {
			final IWsDOMRuntimeExtension runtime = getSupportingRuntime(project);
			if (runtime == null) {
				return new Object[0];
			}
			
			// WS project is loaded already
			final IWebServiceProject wsProject = getWsProject(runtime.getDOM(), project); 
			if (wsProject != null)
			{
				adapterFactory.adapt(wsProject, ITreeItemContentProvider.class);
				return new Object[] { wsProject };
			}
			
			addRuntimeLoadListener(runtime);
			return new Object[] { new LoadingWsProject(project, false) };
		} 
		catch (WsDOMLoadCanceledException e) { // $JL-EXC$
			return new Object[] { new LoadingWsProject(project, true) }; 
		}
	}
	
	protected IWsDomLoadListener addRuntimeLoadListener(final IWsDOMRuntimeExtension runtime) 
	{
		IWsDomLoadListener loadListener = loadListeners.get(runtime); 
		if (loadListener!=null) {
			return loadListener;
		}
		
		loadListener = new LoadListener(runtime);		
		loadListeners.put(runtime, loadListener);		
		runtime.addLoadListener(loadListener);
		
		return loadListener;
	}
	
	protected Object[] extractMethods(final IServiceEndpointInterface sei) 
	{
		final List<IWebMethod> webMethods = sei.getWebMethods();
		for (IWebMethod webMethod : webMethods) {
			adapterFactory.adapt(webMethod, ITreeItemContentProvider.class);
		}
		
		return webMethods.toArray();		
	}
	
	protected Object[] extractParams(final IWebMethod method) 
	{
		final List<IWebParam> params = method.getParameters();
		for (IWebParam webParam : params) {
			adapterFactory.adapt(webParam, ITreeItemContentProvider.class);
		}
		
		return params.toArray();
	}
	
	@Override
	public boolean hasChildren(Object element)
	{
		if (element instanceof IWebServiceProject)
		{
			return true;
		}
		else if(element instanceof ILoadingWsProject) 
		{
			return true;
		}
		else if(element instanceof IWebServiceChildList)
		{
			return !((IWebServiceChildList)element).getWSChildList().isEmpty();
		}
		else if(element instanceof ISEIChildList)
		{
			return !((ISEIChildList)element).getSEIChildList().isEmpty();
		}
		else if (element instanceof IWebService)
		{
			return ((IWebService) element).getServiceEndpoint() != null; 
		}
		else if (element instanceof IServiceEndpointInterface)
		{
			return !((IServiceEndpointInterface)element).getWebMethods().isEmpty();
		}
		else if (element instanceof IWebMethod)
		{
			return !((IWebMethod)element).getParameters().isEmpty();
		}

		return false;
	}
	
	protected IWebServiceProject getWsProject(final IDOM dom, final IProject project)
	{
		if (dom == null) {
			return null;
		}
		
		for (IWebServiceProject wsProject : dom.getWebServiceProjects()) {
			if(wsProject.getName().equals(project.getName())) {
				return wsProject;
			}
		}
		
		return null;
	}
	
	protected IWsDOMRuntimeExtension getSupportingRuntime(final IProject project)
	{
		final Collection<IWsDOMRuntimeInfo> domRuntimeInfos = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
		Map<String, String> facets;
		for (IWsDOMRuntimeInfo domRuntimeInfo : domRuntimeInfos) 
		{
			facets = domRuntimeInfo.getProjectFacets();
			if (hasFacet(facets, project)) {
				return getDomRuntime(domRuntimeInfo.getId());
			}
		}

		return null;
	}
	
	protected boolean hasFacet(final Map<String, String> facets, final IProject project)
	{
		for (String facetId : facets.keySet()) 
		{
			if (facetId == null || facets.get(facetId) == null) {
				continue;
			}
			
			try {
				if ((new FacetUtils()).hasFacetWithVersion(project, facets.get(facetId), facetId, true)){
					return true;
				}
			} 
			catch (CoreException e) {
				(new Logger()).logError(e.getMessage(), e);
			}
		}
		
		return false;
	}
	
	protected IWsDOMRuntimeExtension getDomRuntime(String runtimeId) {
		return WsDOMRuntimeManager.instance().getDOMRuntime(runtimeId);
	}
	
	/**
	 * Class that is used to listen when the DOM load will be finished. After the load has finished
	 * this class traverses the project explorers tree in order to find all instances of
	 * {@link ILoadingWsProject} and substitute them with loaded {@link IWebServiceProject}
	 * 
	 * @author Georgi Vachkov
	 */
	protected class LoadListener implements IWsDomLoadListener
	{
		private final IWsDOMRuntimeExtension runtime;
		
		public LoadListener(final IWsDOMRuntimeExtension runtime) 
		{
			ContractChecker.nullCheckParam(runtime, "runtime"); //$NON-NLS-1$
			this.runtime = runtime;
		}
			
		public void finished() 
		{
			Display.getDefault().asyncExec(new Runnable()
			{
				public void run() 
				{
					final IViewPart viewPart = ProjectExplorerUtil.INSTANCE.findProjectExplorer();
					if (viewPart instanceof CommonNavigator) 
					{
						final CommonViewer commonViewer = ((CommonNavigator)viewPart).getCommonViewer();
						for (TreeItem item : commonViewer.getTree().getItems()) {
							exchangeDummy(item, commonViewer);
						}
					}
				}
			});
		}		

		protected void exchangeDummy(final TreeItem treeItem, final CommonViewer commonViewer) 
		{
			if (treeItem == null || treeItem.getData() instanceof IWebServiceProject) {
				return;
			}
			
			if (treeItem.getData() instanceof ILoadingWsProject) {
				exchangeTreeItemData(treeItem, commonViewer);
			}
			else {
				for (TreeItem child : treeItem.getItems()) {
					exchangeDummy(child, commonViewer);
				}
			}
		}

		private void exchangeTreeItemData(final TreeItem treeItem, final CommonViewer commonViewer) 
		{
			final ILoadingWsProject loadingProject = (ILoadingWsProject)treeItem.getData();
			try {
				final IWebServiceProject wsProject = getWsProject(runtime.getDOM(), loadingProject.getProject());
				adapterFactory.adapt(wsProject, ITreeItemContentProvider.class);
				treeItem.setData(wsProject);

			} catch (WsDOMLoadCanceledException e) { // $JL-EXC$
				treeItem.setData(new LoadingWsProject(loadingProject.getProject(), true));
			}
			
			commonViewer.refresh(loadingProject.getProject());
		}
		
		public void started() {
			// nothing to do here
		}
	}
	
	private class LoadingWsProject implements ILoadingWsProject
	{
		final IProject project;
		final boolean isCanceled;
		
		public LoadingWsProject(final IProject project, final boolean isCanceled) {
			ContractChecker.nullCheckParam(project, "project"); //$NON-NLS-1$
			this.project = project;
			this.isCanceled = isCanceled;
		}
		
		public IProject getProject() {
			return project;
		}

		public boolean isLoadCanceled() {
			return isCanceled;
		}
	}
}
