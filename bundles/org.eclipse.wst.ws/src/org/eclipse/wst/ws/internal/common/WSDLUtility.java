package org.eclipse.wst.ws.internal.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.wsdl.Port;
import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * Utilities for working with a workspace WSDL file resource.
 * 
 * @author joan
 *
 */

public class WSDLUtility {

	protected Definition definition_ = null;
	protected IResource resource_ = null;
	protected String serviceName_ = "";
	protected URL url_ = null;
	
	public WSDLUtility(IResource res)
	{
		super();
		resource_ = res;
		buildDefinition();		
	}
	
	protected boolean buildDefinition()
	{
		
		if (resource_.getType() == IResource.FILE)
	    {        
	        String ext = resource_.getFileExtension();
	        if (ext != null && ext.equalsIgnoreCase("wsdl"))
	        {
		      	String resPath = resource_.getFullPath().toString();
		        WebServicesParser parser = new WebServicesParser();
		      			      	
		      	try {
		      		definition_ = parser.getWSDLDefinition("platform:" + resPath);
		      	   if (definition_ == null){
		      		   throw new Exception("WebServiceUtils.getWSDLAddress - error parsing platform:/resource" + resPath);
		      	   }
		      	}
		      	catch (Exception e){
		      		return false;
		      	}
	        }
	    }
		serviceName_ = "";		
		return true;
	}
	
	/**
	 * Determines the name of the service defined in the WSDL file
	 * 
	 * @param resource The workspace resource for the WSDL file
	 * @return Returns a string containing the value of the name attribute of the service element 
	 */
	public String getServiceName()
	{
		if (serviceName_.equals(""))
		{
			   Map servicesMap = definition_.getServices();
		  	   Object[] services = servicesMap.values().toArray();
		  	   
		  	   // TODO: here can detect if no service tag - send appropriate message
		  	   // TODO: what if > 1 service defined in the WSDL?
		  	   // for now return the first service name found...
		  	   for (int i = 0; i < services.length; i++) {
				   Service s = (Service)services[i];
				   String qString = s.getQName().toString();
				   
				   //get name only from qualified string
				   int index1 = qString.indexOf("}")+1;  //$NON-NLS-1$
				   int index2 = qString.length();
				   
				   serviceName_ = qString.substring(index1, index2);				   
		  	   }  		  
		}
	  	return serviceName_;
	}
	
	public URL getWSDLAddress() 
    {
	   if (url_ == null)
	   {	   
	       String locationURI = null;
	  	   Map servicesMap = definition_.getServices();
	  	   Object[] services = servicesMap.values().toArray();
	  	   
	  	   // TODO: here can detect if no service tag - send appropriate message
	  	   // TODO: what if > 1 service defined in the WSDL?
	  	   for (int i = 0; i < services.length; i++) {
			   Service s = (Service)services[i];
			   Map portsMap = s.getPorts();
			   Object[] ports = portsMap.values().toArray();
	
			   // TODO: here can detect if > 1 port for the service - determine how to handle
			   for (int j = 0; j < ports.length; j++) {
					Port p = (Port)ports[j];
					List extList = p.getExtensibilityElements();
					
					for (int k = 0; j < extList.size(); j++){
					      ExtensibilityElement extElement = (ExtensibilityElement)extList.get(k);
					      if (extElement instanceof SOAPAddress)
					      {  
					        locationURI = ((SOAPAddress)extElement).getLocationURI();
					      }
					      else if (extElement instanceof HTTPAddress)
					       {
					        locationURI = ((HTTPAddress)extElement).getLocationURI();						        
					      }
					      else
					      {
					    	  //TODO: if not SOAP or HTTP - how do we want to handle it... 
					      }
					 }					
			   }
	  	   }  		    	
	  	 
		    try {
		         url_ = new URL(locationURI + "?" + resource_.getFileExtension());	         
		    }
		    catch (MalformedURLException malExc){
		    	
		    }	  		      
	    }
	   return url_;
    }
}
