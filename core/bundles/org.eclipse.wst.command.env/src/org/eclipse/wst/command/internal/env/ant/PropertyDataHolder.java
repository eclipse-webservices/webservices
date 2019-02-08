/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ant;

/**
 * Simple class to hold data mapping information once retrieved from Ant property file.
 * Map is used for many to one mappings involving mapping of multiple properties to a bean.
 * In this case, the key_ and value_ should be set to an empty string and all key value pairs
 * for properties put into the map instead.  The transformation for this case would be a modifier
 * which will set the properties onto the bean.
 */
 
import java.util.Map;

import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class PropertyDataHolder {
	   public AbstractDataModelOperation operation_;
	   public Object transform_;
	   public String key_;
	   public String property_;
	   public String value_;		
	   public Map map_;
}

