/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core.model;

import java.util.Map;
import org.eclipse.emf.common.util.EList;

/**
 * @model
 */
public interface WSDL2JavaDataModel extends CXFDataModel, WSDL2JavaContext {
    /**
     * Returns the package names to use for the generated code.
     * 
     * @model
     */
    Map<String, String> getIncludedNamespaces();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getIncludedNamespaces <em>Included Namespaces</em>}' attribute.
     * <!-- begin-user-doc -->
     * Specifies zero, or more, package names to use for the generated code.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Included Namespaces</em>' attribute.
     * @see #getIncludedNamespaces()
     * @generated
     */
    void setIncludedNamespaces(Map<String, String> value);

    /**
     * Returns a List of the JAXWS or JAXB binding files used, if any.
     * 
     * @model
     */
    EList<String> getBindingFiles();

    /**
     * Returns the java package to excluded namespace mappings.
     * 
     * @model
     */
    Map<String, String> getExcludedNamespaces();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getExcludedNamespaces <em>Excluded Namespaces</em>}' attribute.
     * <!-- begin-user-doc -->
     * Ignore the specified WSDL schema namespace when generating code.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Excluded Namespaces</em>' attribute.
     * @see #getExcludedNamespaces()
     * @generated
     */
    void setExcludedNamespaces(Map<String, String> value);

    /**
     * Returns the catalog file used to map the imported wsdl/schema
     * @model
     */
    String getCatalogFile();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getCatalogFile <em>Catalog File</em>}' attribute.
     * <!-- begin-user-doc -->
     * Specify catalog file to map the imported wsdl/schema.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Catalog File</em>' attribute.
     * @see #getCatalogFile()
     * @generated
     */
    void setCatalogFile(String value);

    /**
     * Returns the value of the Java Source Directory into which the files are generated.
     * 
     * @model
     */
    String getJavaSourceFolder();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getJavaSourceFolder <em>Java Source Folder</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the value of the Java Source Directory into which the files are generated.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Java Source Folder</em>' attribute.
     * @see #getJavaSourceFolder()
     * @generated
     */
    void setJavaSourceFolder(String value);

}
