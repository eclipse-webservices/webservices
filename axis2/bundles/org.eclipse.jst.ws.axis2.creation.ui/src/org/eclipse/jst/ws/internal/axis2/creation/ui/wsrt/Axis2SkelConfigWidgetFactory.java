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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070601   190505 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis2.creation.ui.wsrt;

import org.eclipse.jst.ws.axis2.creation.core.command.Axis2DefaultingCommand;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.internal.axis2.creation.ui.widgets.skeleton.WSDL2JAVASkelConfigWidget;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataContributor;

public class Axis2SkelConfigWidgetFactory implements INamedWidgetContributorFactory {

	private SimpleWidgetContributor  skelConfig1WidgetContrib;
	private DataModel                model;

	public Axis2SkelConfigWidgetFactory(){	  
	}

	public INamedWidgetContributor getFirstNamedWidget(){
		if( skelConfig1WidgetContrib == null ) init();
		return skelConfig1WidgetContrib;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor){
		if( skelConfig1WidgetContrib == null ) init();
		INamedWidgetContributor nextWidgetContrib = null;
		return nextWidgetContrib;
	}

	public void registerDataMappings(DataMappingRegistry dataRegistry){
		// Map the data model from the defaulting command to this widget factory.
		// The framework will actually to the call to getWebServiceDataModel in
		// the ExampleDefaultingCommand class and then call the setWebServiceDataModel
		// method in this class.
		dataRegistry.addMapping( Axis2DefaultingCommand.class, 
                             "WebServiceDataModel",  //$NON-NLS-1$
								             Axis2SkelConfigWidgetFactory.class );
	}

	public void setWebServiceDataModel( DataModel model ){
		this.model = model;
	}

	private void init(){
		WSDL2JAVASkelConfigWidget  skel1 = new WSDL2JAVASkelConfigWidget( model );

		skelConfig1WidgetContrib  = createWidgetContributor( 
				Axis2CreationUIMessages.LABEL_JAVA_2_WSDL_PAGE_HEADING,
				Axis2CreationUIMessages.LABEL_JAVA_2_WSDL_PAGE_SUB_HEADING, 
				skel1 );
	}

	private SimpleWidgetContributor createWidgetContributor(String title, 
															String description, 
															final WidgetDataContributor contributor){
		SimpleWidgetContributor widgetContrib  = new SimpleWidgetContributor();
		widgetContrib.setTitle(title);
		widgetContrib.setDescription(description);
		widgetContrib.setFactory( new WidgetContributorFactory(){
			public WidgetContributor create(){
				return contributor;
			}
		});

		return widgetContrib;
	}   
}
