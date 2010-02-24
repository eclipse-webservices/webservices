/*******************************************************************************
 * Copyright (c) 2007, 2008 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070205   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070206   172186 sandakith@wso2.com	- Fix for 172186, Added a check to overcome the issue.
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070507   185398 sandakith@wso2.com - Lahiru Sandakith
 * 20070813   196173  sandakith@wso2.com - Lahiru Sandakith, Fix 196173, DWP custom location fix
 * 20070814   187840 sandakith@wso2.com - Lahiru Sandakith, Fixing 187840 ITE message
 * 20070814   193593 sandakith@wso2.com - Lahiru Sandakith, custom package name fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080213   218910 kathy@ca.ibm.com - Kathy Chan
 * 20080621   200069 samindaw@wso2.com - Saminda Wijeratne, saving the retrieved WSDL so no need to retrieve it again 
 * 20080616   237363 samindaw@wso2.com - Saminda Wijeratne, get ResourceContext from environment instead of preference
 * 20080924   247929 samindaw@wso2.com - Saminda Wijeratne, source folder not correctly set
 * 20091207   193996 samindaw@wso2.com - Saminda Wijeratne, selecting a specific service/portname
*******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.command;

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
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ContentCopyUtils;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDL2JavaGenerator;
import org.eclipse.jst.ws.axis2.consumption.core.utils.WSDLPropertyReader;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2ClientCodegenCommand extends AbstractDataModelOperation {
	
	  private DataModel model;
		
	  public Axis2ClientCodegenCommand( DataModel model ){
	    this.model = model;  
	  }

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
													 throws ExecutionException {
		IStatus status = Status.OK_STATUS;  
		IEnvironment environment = getEnvironment();
		IStatusHandler statusHandler = environment.getStatusHandler();	
		
		String workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().
													getLocation().toOSString();
		String currentDynamicWebProjectDir = FacetContainerUtils.getProjectRoot(
				                                  model.getWebProjectName()).toOSString();
		String matadataDir = FileUtils.addAnotherNodeToPath(
										workspaceDirectory,
										Axis2Constants.DIR_DOT_METADATA);
	    String matadataPluginsDir = FileUtils.addAnotherNodeToPath(
	    								matadataDir,
	    								Axis2Constants.DIR_DOT_PLUGINS);
	    String matadataAxis2Dir = FileUtils.addAnotherNodeToPath(
	    									matadataPluginsDir, 
	    									Axis2Constants.AXIS2_PROJECT);
		String tempCodegenOutputLocation = FileUtils.addAnotherNodeToPath(
											matadataAxis2Dir, 
											Axis2Constants.CODEGEN_RESULTS);
		File tempCodegenOutputLocationFile = new File(tempCodegenOutputLocation);
		if (tempCodegenOutputLocationFile.exists()){
			FileUtils.deleteDirectories(tempCodegenOutputLocation);
		}
		FileUtils.createDirectorys(tempCodegenOutputLocation);
		WSDL2JavaGenerator generator = new WSDL2JavaGenerator(); 
      
		//AxisService service;
		Object axisServiceInstance;
		
		String transformerFactory = null;
		boolean transformerFactoryModified = false;
		
	try {
		// use the xalan transformer factory if loadable
		try {
			transformerFactory = System.getProperty("javax.xml.transform.TransformerFactory");
			Class.forName("org.apache.xalan.processor.TransformerFactoryImpl");
			String modifiedTransformerFactory = "org.apache.xalan.processor.TransformerFactoryImpl";
			if (!modifiedTransformerFactory.equals(transformerFactory)) {
				System.setProperty("javax.xml.transform.TransformerFactory", modifiedTransformerFactory);
				transformerFactoryModified = true;
			}
		}
		catch (ClassNotFoundException e) {
			// If class not found, keep using the default transformer factory.
		}
		
		//service = generator.getAxisService(model.getWsdlURI());
		ClassLoadingUtil.setInitByClient(true);
		ClassLoadingUtil.cleanAntClassLoader();
		ClassLoadingUtil.init(model.getWebProjectName());
		axisServiceInstance = generator.getAxisService(model.getWsdlURI(),model.getServiceQName(),model.getPortName());
		Map optionsMap = generator.fillOptionMap(	
			model.isASync(), //async
            model.isSync(), //sync
            false,  //servirside false always
            false,  //services.xml false always
            model.isTestCaseCheck(),
            model.isGenerateAllCheck(),
            (model.getServiceName()!=null)?model.getServiceName():null,
            (model.getPortName()!=null)?model.getPortName():null,
            (model.getDatabindingType().toLowerCase()!=null)
            			?model.getDatabindingType().toLowerCase()
            			:null,
            (model.getWsdlURI()!=null)?model.getWsdlURI():null,
            (model.getPackageText()!=null)?model.getPackageText():null,
            "java",
            tempCodegenOutputLocation,
            Axis2CoreUtils.getSourceFolder(model.getWebProjectName()),
            model.getNamespaseToPackageMapping(),
            false	//Serverside interface always false
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
        WSDLPropertyReader reader = new WSDLPropertyReader(model);
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
				.getMethod("setBaseURI", new Class[]{String.class});
		setBaseURIMethod.invoke(CodeGenConfigurationInstance, 
								new Object[]{generator.getBaseUri(model.getWsdlURI())});
		
		//Get the namespace from the AxisService and set it in the configuration 
		Class AxisServiceClass = ClassLoadingUtil
					.loadClassFromAntClassLoader("org.apache.axis2.description.AxisService");

		Method getTargetNamespaceMethod = AxisServiceClass.getMethod("getTargetNamespace", null);
		Object targetNamespace = getTargetNamespaceMethod.invoke(axisServiceInstance, null);
		
		Object stringReturn = null;
		if (model.getPackageText()!=null) {
			stringReturn = model.getPackageText();
		} else {
			Class URLProcessorClass = ClassLoadingUtil
						.loadClassFromAntClassLoader("org.apache.axis2.util.URLProcessor");
			Method makePackageNameMethod = URLProcessorClass
						.getMethod("makePackageName", new Class[]{String.class});
			stringReturn = makePackageNameMethod.invoke(null, new Object[]{targetNamespace});
		}

		
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
        ContentCopyUtils contentCopyUtils = new ContentCopyUtils(getEnvironment());
	    status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
	    							tempCodegenOutputLocation, 
	    							currentDynamicWebProjectDir, 
	    							monitor, 
	    							statusHandler);
      
	} catch (Exception e) {
		status = StatusUtils.errorStatus(NLS.bind(
											Axis2ConsumptionUIMessages.ERROR_CODEGEN_EXCEPTION,
											new String[]{" : "+e.getCause()}),
										e);
		environment.getStatusHandler().reportError(status); 
	} finally {
		if (transformerFactoryModified) {
			// restore to the original TransformerFactory
			if (transformerFactory == null) {
				System.clearProperty("javax.xml.transform.TransformerFactory"); 
			} else {
				System.setProperty("javax.xml.transform.TransformerFactory", transformerFactory); 
			}
		}
	}

		return status;
	}

}
