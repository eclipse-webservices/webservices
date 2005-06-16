package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.ws.internal.common.LiveWSDLFilter;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class WSDLLocator extends AbstractWebServiceLocator {
	
	protected List wsdlServices = null;
	
	public WSDLLocator()
	{
		super();	
	}
	
	public List getWebServices() {
		
		if (wsdlServices == null)
		{
			 try{
			      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
			      WSDLVisitor visitor = new WSDLVisitor();
			      root.accept(visitor);		
			      visitor.visit(root);
			      wsdlServices = visitor.getWSDL();
			    }
			    catch (Exception ex){
			        	
			    }
		}
		
		return wsdlServices;		
	}

	private class WSDLVisitor implements IResourceVisitor
    {
	  
	  private Vector wsdlURLs = new Vector();
      
	  public boolean visit(IResource resource)
      {
    	if (resource.getType() == IResource.FILE)
        {        
          String ext = resource.getFileExtension();
          if (ext != null && ext.equalsIgnoreCase("wsdl"))
          {
        	String resPath = resource.getFullPath().toString();
            StringBuffer sb = new StringBuffer(resPath);
      
            WebServiceInfo wsInfo = new WebServiceInfo();
            wsInfo.setWsdlURL(sb.toString());
            //TODO: add other WSDL information to the wsInfo object
            wsdlURLs.add(wsInfo);
          }
        }
        return true;
      }
      
      public Vector getWSDL()
      {
    	  return wsdlURLs;
      }
    }
	
}
