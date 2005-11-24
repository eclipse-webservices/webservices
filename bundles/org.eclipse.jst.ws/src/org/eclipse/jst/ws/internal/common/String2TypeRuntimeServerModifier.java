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
package org.eclipse.jst.ws.internal.common;

import java.util.Map;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.data.BeanModifier;

public class String2TypeRuntimeServerModifier implements BeanModifier {

	private String SERVICE_RT_ID_KEY = "Service.RuntimeId";  //$NON-NLS-N$
	private String RUNTIME_ID = "RuntimeId";  //$NON-NLS-N$
	private String SERVICE_SRV_ID_KEY = "Service.ServerId";  //$NON-NLS-N$
	private String SERVER_ID = "ServerId";  //$NON-NLS-N$
	private String SERVICE_PREFIX = "Service.";  //$NON-NLS-N$
	private String CLIENT_PREFIX = "Client.";  //$NON-NLS-N$
	
	/**
	 * Modifies the supplied TypeRuntimeServer bean with properties in holder.
	 * If the bean is null, construct a new one and set its properties.
	 * @param bean TypeRuntimeServer bean to be modified
	 * @param holder Map containing values for TypeRuntimeServer properties 
	 * @return TypeRuntimeServer with properties set
	 */
	public void modify(Object bean, Object holder) {
		
		TypeRuntimeServer types = null;
		if (bean == null || !(bean instanceof TypeRuntimeServer))
		{
			types = new TypeRuntimeServer();			
		}
		else
		{
			types = (TypeRuntimeServer)bean;
		}
		
		if (holder instanceof Map)
		{	
			Map typesMap = (Map)holder;
			String prefix = "";
			
			
			if (typesMap.containsKey(SERVICE_RT_ID_KEY)||typesMap.containsKey(SERVICE_SRV_ID_KEY))
			{
				prefix = SERVICE_PREFIX;
			}			  	
			else
			{
				prefix = CLIENT_PREFIX;
			}
			
			String runtimeID = (String)typesMap.get(prefix+RUNTIME_ID);
			if (runtimeID != null)
				types.setRuntimeId(runtimeID);		
			
			String serverID = (String)typesMap.get(prefix+SERVER_ID);
			if (serverID  != null)				
				types.setServerId(serverID);			
		}
  }

	
}

