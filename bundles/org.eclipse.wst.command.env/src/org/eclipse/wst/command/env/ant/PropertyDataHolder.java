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
package org.eclipse.wst.command.env.ant;

import java.util.Map;

import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class PropertyDataHolder {
	   public AbstractDataModelOperation operation_;
	   public String transform_;
	   public String key_;
	   public String property_;
	   public String value_;		
	   public Map map_;
}

