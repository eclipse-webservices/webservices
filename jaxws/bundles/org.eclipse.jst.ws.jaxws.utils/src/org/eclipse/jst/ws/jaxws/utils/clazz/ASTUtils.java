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
package org.eclipse.jst.ws.jaxws.utils.clazz;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

/**
 * Utility class that provides helper methods for {@link AST} processing.
 * 
 * @author Georgi Vachkov
 */
public class ASTUtils 
{
	private static ASTUtils instance;
	
	private ASTUtils() {
		// disable construction
	}
	
	/**
	 * @return create new instance for this utility
	 */
	public static ASTUtils getInstance() {
		if(instance==null) {
			instance = new ASTUtils(); 
		}
		return instance;
	}
	
	/**
	 * Creates AST tree out of {@link File} instance.
	 * @param sourceFile java class file  
	 * @param monitor progress monitor
	 * @return created {@link ASTNode}
	 * @throws IOException
	 */
	public ASTNode createAST(final File sourceFile, final IProgressMonitor monitor) throws IOException 
	{
		nullCheckParam(sourceFile, "sourceFile"); //$NON-NLS-1$
		
		final ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(FileUtils.getInstance().getFileContent(sourceFile).toCharArray());
		return parser.createAST(monitor);
	}

	/**
	 * Creates {@link AST} tree out of {@link IType} instance.
	 * @param sourceType java class
	 * @param monitor progress monitor
	 * @return created {@link ASTNode} instance
	 */
	public CompilationUnit createCompilationUnit(final ICompilationUnit sourceCu, final IProgressMonitor monitor)
	{
		nullCheckParam(sourceCu, "sourceCu"); //$NON-NLS-1$
		final ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(sourceCu);
		parser.setBindingsRecovery(false);
		return (CompilationUnit)parser.createAST(monitor);
	}		
	
	/**
	 * Finds {@link FieldDeclaration} in {@link TypeDeclaration} with name <code>fieldType.getElementName()</code>.
	 * 
	 * @param fieldType
	 * @param typeDeclaration
	 * @return found object or <code>null</code>
	 */
	public FieldDeclaration getFieldDeclaration(final IField fieldType, final TypeDeclaration typeDeclaration)
	{
		for (FieldDeclaration fd : typeDeclaration.getFields())
		{
			for (Object fr : fd.fragments())
			{
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) fr;
				if (vdf.getName().getFullyQualifiedName().equals(fieldType.getElementName()))
				{
					return fd;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private boolean compareMethodParams(final IMethod methodType, final MethodDeclaration md)
	{
		List<SingleVariableDeclaration> list = md.parameters();
		if (list.size() == methodType.getNumberOfParameters())
		{
			String[] parameterTypes = methodType.getParameterTypes();
			for (int i = 0; i < list.size(); i++)
			{
				if (!isSameParam(parameterTypes[i], (SingleVariableDeclaration) list.get(i)))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean isSameParam(final String type, final SingleVariableDeclaration svDecl)
	{
		return type.equals(getTypeSignature(svDecl));
	}
	
	/**
	 * Retrieves type signature from {@link SingleVariableDeclaration}. As {@link SingleVariableDeclaration} returns only the clean type in case
	 * declaration of type <tt>int paraArr[][]</tt> is in code the method adds array identifiers for type declaration int this case.
	 * 
	 * @param svDecl
	 * @return the signatyre for the type
	 */
	public String getTypeSignature(final SingleVariableDeclaration svDecl)
	{
		String typeName = Signature.createTypeSignature(svDecl.getType().toString(), false);

		if (svDecl.getExtraDimensions() > 0 && typeName.indexOf('[') == -1)
		{
			for (int i = 0; i < svDecl.getExtraDimensions(); i++)
			{
				typeName = "[" + typeName;// $JL-STR_CONCAT$ //$NON-NLS-1$
			}
		}

		return typeName;
	}
	
	/**
	 * Finds {@link MethodDeclaration} in {@link TypeDeclaration} having name <code>methodType.getElementName()</code>.
	 * 
	 * @param methodType
	 * @param typeDeclaration
	 * @return found object or <code>null</code>
	 */
	public  MethodDeclaration getMethodDeclaration(final IMethod methodType, final TypeDeclaration typeDeclaration)
	{
		for (MethodDeclaration md : typeDeclaration.getMethods())
		{
			if (md.getName().getFullyQualifiedName().equals(methodType.getElementName()))
			{
				if (compareMethodParams(methodType, md))
				{
					return md;
				}
			}
		}

		return null;
	}
	
	/**
	 * Retrieves {@link TypeDeclaration} from {@link CompilationUnit} with name <tt>className</tt>
	 * 
	 * @param className
	 * @param unit
	 * @return found {@link TypeDeclaration} or <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public AbstractTypeDeclaration getTypeDeclaration(final String className, final CompilationUnit unit)
	{
		AbstractTypeDeclaration typeDeclaration = null;
		// looking up the selected type
		for (Object typeObject : unit.types())
		{
			AbstractTypeDeclaration tmpTypeDeclaration = (AbstractTypeDeclaration) typeObject;
			if (tmpTypeDeclaration.getName().getFullyQualifiedName().equals(className))
			{
				typeDeclaration = tmpTypeDeclaration;
				break;
			}
		}
		
		if (typeDeclaration == null)
		{
			List<AbstractTypeDeclaration> list = unit.types();
			typeDeclaration = getInnerClass(list.toArray(new AbstractTypeDeclaration[list.size()]), className);
		}
		
		return typeDeclaration;
	}
	
	private AbstractTypeDeclaration getInnerClass(final AbstractTypeDeclaration[] types, final String className)
	{
		AbstractTypeDeclaration result = null;
		for (AbstractTypeDeclaration abstractType : types)
		{
			if (abstractType.getName().getFullyQualifiedName().equals(className)) {
				return abstractType;
			}

			if (!(abstractType instanceof TypeDeclaration)) {
				continue;
			}
			
			result = getInnerClass((AbstractTypeDeclaration[]) ((TypeDeclaration) abstractType).getTypes(), className);
			if (result != null) {
				return result;
			}
		}

		return null;
	}	
}
