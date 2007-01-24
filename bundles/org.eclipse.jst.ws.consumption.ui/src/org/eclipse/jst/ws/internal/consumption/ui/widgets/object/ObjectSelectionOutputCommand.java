/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060830   155114 pmoogk@ca.ibm.com - Peter Moogk, Updated patch for this defect.
 * 20070116   159618 makandre@ca.ibm.com - Andrew Mak, Project and EAR not defaulted properly when wizard launched from JSR-109 Web services branch in J2EE Project Explorer
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.UniversalPathTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;


public class ObjectSelectionOutputCommand extends AbstractDataModelOperation
{
  private String                 objectSelectionWidgetId_;
  private IStructuredSelection   objectSelection_;
  private IObjectSelectionWidget objectSelectionWidget_;
  private IProject               project_;
  private String                 componentName_;
  private WebServicesParser      parser_;
  
  private UniversalPathTransformer transformer_ = new UniversalPathTransformer();
  
  private boolean                topDown_ = false;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
    // Transformation
    if (objectSelectionWidgetId_ != null && objectSelectionWidgetId_.length() > 0)
    {
      Transformer transformer          = ObjectSelectionRegistry.getInstance().getTransformer(objectSelectionWidgetId_);
      Object      transformedSelection = transformer == null ? null : transformer.transform(objectSelection_);
      
      if (transformedSelection instanceof IStructuredSelection)
      {
        objectSelection_ = (IStructuredSelection)transformedSelection;
      }
    }
	
    // Validation
    IStatus status = (objectSelectionWidget_ != null) ? objectSelectionWidget_.validateSelection(getObjectSelection()) : Status.OK_STATUS;
    if (status.getSeverity() != Status.OK && env != null)
    {
      try
      {
        env.getStatusHandler().report(status);
      }
      catch (StatusException se)
      {
      }
    }
	
    return status;
  }

  /**
   * @param typeRuntimeServer The typeRuntimeServer to set.
   */
  public void setTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer)
  {
    if (typeRuntimeServer != null)
    {
      String wst = typeRuntimeServer.getTypeId();

      int scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(wst);
      String implId = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(wst);

      WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils2.getWebServiceImplById(implId);
	    //IWebServiceType wst = WebServiceRuntimeExtensionUtils2.getWebServiceTypeById(typeRuntimeServer.getTypeId());

      if (wsimpl != null)
      {
        String objectSelectionWidgetId = null;
        if (scenario == WebServiceScenario.TOPDOWN)
        {
          objectSelectionWidgetId = "org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWidget";
          topDown_ = true;
        }
        else
        {
          objectSelectionWidgetId = wsimpl.getObjectSelectionWidget();
        }        
        
		    objectSelectionWidgetId_ = objectSelectionWidgetId;
        
		    Object object = ObjectSelectionRegistry.getInstance().getSelectionWidget(objectSelectionWidgetId_);
        
        if( object instanceof IObjectSelectionWidget )
        {
          objectSelectionWidget_ = (IObjectSelectionWidget)object;
        }
      }
    }
  }

  /**
   * @return Returns the selection.
   */
  public IStructuredSelection getObjectSelection()
  {
    return objectSelection_;
  }
  /**
   * @param selection The selection to set.
   */
  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    objectSelection_ = objectSelection;
    
    //Set the project if you can
    if (project_==null)
    {
      project_ = getProjectFromObjectSelection(objectSelection);
    }
    
    //Set the componentName if you can
    if (componentName_ == null)
    {
      componentName_ = getComponentNameFromObjectSelection(objectSelection);
    }
    
    // Check if this is a WSDL selection object.  If it is we need
    // to unwrap it.
    if( objectSelection != null && !objectSelection.isEmpty() )
    {
      Object object = objectSelection.getFirstElement();
      
      if( object instanceof WSDLSelectionWrapper )
      {
        WSDLSelectionWrapper wsdlWrapper = (WSDLSelectionWrapper)object;
        
        objectSelection_ = wsdlWrapper.wsdlSelection;
        parser_          = wsdlWrapper.parser;
      }
    }
  }
  
  public IProject getProject()
  {
    if (project_ == null && topDown_)
      project_ = getProjectFromTransformedSelection();
    return project_;
  }
  
  public void setProject(IProject project)
  {
    this.project_ = project;
  }  
  
  public String getComponentName()
  {
    if (componentName_ == null && topDown_)
      componentName_ = getComponentNameFromTransformedSelection();
    return componentName_;
  }

  public void setComponentName(String componentName)
  {
    this.componentName_ = componentName;
  }

  /**
   * @return Returns the parser_.
   */
  public WebServicesParser getWebServicesParser()
  {
    return parser_;
  }
  
  private boolean hasProtocol(String url) { 
    return url.indexOf(":") != -1;  
  }
  
  private IResource findResourceFromSelection(Object selection) throws CoreException {
	  
      IResource resource = ResourceUtils.getResourceFromSelection(selection); 
	  
      // try finding at least the project using the EMF way
      if (resource == null && selection instanceof EObject)
          resource = ProjectUtilities.getProject(selection);
	  
      return resource;
  }
  
  private IProject getProjectFromTransformedSelection() {
    if (objectSelection_ != null && objectSelection_.size() == 1)
    {
      Object obj = objectSelection_.getFirstElement();
      if (obj instanceof String) {
        String str = transformer_.toPath((String) obj);
        if (hasProtocol(str)) return null;
        return ResourceUtils.getProjectOf(new Path(str));
      }
    }
    return null;	  
  }
  
  private IProject getProjectFromObjectSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = findResourceFromSelection(obj);
          if (resource==null) 
            return null;
          if (resource instanceof IProject)
        	return (IProject) resource;
          IProject p = ResourceUtils.getProjectOf(resource.getFullPath());
          return p;
        } catch(CoreException e)
        {
			e.printStackTrace();
          return null;
        }        
      }
    }
    return null;
  }
  
  private String getComponentNameFromTransformedSelection() {
    if (objectSelection_ != null && objectSelection_.size() == 1)
    {
      Object obj = objectSelection_.getFirstElement();
      if (obj instanceof String) { 
        String str = transformer_.toPath((String) obj);
        if (hasProtocol(str)) return null;
        IVirtualComponent comp = ResourceUtils.getComponentOf(new Path(str));
        return comp == null ? null : comp.getName();
      }
    }
    return null;	  
  }
  
  private String getComponentNameFromObjectSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = findResourceFromSelection(obj);
          if (resource==null) 
            return null;
     
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
     
          if (comp!=null)
          {
            return comp.getName();
          }
        } catch(CoreException e)
        {
      e.printStackTrace();
          return null;
        }        
      }
    }
    return null;
  }  
}
