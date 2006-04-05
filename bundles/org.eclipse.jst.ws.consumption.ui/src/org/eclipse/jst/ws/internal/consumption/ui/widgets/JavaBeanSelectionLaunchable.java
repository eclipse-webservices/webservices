package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsdd.BeanLink;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.PortComponentImpl;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.ServiceImplBeanImpl;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class JavaBeanSelectionLaunchable extends AbstractObjectSelectionLaunchable {

	  private IProject           serverProject_ = null;
	  private String             serverComponentName_ = null;	  
	  private String beanClassString_="";
		   
	  public void setInitialSelection(IStructuredSelection initialSelection)
	  {
	    if (initialSelection != null && !initialSelection.isEmpty())
	    {
	      Object object = initialSelection.getFirstElement();
	      if (object instanceof IFile)
	      {
	      	IFile iFile = (IFile)object;
	      	String fileExt = iFile.getFileExtension().toLowerCase();
	      	if (fileExt.equals("java") || fileExt.equals("class"))
	          setBeanClass(iFile);
	      }
	      else if (object instanceof ICompilationUnit)
	      	setBeanClass(((ICompilationUnit)object).getResource());
	      else if (object instanceof ServiceImplBeanImpl)
	      	setBeanClass((ServiceImplBeanImpl)object);
	      else if(object instanceof BeanLink)
	      	setBeanClass((BeanLink)object);
	    }
	  }

	 public IStructuredSelection getObjectSelection()
	  {		 
	    return new StructuredSelection(beanClassString_);
	  }
	  
	  public IProject getProject()
	  {
	    return serverProject_;
	  }

	  public String getComponentName()
	  {
	    return serverComponentName_;
	  }

	public int launch(Shell shell) {		
		   
		    IType itype = DialogUtils.browseClassesAsIType(shell, ResourcesPlugin.getWorkspace().getRoot().getProjects(), new ProgressMonitorDialog(shell));
		    if (itype != null)
		    {
		       beanClassString_ = itype.getFullyQualifiedName();
		      try
		      {
		        IResource res = itype.getCorrespondingResource();
		        if (res != null)
		        {
		          serverProject_ = res.getProject();
		          IVirtualComponent comp = ResourceUtils.getComponentOf(res);
		          if (comp!=null)
		          {
		            serverComponentName_ = comp.getName();
		          }
		        }
		        else
		        {
		          serverProject_ = null;
		          serverComponentName_ = null;
		        }
		        return Status.OK;
		      }
		      catch (JavaModelException jme)
		      {
		        serverProject_ = null;
		        serverComponentName_ = null;
		        return Status.ERROR;
		      }
		    }			    
		return Status.CANCEL;
	}	
  	
	  private void setBeanClass(IResource resource)
	  {
	    if( resource != null && resource instanceof IFile )
	    {
	      IPath  path     = resource.getFullPath();
	      String basename = path.lastSegment();
	      
	      if( basename != null && basename.length() > 0 )
	      {
	        String beanPackage = org.eclipse.jst.ws.internal.common.ResourceUtils.getJavaResourcePackageName(path);
	        String beanClass = (beanPackage == null || beanPackage.length() == 0 ? basename : (beanPackage + "." + basename));
	        
	        if( beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class" ) )
	        {
	          beanClass = beanClass.substring( 0, beanClass.lastIndexOf('.') );
	        }
	        
	        beanClassString_ = beanClass;
	        serverProject_ =  ResourceUtils.getProjectOf(path);
	        IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
	        
	        if (comp!=null)
	        {	
	          serverComponentName_ = comp.getName();
	        }        
	        
	      }
	    }
	  }

	  private void setBeanClass(ServiceImplBeanImpl serviceImpl)
	  {
	    if( serviceImpl != null )
	    {
	      EObject eObject = serviceImpl.eContainer();
	      if(eObject instanceof PortComponentImpl){
	      	PortComponentImpl pci = (PortComponentImpl)eObject;
	      	beanClassString_ = pci.getServiceEndpointInterface();	      	
	      }
	    }
	  }
	  
	  private void setBeanClass(BeanLink serviceImpl)
	  {
	    if( serviceImpl != null )
	    {
	      EObject eObject = serviceImpl.eContainer();
	      if(eObject instanceof ServiceImplBeanImpl){
	      	setBeanClass((ServiceImplBeanImpl)eObject);
	      }
	    }
	  }
	  
	  public IStatus getStatus()
	  {
		if (beanClassString_.length() <= 0)
	    {
	      return StatusUtils.errorStatus(ConsumptionUIMessages.PAGE_MSG_BEAN_CANNOT_BE_EMPTY);
	    }
	    return Status.OK_STATUS;
	  }
	  
	  public IStatus validateSelection(IStructuredSelection objectSelection)
	  {
	    return Status.OK_STATUS;
	  }
	  
	  public String getObjectSelectionDisplayableString() {		  
			  return beanClassString_;
	  }

}
