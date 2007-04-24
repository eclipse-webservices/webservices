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
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ContentCopyUtils;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDL2JavaGenerator;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2WSDL2JavaCommand extends AbstractDataModelOperation {
	
	  private DataModel model;
		
	  public Axis2WSDL2JavaCommand( DataModel model ){
	    this.model = model;  
	  }


	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
													 throws ExecutionException {
		
		IStatus status = Status.OK_STATUS;  
		IEnvironment environment = getEnvironment();
		IStatusHandler statusHandler = environment.getStatusHandler();	
		
		String workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().
													getLocation().toOSString();
		String currentDynamicWebProjectDir = FileUtils.addAnotherNodeToPath(
														workspaceDirectory,
														model.getWebProjectName());
		String matadataDir = FileUtils.addAnotherNodeToPath(
										workspaceDirectory,
										Axis2CreationUIMessages.DIR_DOT_METADATA);
	    String matadataPluginsDir = FileUtils.addAnotherNodeToPath(
	    								matadataDir,
	    								Axis2CreationUIMessages.DIR_DOT_PLUGINS);
	    String matadataAxis2Dir = FileUtils.addAnotherNodeToPath(
	    									matadataPluginsDir, 
	    									Axis2CreationUIMessages.AXIS2_PROJECT);
		String tempCodegenOutputLocation = FileUtils.addAnotherNodeToPath(
											matadataAxis2Dir, 
											Axis2CreationUIMessages.CODEGEN_RESULTS);
		File tempCodegenLocationFile = new File(tempCodegenOutputLocation);
		if (tempCodegenLocationFile.exists())FileUtils.deleteDirectories(tempCodegenOutputLocation);
		FileUtils.createDirectorys(tempCodegenOutputLocation);
        WSDL2JavaGenerator generator = new WSDL2JavaGenerator(); 
        
        //AxisService service;
        Object axisServiceInstance;
        
	try {
		//service = generator.getAxisService(model.getWsdlURI());
		ClassLoadingUtil.init(model.getWebProjectName());
		axisServiceInstance = generator.getAxisService(model.getWsdlURI());
        Map optionsMap = generator.fillOptionMap(	
        	false,  //async always false
            false,	//sync always false
            true,   //is serverside true
            model.isServerXMLCheck(),
            model.isTestCaseCheck(),
            model.isGenerateAllCheck(),
            (model.getServiceName()!=null)?model.getServiceName():null,
            (model.getPortName()!=null)?model.getPortName():null,
            (model.getDatabindingType().toLowerCase()!=null)?model.getDatabindingType().toLowerCase():null,
            (model.getWsdlURI()!=null)?model.getWsdlURI():null,
            (model.getPackageText()!=null)?model.getPackageText():null,
            "java",
            tempCodegenOutputLocation,
            model.getNamespaseToPackageMapping(),
            model.isGenerateServerSideInterface()
          );
        
        
        //CodeGenConfiguration codegenConfig = new CodeGenConfiguration(service, optionsMap);
		Class CodeGenConfigurationClass = ClassLoadingUtil.loadClassFromAntClassLoader("org.apache.axis2.wsdl.codegen.CodeGenConfiguration");

		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = axisServiceInstance.getClass();
		parameterTypes[1] = Map.class;
		Constructor CodeGenConfigurationConstructor = CodeGenConfigurationClass.getConstructor(parameterTypes);
       
		Object initargs[] = new Object[2];
		initargs[0] = axisServiceInstance;
		initargs[1] = optionsMap;
		Object CodeGenConfigurationInstance  = CodeGenConfigurationConstructor.newInstance(initargs);
		
        //set the baseURI
        //codegenConfig.setBaseURI(generator.getBaseUri(model.getWsdlURI()));
		
		Class[] parameterTypes1 = new Class[1];
		parameterTypes1[0] = String.class;
		Method setBaseURIMethod = CodeGenConfigurationClass.getMethod("setBaseURI", parameterTypes1);

		Object args[] = new Object[1];
		args[0] = generator.getBaseUri(model.getWsdlURI());
		setBaseURIMethod.invoke(CodeGenConfigurationInstance, args);
		
		//Get the namespace from the AxisService and set it in the configuration 
		Class AxisServiceClass = ClassLoadingUtil.loadClassFromAntClassLoader("org.apache.axis2.description.AxisService");

		Method getTargetNamespaceMethod = AxisServiceClass.getMethod("getTargetNamespace", null);
		Object targetNamespace = getTargetNamespaceMethod.invoke(axisServiceInstance, null);
		
		
		Class URLProcessorClass = ClassLoadingUtil.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
		Class parameterTypes4[] = new Class[1];
		parameterTypes4[0] = String.class;
		Method makePackageNameMethod = URLProcessorClass.getMethod("makePackageName", parameterTypes4);
		
		Object args4[] = new Object[1];
		args4[0] = targetNamespace;
		Object stringReturn = makePackageNameMethod.invoke(null, args4);
		
		model.setPackageText(stringReturn.toString());
		
		Class[] parameterTypes3 = new Class[1];
		parameterTypes3[0] = String.class;
		Method setPackageNameMethod = CodeGenConfigurationClass.getMethod("setPackageName", parameterTypes3);

		Object args2[] = new Object[1];
		args2[0] = stringReturn;
		setPackageNameMethod.invoke(CodeGenConfigurationInstance, args2);
		
        //new CodeGenerationEngine(codegenConfig).generate();
		
        //Class CodeGenerationEngineClass = Class.forName("org.apache.axis2.wsdl.codegen.CodeGenerationEngine");
		Class CodeGenerationEngineClass = ClassLoadingUtil.loadClassFromAntClassLoader("org.apache.axis2.wsdl.codegen.CodeGenerationEngine");

		Class[] parameterTypes2 = new Class[1];
		parameterTypes2[0] = CodeGenConfigurationInstance.getClass();
		Constructor CodeGenerationEngineConstructor = CodeGenerationEngineClass.getConstructor(parameterTypes2);
        
		Object initargs1[] = new Object[1];
		initargs1[0] = CodeGenConfigurationInstance;
		Object CodeGenerationEngineInstance  = CodeGenerationEngineConstructor.newInstance(initargs1);
		
		//Invoke Codegen Method
		Method generateMethod = CodeGenerationEngineClass.getMethod("generate", null);
		generateMethod.invoke(CodeGenerationEngineInstance, null);
		
        
		//Copy the existing codegen results to the current project
        ContentCopyUtils contentCopyUtils = new ContentCopyUtils();
	    status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
	    							tempCodegenOutputLocation, 
	    							currentDynamicWebProjectDir, 
	    							monitor, 
	    							statusHandler);

        
	} catch (Exception e) {
		status = StatusUtils.errorStatus(NLS.bind(
											Axis2CreationUIMessages.ERROR_CODEGEN_EXCEPTION,
											new String[]{e.getLocalizedMessage()}),
										e);
		e.printStackTrace();
		environment.getStatusHandler().reportError(status); 
	}
		
		return status;
	}

}
