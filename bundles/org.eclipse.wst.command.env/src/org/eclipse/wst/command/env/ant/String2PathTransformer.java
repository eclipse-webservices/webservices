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

import org.eclipse.core.runtime.Path;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class String2PathTransformer implements Transformer {

	public Object transform(Object value) {				 
		Path path = new Path(value.toString());
		return path;		 
	}

}

