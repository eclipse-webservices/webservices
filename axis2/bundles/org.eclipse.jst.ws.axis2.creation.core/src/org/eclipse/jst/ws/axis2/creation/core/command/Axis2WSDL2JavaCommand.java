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
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ContentCopyUtils;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDL2JavaGenerator;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDLPropertyReader;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.axis2.creation.core.utils.PathLoadingUtil;
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
		
		PathLoadingUtil.init(model);
		String tempCodegenOutputLocation =PathLoadingUtil.getTempCodegenOutputLocation();
		String currentDynamicWebProjectDir = PathLoadingUtil.getCurrentDynamicWebProjectDir();
		
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
            (model.getDatabindingType().toLowerCase()!=null)?
            		model.getDatabindingType().toLowerCase():
            		null,
            (model.getWsdlURI()!=null)?model.getWsdlURI():null,
            (model.getPackageText()!=null)?model.getPackageText():null,
            "java",
            tempCodegenOutputLocation,
            model.getNamespaseToPackageMapping(),
            model.isGenerateServerSideInterface()
          );
        
        
        //CodeGenConfiguration codegenConfig = new CodeGenConfiguration(service, optionsMap);
		Class CodeGenConfigurationClass = ClassLoadingUtil
				.loadClassFromAntClassLoader("org.apache.axis2.wsdl.codegen.CodeGenConfiguration");

		//-----------------------------------------------------------------------------------//
		//Fix for the Axis2 1.2 
		//Constructor CodeGenConfigurationConstructor = CodeGenConfigurationClass
		//		.getConstructor(new Class[]{axisServiceInstance.getClass(),Map.class});
		//Object CodeGenConfigurationInstance = CodeGenConfigurationConstructor
		//		.newInstance(new Object[]{axisServiceInstance,optionsMap});
		
		Constructor CodeGenConfigurationConstructor = CodeGenConfigurationClass
				.getConstructor(new Class[]{Map.class});
		Object CodeGenConfigurationInstance = CodeGenConfigurationConstructor
				.newInstance(new Object[]{optionsMap});
		
		// codegenConfig.addAxisService(service);
		Method addAxisServiceMethod = CodeGenConfigurationClass
				.getMethod("addAxisService", new Class[]{ axisServiceInstance.getClass()});
		addAxisServiceMethod.invoke(CodeGenConfigurationInstance, 
							new Object[]{axisServiceInstance});
		
        //set the wsdl definision for codegen config for skeleton generarion.
        WSDLPropertyReader reader = new WSDLPropertyReader();
        reader.readWSDL(model.getWebProjectName(),model.getWsdlURI());
        Object wsdlDefinitionInstance = reader.getWsdlDefinitionInstance();
        //Class DefinitionClass = ClassLoadingUtil.loadClassFromAntClassLoader("javax.wsdl.Definition");
        //codegenConfig.setWsdlDefinition(wsdlDefinition);
		Method setWsdlDefinitionMethod = CodeGenConfigurationClass
				.getMethod("setWsdlDefinition", new Class[]{reader.getWsdlDefinitionClass()});
		setWsdlDefinitionMethod.invoke(CodeGenConfigurationInstance, 
					new Object[]{wsdlDefinitionInstance});       
        
		//-----------------------------------------------------------------------------------//

		//set the baseURI
        //codegenConfig.setBaseURI(generator.getBaseUri(model.getWsdlURI()));
		
		Method setBaseURIMethod = CodeGenConfigurationClass
			.getMethod("setBaseURI", new Class[]{ String.class});
		setBaseURIMethod.invoke(CodeGenConfigurationInstance, 
								new Object[]{generator.getBaseUri(model.getWsdlURI())});
		
		//Get the namespace from the AxisService and set it in the configuration 
		Class AxisServiceClass = ClassLoadingUtil
					.loadClassFromAntClassLoader("org.apache.axis2.description.AxisService");		
		Method getTargetNamespaceMethod = AxisServiceClass.getMethod("getTargetNamespace", null);
		Object targetNamespace = getTargetNamespaceMethod.invoke(axisServiceInstance, null);
		
		
		Class URLProcessorClass = ClassLoadingUtil
				.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
		Method makePackageNameMethod = URLProcessorClass
				.getMethod("makePackageName", new Class[]{String.class});
		Object stringReturn = makePackageNameMethod.invoke(null, new Object[]{targetNamespace});
		
		model.setPackageText(stringReturn.toString());
		
		Method setPackageNameMethod = CodeGenConfigurationClass
					.getMethod("setPackageName", new Class[]{String.class});
		setPackageNameMethod.invoke(CodeGenConfigurationInstance, new Object[]{stringReturn});
		
        //new CodeGenerationEngine(codegenConfig).generate();
		Class CodeGenerationEngineClass = ClassLoadingUtil
				.loadClassFromAntClassLoader("org.apache.axis2.wsdl.codegen.CodeGenerationEngine");
		Constructor CodeGenerationEngineConstructor = CodeGenerationEngineClass
						.getConstructor(new Class[]{CodeGenConfigurationInstance.getClass()});
		Object CodeGenerationEngineInstance  = CodeGenerationEngineConstructor
						.newInstance(new Object[]{CodeGenConfigurationInstance});
		
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
