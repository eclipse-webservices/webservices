/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070412   177005 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.common;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.wst.command.internal.env.core.data.BeanModifier;
import org.eclipse.wst.common.internal.environment.eclipse.FileResourceUtils;

public class JavaWSDLParamModifier implements BeanModifier {

	public void modify(Object bean, Object propertyHolder)
	{
		JavaWSDLParameter javaWSDLParam;
		if (bean != null)
		{
			javaWSDLParam = (JavaWSDLParameter)bean;	
		}
		else
		{
			javaWSDLParam = new JavaWSDLParameter();
		}
		 
		//look for each known key in the map & if there, convert the properties and set on javaWSDLParam
		if (propertyHolder instanceof Map)
		{	
			Map propertyMap = (Map)propertyHolder;
			
			if (propertyMap.containsKey("Use"))
			{				
			   javaWSDLParam.setUse((String)propertyMap.get("Use"));	
			}
			if (propertyMap.containsKey("Style"))
			{
				javaWSDLParam.setStyle((String)propertyMap.get("Style"));
			}
			if (propertyMap.containsKey("JavaOutput"))
			{				 
              String outputLocation = (String)propertyMap.get("JavaOutput");
              int    index          = outputLocation.indexOf( ':' );
        
              if( index == -1 )
              {
                // A colon was not found in the string so we will assume that
                // it is a workspace relative uri for now.
                IResource resource = FileResourceUtils.findResource( new Path( outputLocation ) );
          
                if( resource == null || resource.getLocation() == null )
                {
                  throw new IllegalArgumentException( AxisConsumptionCoreMessages.bind( AxisConsumptionCoreMessages.MSG_ERROR_FOLDER_NOT_FOUND,
                                                                                        outputLocation ) ); 
                }
          
                outputLocation = resource.getLocation().toString();
              }
        
		      javaWSDLParam.setJavaOutput( outputLocation );
			}
			if (propertyMap.containsKey("Methods"))
			{
			  //get Method list from the map
			  String methodList = (String)propertyMap.get("Methods");
				
			  //tokenize the method list
			  StringTokenizer tok = new StringTokenizer(methodList, " ");
			  Hashtable methods = new Hashtable();
			  while (tok.hasMoreTokens())
			  {
                String method = (String)tok.nextToken();
          
                if( method.indexOf( '(' ) == -1 )
                {
                  method = method + "()";
                }
          
				methods.put( method, new Boolean(true));
			  }				
			  javaWSDLParam.setMethods(methods);
			}
			if (propertyMap.containsKey("Mappings"))
			{
			  //get name of the file that has the mappings in it from the map
			  String filename = (String)propertyMap.get("Mappings");
        
			  //get the list of mapping from the file
			  HashMap mappings = readMappingsFromFile(filename);
        
			  javaWSDLParam.setMappings(mappings);
        
              String beanName = javaWSDLParam.getBeanName();
        
              if( beanName != null && !beanName.equals("") )
              {
                int    lastDot     = beanName.lastIndexOf( '.' );
                String packageName = lastDot == -1 ? null : beanName.substring( 0, lastDot );
          
                if( mappings != null && packageName != null )
                {
                  String tns = (String)mappings.get( packageName );
            
                  if( tns != null )
                  {
                    javaWSDLParam.setNamespace(tns);
                  }
                }   
              }
			}
		}
	}
	
	private HashMap readMappingsFromFile(String filename)
	  {
		HashMap hashmap = new HashMap();
		IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filename));
		
		if (resource != null )
		{
		  try
		  {
			Properties props = new Properties();
			props.load(resource.getContents());			
			hashmap.putAll(props);						
		  }
		  catch (Exception e)
		  {
		    // TODO Report some error here.
		  }
		}	
		return hashmap;
	  }
}
