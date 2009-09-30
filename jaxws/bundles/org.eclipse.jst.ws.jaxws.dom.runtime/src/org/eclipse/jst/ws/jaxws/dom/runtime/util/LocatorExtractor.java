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
package org.eclipse.jst.ws.jaxws.dom.runtime.util;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;


/**
 * Class that extracts {@link ILocator} out of DOM object
 * 
 * @author Georgi Vachkov
 */
public class LocatorExtractor 
{
	private static LocatorExtractor instance;
	
	private LocatorExtractor() {
		// singleton instance
	}
	
	/**
	 * Extracts the {@link ILocator}  
	 * @param eObject
	 * @return {@link ILocator} instance or <code>null</code> in case the {@link IJavaElement} 
	 * cannot be found for this <code>eObject</code>.
	 * @throws JavaModelException
	 * @throws BadLocationException
	 * @throws IllegalArgumentException in case <code>eObject</code> is DOM object for which
	 * this method is not implemented 
	 */
	public ILocator find(final EObject eObject) throws JavaModelException, BadLocationException 
	{
		final IType seiType = findIType(eObject);
		if (seiType == null) {
			return null;
		}
		
		switch(eObject.eClass().getClassifierID()) 
		{
		case DomPackage.IWEB_METHOD:
			return getLocatorForMethod(seiType, (IWebMethod)eObject);
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE:
			return getPosition(seiType);
		case DomPackage.IWEB_SERVICE:
			return getPosition(seiType);
		case DomPackage.IWEB_PARAM:
			return getLocatorForMethod(seiType, (IWebMethod)eObject.eContainer());
		}
		
		throw new IllegalArgumentException("Unknown object type");//$NON-NLS-1$
	}
	
	private ILocator getLocatorForMethod(final IType seiType, final IWebMethod webMethod) throws JavaModelException, BadLocationException
	{
		final IMethod method = DomUtil.INSTANCE.findMethod(seiType, webMethod);
		if (method!=null) {
			return getPosition(method);
		}
		
		return null;
	}
	
	private IType findIType(final EObject eObject) throws JavaModelException {
		return Dom2ResourceMapper.INSTANCE.findType(eObject);
	}
	
	private ILocator getPosition(final IMember member) throws JavaModelException, BadLocationException 
	{
		final int offset = member.getNameRange().getOffset();
		final int length = member.getNameRange().getLength();
		final int lineNumber = getLineNumber(member);
		
		return new ILocator() 
		{			
			public int getStartPosition() {
				return offset;
			}

			public int getLength() {				
				return length;
			}

			public int getLineNumber() {
				return lineNumber;
			}
		};
	}
	
	private int getLineNumber(final IMember member) throws JavaModelException, BadLocationException
	{
		final IDocument doc = new Document(member.getCompilationUnit().getBuffer().getContents());
		
		return doc.getLineOfOffset(member.getNameRange().getOffset()) + 1;
	}
	
	/**
	 * @return the singleton instance
	 */
	public static LocatorExtractor getInstance()
	{
		if (instance==null) {
			instance = new LocatorExtractor();
		}
		
		return instance;
	}
}
