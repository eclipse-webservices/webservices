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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.clazz.ASTUtils;

/**
 * Class, which is made to load and return annotations from a given IJavaElemet.
 * 
 * @author Plamen Pavlov
 */
public class AnnotationInspectorImpl implements IAnnotationInspector
{
	private IType iType;
	private CompilationUnit cUnit;

	public AnnotationInspectorImpl(final IType type)
	{
		ContractChecker.nullCheckParam(type, "type"); //$NON-NLS-1$
		
		this.iType = type;
	}	

	public IAnnotation<IMethod> inspectMethod(final IMethod method, final String annotationQName) throws JavaModelException
	{
		ContractChecker.nullCheckParam(method, "method"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(annotationQName, "annotationQName"); //$NON-NLS-1$
		if(!(method instanceof IMethod))
		{
			throw new IllegalArgumentException("method argument is not of correct Type!"); //$NON-NLS-1$
		}
		if(annotationQName.trim().equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("annotationQName argument is not with propper value!"); //$NON-NLS-1$
		}

		return getSpecificAnnotationFromMember(method, method.getParent().getElementName(), annotationQName);		
	}	

	public Collection<IAnnotation<IMethod>> inspectMethod(IMethod method) throws JavaModelException
	{
		ContractChecker.nullCheckParam(method, "method"); //$NON-NLS-1$
		
		if(!(method instanceof IMethod))
		{
			throw new IllegalArgumentException("method argument is not of correct Type!");	//$NON-NLS-1$
		}

		return getFromMember(method, method.getParent().getElementName());		
	}
	
	public IAnnotation<IField> inspectField(final IField field, final String annotationQName) throws JavaModelException
	{
		nullCheckParam(field, "field"); //$NON-NLS-1$
		nullCheckParam(annotationQName, "annotationQName"); //$NON-NLS-1$

		if(!(field instanceof IField))
		{
			throw new IllegalArgumentException("field argument is not of correct Type!"); //$NON-NLS-1$
		}
		if(annotationQName.trim().equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("annotationQName argument is not with propper value!"); //$NON-NLS-1$
		}

		final String className = field.getParent().getElementName();
		return getSpecificAnnotationFromMember(field, className, annotationQName);		
	}
	
	public Collection<IAnnotation<IField>> inspectField(IField field) throws JavaModelException
	{
		if (field == null)
		{
			throw new NullPointerException("field should not be null!"); //$NON-NLS-1$
		}
		if(!(field instanceof IField))
		{
			throw new IllegalArgumentException("field argument is not of correct Type!"); //$NON-NLS-1$
		}

		final String className = field.getParent().getElementName();
		return getFromMember(field, className);
	}

	public IAnnotation<ITypeParameter> inspectParam(final ITypeParameter param, final String annotationQName) throws JavaModelException
	{
		nullCheckParam(param, "param"); //$NON-NLS-1$
		nullCheckParam(annotationQName, "annotationQName"); //$NON-NLS-1$
		
		if(!(param instanceof ITypeParameter))
		{
			throw new IllegalArgumentException("param argument is not of correct Type!"); //$NON-NLS-1$
		}
		if(annotationQName.trim().equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("annotationQName argument is not with propper value!"); //$NON-NLS-1$
		}

		final String className = getClassNameFromParam(param);
		return getSpecificAnnotationFromMember(param, className, annotationQName);		
	}
	
	public Collection<IAnnotation<ITypeParameter>> inspectParam(ITypeParameter param) throws JavaModelException
	{
		if (param == null)
		{
			throw new NullPointerException("param should not be null!"); //$NON-NLS-1$
		}
		if(!(param instanceof ITypeParameter))
		{
			throw new IllegalArgumentException("param argument is not of correct Type!"); //$NON-NLS-1$
		}

		return getFromMember(param, getClassNameFromParam(param));
	}

	public IAnnotation<IType> inspectType(final String annotationQName) throws JavaModelException
	{
		nullCheckParam(annotationQName, "annotationQName"); //$NON-NLS-1$
		
		if (annotationQName.trim().equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("annotationQName argument is not with propper value!"); //$NON-NLS-1$
		}
		
		return getSpecificAnnotationFromMember(iType, iType.getElementName(), annotationQName);
	}
	
	public Collection<IAnnotation<IType>> inspectType() throws JavaModelException
	{
		return getFromMember(iType, iType.getElementName());
	}	
	
	// # implementation
	
	private String getClassNameFromParam(final ITypeParameter param) throws JavaModelException
	{
		if(param.getDeclaringMember() instanceof IMethod)
		{
			return param.getDeclaringMember().getParent().getElementName();
		}

		return param.getDeclaringMember().getElementName();
	}

	protected <T extends IJavaElement> Set<IAnnotation<T>> getFromMember(T type, String className) throws JavaModelException
	{
		if (!isSupportedJavaElement(type)) {
			throw new IllegalArgumentException("passed argument is not of correct type."); //$NON-NLS-1$
		}
		
		ICompilationUnit cUnit = null;
		if(type instanceof IMember) {
			cUnit = ((IMember)type).getCompilationUnit();
		}
		else {
			cUnit = ((ITypeParameter)type).getDeclaringMember().getCompilationUnit();
		}
		
		if (cUnit == null) {
			return new HashSet<IAnnotation<T>>();
		}

		final CompilationUnit unit = getCompilationUnit();
		final AbstractTypeDeclaration typeDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, unit);
		
		switch(type.getElementType())
		{
		case IJavaElement.TYPE:
			return getAnnotations(type, typeDeclaration);
			
		case IJavaElement.FIELD:
			FieldDeclaration fieldDeclaration = ASTUtils.getInstance().getFieldDeclaration((IField) type, (TypeDeclaration)typeDeclaration);
			return getAnnotations(type, fieldDeclaration);
			
		case IJavaElement.METHOD:
			MethodDeclaration methodDeclaration = ASTUtils.getInstance().getMethodDeclaration((IMethod) type, (TypeDeclaration)typeDeclaration);
			return getAnnotations(type, methodDeclaration);
			
		case IJavaElement.TYPE_PARAMETER:
			methodDeclaration = ASTUtils.getInstance().getMethodDeclaration((IMethod)((ITypeParameter) type).getParent(), (TypeDeclaration)typeDeclaration);
			return getParamAnnotations(type, methodDeclaration);
		}

		return new HashSet<IAnnotation<T>>();
	}

	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> Set<IAnnotation<T>> getAnnotations(T iMember, BodyDeclaration bodyDeclaration) throws JavaModelException
	{
		if (bodyDeclaration != null)
		{
			List list = bodyDeclaration.modifiers();
			return extractAnnotations(iMember, list);
		}
		return new HashSet<IAnnotation<T>>();
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> Set<IAnnotation<T>> getParamAnnotations(T iMember, MethodDeclaration methodDeclaration) throws JavaModelException
	{
		if (methodDeclaration != null)
		{
			for (Object param : methodDeclaration.parameters())
			{
				if (param instanceof SingleVariableDeclaration &&
						((ITypeParameter)iMember).getElementName().toString().equals(((SingleVariableDeclaration)param).getName().toString()))
				{
					List list = ((SingleVariableDeclaration) param).modifiers();
				
					return extractAnnotations(iMember, list);
				}
			}			
		}
		return new HashSet<IAnnotation<T>>();
	}
	

	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> Set<IAnnotation<T>> extractAnnotations(T iMember, List list) throws JavaModelException
	{
		Iterator iter = list.iterator();
		Set<IValue> values = new HashSet<IValue>();
		while (iter.hasNext())
		{
			IExtendedModifier element = (IExtendedModifier) iter.next();
			if (element.isAnnotation())
			{
				values.add(convertExpression((Expression) element));
			}
		}

		Set<IAnnotation<T>> expressions = new HashSet<IAnnotation<T>>();
		for (IValue value : values) 
		{
			AnnotationImpl<T> ann = (AnnotationImpl<T>)value;
			ann.setAppliedElementWithoutSave(iMember);
			expressions.add(ann);
		}
		return expressions;		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IJavaElement> IAnnotation<T> extractSpecificAnnotation(T iMember, List list, IType type, String annotationQName) throws JavaModelException
	{
		Iterator iter = list.iterator();
		Set<IValue> values = new HashSet<IValue>();
		while (iter.hasNext())
		{
			IExtendedModifier element = (IExtendedModifier) iter.next();
			if (element.isAnnotation())
			{				
				final Name typeName = ((Annotation)element).getTypeName();
				if (annotationQName.endsWith(typeName.getFullyQualifiedName())) {
					values.add(convertExpression((Expression) element));
				}
			}
		}

		for (IValue value : values) 
		{
			AnnotationImpl<T> tmpAnnotation = (AnnotationImpl<T>)value;
			if(checkSpecificAnnotattion(tmpAnnotation, type, annotationQName))
			{
				AnnotationImpl<T> result = new AnnotationImpl<T>(annotationQName, tmpAnnotation.getParamValuePairs());
				result.setAppliedElementWithoutSave(iMember);
				result.setLocator(tmpAnnotation.getLocator());
				return result;
			}
		}
		return null;		
	}	

	private <T extends IJavaElement> boolean checkSpecificAnnotattion(final AnnotationImpl<T> annotation, final IType type, final String annotationQName) throws JavaModelException
	{
		if(annotation.getAnnotationName().equals(annotationQName) || 
				(type.getPackageFragment().getElementName() + "." + annotation.getAnnotationName()).equals(annotationQName)) //$NON-NLS-1$
		{
			return true;
		}
		
		ICompilationUnit cUnit = type.getCompilationUnit();
		IImportDeclaration[] imports = cUnit.getImports();
		for (IImportDeclaration importDeclaration : imports)
		{
			if(annotationQName.endsWith(annotation.getAnnotationName()) && importDeclaration.getElementName().equals(annotationQName))
			{
				return true;
			}
		}
		
		for (IImportDeclaration importDeclaration : imports)
		{
			if(importDeclaration.getElementName().endsWith("*")) //$NON-NLS-1$
			{
				String importStr = importDeclaration.getElementName().substring(0, importDeclaration.getElementName().length() - 1);
				if(annotationQName.endsWith(annotation.getAnnotationName()) && annotationQName.indexOf(importStr) == 0)
				{
					return true;
				}
			}
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	private IValue convertExpression(Expression element) throws JavaModelException
	{
		switch (element.getNodeType())
		{
			case ASTNode.MARKER_ANNOTATION:
			{
				MarkerAnnotation ma = (MarkerAnnotation) element;
				AnnotationImpl annotation = new AnnotationImpl(getFullyQualifiedAnnotationName(ma), new HashSet<IParamValuePair>());
				annotation.setLocator(createLocator(ma));
				return annotation;
			}
			case ASTNode.SINGLE_MEMBER_ANNOTATION:
			{
				final SingleMemberAnnotation sma = (SingleMemberAnnotation) element;
				final Set<IParamValuePair> values = new HashSet<IParamValuePair>();

				final IValue value = convertExpression(sma.getValue());
				if (value!=null) {
					values.add(AnnotationFactory.createParamValuePairValue("value", value)); //$NON-NLS-1$
				}
				
				AnnotationImpl annotation = new AnnotationImpl(getFullyQualifiedAnnotationName(sma), values);
				annotation.setLocator(createLocator(sma));
				return annotation;
			}
			case ASTNode.NORMAL_ANNOTATION:
			{
				return getNormalAnnotationValue((NormalAnnotation) element);
			}
			case ASTNode.ARRAY_INITIALIZER:
			{
				ArrayInitializer arr = (ArrayInitializer) element;
				List<Expression> list = arr.expressions();
				Iterator<Expression> iter = list.iterator();
				Set<IValue> result = new HashSet<IValue>();
				while (iter.hasNext())
				{
					Expression expr = (Expression) iter.next();
					result.add(convertExpression(expr));
				}
				return new ArrayValueImpl(result);
			}
			case ASTNode.NULL_LITERAL:
			{
				// return AnnotationFactory.createNullValue();
				break;
			}
			case ASTNode.SIMPLE_NAME:
			{
				break;//return new StringValueImpl(((SimpleName)element).getIdentifier());
			}
			case ASTNode.TYPE_LITERAL:
			{
				return getTypeLiteralValue((TypeLiteral) element);
			}
			case ASTNode.QUALIFIED_NAME:
			{
				QualifiedName qn = (QualifiedName) element;
				QualifiedNameValueImpl qnValue = new QualifiedNameValueImpl(qn.getFullyQualifiedName());
				qnValue.setLocator(createLocator(qn));
				return qnValue;
			}
			case ASTNode.BOOLEAN_LITERAL:
			{
				BooleanLiteral bl = (BooleanLiteral) element;
				BooleanValueImpl bValue = new BooleanValueImpl(bl.booleanValue());
				bValue.setLocator(createLocator(bl));
				return bValue;
			}
			case ASTNode.CHARACTER_LITERAL:
			{
				// CharacterLiteral cl = (CharacterLiteral) element;
				// TODO Finish with this return new CharacterValue(cl.charValue());
				break;
			}
			case ASTNode.NUMBER_LITERAL:
			{
				NumberLiteral nl = (NumberLiteral) element;
				IntegerValueImpl intValue = new IntegerValueImpl(nl.getToken());
				intValue.setLocator(createLocator(nl));
				return intValue;
			}
			case ASTNode.STRING_LITERAL:
			{
				StringLiteral sl = (StringLiteral) element;
				StringValueImpl strValue = new StringValueImpl(sl.getLiteralValue());
				strValue.setLocator(createLocator(sl));
				return strValue;
			}
		}
		return null;
	}
	
	private IValue getTypeLiteralValue(TypeLiteral tl) throws JavaModelException
	{
		Type type = tl.getType();
		Name name = null;
		if (type.isQualifiedType())
		{
			name = ((QualifiedType) type).getName();
		} else
		{
			if (type.isSimpleType())
			{
				name = ((SimpleType) type).getName();
			}
		}
		if (name != null)
		{
			CompilationUnit cu = getCompilationUnit(tl);
			if (cu != null)
			{
				String fullyQualifiedName = name.getFullyQualifiedName();
				if (type.isSimpleType())
				{
					fullyQualifiedName = resolveType((ICompilationUnit) cu.getJavaElement(), fullyQualifiedName);
				}

				ClassValueImpl cValue = new ClassValueImpl(fullyQualifiedName);
				cValue.setLocator(createLocator(tl));
				return cValue;
			}
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	private IValue getNormalAnnotationValue(NormalAnnotation na) throws JavaModelException
	{
		List<MemberValuePair> l = na.values();
		Iterator<MemberValuePair> iter = l.iterator();
		Set<IParamValuePair> result = new HashSet<IParamValuePair>();
		while (iter.hasNext())
		{
			MemberValuePair mvp = (MemberValuePair) iter.next();
			IValue mvpval = convertExpression(mvp.getValue());
			ParamValuePairImpl pair = new ParamValuePairImpl(mvp.getName().getIdentifier(), mvpval);
			pair.setLocator(createLocator(mvp));
			result.add(pair);
		}
		
		AnnotationImpl annotation = new AnnotationImpl(getFullyQualifiedAnnotationName(na), result);
		annotation.setLocator(createLocator(na));
		return annotation;
	}	

	private String getFullyQualifiedAnnotationName(Annotation anno) throws JavaModelException
	{
		Name typeName = anno.getTypeName();
		String fullyQualifiedName = typeName.getFullyQualifiedName();
		if (typeName.isSimpleName())
		{
			CompilationUnit cu = getCompilationUnit(anno);
			fullyQualifiedName = resolveType((ICompilationUnit) cu.getJavaElement(), fullyQualifiedName);
		}
		return fullyQualifiedName;
	}
	
	private CompilationUnit getCompilationUnit(Expression element)
	{
		ASTNode cu = element;
		while (cu != null && !(cu instanceof CompilationUnit))
		{
			cu = cu.getParent();
		}
		return (CompilationUnit) cu;
	}
	
	private String resolveType(ICompilationUnit icu, String fullyQualifiedName) throws JavaModelException
	{
		String localFullyQualifiedName = fullyQualifiedName;
		IType[] allTypes = icu.getAllTypes();
		if (allTypes == null)
		{
			return localFullyQualifiedName;
		}

		for (int i = 0; i < allTypes.length; i++)
		{
			String[][] resolvedType = allTypes[i].resolveType(localFullyQualifiedName);
			if (resolvedType != null && resolvedType.length > 0)
			{
				StringBuffer buf = new StringBuffer();
				for (int j = 0; j < resolvedType[0].length; j++)
				{
					buf = appendDot(j, buf);
					buf.append(resolvedType[0][j]);
				}
				localFullyQualifiedName = buf.toString();
				break;
			}
		}

		return localFullyQualifiedName;
	}
	
	private StringBuffer appendDot(int index, StringBuffer buff)
	{
		StringBuffer result = buff;
		if (index != 0)
		{
			result.append('.');
		}
		return result;
	}
	
	protected <T extends IJavaElement> IAnnotation<T> getSpecificAnnotationFromMember(final T type, final String className, final String annotationQName) throws JavaModelException
	{
		if (!isSupportedJavaElement(type))
		{
			throw new IllegalArgumentException("passed argument is not of correct type."); //$NON-NLS-1$
		}
		
		ICompilationUnit cUnit = null;
		if (type instanceof IMember) {
			cUnit = ((IMember)type).getCompilationUnit();
		}
		else {
			cUnit = ((ITypeParameter)type).getDeclaringMember().getCompilationUnit();
		}
		
		if (cUnit == null) {
			return null;
		}

		final CompilationUnit unit = getCompilationUnit();
		final AbstractTypeDeclaration typeDeclaration = ASTUtils.getInstance().getTypeDeclaration(className, unit);
		
		switch(type.getElementType())
		{
		case IJavaElement.TYPE:
			return getSpecificAnnotation(type, typeDeclaration, (IType)type, annotationQName);
		
		case IJavaElement.FIELD:
			IField field = (IField) type;
			FieldDeclaration fieldDeclaration = ASTUtils.getInstance().getFieldDeclaration(field, (TypeDeclaration)typeDeclaration);
			return getSpecificAnnotation(type, fieldDeclaration, field.getDeclaringType(), annotationQName);

		case IJavaElement.METHOD:
			IMethod method = (IMethod) type;
			MethodDeclaration methodDeclaration = ASTUtils.getInstance().getMethodDeclaration(method, (TypeDeclaration)typeDeclaration);
			return getSpecificAnnotation(type, methodDeclaration, method.getDeclaringType(), annotationQName);

		case IJavaElement.TYPE_PARAMETER:
			method = (IMethod)((ITypeParameter) type).getParent();
			methodDeclaration = ASTUtils.getInstance().getMethodDeclaration(method, (TypeDeclaration)typeDeclaration);
			return getSpecificParamAnnotation(type, methodDeclaration, method.getDeclaringType(), annotationQName);
		}

		return null;
	}	
	
	private <T extends IJavaElement> IAnnotation<T> getSpecificAnnotation(final T iMember, final BodyDeclaration bodyDeclaration, final IType type, final String annotationQName) throws JavaModelException
	{
		if (bodyDeclaration != null)
		{
			@SuppressWarnings("unchecked")
			final List list = bodyDeclaration.modifiers();
			return extractSpecificAnnotation(iMember, list, type, annotationQName);
		}
		return null;
	}
	
	private <T extends IJavaElement> IAnnotation<T> getSpecificParamAnnotation(T iMember, MethodDeclaration methodDeclaration, final IType type, final String annotationQName) throws JavaModelException
	{
		if (methodDeclaration != null)
		{
			for (Object param : methodDeclaration.parameters())
			{
				if (param instanceof SingleVariableDeclaration &&
						((ITypeParameter)iMember).getElementName().toString().equals(((SingleVariableDeclaration)param).getName().toString()))
				{
					@SuppressWarnings("unchecked")
					final List list = ((SingleVariableDeclaration) param).modifiers();
				
					return extractSpecificAnnotation(iMember, list, type, annotationQName);
				}
			}			
		}
		return null;
	}	
	
	private boolean isSupportedJavaElement(final IJavaElement javaElement) 
	{
		return 	javaElement.getElementType()==IJavaElement.METHOD ||
				javaElement.getElementType()==IJavaElement.TYPE ||
				javaElement.getElementType()==IJavaElement.FIELD ||
				javaElement.getElementType()==IJavaElement.TYPE_PARAMETER;
	}
	
	private CompilationUnit getCompilationUnit() 
	{
		if (cUnit==null) {
			cUnit = ASTUtils.getInstance().createCompilationUnit(iType.getCompilationUnit(), null);
		}
		
		return cUnit;
	}
	
	private ILocator createLocator(final ASTNode node)
	{
		final int lineNumber = getCompilationUnit().getLineNumber(node.getStartPosition());
		return new LocatorImpl(lineNumber, node.getStartPosition(), node.getLength());
	}
}