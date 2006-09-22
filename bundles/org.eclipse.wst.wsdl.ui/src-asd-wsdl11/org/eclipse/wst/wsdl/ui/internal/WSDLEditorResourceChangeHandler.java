/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
      Display display = Display.getCurrent();
      	     	
      if (display != null && isListeningToResourceChanges() && !isUpdateRequired)
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
            display.timerExec(2000, new TimerEvent());
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

    class TimerEvent implements Runnable
    {
      public TimerEvent()
      {
        //System.out.println("NewTimerEvent(" + wsdlEditor.getDefinition().eResource().getURI() + ") " + count);      	
      }

      public void run()
      {
//        for (Iterator i = list.iterator(); i.hasNext();)
//        {
//          IResource resource = (IResource)i.next();
//          String platformPath = URI.createPlatformResourceURI(resource.getFullPath().toString()).toString();
//        }

        Map dependencyMap = computeDependencyMap();

        for (Iterator i = list.iterator(); i.hasNext();)
        {
          IResource resource = (IResource)i.next();
          String platformPath = URI.createPlatformResourceURI(resource.getFullPath().toString()).toString();
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

/*
class DependencyVisitor
{
	public void visitImport(Import theImport)
	{
		if (theImport.getEDefinition() != null)
		{
			visitDefinition(theImport.getEDefinition());
		}
		else if (theImport.getESchema() != null)
		{
			visitSchema(theImport.getESchema());
		}
	}

	public void visitXSDSchemaDirective(XSDSchemaDirective directive)
	{
		XSDSchema referencedSchema = directive.getResolvedSchema();
		if (referencedSchema != null)
		{
			visitSchema(referencedSchema);
		}
	}

	public void visitDefinition(Definition definition)
	{
		if (definition != null)
		{
			for (Iterator i = definition.getEImports().iterator(); i.hasNext();)
			{
				visitImport((Import)i.next());
			}
			Types types = definition.getETypes();
			if (types != null)
			{

				for (Iterator i = types.getEExtensibilityElements().iterator(); i.hasNext();)
				{
					Object o = i.next();
					if (o instanceof XSDSchemaExtensibilityElement)
					{
						XSDSchemaExtensibilityElement e = (XSDSchemaExtensibilityElement)o;
						if (e.getEXSDSchema() != null)
						{
							visitSchema(e.getEXSDSchema());
						}
					}
				}
			}
		}
	}

	public void visitSchema(XSDSchema schema)
	{
		for (Iterator i = schema.getContents().iterator(); i.hasNext();)
		{
			Object o = i.next();
			if (o instanceof XSDSchemaDirective)
			{
				visitXSDSchemaDirective((XSDSchemaDirective)o);
			}
		}
	}
}

class ReloadDependencyVisitor extends DependencyVisitor
{
	public void visitImport(Import theImport)
	{
		ComponentHandler handler = WSDLReconciler.getReconciler(theImport);
		Element element = WSDLUtil.getInstance().getElementForObject(theImport);
		if (element != null && handler != null)
		{
			handler.reconcile(wsdlEditor.getDefinition(), theImport, element);
		}
	}

	public void visitXSDSchemaDirective(XSDSchemaDirective directive)
	{

	}
}*/