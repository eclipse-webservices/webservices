package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;
import java.util.Vector;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

/**
 * Extends the Web Services Finder Framework.
 * Finds all .wsdl files in the workspace.
 * Not intended to be used directly.  WorkspaceWSDLLocator registers with WebServiceLocatorRegistry 
 * and is accessed by the {@link WebServiceFinder}. 
 */

public class WorkspaceWSDLLocator extends AbstractWebServiceLocator {
	
	protected List wsdlServices = null;
	private static final String PLATFORM_RES = "platform:/resource";  //$NON-NLS-1$
	private static final String WSDL_EXT = "wsdl";  //$NON-NLS-1$
	
	public WorkspaceWSDLLocator()
	{
		super();	
	}
	
	/**
	 * Returns the collection of all .wsdl files in the workspace.  Currently does not eliminate multiple 
	 * occurences of the same web service. 
	 * 
	 * TODO: add a listener to the workspace resource tree so that getWebServices doesn't always
	 * use the WSDLVisitor to walk the entire resource tree.  That should only happen once.  After
	 * that the resource tree can be monitored for modifications to .wsdl files and changes made to a cache. 
	 * 
	 * @return list of WebServiceInfo objects
	 */
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

	/**
	 *  Uses the Visitor pattern to walk the workspace resource tree
	 */
	
	private class WSDLVisitor implements IResourceVisitor
    {
	  
	  private Vector wsdl = new Vector();
    
	  /**
	   *
	   * visits every node on the resource tree stopping a file resources
	   * if file resource has extension .wsdl a WebServiceInfo object is created and added to a vector 
	   *  TODO: look at caching to eliminate duplicate web service definitions in the vector returned
	   *  TODO: add more information to the WebServiceInfo object.  Currently only the qualified filename is added.
	   */
	 
	  public boolean visit(IResource resource)
      {
    	if (resource.getType() == IResource.FILE)
        {        
          String ext = resource.getFileExtension();
          if (ext != null && ext.equalsIgnoreCase(WSDL_EXT))
          {
        	String urlString = resource.getFullPath().toString();          
        	urlString = PLATFORM_RES + urlString;
        	
        	WebServiceInfo wsInfo = new WebServiceInfo();
            wsInfo.setWsdlURL(urlString);           
            
            wsdl.add(wsInfo);
          }
        }
        return true;
      }
      
	  /**
	   * 
	   * @return vector of WebServiceInfo objects
	   */
      public Vector getWSDL()
      {
    	  return wsdl;
      }
    }
	
}
