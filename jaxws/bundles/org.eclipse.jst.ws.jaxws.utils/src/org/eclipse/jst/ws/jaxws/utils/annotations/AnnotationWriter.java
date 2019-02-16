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
package org.eclipse.jst.ws.jaxws.utils.annotations;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.ws.jaxws.utils.clazz.ASTUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.jst.ws.jaxws.utils.resources.EditResourcesManager;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.IFileUtils;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * Class, which is used to save the annotations.
 * 
 * @author Plamen Pavlov
 *
 */
public class AnnotationWriter
{
	private static AnnotationWriter writer = null;
	private final IFileUtils fileUtils = FileUtils.getInstance();

	/**
	 * The factory method.
	 * 
	 * @return a AnnotationWriter instance.
	 */
	public static<T extends IJavaElement> AnnotationWriter getInstance()
	{
		if (writer == null)
		{
			writer = new AnnotationWriter();
		}
		return writer;
	}
	
	/**
	 * Adds Annotation to specific IJavaElement.
	 * 
	 * @param javaElement
	 * 
	 * @throws NullPointerException if <tt>javaElement</tt> is null.
	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: IType, IMethod, IField, ITypeParameter.
	 * @throws AnnotationGeneratorException.
	 */
	public <T extends IJavaElement> void setAppliedElement(final IAnnotation<T> annotation, final T javaElement) throws AnnotationGeneratorException
	{
		AnnotationImpl<T> annotationImpl = (AnnotationImpl<T>)annotation;
		annotationImpl.setJavaElement(javaElement);
		
		setAnnotatationStringVallue(annotationImpl, true, javaElement);
	}
	
	private <T extends IJavaElement> void setAnnotatationStringVallue(AnnotationImpl<T> annotationImpl, boolean needSave, final T javaElement) throws AnnotationGeneratorException
	{
		if (!needSave) {
			return;
		}
		
		if(javaElement instanceof IType)
		{
			addToClass(annotationImpl, (IType)javaElement);				
		}
		if(javaElement instanceof IField)
		{
			addToField(annotationImpl, (IField)javaElement);
		}
		if(javaElement instanceof IMethod)
		{
			addToMethod(annotationImpl, (IMethod)javaElement);
		}
		if(javaElement instanceof ITypeParameter)
		{
			addToParam(annotationImpl, (ITypeParameter)javaElement);
		}
	}
	
	private <T extends IJavaElement> void addToClass(AnnotationImpl<T> annotationImpl, IType classType) throws AnnotationGeneratorException
	{
		String className = classType.getElementName();

		addToMember(annotationImpl, classType.getCompilationUnit(), classType, className);
	}

	private <T extends IJavaElement> void addToField(AnnotationImpl<T> annotationImpl, IField fieldType) throws AnnotationGeneratorException
	{
		String className = fieldType.getParent().getElementName();

		addToMember(annotationImpl, fieldType.getCompilationUnit(), fieldType, className);
	}

	private <T extends IJavaElement> void addToMethod(AnnotationImpl<T> annotationImpl, IMethod methodType) throws AnnotationGeneratorException
	{
		String className = methodType.getParent().getElementName();

		addToMember(annotationImpl, methodType.getCompilationUnit(), methodType, className);
	}

	private <T extends IJavaElement> void addToParam(AnnotationImpl<T> annotationImpl, ITypeParameter typeParam) throws AnnotationGeneratorException
	{
		IMember member = typeParam.getDeclaringMember();
		String className = null;
		if (member.getElementType() == IJavaElement.METHOD)
		{
			className = member.getDeclaringType().getElementName();
		} else
		{
			className = member.getElementName();
		}

		addToMember(annotationImpl, member.getCompilationUnit(), typeParam, className);
	}

