/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import org.eclipse.jdt.apt.core.env.EclipseAnnotationProcessorEnvironment;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;

public class WebServiceParametersReturnTypesRule extends AbstractAnnotationProcessor {

    private static final Set<String> JAVA_TYPES = new HashSet<String>();

    private static final Set<String> ALLOWED_IN_MULTIPLE_INHERITANCE = new HashSet<String>();

    private static final String XML_TYPE = "javax.xml.bind.annotation.XmlType";
    
    static {
        JAVA_TYPES.add("java.util.Calendar"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.Date"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.GregorianCalendar"); //$NON-NLS-1$
        JAVA_TYPES.add("javax.xml.datatype.XMLGregorianCalendar"); //$NON-NLS-1$

        JAVA_TYPES.add("java.util.ArrayList"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.HashSet"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.LinkedList"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.List"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.Stack"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.Vector"); //$NON-NLS-1$

        JAVA_TYPES.add("java.math.BigDecimal"); //$NON-NLS-1$
        JAVA_TYPES.add("java.math.BigInteger"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Object"); //$NON-NLS-1$
        JAVA_TYPES.add("javax.xml.namespace.QName"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.UUID"); //$NON-NLS-1$
        JAVA_TYPES.add("java.net.URI"); //$NON-NLS-1$
        JAVA_TYPES.add("java.awt.Image"); //$NON-NLS-1$
        JAVA_TYPES.add("javax.xml.datatype.Duration"); //$NON-NLS-1$
        JAVA_TYPES.add("javax.xml.transform.Source"); //$NON-NLS-1$
        JAVA_TYPES.add("javax.activation.DataHandler"); //$NON-NLS-1$

        JAVA_TYPES.add("java.lang.String"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Boolean"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Integer"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Character"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Float"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Byte"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Double"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Long"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Short"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Void"); //$NON-NLS-1$

        JAVA_TYPES.add("java.rmi.RemoteException"); //$NON-NLS-1$

        JAVA_TYPES.add("java.io.Serializable"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Cloneable"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Comparable"); //$NON-NLS-1$

        JAVA_TYPES.add("java.util.Map"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.HashMap"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.Hashtable"); //$NON-NLS-1$
        JAVA_TYPES.add("java.util.Collection"); //$NON-NLS-1$

        JAVA_TYPES.add("java.lang.Exception"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.Throwable"); //$NON-NLS-1$
        JAVA_TYPES.add("java.lang.StackTraceElement"); //$NON-NLS-1$

        ALLOWED_IN_MULTIPLE_INHERITANCE.add("java.lang.Cloneable"); // $NON-NLS$
        ALLOWED_IN_MULTIPLE_INHERITANCE.add("java.lang.Comparable"); // $NON-NLS$
        ALLOWED_IN_MULTIPLE_INHERITANCE.add("java.io.Serializable"); // $NON-NLS$
    }

    private static final String REMOTE_OBJECT = "java.rmi.Remote";

    @Override
    public void process() {
        AnnotationTypeDeclaration webServiceDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(webServiceDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof InterfaceDeclaration) {
                validateMethodParametersAndReturnTypes((InterfaceDeclaration) declaration);
            }
        }
    }

    private void validateMethodParametersAndReturnTypes(InterfaceDeclaration interfaceDeclaration) {
        Collection<? extends MethodDeclaration> methods = interfaceDeclaration.getMethods();
        for (MethodDeclaration methodDeclaration : methods) {
            TypeMirror returnType = methodDeclaration.getReturnType();
            validateTypeMirror(returnType, methodDeclaration);
            Collection<ParameterDeclaration> parameters =  methodDeclaration.getParameters();
            for (ParameterDeclaration parameterDeclaration : parameters) {
                validateTypeMirror(parameterDeclaration.getType(), methodDeclaration);
            }
        }
    }

    private void validateTypeMirror(TypeMirror returnType, MethodDeclaration methodDeclaration) {
        if (returnType instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) returnType;

            TypeMirror componentType = arrayType.getComponentType();
            if (componentType instanceof ClassDeclaration) {
                ClassDeclaration classDeclaration = (ClassDeclaration) componentType;
                checkClassRestriction(classDeclaration, methodDeclaration);
            }
        }

        if (returnType instanceof InterfaceType) {
            InterfaceType interfaceType = (InterfaceType) returnType;
            Collection<TypeMirror> typeArguments = interfaceType.getActualTypeArguments();
            if (typeArguments.size() > 0) {
                for (TypeMirror typeMirror : typeArguments) {
                    if (typeMirror instanceof ClassDeclaration) {
                        ClassDeclaration classDeclaration = (ClassDeclaration) typeMirror;
                        checkClassRestriction(classDeclaration, methodDeclaration);
                    }
                    if (typeMirror instanceof InterfaceDeclaration) {
                        checkInterface((InterfaceDeclaration) typeMirror, methodDeclaration);
                    }
                }
            }
            checkInterface(interfaceType.getDeclaration(), methodDeclaration);

        }

        if (returnType instanceof ClassType) {
            ClassType classType = (ClassType) returnType;
            Collection<TypeMirror> typeArguments = classType.getActualTypeArguments();
            if (typeArguments.size() > 0) {
                for (TypeMirror typeMirror : typeArguments) {
                    if (typeMirror instanceof ClassDeclaration) {
                        ClassDeclaration classDeclaration = (ClassDeclaration) typeMirror;
                        checkClassRestriction(classDeclaration, methodDeclaration);
                    }
                }
            } else {
                if (classType.getDeclaration() != null) {
                    checkClassRestriction(classType.getDeclaration(), methodDeclaration);
                }
            }
        }

    }

    private void checkClassRestriction(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration) {
        checkInnerPublicStaticTypes(classDeclaration, methodDeclaration);
        checkIfRemoteObject(classDeclaration, methodDeclaration);
        checkAbstactType(classDeclaration, methodDeclaration);
        checkMultipleInheritance(classDeclaration, methodDeclaration);
    }

    private void checkInnerPublicStaticTypes(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration) {
        if (hasInnerNonPublicStaticTypes(classDeclaration)) {
            printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.HAS_INADMISSIBLE_INNER_TYPES, classDeclaration.getQualifiedName()));
        }
    }

    private boolean hasInnerNonPublicStaticTypes(ClassDeclaration classDeclaration) {
        if (isJavaType(classDeclaration.getQualifiedName()) && JAVA_TYPES.contains(classDeclaration.getQualifiedName())) {
            return false;
        }
        Collection<TypeDeclaration> nestedTypes = classDeclaration.getNestedTypes();
        for (TypeDeclaration typeDeclaration : nestedTypes) {
            if (!typeDeclaration.getModifiers().contains(Modifier.PUBLIC)
                    || !typeDeclaration.getModifiers().contains(Modifier.STATIC)) {
                return true;
            }
        }
        return false;
    }

    private void checkInterface(InterfaceDeclaration interfaceDeclaration, MethodDeclaration methodDeclaration) {
        if (!isSuitableInterface(interfaceDeclaration)) {
            printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.INTERFACES_NOT_SUPPORTED, interfaceDeclaration.getQualifiedName()));
        }
    }

    private boolean isSuitableInterface(InterfaceDeclaration interfaceDeclaration) {
        return isJavaType(interfaceDeclaration.getQualifiedName()) && JAVA_TYPES.contains(interfaceDeclaration.getQualifiedName())
        || isXMLType(interfaceDeclaration) || isAsyncType(interfaceDeclaration);
    }
    
    private boolean isAsyncType(InterfaceDeclaration interfaceDeclaration) {
    	String qn = Signature.getTypeErasure(interfaceDeclaration.getQualifiedName());
		if (qn.equals("javax.xml.ws.Response") || qn.equals("java.util.concurrent.Future") //$NON-NLS-1$ //$NON-NLS-2$
			|| qn.equals("javax.xml.ws.AsyncHandler")) { //$NON-NLS-1$
			return true;
		}
        return false;
    }

    private boolean isXMLType(InterfaceDeclaration interfaceDeclaration) {
        Collection<AnnotationMirror> aannotationMirrors = interfaceDeclaration.getAnnotationMirrors();

        for (AnnotationMirror annotationMirror : aannotationMirrors) {
            AnnotationTypeDeclaration annotationTypeDeclaration = annotationMirror.getAnnotationType().getDeclaration();
            if (annotationTypeDeclaration != null
                    && annotationTypeDeclaration.getQualifiedName().equals(XML_TYPE)) {
                return true;
            }
        }
        return false;
    }
    
    private void checkIfRemoteObject(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration) {
        if (isRemoteObject(classDeclaration)) {
            printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.IS_REMOTE_OBJECT, classDeclaration.getQualifiedName()));
        }
    }

    private boolean isRemoteObject(ClassDeclaration classDeclaration) {
        Collection<InterfaceType> superInterfaces = classDeclaration.getSuperinterfaces();
        for (InterfaceType interfaceType : superInterfaces) {

            if (interfaceType.getDeclaration() != null && interfaceType.getDeclaration().getQualifiedName().equals(REMOTE_OBJECT)) {
                return true;
            }
        }
        return false;
    }

    private void checkAbstactType(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration) {
        if (classDeclaration.getModifiers().contains(Modifier.ABSTRACT)) {
            if (environment instanceof EclipseAnnotationProcessorEnvironment) {
                EclipseAnnotationProcessorEnvironment eclipseEnvironment = (EclipseAnnotationProcessorEnvironment) environment;
                IJavaProject javaProject = eclipseEnvironment.getJavaProject();
                try {
                    IType type = javaProject.findType(classDeclaration.getQualifiedName());
                    ITypeHierarchy hierarchy = type.newTypeHierarchy(null);
                    IType[] subTypes = hierarchy.getAllSubtypes(type);
                    if (subTypes.length > 0) {
                        for (IType subType : subTypes) {
                            if (!Flags.isAbstract(subType.getFlags())) {
                                TypeDeclaration typeDeclaration =  environment.getTypeDeclaration(subType.getFullyQualifiedName());
                                if (typeDeclaration instanceof ClassDeclaration) {
                                    ClassDeclaration subTypeDeclaration = (ClassDeclaration) typeDeclaration;
                                    if (isRemoteObject(subTypeDeclaration) || hasInnerNonPublicStaticTypes(subTypeDeclaration)
                                            || implementsMultipleInterfaces(subTypeDeclaration)
                                            || implementsClassAndInterface(subTypeDeclaration)) {
                                        printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                                                JAXWSCoreMessages.ABSTRACT_CLASS_NOT_IMPLEMENTED,
                                                classDeclaration.getQualifiedName()));
                                    }
                                }
                            }
                        }
                    } else {
                        printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                                JAXWSCoreMessages.ABSTRACT_CLASS_NOT_IMPLEMENTED,
                                classDeclaration.getQualifiedName()));
                    }
                } catch (JavaModelException jme) {
                    JAXWSCorePlugin.log(jme.getStatus());
                }
            }
        }
    }

    private boolean implementsMultipleInterfaces(ClassDeclaration classDeclaration) {
        if (hasSuperClass(classDeclaration)) {
            return countAllowedInterfaces(classDeclaration.getSuperinterfaces()) > 1;
        }
        return false;
    }

    private boolean implementsClassAndInterface(ClassDeclaration classDeclaration) {
        if (!hasSuperClass(classDeclaration)) {
            return countAllowedInterfaces(classDeclaration.getSuperinterfaces()) > 0;
        }
        return false;
    }

    private boolean hasSuperClass(ClassDeclaration classDeclaration) {
        ClassType superClass = classDeclaration.getSuperclass();
        if (superClass.getDeclaration() != null) {
            String qualifiedName = superClass.getDeclaration().getQualifiedName();
            return qualifiedName.equals(Object.class.getName()) || qualifiedName.equals(Exception.class.getName());
        }
        return false;
    }

    private void checkMultipleInheritance(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration) {
        if (implementsMultipleInterfaces(classDeclaration)) {
            List<String> interfaceNames = getAllowedInterfaceNames(classDeclaration.getSuperinterfaces());
            printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.IMPLEMENTS_MULTIPLE_INTERFACES,
                    new Object [] { classDeclaration.getQualifiedName(), interfaceNames.get(0), interfaceNames.get(1) }));
        }
        if (implementsClassAndInterface(classDeclaration)) {
            List<String> interfaceNames = getAllowedInterfaceNames(classDeclaration.getSuperinterfaces());
            printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.INHERITANCE_AND_IMPLEMENTATION,
                    new Object[] {classDeclaration.getQualifiedName(),
                            classDeclaration.getSuperclass().getDeclaration().getQualifiedName(),
                            interfaceNames.get(0)} ));
        }
    }

    private List<String> getAllowedInterfaceNames(Collection<InterfaceType> superInterfaces) {
        List<String> interfaceNames = new ArrayList<String>();
        for (InterfaceType interfaceType : superInterfaces) {
            if (interfaceType.getDeclaration() != null) {
                InterfaceDeclaration superInterface = interfaceType.getDeclaration();
                if (!ALLOWED_IN_MULTIPLE_INHERITANCE.contains(superInterface.getQualifiedName())) {
                    interfaceNames.add(superInterface.getQualifiedName());
                }
            }
        }
        return interfaceNames;
    }

    private int countAllowedInterfaces(Collection<InterfaceType> superInterfaces) {
        int count = 0;
        for (InterfaceType interfaceType : superInterfaces) {
            if (interfaceType.getDeclaration() != null) {
                InterfaceDeclaration superInterface = interfaceType.getDeclaration();
                if (!ALLOWED_IN_MULTIPLE_INHERITANCE.contains(superInterface.getQualifiedName())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isJavaType(String typeName) {
        return typeName.startsWith("java.")  || typeName.startsWith("javax."); //$NON-NLS-1$ //$NON-NLS-2$
    }

}