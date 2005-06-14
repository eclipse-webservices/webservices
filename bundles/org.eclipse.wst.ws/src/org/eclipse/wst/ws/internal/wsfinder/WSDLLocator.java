package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.ws.internal.common.LiveWSDLFilter;

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
		//TODO: look at validation...
		//return returnValidWSDL(wsdlServices);
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
            wsdlURLs.add(sb.toString());
          }
        }
        return true;
      }
      
      public Vector getWSDL()
      {
    	  return wsdlURLs;
      }
    }
	

	// TODO: this is currently unused.  Need to look at validation for various locators... 
	private List returnValidWSDL(List wsdlURLs)
	{	
	   List validURLs = wsdlURLs;
	   LiveWSDLFilter[] filters = new LiveWSDLFilter[validURLs.size()];
	   for (int i = 0; i < filters.length; i++)
	   {
		 filters[i] = new LiveWSDLFilter((String)validURLs.get(i));
	     filters[i].start();
	   }
	   for (int i = 0; i < filters.length; i++)
	   {
		 if (!filters[i].isFinish())
	     {
	       Thread.yield();
	       i = -1;
	     }
	   }
	   for (int i = 0; i < filters.length; i++)
	   {
		 if (!filters[i].isWSDLLive())
	     {
	    	validURLs.remove(filters[i].getWSDLURL());
	     }
	   }	   
	   return validURLs;
	}
}
