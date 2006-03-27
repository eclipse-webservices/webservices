/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.extensions;

import org.eclipse.ui.IEditorPart;

public interface WSDLEditorExtension
{                                          
  public static final int OUTLINE_TREE_CONTENT_PROVIDER = 1;    // req'd for model based ext
  public static final int OUTLINE_LABEL_PROVIDER = 2;           // req'd for model based ext

  public static final int MENU_ACTION_CONTRIBUTOR = 3;          // node
  public static final int DETAILS_VIEWER_PROVIDER = 4;          // node

  public static final int EDIT_PART_FACTORY = 5;                // req'd for model based ext
  public static final int TYPE_SYSTEM_PROVIDER = 6;             // req'd for type system ext
  public static final int NODE_RECONCILER = 7;                  // req'd for model based ext
  public static final int NODE_ASSOCIATION_PROVIDER = 8;        // req'd for model based ext
  public static final int MODEL_QUERY_CONTRIBUTOR = 9;          // req'd for model based ext

  public static final int PROPERTY_SOURCE_PROVIDER = 10;        // req'd for model based ext
  public static final int PROPERTY_SECTION_DESCRIPTOR_PROVIDER = 11;  // req'd for model based ext
  public static final int XSD_DELETE_ACTION = 12;
  
  boolean isExtensionTypeSupported(int type);
  boolean isApplicable(Object modelObject);
  Object createExtensionObject(int type, IEditorPart editor);
}