	private <T extends IJavaElement> void addToMember(AnnotationImpl<T> annotationImpl, ICompilationUnit cUnit, IJavaElement type, String className) throws AnnotationGeneratorException
	{
		try
		{
			if (cUnit == null)
			{
				return;
			}

			CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(cUnit, null);
			AST ast = unit.getAST();

			TypeDeclaration typeDeclaration = (TypeDeclaration)ASTUtils.getInstance().getTypeDeclaration(className, unit);
			if (typeDeclaration != null)
			{
				switch (type.getElementType())
				{
				case IJavaElement.TYPE:
					addAnnotation(annotationImpl, cUnit, unit, ast, typeDeclaration);
					break;
				case IJavaElement.METHOD:
					MethodDeclaration methodDeclaration = ASTUtils.getInstance().getMethodDeclaration((IMethod) type, typeDeclaration);
					addAnnotation(annotationImpl, cUnit, unit, ast, methodDeclaration);
					break;
				case IJavaElement.FIELD:
					FieldDeclaration fieldDeclaration = ASTUtils.getInstance().getFieldDeclaration((IField) type, typeDeclaration);
					addAnnotation(annotationImpl, cUnit, unit, ast, fieldDeclaration);
					break;
				case IJavaElement.TYPE_PARAMETER:
					ITypeParameter typeParameter = (ITypeParameter) type;
					if (typeParameter.getDeclaringMember().getElementType() == IJavaElement.METHOD)
					{
						methodDeclaration = ASTUtils.getInstance().getMethodDeclaration((IMethod) typeParameter.getDeclaringMember(), typeDeclaration);
						SingleVariableDeclaration paramDeclaration = getParamDeclaration(typeParameter, methodDeclaration);
						addAnnotation(annotationImpl, cUnit, unit, ast, paramDeclaration);
					}
					break;
				default:
					throw new IllegalArgumentException("unsupported type for annotation"); //$NON-NLS-1$
				}
			}
		}
		catch (MalformedTreeException e)
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.InvalidTreeStateMsg, e);
		}
		catch (CoreException e)
		{
			throw new AnnotationGeneratorException(e.getMessage(), e.getStatus().getMessage(), e);
		}
		catch (BadLocationException e)
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CannotPerformEditMsg, e);
		}
		catch (FileNotFoundException e)
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CompUnitMissingMsg, e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> void addAnnotation(AnnotationImpl<T> annotationImpl, ICompilationUnit cUnit, CompilationUnit unit, AST ast, ASTNode declaration) 
		throws CoreException, MalformedTreeException, BadLocationException, FileNotFoundException, AnnotationGeneratorException
	{
		editResManager().setFileEditable((IFile) cUnit.getResource());

		unit.recordModifications();
		Expression annot = annotationImpl.getExpression(unit, ast);

		List modifiers = null;

		switch (declaration.getNodeType())
		{
		case ASTNode.METHOD_DECLARATION:
		case ASTNode.FIELD_DECLARATION:
		case ASTNode.TYPE_DECLARATION:
			modifiers = ((BodyDeclaration) declaration).modifiers();
			break;
		case ASTNode.SINGLE_VARIABLE_DECLARATION:
			modifiers = ((SingleVariableDeclaration) declaration).modifiers();
			break;
		default:
			throw new IllegalArgumentException("addAnnotation() illegal declataion type"); //$NON-NLS-1$
		}

		modifiers.add(0, annot);

		IDocument doc = AnnotationUtils.getInstance().getDocument(cUnit);

		TextEdit edit = unit.rewrite(doc, null);
		edit.apply(doc);

		IStatus status = editResManager().setFileEditable((IFile) cUnit.getResource());
		if(status.getSeverity() == IStatus.OK)
		{
			fileUtils.setCompilationUnitContentAndSaveDirtyEditors(cUnit, doc.get(), true, null);
		}
		else
		{
			throw new AnnotationGeneratorException("Annotattion could not be stored, the file is not writable", //$NON-NLS-1$
											JaxWsUtilMessages.AnnotationCannotBeStoredMsg); 
		}
	}

	private static SingleVariableDeclaration getParamDeclaration(ITypeParameter typeParameter, MethodDeclaration methodDeclaration)
	{
		//TODO check this method
		final String paramName = typeParameter.getElementName();

		for (Object decl : methodDeclaration.parameters())
		{
			if (!(decl instanceof SingleVariableDeclaration))
			{
				continue;
			}

			if (paramName.equals(((SingleVariableDeclaration) decl).getName().toString()))
			{
				return (SingleVariableDeclaration) decl;
			}
		}

		return null;
	}
	
	/**
	 * Updates the Parameter-Value Pairs of specific Annotation.
	 * 
	 * @param annotation - Annotation, which will be updated.
	 * @throws AnnotationGeneratorException
	 * @throws CoreException 
	 */
	public <T extends IJavaElement> void update(final IAnnotation<T> annotation) throws AnnotationGeneratorException, CoreException
	{
		AnnotationImpl<T> annotattionImpl = new AnnotationImpl<T>(annotation.getAnnotationName(), annotation.getParamValuePairs());
		try
		{
			updateTheAnnotation(annotation.getAppliedElement(), annotattionImpl);
		} 
		catch(MalformedTreeException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.InvalidTreeStateMsg, e);
		} 
		catch (BadLocationException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CannotPerformEditMsg, e);
		} 
		catch (FileNotFoundException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CompUnitMissingMsg, e);
		}
	}
	
	/**
	 * Updates the Param-Value Pairs of specific Annotation.
	 * 
	 * @param annotation - Annotation, which will be updated.
	 * @param paramValuePairs which will be updated. Old pairs will be replaced with the new once.
	 * @param replacePreviousPairs boolean value which specifies the way, how new IParamValuePairs will be updated. If the value is <tt>true</tt>,
	 * the Set with the old IParamValuePairs will be cleared and the new values will be applied. If the value is <tt>false</tt> the old pairs will be kept
	 * and will be updated with the  values from the new Set, if there are params which do not exist in the old one, they will be added.
	 * 
	 * @throws CoreException 
	 * @throws AnnotationGeneratorException
	 * 
	 * @deprecated - use public <T extends IJavaElement> void update(final IAnnotation<T> annotation) instead.
	 */
	@Deprecated
	public <T extends IJavaElement> void update(final IAnnotation<T> annotation, Set<IParamValuePair> paramValuePairs, boolean replacePreviousPairs) throws AnnotationGeneratorException, CoreException
	{	
		updateAnnotation(annotation, annotation.getAppliedElement(), paramValuePairs, replacePreviousPairs);
	}

	/**
	 * 
	 * @param <T>
	 * @param annotation
	 * @param javaElement
	 * @param paramValuePairs
	 * @param replacePreviousPairs
	 * @throws AnnotationGeneratorException
	 * @throws CoreException
	 * 
	 */
	private <T extends IJavaElement> void updateAnnotation(final IAnnotation<T> annotation, final T javaElement, Set<IParamValuePair> paramValuePairs, boolean replacePreviousPairs) throws AnnotationGeneratorException, CoreException
	{
		AnnotationImpl<T> annotationImpl = (AnnotationImpl<T>)annotation;
		try
		{
			if(replacePreviousPairs)
			{
				annotationImpl.setParamValuePairs(paramValuePairs);
			}
			else
			{
				for (IParamValuePair pair : paramValuePairs)
				{
					boolean isUpdated = false;
					Set<IParamValuePair> tmpParamValuePairs = annotationImpl.getParamValuePairs();
					for (IParamValuePair thisPair : tmpParamValuePairs)
					{
						if(thisPair.getParam().equals(pair.getParam()))
						{
							tmpParamValuePairs.remove(thisPair);
							tmpParamValuePairs.add(pair);
							isUpdated = true;
							break;
						}
					}
					if(!isUpdated)
					{
						tmpParamValuePairs.add(pair);
					}
					annotationImpl.setParamValuePairs(tmpParamValuePairs);
				}
			}
			updateTheAnnotation(javaElement, annotationImpl);
			
			//remove(annotation);
			//setAppliedElement(annotation, javaElement);
		} 
		catch(MalformedTreeException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.InvalidTreeStateMsg, e);
		} 
		catch (BadLocationException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CannotPerformEditMsg, e);
		} 
		catch (FileNotFoundException e) {
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CompUnitMissingMsg, e);
		}
	}


	/**
	 * Removes Annotation.
	 * 
	 * @param annotation - Annotation, which will be removed.
	 * 
	 * @throws BadLocationException 
	 * @throws CoreException 
	 * @throws AnnotationGeneratorException 
	 * @throws FileNotFoundException 
	 */
	public <T extends IJavaElement> void remove(final IAnnotation<T> annotation) throws AnnotationGeneratorException, CoreException
	{
		removeAnnotation(annotation, annotation.getAppliedElement());
	}

	private <T extends IJavaElement> void removeAnnotation(final IAnnotation<T> annotation, final T javaElement) throws AnnotationGeneratorException, CoreException
	{
		AnnotationImpl<T> annotationImpl = (AnnotationImpl<T>)annotation;
		try
		{
//			if(annotationImpl.getAnnotationStringValue() == null || annotationImpl.getAnnotationStringValue().trim().equals(""))
//			{
//				annotationImpl.setAnnotationStringValue(createAnnotatationStringVallue(annotationImpl, javaElement));
//				AnnotationUtils.getInstance().removeAnnotation(javaElement, annotationImpl.getAnnotationStringValue());
//			}
//			else
//			{
				AnnotationUtils.getInstance().removeAnnotation(javaElement, annotationImpl.getSimpleAnnotationName());
//			}
		} catch (BadLocationException ble)
		{
			throw new AnnotationGeneratorException(ble.getMessage(), JaxWsUtilMessages.CannotPerformEditMsg, ble);
		} catch (FileNotFoundException fnfe)
		{
			throw new AnnotationGeneratorException(fnfe.getMessage(), JaxWsUtilMessages.CompUnitMissingMsg, fnfe);
		}	
	}

	// i036509 added
	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> void updateTheAnnotation(final T javaElement, final AnnotationImpl<T> annotationImpl) throws BadLocationException, CoreException, AnnotationGeneratorException, FileNotFoundException
	{		
		final ICompilationUnit iCu = getCu(javaElement);
		final CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(iCu, null);
		
		BodyDeclaration bd = null;
		Annotation found = null;
		if(javaElement instanceof IType)
		{
			bd = ASTUtils.getInstance().getTypeDeclaration(javaElement.getElementName(), unit);
			found = findAnnotation(bd, annotationImpl);

			unit.recordModifications();
			if (found!=null) {
				bd.modifiers().remove(found);
			}
			
			final Expression annot = annotationImpl.getExpression(unit, unit.getAST());
			bd.modifiers().add(0, annot);
		}
		if(javaElement instanceof IMethod)
		{
			TypeDeclaration td = (TypeDeclaration)ASTUtils.getInstance().getTypeDeclaration(((IMethod)javaElement).getDeclaringType().getElementName(), unit);
			bd = ASTUtils.getInstance().getMethodDeclaration(((IMethod)javaElement), td);
			found = findAnnotation(bd, annotationImpl);
		
			unit.recordModifications();
			if (found!=null) {
				bd.modifiers().remove(found);
			}
			
			final Expression annot = annotationImpl.getExpression(unit, unit.getAST());
			bd.modifiers().add(0, annot);
		}
		if(javaElement instanceof IField)
		{
			TypeDeclaration td = (TypeDeclaration)ASTUtils.getInstance().getTypeDeclaration(((IField)javaElement).getDeclaringType().getElementName(), unit);
			bd = ASTUtils.getInstance().getFieldDeclaration(((IField)javaElement), td);
			found = findAnnotation(bd, annotationImpl);
		
			unit.recordModifications();
			if (found!=null) {
				bd.modifiers().remove(found);
			}
			
			final Expression annot = annotationImpl.getExpression(unit, unit.getAST());
			bd.modifiers().add(0, annot);
		}
		if(javaElement instanceof ITypeParameter)
		{
			if(((ITypeParameter)javaElement).getDeclaringMember() instanceof IType)
			{
				bd = ASTUtils.getInstance().getTypeDeclaration(((ITypeParameter)javaElement).getDeclaringMember().getElementName(), unit);
				SingleVariableDeclaration param = findParameter(bd, javaElement);
				if(param != null)
				{
					found = findParamAnnotation(param, annotationImpl, javaElement);
				}

				unit.recordModifications();
				if (found!=null) {
					param.modifiers().remove(found);
				}
				
				final Expression annot = annotationImpl.getExpression(unit, unit.getAST());
				param.modifiers().add(0, annot);
			}
			else
			{
				TypeDeclaration td = (TypeDeclaration)ASTUtils.getInstance().getTypeDeclaration(((IMethod)((ITypeParameter)javaElement).getParent()).getDeclaringType(). getElementName(), unit);
				bd = ASTUtils.getInstance().getMethodDeclaration(((IMethod)((ITypeParameter)javaElement).getParent()), td);
				SingleVariableDeclaration param = findParameter(bd, javaElement);
				if(param != null)
				{
					found = findParamAnnotation(param, annotationImpl, javaElement);
				}

				unit.recordModifications();
				if (found!=null) {
					param.modifiers().remove(found);
				}
				
				final Expression annot = annotationImpl.getExpression(unit, unit.getAST());
				param.modifiers().add(0, annot);
			}
		}
				
		setAnnotatationStringVallue(annotationImpl, false, javaElement);
		final IDocument doc = AnnotationUtils.getInstance().getDocument(iCu);	
		final TextEdit edit = unit.rewrite(doc, javaElement.getJavaProject().getOptions(true));
		edit.apply(doc);

		IStatus status = editResManager().setFileEditable((IFile) iCu.getResource());
		if(status.getSeverity() == IStatus.OK)
		{
			fileUtils.setCompilationUnitContentAndSaveDirtyEditors(iCu, doc.get(), true, null);
		}
		else
		{
			throw new AnnotationGeneratorException("Annotattion could not be stored, the file is not writable", //$NON-NLS-1$
											JaxWsUtilMessages.AnnotationCannotBeStoredMsg); 
		}
	}
	
	@SuppressWarnings("unchecked")
	private Annotation findAnnotation(final BodyDeclaration td, final IAnnotation<? extends IJavaElement> annotation)
	{
		Annotation ann;
		for (IExtendedModifier modifier : (List<IExtendedModifier>)td.modifiers()) 
		{
			if(!modifier.isAnnotation()) {
				continue;
			}
			
			ann = (Annotation)modifier;
			if (annotation.getAnnotationName().endsWith(ann.getTypeName().getFullyQualifiedName())) {
				return ann;
			}
		}
		
		return null;
	}
	
	private <T extends IJavaElement> SingleVariableDeclaration findParameter(final BodyDeclaration td, T javaElement)
	{
		if (td != null)
		{
			for (Object param : ((MethodDeclaration)td).parameters())
			{
				if (param instanceof SingleVariableDeclaration &&
						((ITypeParameter)javaElement).getElementName().toString().equals(((SingleVariableDeclaration)param).getName().toString()))
				{
					return (SingleVariableDeclaration)param;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> Annotation findParamAnnotation(final SingleVariableDeclaration param, final IAnnotation<? extends IJavaElement> annotation, T javaElement)
	{
		Annotation ann;
		for (IExtendedModifier modifier : (List<IExtendedModifier>)(param.modifiers())) 
		{
			if(!modifier.isAnnotation()) {
				continue;
			}
			
			ann = (Annotation)modifier;
			if (annotation.getAnnotationName().endsWith(ann.getTypeName().getFullyQualifiedName())) {
				return ann;
			}
		}
		return null;
	}
	
	private ICompilationUnit getCu(final IJavaElement javaElement)
	{
		switch (javaElement.getElementType()) 
		{
		case IJavaElement.TYPE:
			return ((IType) javaElement).getCompilationUnit();
		case IJavaElement.FIELD:
			return ((IField) javaElement).getCompilationUnit();
		case IJavaElement.METHOD:
			return ((IMethod) javaElement).getCompilationUnit();
		case IJavaElement.TYPE_PARAMETER:
			return ((ITypeParameter) javaElement).getDeclaringMember().getCompilationUnit();
		}
		
		return null;
	}
	
	private EditResourcesManager editResManager()
	{
		return new EditResourcesManager();
	}
}
