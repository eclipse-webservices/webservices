/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.clazz.ASTUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.jst.ws.jaxws.utils.resources.EditResourcesManager;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.IFileUtils;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * Contains utilities for getting AST objects needed in annotation processing
 * 
 * @author Plamen Pavlov
 */
public class AnnotationUtils
{
	private static AnnotationUtils utils = null;
	private final IFileUtils fileUtils = FileUtils.getInstance();

	/**
	 * The factory method.
	 * 
	 * @return a AnnotationUtils instance.
	 */
	public static AnnotationUtils getInstance()
	{
		if (utils == null)
		{
			utils = new AnnotationUtils();
		}
		return utils;
	}
	
	/**
	 * Gets IDocument, which coresponds to one CompilationUnit.
	 * 
	 * @param unit -
	 *            Compilation Unit, which will be used as starting point to get the IDocument.
	 * 
	 * @return IDocument which is found.
	 * 
	 * @throws CoreException
	 */
	public IDocument getDocument(ICompilationUnit unit) throws CoreException
	{
		String source = unit.getBuffer().getContents(); 
		return new Document(source); 
	}
	
	/**
	 * Removes Annotations of specified IJavaElement.
	 * 
	 * @param javaElement - IJavaElement which will be used as starting poin for removing the annotations.
	 * @param annotations - Set which contains String values for the annotations which will be removed. If this Set is empty all Annotations will be removed.
	 * @param deepRemove - Boolean value, representing whether Annotations from javaElemet childs will be removed. 
	 * @throws AnnotationGeneratorException 
	 * 
	 * @throws AnnotationGeneratorException
	 *             thown if AST TypeDeclaration cannot be found for <code>type</code> and also is used as wrapper in case {@link CoreException} or
	 *             {@link BadLocationException} is thrown while processing.
	 * @throws BadLocationException 
	 * @throws CoreException 
	 * @throws FileNotFoundException 
	 * @throws MalformedTreeException 
	 */
	public void removeAnnotations(IJavaElement javaElement, Set<String> annotations, boolean deepRemove) throws AnnotationGeneratorException, MalformedTreeException, FileNotFoundException, CoreException, BadLocationException
	{
		if(javaElement instanceof IType)
		{
			IType type = (IType)javaElement;
			ICompilationUnit cUnit = type.getCompilationUnit();
			String className = type.getElementName();
			CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(cUnit, null);

			AbstractTypeDeclaration typeDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, unit);
			if (typeDeclaration == null)
			{
				throw new AnnotationGeneratorException("typeDeclaration should not be null",//$NON-NLS-1$
							MessageFormat.format(JaxWsUtilMessages.AnnotationUtils_ParamShouldNotBeNullMsg, "typeDeclaration")); //$NON-NLS-1$
			}
			
			removeTypeAnnotations(type, typeDeclaration, unit, cUnit, annotations);

			if(!deepRemove)
			{
				return;
			}
			
			IField[] tmpFields = type.getFields();
			if (tmpFields != null && tmpFields.length > 0)
			{
				for (IField tmpField : tmpFields)
				{
					removeFieldAnnotations(tmpField, type, cUnit, annotations);
				}
			}
			
			IMethod[] tmpMethods = type.getMethods();
			if (tmpMethods != null && tmpMethods.length > 0)
			{
				for (IMethod tmpMethod : tmpMethods)
				{
					removeMethodAnnotations(tmpMethod, type, cUnit, annotations);

					//Remove params annotations
					for (String paramName : tmpMethod.getParameterNames())
					{
						ITypeParameter typeParameter = tmpMethod.getTypeParameter(paramName);
						removeParamAnnotations(typeParameter, type, cUnit, annotations, paramName);			
					}
				}
			}
		}
		
		if(javaElement instanceof IField)
		{
			IField field = (IField)javaElement;
			IType declaringType = field.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();
			
			removeFieldAnnotations(field, declaringType, declaringTypeCompUnit, annotations);
		}
		
		if(javaElement instanceof IMethod)
		{
			IMethod method = (IMethod)javaElement;
			IType declaringType = method.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();
			
			removeMethodAnnotations(method, declaringType, declaringTypeCompUnit, annotations);
			if(!deepRemove)
			{
				return;
			}

			//Remove params annotations
			for (String paramName: method.getParameterNames())
			{
			  ITypeParameter param = method.getTypeParameter(paramName);
			  removeParamAnnotations(param, declaringType, declaringTypeCompUnit, annotations, paramName);
			}
		}
		
