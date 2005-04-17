/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebService;
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebServiceInfo;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class AxisOutputCommand extends SimpleCommand {

	private static String LABEL = "TASK_LABEL_BU_AXIS_OUTPUT";
	private static String DESCRIPTION = "TASK_DESC_BU_AXIS_OUTPUT";
	
	private AxisWebService ws_;
	private JavaWSDLParameter javaWSDLParam_;

	private String            wsdlURI_;
	  
	  private boolean isWebProjectStartupRequested_ = false;
	  
	  private MessageUtils msgUtils_;
	  
		/**
		* Default CTOR
		*/
		public AxisOutputCommand() {
			String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
		}
		
		public AxisOutputCommand(AxisWebService ws) {
			String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
			ws_ = ws;
		}
		
		public Status execute(Environment env){
		  	
		  	Status status = new SimpleStatus("");  	
		  	ws_.getWebServiceInfo().setWsdlURL(wsdlURI_);
			AxisWebServiceInfo axisWSInfo = new AxisWebServiceInfo();
			axisWSInfo.setJavaWSDLParameter(javaWSDLParam_);
			ws_.setAxisWebServiceInfo(axisWSInfo);		    
		  	
		  	return status;      	
		  }

		  /**
		   * @param wsdlURI
		   *            The wsdlURI to set.
		   */
		  public void setWsdlURI(String wsdlURI)
		  {
		    wsdlURI_ = wsdlURI;
		  }

		public void setJavaWSDLParam (JavaWSDLParameter javaWSDLParam_) {
			this.javaWSDLParam_ = javaWSDLParam_;
		}
		  
}
