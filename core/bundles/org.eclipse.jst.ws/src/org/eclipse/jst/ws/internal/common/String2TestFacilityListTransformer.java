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

import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;

public class String2TestFacilityListTransformer implements Transformer {

	public Object transform(Object value) {
		String testFacility = (String)value;
		//value represents name of a test facility
		//pick up list of test facilities
		String[] testTypes = WebServicePlugin.getInstance().getScenarioContext().getWebServiceTestTypes();        	
		
		int index = 0;
		//match value to name of test facility and set index accordingly
		for (int i = 0; i < testTypes.length; i++) {
			if (testTypes[i].equals(testFacility))
			{		
				break;
			}			
			index++;
		}
		
		SelectionList selectionList = new SelectionList(testTypes, index);		
		return selectionList;
	}
}