		if(javaElement instanceof ITypeParameter)
		{
			ITypeParameter typeParameter = (ITypeParameter)javaElement;
			IMethod tmpMethod = (IMethod) typeParameter.getDeclaringMember();
			//TODO check this
			//param.getParent();
			IType declaringType = tmpMethod.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();
			//TODO FIX THIS
			removeParamAnnotations(typeParameter, declaringType, declaringTypeCompUnit, annotations, null);
		}
	}
	
	private void removeTypeAnnotations(IType type, AbstractTypeDeclaration typeDeclaration, CompilationUnit unit, ICompilationUnit cUnit, Set<String> annotationsSet) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		unit.recordModifications();
		if (removeAnnotations(typeDeclaration.modifiers(), annotationsSet))
		{
			IDocument doc = AnnotationUtils.getInstance().getDocument(cUnit);
			TextEdit edit = unit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(cUnit, doc);
		}
	}

	private void removeFieldAnnotations(IField field, IType declaringType, ICompilationUnit declaringTypeCompUnit, Set<String> annotationsSet) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		ICompilationUnit fcUnit = field.getCompilationUnit();
		String className = declaringType.getElementName();
		CompilationUnit fUnit = ASTUtils.getInstance().createCompilationUnit(fcUnit, null);

		BodyDeclaration fBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, fUnit);
		fBodyDeclaration = ASTUtils.getInstance().getFieldDeclaration(field, (TypeDeclaration) fBodyDeclaration);

		fUnit.recordModifications();
		if (removeAnnotations(fBodyDeclaration.modifiers(), annotationsSet))
		{
			IDocument doc = getDocument(declaringTypeCompUnit);
			TextEdit edit = fUnit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(declaringTypeCompUnit, doc);
		}
	}

	private void removeMethodAnnotations(IMethod method, IType declaringType, ICompilationUnit declaringTypeCompUnit, Set<String> annotationsSet) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		ICompilationUnit mcUnit = method.getCompilationUnit();
		String className = declaringType.getElementName();
		CompilationUnit mUnit = ASTUtils.getInstance().createCompilationUnit(mcUnit, null);

		BodyDeclaration mBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, mUnit);
		mBodyDeclaration = ASTUtils.getInstance().getMethodDeclaration(method, (TypeDeclaration) mBodyDeclaration);
		mUnit.recordModifications();
		if (removeAnnotations(mBodyDeclaration.modifiers(), annotationsSet))
		{
			IDocument doc = getDocument(declaringTypeCompUnit);
			TextEdit edit = mUnit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(declaringTypeCompUnit, doc);
		}
	}

	private void removeParamAnnotations(ITypeParameter param, IType declaringType, ICompilationUnit declaringTypeCompUnit, Set<String> annotationsSet, String paramName) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		IMethod tmpMethod = (IMethod)param.getDeclaringMember();
		//TODO check this
		//param.getParent();
		String className = declaringType.getElementName();
		ICompilationUnit mcUnit = tmpMethod.getCompilationUnit();
		CompilationUnit mUnit = ASTUtils.getInstance().createCompilationUnit(mcUnit, null);

		BodyDeclaration mBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, mUnit);
		mBodyDeclaration = ASTUtils.getInstance().getMethodDeclaration(tmpMethod, (TypeDeclaration) mBodyDeclaration);
		mUnit.recordModifications();

		for (Object object : ((MethodDeclaration) mBodyDeclaration).parameters())
		{
			if (object instanceof SingleVariableDeclaration)
			{
			  SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
				if ((paramName == null || svd.getName().toString().equals(paramName)) &&
				        removeAnnotations(svd.modifiers(), annotationsSet))
				{
					IDocument doc = getDocument(declaringTypeCompUnit);
					TextEdit edit = mUnit.rewrite(doc, null);
					edit.apply(doc);
					
					saveContent(declaringTypeCompUnit, doc);
				}
			}
		}
	}
	
	private void saveContent(ICompilationUnit cUnit, IDocument doc) throws FileNotFoundException, JavaModelException, AnnotationGeneratorException
	{
		IStatus status = (new EditResourcesManager()).setFileEditable((IFile) cUnit.getResource());
		if(status.getSeverity() == IStatus.OK )
		{
			fileUtils.setCompilationUnitContentAndSaveDirtyEditors(cUnit, doc.get(), true, null);
		}
		else
		{
			throw new AnnotationGeneratorException("Annotattion could not be stored, the file is not writable", //$NON-NLS-1$
											JaxWsUtilMessages.AnnotationCannotBeStoredMsg);
		}
	}
	
	/**
	 * Removes specific Annotation of specified IJavaElement.
	 * 
	 * @param javaElement - IJavaElement which will be used as starting poin for removing the Annotation.
	 * @param annotationName - String which is the representation Name of the Annotation, which will be removed.
	 * 
	 * @throws AnnotationGeneratorException 
	 * @throws AnnotationGeneratorException
	 *             thown if AST TypeDeclaration cannot be found for <code>type</code> and also is used as wrapper in case {@link CoreException} or
	 *             {@link BadLocationException} is thrown while processing.
	 * @throws BadLocationException 
	 * @throws CoreException 
	 * @throws FileNotFoundException 
	 * @throws MalformedTreeException 
	 */
	public void removeAnnotation(IJavaElement javaElement, String annotationName) throws AnnotationGeneratorException, FileNotFoundException, CoreException, BadLocationException
	{
		if(javaElement instanceof IType)
		{
			IType type = (IType)javaElement;
			ICompilationUnit cUnit = type.getCompilationUnit();
			String className = type.getElementName();
			CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(cUnit, null);

			AbstractTypeDeclaration typeDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, unit);
			if (typeDeclaration == null)
			{
				throw new AnnotationGeneratorException("typeDeclaration should not be null", //$NON-NLS-1$
												MessageFormat.format(JaxWsUtilMessages.AnnotationUtils_ParamShouldNotBeNullMsg, "typeDeclaration")); //$NON-NLS-1$
			}
			
			removeTypeAnnotation(type, typeDeclaration, unit, cUnit, annotationName);

		}
		
		if(javaElement instanceof IField)
		{
			IField field = (IField)javaElement;
			IType declaringType = field.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();
			
			removeFieldAnnotation(field, declaringType, declaringTypeCompUnit, annotationName);
		}
		
		if(javaElement instanceof IMethod)
		{
			IMethod method = (IMethod)javaElement;
			IType declaringType = method.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();
			
			removeMethodAnnotation(method, declaringType, declaringTypeCompUnit, annotationName);
		}
		
		if(javaElement instanceof ITypeParameter)
		{
			ITypeParameter typeParameter = (ITypeParameter)javaElement;
			IMethod tmpMethod = (IMethod) typeParameter.getDeclaringMember();
			//TODO check this
			//param.getParent();
			IType declaringType = tmpMethod.getDeclaringType();
			ICompilationUnit declaringTypeCompUnit = declaringType.getCompilationUnit();

			removeParamAnnotation(typeParameter, declaringType, declaringTypeCompUnit, annotationName);
		}
	}
	
	private void removeTypeAnnotation(IType type, AbstractTypeDeclaration typeDeclaration, CompilationUnit unit, ICompilationUnit cUnit, String annotationName) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		unit.recordModifications();
		if (removeAnnotation(typeDeclaration.modifiers(), annotationName))
		{
			IDocument doc = AnnotationUtils.getInstance().getDocument(cUnit);
			TextEdit edit = unit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(cUnit, doc);
		}
	}

	private void removeFieldAnnotation(IField field, IType declaringType, ICompilationUnit declaringTypeCompUnit, String annotationName) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		ICompilationUnit fcUnit = field.getCompilationUnit();
		String className = declaringType.getElementName();
		CompilationUnit fUnit = ASTUtils.getInstance().createCompilationUnit(fcUnit, null);

		BodyDeclaration fBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, fUnit);
		fBodyDeclaration = ASTUtils.getInstance().getFieldDeclaration(field, (TypeDeclaration) fBodyDeclaration);

		fUnit.recordModifications();
		if (removeAnnotation(fBodyDeclaration.modifiers(), annotationName))
		{
			IDocument doc = getDocument(declaringTypeCompUnit);
			TextEdit edit = fUnit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(declaringTypeCompUnit, doc);
		}
	}

	private void removeMethodAnnotation(IMethod method, IType declaringType, ICompilationUnit declaringTypeCompUnit, String annotationName) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		ICompilationUnit mcUnit = method.getCompilationUnit();
		String className = declaringType.getElementName();
		CompilationUnit mUnit = ASTUtils.getInstance().createCompilationUnit(mcUnit, null);

		BodyDeclaration mBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, mUnit);
		mBodyDeclaration = ASTUtils.getInstance().getMethodDeclaration(method, (TypeDeclaration) mBodyDeclaration);
		mUnit.recordModifications();
		if(removeAnnotation(mBodyDeclaration.modifiers(), annotationName))
		{
			IDocument doc = getDocument(declaringTypeCompUnit);
			TextEdit edit = mUnit.rewrite(doc, null);
			edit.apply(doc);

			saveContent(declaringTypeCompUnit, doc);
		}
	}

	private void removeParamAnnotation(ITypeParameter param, IType declaringType, ICompilationUnit declaringTypeCompUnit, String annotationName) throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		IMethod tmpMethod = (IMethod)param.getDeclaringMember();
		//TODO check this
		//param.getParent();
		String className = declaringType.getElementName();
		ICompilationUnit mcUnit = tmpMethod.getCompilationUnit();
		className = tmpMethod.getParent().getElementName();
		CompilationUnit mUnit = ASTUtils.getInstance().createCompilationUnit(mcUnit, null);

		BodyDeclaration mBodyDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, mUnit);
		mBodyDeclaration = ASTUtils.getInstance().getMethodDeclaration(tmpMethod, (TypeDeclaration) mBodyDeclaration);

		mUnit.recordModifications();
		for (Object object : ((MethodDeclaration) mBodyDeclaration).parameters())
		{
			if (object instanceof SingleVariableDeclaration)
			{
				if (removeAnnotation(((SingleVariableDeclaration) object).modifiers(), annotationName))
				{
					IDocument doc = getDocument(declaringTypeCompUnit);
					TextEdit edit = mUnit.rewrite(doc, null);
					edit.apply(doc);

					saveContent(declaringTypeCompUnit, doc);
				}
			}
		}
	}

	/**
	 * Removes specific annotation from the list of modifiers.
	 * 
	 * @param modifiers
	 * @param annotationName
	 * 
	 * @return boolean value, which represents wheter the annotation is removed or not.
	 */
	@SuppressWarnings("unchecked")
	private boolean removeAnnotation(List modifiers, String annotationName)
	{
		Annotation[] mAnnotations = extractAnnotations(modifiers);
		for (Annotation mAnnotation : mAnnotations)
		{
			if(validateAnnotatation(mAnnotation, annotationName))
			{
				modifiers.remove(mAnnotation);
				return true;
			}
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean removeAnnotations(List modifiers, Set<String> annotationsSet)
	{
		boolean changed = false;
		Annotation[] mAnnotations = extractAnnotations(modifiers);
		for (int mai = 0; mai < mAnnotations.length; mai++)
		{
			if (annotationsSet != null)
			{
				if (annotationsSet.contains(mAnnotations[mai].getTypeName().getFullyQualifiedName()))
				{
					modifiers.remove(mAnnotations[mai]);
					changed = true;
				}
			} else
			{
				modifiers.remove(mAnnotations[mai]);
				changed = true;
			}
		}

		return changed;
	}

	@SuppressWarnings("unchecked")
	private Annotation[] extractAnnotations(List modifiers)
	{
		Iterator iterator = modifiers.iterator();
		Set<Annotation> annotationsSet = new HashSet<Annotation>();

		while (iterator.hasNext())
		{
			IExtendedModifier modif = (IExtendedModifier) iterator.next();
			if (modif.isAnnotation())
			{
				annotationsSet.add((Annotation) modif);
			}
		}

		return annotationsSet.toArray(new Annotation[annotationsSet.size()]);
	}

	private boolean validateAnnotatation(Annotation mAnnotation, String annotationName)
	{
		String mAnnotationString = mAnnotation.toString();
		int index = mAnnotationString.indexOf("("); //$NON-NLS-1$
		if(index == -1)
		{
			mAnnotationString = mAnnotationString.trim();
		}
		else
		{
			mAnnotationString = mAnnotationString.substring(0, index).trim();
		}
		
		if(mAnnotationString.endsWith(annotationName))
		{
			return true;
		}
		 
		return false;
	}
}
