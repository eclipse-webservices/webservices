/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.provisional.env.core.data;

public interface DataMappingRegistry
{
  /**
   * This method adds a data mapping from a source object to a target
   * object.  When the sourceObject is encountered by the framework the
   * sourceProperty will be called and the data will be passed to the
   * targetProperty in the targetObject.  If a transformer object is
   * specified the sourceObject is transformed before being passed to
   * the target object.
   * 
   * @param sourceType      The source object.
   * @param sourceProperty  The source property.
   * @param targetType      The target object.
   * @param targetProperty  The target property.
   * @param transformer     The transformer object that transforms the
   *                        the source object.
   */
  public void addMapping( Class       sourceType,
						  String      sourceProperty,
						  Class       targetType,
						  String      targetProperty,
						  Transformer transformer );
  
  /**
   * This method is equivalent to the above with targetProperty the same
   * as the sourceProperty and with the transformer set to null.
   * 
   * @param sourceType     The source object.
   * @param sourceProperty The source property.
   * @param targetType     The target object.
   */
  public void addMapping( Class       sourceType,
	                      String      sourceProperty,
	                      Class       targetType );
}
