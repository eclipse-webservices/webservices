package org.eclipse.jst.ws.internal.axis.consumption.core.common;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.command.internal.env.core.data.BeanModifier;

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
				javaWSDLParam.setJavaOutput((String)propertyMap.get("JavaOutput"));
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
					methods.put(tok.nextToken(), new Boolean(true));
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
