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

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.ValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolver;

/**
 * An implemenation of WSDL11ValidationInfo.
 */
public class WSDL11ValidationInfoImpl implements WSDL11ValidationInfo
{
  private ValidationInfo valinfo = null;
  private Hashtable elementlocations = null;
  private List schemas = new Vector();
  
  public WSDL11ValidationInfoImpl(ValidationInfo valinfo)
  {
    this.valinfo = valinfo;
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#getFileURI()
   */
  public String getFileURI()
  {
    return valinfo.getFileURI();
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#addSchema(org.apache.xerces.xs.XSModel)
   */
  public void addSchema(XSModel xsModel)
  {
    if (xsModel != null)
    {
      schemas.add(xsModel);
    }

  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#getSchemas()
   */
  public XSModel[] getSchemas()
  {
    return (XSModel[])schemas.toArray(new XSModel[schemas.size()]);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#cleardSchemas()
   */
  public void clearSchemas()
  {
    schemas.clear();
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#setElementLocations(java.util.Hashtable)
   */
  public void setElementLocations(Hashtable elementLocations)
  {
    this.elementlocations = elementLocations;
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#addError(java.lang.String, java.lang.Object)
   */
  public void addError(String message, Object element)
  {
    LocationHolder location;
    if (elementlocations.containsKey(element))
    {
      location = (LocationHolder)elementlocations.get(element);
      addError(message, location.getLine(), location.getColumn(), location.getURI());
    }
    // if we give it an element that hasn't been defined we'll set the location
    // at (0,0) so the error shows up but no line marker in the editor
    else
    {
      addError(message, 0, 1, getFileURI());
    }
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#addWarning(java.lang.String, java.lang.Object)
   */
  public void addWarning(String message, Object element)
  {
    LocationHolder location;
    if (elementlocations.containsKey(element))
    {
      location = (LocationHolder)elementlocations.get(element);
      addWarning(message, location.getLine(), location.getColumn(), location.getURI());
    }
    // if we give it an element that hasn't been defined we'll set the location
    // at (0,0) so the error shows up but no line marker in the editor
    else
    {
      addWarning(message, 0, 1, getFileURI());
    }

  }

  /**
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#addNamespaceWithNoValidator(java.lang.String)
   */
//  public void addNamespaceWithNoValidator(String namespace)
//  {
//    valinfo.addNamespaceWithNoValidator(namespace);
//
//  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#addError(java.lang.String, int, int)
   */
  public void addError(String message, int line, int column, String uri)
  {
    valinfo.addError(message, line, column, uri);
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo#addWarning(java.lang.String, int, int)
   */
  public void addWarning(String message, int line, int column, String uri)
  {
    valinfo.addWarning(message, line, column, uri);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#getURIResolver()
   */
  public IURIResolver getURIResolver() 
  {
	return valinfo.getURIResolver();
  }
}
