/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.visitor;

import org.eclipse.xsd.XSDElementDeclaration;

/*
 * Class used to find and return the "inner" XSDElement when given an XSDElement.
 * This class extends W11XSDVisitor which removes the need for this class to reimplement
 * the actual traversal of the XSD Model to locate the inner XSDElement.
 * 
 * By default, this class will locate the inner XSDElement at depth 2.  However, this depth
 * can be changed by calling setElementDepth(newDepth).
 */
public class W11FindInnerElementVisitor extends W11XSDVisitor {
	  protected XSDElementDeclaration elementAtDepth;
	  protected int elementDepth = 2;
	  protected int depth = 1;
	  
	  public XSDElementDeclaration getInnerXSDElement(XSDElementDeclaration xsdElement) {
		  elementAtDepth = xsdElement;
		  depth = 1;
		  visitElementDeclaration(xsdElement);
		  
		  return elementAtDepth;
	  }
	  
	  public void visitElementDeclaration(XSDElementDeclaration element) {
		  if (depth == elementDepth) {
			  elementAtDepth = element;
		  }
		  depth++;
		  
		  super.visitElementDeclaration(element);
	  }
	  
	  public void setElementDepth(int elementDepth) {
		  this.elementDepth = elementDepth; 
	  }
}
