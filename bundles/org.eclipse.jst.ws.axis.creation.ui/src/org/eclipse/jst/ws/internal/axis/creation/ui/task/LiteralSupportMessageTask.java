/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;



import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;



public class LiteralSupportMessageTask extends SimpleCommand {

	private final String LABEL = "TASK_LABEL_LITERAL_SUPPORT_MESSAGE";
	private final String DESCRIPTION =
		"TASK_DESC_LITERAL_SUPPORT_MESSAGE";
	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;

	private JavaWSDLParameter javaWSDLParam_;

	public LiteralSupportMessageTask() {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId+".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setDescription(msgUtils_.getMessage(DESCRIPTION));
	    setName(msgUtils_.getMessage(LABEL));	
	}
	
	public LiteralSupportMessageTask(JavaWSDLParameter javaWSDLParam) {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId+".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setDescription(msgUtils_.getMessage(DESCRIPTION));
	    setName(msgUtils_.getMessage(LABEL));	
		javaWSDLParam_ = javaWSDLParam;

	}


	/**
	* Execute LiteralSupportMessageTask
	*/
	public Status execute(Environment env) {
        Status status = new SimpleStatus("");
		if (javaWSDLParam_ == null) {
		    status = new SimpleStatus("",coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
		    env.getStatusHandler().reportError(status);
		    return status;
		}

		String use = javaWSDLParam_.getUse();
		if(use != null && 
			use.equals(JavaWSDLParameter.USE_LITERAL)){
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_LITERAL_SUPPORT_MESSAGE"), Status.WARNING);
		    try
			{
		    	env.getStatusHandler().report(status);
		    }
		    catch(StatusException se)
			{
		    	status = new SimpleStatus("","User aborted",Status.ERROR);
		    }
		}
		
		return status;
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
	{
		javaWSDLParam_ = javaWSDLParam;
	}
	
}
