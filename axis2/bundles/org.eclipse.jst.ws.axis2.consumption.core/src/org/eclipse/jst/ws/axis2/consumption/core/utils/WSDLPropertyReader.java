/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070118   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070507   184729 sandakith@wso2.com - Lahiru Sandakith
 * 20080621   200069 samindaw@wso2.com - Saminda Wijeratne, saving the retrieved WSDL so no need to retrieve it again
 *******************************************************************************/

package org.eclipse.jst.ws.axis2.consumption.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jst.ws.axis2.consumption.core.data.Model;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;


/**
 * This class presents a convenient way of reading the 
 * WSDL file(url) and producing a useful set of information
 * It does NOT use any of the standard WSDL classes from 
 * Axis2, rather it uses wsdl4j to read the wsdl and extract 
 * the properties (This is meant as a convenience for the UI
 * only. We may not need the whole conversion the WSDLpump 
 * goes through)
 * One would need to change this to suit a proper WSDL 
 */
public class WSDLPropertyReader {


	//private Definition wsdlDefinition = null;
	private Object DefinitionInstance = null;
	private Class DefinitionClass = null;
	private Class ServiceClass = null;
	private Model model;

	public WSDLPropertyReader(Model model){
		this.model=model;
	}
	
	public void readWSDL(String projectName, String filepath) throws Exception {
		
		ClassLoadingUtil.init(projectName);
		
		DefinitionClass = ClassLoadingUtil.loadClassFromAntClassLoader("javax.wsdl.Definition");
		
		//		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		//		wsdlDefinition = reader.readWSDL(filepath); 

		if (!model.isWsdlAlreadyLoaded(filepath)){
			Class WSDLFactoryClass = ClassLoadingUtil
					.loadClassFromAntClassLoader("javax.wsdl.factory.WSDLFactory");
			Method newInstanceMethod = WSDLFactoryClass.getMethod("newInstance", null);
			Object WSDLFactoryObject = newInstanceMethod.invoke(null, null);
			Class WSDLFactoryImplClass = ClassLoadingUtil
					.loadClassFromAntClassLoader(WSDLFactoryObject.getClass().getName());
			Method newWSDLReaderMethod = WSDLFactoryImplClass.getMethod("newWSDLReader", null);
			Object WSDLReaderObject = newWSDLReaderMethod.invoke(WSDLFactoryObject, null);
			Class WSDLReaderClass = ClassLoadingUtil
					.loadClassFromAntClassLoader(WSDLReaderObject.getClass().getName());
			Method readWSDLMethod = WSDLReaderClass.getMethod("readWSDL", new Class[]{String.class});
			model.setWsdlDefinitionInstance(
					readWSDLMethod.invoke(WSDLReaderObject, new Object[]{filepath}),filepath);
		}
		
		DefinitionInstance = model.getWsdlDefinitionInstance();
	}

	/**
	 * Returns the namespace map from definition
	 * @return
	 */
	public Map getDefinitionNamespaceMap(){
		//return wsdlDefinition.getNamespaces();
		Map map = null;
		try {
			Method getNamespacesMethod = DefinitionClass.getMethod("getNamespaces", null);
			map = (Map)getNamespacesMethod.invoke(DefinitionInstance, null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * get the default package derived by the targetNamespace
	 */
	public String packageFromTargetNamespace(){
		//return  URLProcessor.makePackageName(wsdlDefinition.getTargetNamespace());
		String returnString = null;
		try{
			Method getTargetNamespaceMethod = DefinitionClass.getMethod("getTargetNamespace", null);
			String packageName = (String)getTargetNamespaceMethod.invoke(DefinitionInstance, null);
			Class URLProcessorClass = ClassLoadingUtil
					.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
			Method makePackageNameMethod = URLProcessorClass
					.getMethod("makePackageName", new Class[]{String.class});
			returnString = (String)makePackageNameMethod.invoke(null, new Object[]{packageName});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return returnString;
	}

	/**
	 * Returns a list of service names
	 * the names are QNames
	 * @return
	 */
	public List getServiceList(){
		//Service service = null;
		//Map serviceMap = wsdlDefinition.getServices();
		//service = (Service)serviceIterator.next();
		//returnList.add(service.getQName());
		List returnList = new ArrayList();
		try{
			Object serviceInstance;
			Method getServicesMethod = DefinitionClass.getMethod("getServices", null);
			Map serviceMap = (Map)getServicesMethod.invoke(DefinitionInstance, null);

			if(serviceMap!=null && !serviceMap.isEmpty()){
				Iterator serviceIterator = serviceMap.values().iterator();
				while(serviceIterator.hasNext()){
					ServiceClass = ClassLoadingUtil
							.loadClassFromAntClassLoader("javax.wsdl.Service");
					Method getQNameMethod = ServiceClass.getMethod("getQName", null);
					serviceInstance = (Object)serviceIterator.next();
					returnList.add(getQNameMethod.invoke(serviceInstance, null));   
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	/**
	 * Returns a list of ports for a particular service
	 * the names are QNames
	 * @return
	 */
	public List getPortNameList(Object serviceName){
		//List returnList = new ArrayList();
		//Service service = wsdlDefinition.getService(serviceName);
		//Port port = null; 
		//if(service!=null){
		//	Map portMap = service.getPorts();
		//	if (portMap!=null && !portMap.isEmpty()){
		//		Iterator portIterator = portMap.values().iterator();
		//		while(portIterator.hasNext()){
		//			port = (Port)portIterator.next();
		//			returnList.add(port.getName());
		//		}
		//	}
		//}
		//return returnList;

		List returnList = new ArrayList();
		try{	
			Method getServiceMethod = DefinitionClass
						.getMethod("getService", new Class[]{serviceName.getClass()});
			Object serviceInstance = getServiceMethod
						.invoke(DefinitionInstance, new Object[]{serviceName});
			Object portInstance = null;
			if(serviceInstance!=null){
				Method getPortsMethod = ServiceClass.getMethod("getPorts", null);
				Map portMap = (Map)getPortsMethod.invoke(serviceInstance, null);
				if (portMap!=null && !portMap.isEmpty()){
					Iterator portIterator = portMap.values().iterator();
					while(portIterator.hasNext()){
						portInstance = (Object)portIterator.next();
						Class PortClass = ClassLoadingUtil
								.loadClassFromAntClassLoader("javax.wsdl.Port");
						Method getNameMethod = PortClass.getMethod("getName", null);
						returnList.add(getNameMethod.invoke(portInstance,null)); 
					}
				}

			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
    /**
     * public method to get loaded wsdl Definition Instance
     * @return
     */
    public Object getWsdlDefinitionInstance() {
            return DefinitionInstance;
    }
    
    /**
     * public method to get loaded Definition Class
     * @return
     */
    public Class getWsdlDefinitionClass() {
            return DefinitionClass;
    }

}

