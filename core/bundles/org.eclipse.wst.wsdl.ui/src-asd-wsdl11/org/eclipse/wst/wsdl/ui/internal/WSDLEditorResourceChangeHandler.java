/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;


public class WSDLEditorResourceChangeHandler
{
  protected InternalWSDLMultiPageEditor wsdlEditor;
  protected boolean isUpdateRequired;
  protected InternalResourceChangeListener resourceChangeListener;
  protected InternalPartListener partListener;

  public WSDLEditorResourceChangeHandler(InternalWSDLMultiPageEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor;
    resourceChangeListener = new InternalResourceChangeListener();
    partListener = new InternalPartListener();
  }

  public void attach()
  {
    ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);
    wsdlEditor.getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
  }

  public void dispose()
  {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
    wsdlEditor.getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
  }

  protected Map computeDependencyMap()
  {
    Map map = new HashMap();
    IDescription description = ((InternalWSDLMultiPageEditor) wsdlEditor).getModel();
    Definition definition = (Definition) ((W11Description) description).getTarget();
    ResourceSet resourceSet = definition.eResource().getResourceSet();
    for (Iterator i = resourceSet.getResources().iterator(); i.hasNext();)
    {
      Resource resource = (Resource)i.next();
      if (resource != definition.eResource())
      {
        String uri = resource.getURI().toString();
        if (map.get(uri) == null)
        {
          map.put(uri, uri);
        }
      }
    }
    return map;
  }

  public void performReload()
  {
  	boolean doReload = false;  	
	int policy = WSDLEditorPlugin.getInstance().getDependenciesChangedPolicy();
	
  	if (policy == WSDLEditorPlugin.DEPENDECIES_CHANGED_POLICY_PROMPT)
  	{
		doReload = MessageDialog.openQuestion(wsdlEditor.getSite().getShell(), Messages._UI_DEPENDENCIES_CHANGED, Messages._UI_DEPENDENCIES_CHANGED_REFRESH);  	 //$NON-NLS-1$ //$NON-NLS-2$
  	}
  	else if (policy == WSDLEditorPlugin.DEPENDECIES_CHANGED_POLICY_RELOAD)
  	{
  		doReload = true;
  	}
  	
    if (doReload)
    {    
      wsdlEditor.reloadDependencies();
    }  
  }
  
  public boolean isListeningToResourceChanges()
  {
  	int policy = WSDLEditorPlugin.getInstance().getDependenciesChangedPolicy();
  	return policy == WSDLEditorPlugin.DEPENDECIES_CHANGED_POLICY_PROMPT ||
  		   policy == WSDLEditorPlugin.DEPENDECIES_CHANGED_POLICY_RELOAD;
  }


  class InternalResourceChangeListener implements IResourceChangeListener, IResourceDeltaVisitor
  {
    protected List list = new ArrayList();
    protected boolean isPending = false;
    protected int count = 0;

    public void resourceChanged(IResourceChangeEvent event)
    {      	
      if (isListeningToResourceChanges() && !isUpdateRequired)
      {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE)
        {
          IResourceDelta[] deltas = event.getDelta().getAffectedChildren();
          for (int i = 0; i < deltas.length; i++)
          {
            try
            {
              deltas[i].accept(this);
            }
            catch (Exception e)
            {
            }
          }
        }
        if (list.size() > 0)
        {
          if (!isPending)
          {
            isPending = true;
            new ReloadDependenciesJob().schedule(2000);
          }
        }
      }
    }

    public boolean visit(IResourceDelta delta) throws CoreException
    {
      IResource resource = delta.getResource();
      if (resource.getType() == IResource.FILE)
      {
        if (!list.contains(resource))
        {
          list.add(resource);
        }
      }
      return true;
    }

    class ReloadDependenciesJob extends Job
    {
      
      public ReloadDependenciesJob()
      {
        super("Reload WSDL dependencies"); //$NON-NLS-1$
        setSystem(true);
        setPriority(SHORT);
      }
      
      protected IStatus run(IProgressMonitor monitor)
      {
        Display.getDefault().asyncExec(new Runnable()
          {
            
            public void run()
            {
              Map dependencyMap = computeDependencyMap();

              for (Iterator i = list.iterator(); i.hasNext();)
              {
                IResource resource = (IResource)i.next();
                String platformPath = URI.createPlatformResourceURI(resource.getFullPath().toString(), false).toString();
                if (dependencyMap.get(platformPath) != null)
                {
                  isUpdateRequired = true;
                  if (wsdlEditor.getSite().getWorkbenchWindow().getPartService().getActivePart() == wsdlEditor)
                  {
                    isUpdateRequired = false;
                    performReload();
                  }
                }
              }

              isPending = false;
              list = new ArrayList();
            }
          });
        return Status.OK_STATUS;
      }
    }
  }

  class InternalPartListener implements IPartListener
  {
    public void partActivated(IWorkbenchPart part)
    {
      if (part == wsdlEditor)
      {     
        if (isUpdateRequired)
        {
          isUpdateRequired = false;
          performReload();
        }
      }
    }

    public void partBroughtToTop(IWorkbenchPart part)
    {
    }

    public void partClosed(IWorkbenchPart part)
    {
    }

   
    public void partDeactivated(IWorkbenchPart part)
    {
    }

    public void partOpened(IWorkbenchPart part)
    {
    }
  }


}