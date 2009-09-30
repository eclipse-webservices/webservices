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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationProperty;


public class AnnotationProperty implements IAnnotationProperty
{
	private String annotationName;
	private String attributeName;
	private String value;
	private AttributeTypeEnum attributeType;
	
	/**
	 * Public constructor for AnnotationProperty objects
	 * 
	 * @param annotationName - String representation of the Name for the Annotation.
	 * @param attributeName - String representation of the Name for the Attribute.
	 * @param value - String representation of the Value for the Attribute.
	 */
	public AnnotationProperty(final String annotationName, final String attributeName, final String value, final AttributeTypeEnum attributeType)
	{
		this.annotationName = annotationName;
		this.attributeName = attributeName;
		this.value = value;
		this.attributeType = attributeType;
	}
		
	public String getAnnotationName()
	{
		return this.annotationName;
	}

	public String getAttributeName()
	{
		return this.attributeName;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
	public AttributeTypeEnum getAttributeType()
	{
		return this.attributeType;
	}
}
