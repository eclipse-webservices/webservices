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
			
			if (typesMap.containsKey("Service.TypeId")||typesMap.containsKey("Service.RuntimeId")||
					typesMap.containsKey("Service.ServerId"))
			  	prefix = "Service.";
			else
				prefix = "Client.";
			
			types.setTypeId((String)typesMap.get(prefix+"TypeId"));
			types.setRuntimeId((String)typesMap.get(prefix+"RuntimeId"));
			types.setServerId((String)typesMap.get(prefix+"ServerId"));			
		}
  }

	
}

