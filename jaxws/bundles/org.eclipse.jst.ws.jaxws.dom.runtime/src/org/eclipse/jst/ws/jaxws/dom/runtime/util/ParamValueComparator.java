/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.util;

import java.util.Comparator;
import java.util.TreeSet;

import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Class that compares {@link IParamValuePair} by property name. Intended to be used when 
 * creating a {@link TreeSet} for annotation properties.
 * 
 * @author Georgi Vachkov
 */
public class ParamValueComparator implements Comparator<IParamValuePair>
{		
	public enum Order {Normal, Reversed};

	private final Order order;
	
	public ParamValueComparator() {
		order = Order.Normal;
	}
	
	public ParamValueComparator(final Order order) {
		this.order = order;
	}

	
	public int compare(IParamValuePair o1, IParamValuePair o2) 
	{
		if(order==Order.Normal) {
			return o1.getParam().compareTo(o2.getParam());
		}
		
		return o2.getParam().compareTo(o1.getParam());
	}		
}	