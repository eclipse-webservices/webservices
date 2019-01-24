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
package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.runtime.Path;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

/**
 * 
 * Transforms a input String to a Path
 * 
 * @author joan
 *
 */

public class String2PathTransformer implements Transformer {
	
	public Object transform(Object value) {				 
		Path path = new Path(value.toString());
		return path;		 
	}

}

