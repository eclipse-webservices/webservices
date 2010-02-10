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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.validation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.MethodValidator;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.AbstractClassNotImplementedException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.ConstructorNotExposableException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.HasInadmisableInnerTypesException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InheritanceAndImplementationExecption;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InterfacesNotSupportedException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.MethodNotPublicException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.NoDefaultConstructorException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.RemoteObjectException;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;

@SuppressWarnings("nls")
public class MethodValidatorTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "runtime.type.validation.test";

	private MethodValidator validator;

	private IType endpoint;

	@Override
	protected void setUp() throws Exception
	{
		createJavaProject(SRC, PCK);

		createClasses("mock/RuntimeClasses.src");
		endpoint = createClass("mock/Endpoint.src", "Endpoint");

		validator = new MethodValidator();
	}

	public void testCheckGeneralCases() throws Exception
	{
		String[] emptyParams = new String[] {};

		// check constructor
		IMethod method = endpoint.getMethod("Endpoint", emptyParams);
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertTrue(status.getException() instanceof ConstructorNotExposableException);

		// check private method
		method = endpoint.getMethod("privateMethod", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof MethodNotPublicException);

		// check protected
		method = endpoint.getMethod("protectedMethod", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof MethodNotPublicException);

		// check use unsupported javax.* class for return type
		method = endpoint.getMethod("useUnsupportedJavaType", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof NoDefaultConstructorException);

		// check use unsupported javax.* class for return type
		method = endpoint.getMethod("useSupportedJavaType", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// check use void return type
		method = endpoint.getMethod("useVoid", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// check use array as return type
		method = endpoint.getMethod("useArrayAsReturnValue", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// check use array as paramether
		method = endpoint.getMethod("useArrayAsParam", new String[] { "[I" });
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// check use multy dimention array as paramether
		method = endpoint.getMethod("useMDArrayAsParam", new String[] { "[[[I" });
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// check use nested generics
		method = endpoint.getMethod("useNestedGenerics", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());

		method = endpoint.getMethod("useExtendsInGenerics", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());

		method = endpoint.getMethod("useSuperInGenerics", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());

		method = endpoint.getMethod("useSuperInGenericsAsParam", new String[] { "QList<-QString;>;" });
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());

		// check method returning enum
		method = endpoint.getMethod("useEnum", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());

		// check method returning custom map
		method = endpoint.getMethod("useMyMap", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertNotNull(status.getMessage());
		assertNull(status.getException());
	}

	public void testCeckTypesIncompatible() throws Exception
	{
		String[] emptyParams = new String[] {};

		// use type which doesn't have default constructor
		IMethod method = endpoint.getMethod("useImplementsRemote", emptyParams);
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof RemoteObjectException);

		// use type which doesn't have default constructor
		method = endpoint.getMethod("useNoDefaultConstructor", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof NoDefaultConstructorException);

		// use type which has non public non static inner class
		method = endpoint.getMethod("useHasInadmisableInnerClass", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof HasInadmisableInnerTypesException);

		// use type as paramether which contains unsupported java type as public field
		String[] params = new String[] { "QUsesUnsupportedAsField;" };
		method = endpoint.getMethod("useUsesUnsupportedAsField", params);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof NoDefaultConstructorException);
	}

	public void testCeckTypesIncompatibleImplementation() throws Exception
	{
		String[] emptyParams = new String[] {};

		// use not implemented abstract class
		IMethod method = endpoint.getMethod("useNotImplementedAbstractClass", emptyParams);
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof AbstractClassNotImplementedException);

		// use implemented abstract class
		method = endpoint.getMethod("useImplementedAbstractClass", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());
	}

	public void testCheckInterfacesUsed() throws Exception
	{
		String[] emptyParams = new String[] {};

		// use not implemented interface
		IMethod method = endpoint.getMethod("useInterface", emptyParams);
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof InterfacesNotSupportedException);
	}

	public void testCeckTypesIncompatibleInheritance() throws Exception
	{
		String[] emptyParams = new String[] {};

		// use class that extends custom class and custom interface
		IMethod method = endpoint.getMethod("useMultyInheritanceUseClass", new String[] { "QMultyInheritanceUseClass;" });
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.ERROR);
		assertNotNull(status.getMessage());
		assertTrue(status.getException() instanceof InheritanceAndImplementationExecption);

		// use class that extends class and implements 3 supported interfaces
		method = endpoint.getMethod("useSupportedMultyInheritanceWithBaseClass", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		// SupportedMultyInheritanceWithInterface
		method = endpoint.getMethod("useSupportedMultyInheritanceWithInterface", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		method = endpoint.getMethod("useImplementedBaseInGenerics", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());
	}

	public void testCeckMethodsWithExceptions() throws Exception
	{
		String[] emptyParams = new String[] {};

		// use class that extends java.lang.Exception and custom interface
		IMethod method = endpoint.getMethod("useCustomException", emptyParams);
		assertNotNull(method);
		IStatus status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());

		method = endpoint.getMethod("useException", emptyParams);
		assertNotNull(method);
		status = validator.check(method);
		assertTrue(status.getSeverity() == IStatus.OK);
		assertTrue(status.getMessage().equals(""));
		assertNull(status.getException());
	}

	public void testCheckMethodWithDefaultAccessInInterface() throws CoreException
	{
		final String seiCode = "public interface SEIWithDefaultAccessMethod { \n" +
				"	String test(); \n" +
				"}";
		final IType seiType = getTestProject().createType(defaultPackage, "SEIWithDefaultAccessMethod.java", seiCode);
		final IMethod method = seiType.getMethod("test", new String []{});
		IStatus status = validator.check(method);
		assertEquals("Default method in SEI reported as incorrect", IStatus.OK, status.getSeverity());
	}
}
