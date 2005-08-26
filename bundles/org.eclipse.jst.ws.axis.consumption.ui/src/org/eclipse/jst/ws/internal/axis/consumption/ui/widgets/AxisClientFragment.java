/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.Stub2BeanCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Condition;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;


public class AxisClientFragment extends SequenceFragment 
{

	public AxisClientFragment() 
	{
		//add(new SimpleFragment(new AxisClientDefaultingCommand(), ""));
		add(new SimpleFragment("AxisClientStart"));
		add(new MappingFragment());
		add(new AxisClientCommandsFragment());
		// TODO: Look into AddWSDLtoWSILCommand for the client scenario
	}
	
	public void registerDataMappings(DataMappingRegistry registry) 
	{
		// AxisClientDefaultingCommand
		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProject", AxisClientDefaultingCommand.class, "ClientProject",
				new StringToIProjectTransformer());
		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientRuntime", AxisClientDefaultingCommand.class, "ClientRuntimeID",
				null);
		registry.addMapping(ClientExtensionDefaultingCommand.class, "WebServicesParser", AxisClientDefaultingCommand.class);
		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientProjectEAR", AxisClientDefaultingCommand.class,
				"ClientProjectEAR", new StringToIProjectTransformer());
		registry.addMapping(ClientExtensionDefaultingCommand.class, "WsdlURI", AxisClientDefaultingCommand.class, "WsdlURL", null); // URI
																																	// to
		// URL
		// transformer
		// req'd??
		registry.addMapping(ClientExtensionDefaultingCommand.class, "TestProxySelected", AxisClientDefaultingCommand.class,
				"TestProxySelected", null);
		registry.addMapping(ClientExtensionDefaultingCommand.class, "ClientServer", AxisClientDefaultingCommand.class);
		registry.addMapping(ClientExtensionDefaultingCommand.class, "IsClientScenario", AxisClientDefaultingCommand.class);
		registry.addMapping(ClientExtensionDefaultingCommand.class, "GenerateProxy", AxisClientDefaultingCommand.class);
		// DefaultsForHTTPBasicAuthCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", DefaultsForHTTPBasicAuthCommand.class); //OK
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", DefaultsForHTTPBasicAuthCommand.class, "WsdlServiceURL", null); //OK
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", DefaultsForHTTPBasicAuthCommand.class); //OK
	    registry.addMapping(AxisClientDefaultingCommand.class, "CustomizeClientMappings", MappingFragment.class );    
		
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", CopyAxisJarCommand.class, "Project", null);
		// AddJarsToProjectBuildPathTask()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", AddJarsToProjectBuildPathTask.class, "Project", null);
		// DefaultsForClientJavaWSDLCommand() // javaParam_, model_
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", DefaultsForClientJavaWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", DefaultsForClientJavaWSDLCommand.class, "ProxyProject",
				null);
		//		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL",
		// DefaultsForClientJavaWSDLCommand.class,"WSDLServicePathname",null);
		// //
		// URL to URL??
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", DefaultsForClientJavaWSDLCommand.class, "WSDLServiceURL", null); // URI
		// to
		// URL??
		// ValidateWSDLCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", ValidateWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlServiceURL", ValidateWSDLCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", ValidateWSDLCommand.class);
		// WSDL2JavaCommand() // javaParam_
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WsdlURL", WSDL2JavaCommand.class, "WsdlURI", null); // URL
																													// to
																													// URI??
		// RefreshProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", RefreshProjectCommand.class, "Project", null);
		// Stub2BeanCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "JavaWSDLParam", Stub2BeanCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "WebServicesParser", Stub2BeanCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", Stub2BeanCommand.class);
		// BuildProjectCommand()
		registry.addMapping(AxisClientDefaultingCommand.class, "ClientProject", BuildProjectCommand.class, "Project", null);
		registry.addMapping(AxisClientDefaultingCommand.class, "ForceBuild", BuildProjectCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "ValidationManager", BuildProjectCommand.class);
		registry.addMapping(Stub2BeanCommand.class, "ProxyBean", ClientExtensionOutputCommand.class, "ProxyBean", null);
		registry.addMapping(AxisClientDefaultingCommand.class, "GenerateProxy", AxisClientCommandsFragment.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "GenerateProxy", ClientExtensionOutputCommand.class);
		registry.addMapping(AxisClientDefaultingCommand.class, "SetEndpointMethod", ClientExtensionOutputCommand.class);
	}

	public class MappingFragment extends BooleanFragment {

		private boolean showMappings_;
		public MappingFragment() {

			super();
			setTrueFragment(new SimpleFragment("AxisClientBeanMapping"));
			setCondition(new Condition() {

				public boolean evaluate() {

					return showMappings_;
				}
			});
		}
		public void setCustomizeClientMappings(boolean showMappings) {

			showMappings_ = showMappings;
		}
	}
}