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

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.RuntimeTypeValidator;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.AbstractClassNotImplementedException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.HasInadmisableInnerTypesException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InadmissableTypeException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InheritanceAndImplementationExecption;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.MultipleImplementationException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.NoDefaultConstructorException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.RemoteObjectException;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;

/**
 * Test for runtime types validation.
 *
 * @author Georgi Vachkov
 */
@SuppressWarnings("nls")
public class RuntimeTypeValidatorTest extends ClassLoadingTest
{
	private static String SRC = "src";

	private static String PCK = "runtime.type.validation.test";

	private RuntimeTypeValidator validator;

	@Override
	protected void setUp() throws Exception // $JL-EXC$
	{
		createJavaProject(SRC, PCK);

		createClasses("mock/RuntimeClasses.src");
		validator = new RuntimeTypeValidator();
	}

	public void testValidateGeneralCases() throws Exception // $JL-EXC$
	{
		// check unsupported javax.* class
		IType type = TypeFactory.create(getProjectName(), "java.sql.Date");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("NotSupportedJavaType");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}

		// check supported java type
		type = TypeFactory.create(getProjectName(), "java.net.URI");
		assertNotNull(type);
		validator.validate(type);

		// check unsupported javax.* class used in getter
		type = TypeFactory.create(getProjectName(), PCK + ".UsesUnsupportedAsProperty");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("UsesUnsupportedAsProperty");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}

		// uses unsupported in not a getter method
		type = TypeFactory.create(getProjectName(), PCK + ".UsesUnsupportedInMethod");
		assertNotNull(type);
		validator.validate(type);

	}

	public void testJavaSqlTymestampNotSupported() throws Exception
	{
		IType type = TypeFactory.create(getProjectName(), "java.sql.Timestamp");
		try {
			validator.validate(type);
			fail("java.sql.Timestamp not detected as unsupported");
		} catch (NoDefaultConstructorException _) {
		}
	}

	public void testValidateNoDefaultConstructor() throws Exception // $JL-EXC$
	{
		// use type which doesn't have default constructor
		IType type = TypeFactory.create(getProjectName(), PCK + ".NoDefaultConstructor");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("NoDefaultConstructor");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}

		type = TypeFactory.create(getProjectName(), PCK + ".NoDefaultConstructorPrivateConstructors");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("NoDefaultConstructorPrivateConstructors");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}
	}

	public void testUsesUnsupportedInTransientMethod() throws Exception
	{
		// uses unsupported in xml transient method
		IType type = TypeFactory.create(getProjectName(), PCK + ".UsesUnsupportedInTransientMethod");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testXmlTypeAnnotated() throws Exception
	{
		IType type = TypeFactory.create(getProjectName(), PCK + ".XmlTypeClass");
		assertNotNull(type);
		try
		{
			validator.validate(type);
		} catch (NoDefaultConstructorException e)
		{
			fail("XML Type annotated class not recognized");
		}
	}

	public void testWebFaultAnnotated() throws Exception
	{
		IType type = TypeFactory.create(getProjectName(), PCK + ".WebFaultClass");
		assertNotNull(type);
		try
		{
			validator.validate(type);
		} catch (NoDefaultConstructorException e)
		{
			fail("Web Fault annotated class not recognized");
		}
	}

	public void testValidateCustomCollection() throws Exception // $JL-EXC$
	{
		IType type = TypeFactory.create(getProjectName(), PCK + ".MySet");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateCustomMap() throws Exception // $JL-EXC$
	{
		IType type = TypeFactory.create(getProjectName(), PCK + ".MyMap");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateTypesIncompatible() throws Exception // $JL-EXC$
	{
		// use type which doesn't have default constructor
		IType type = TypeFactory.create(getProjectName(), PCK + ".ImplementsRemote");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("ImplementsRemote");
		} catch (RemoteObjectException e)
		{
			assertTrue(true);
		}

		// use type which has non public non static inner class
		type = TypeFactory.create(getProjectName(), PCK + ".HasInadmisableInnerClass");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("ImplementsRemote");
		} catch (HasInadmisableInnerTypesException e)
		{
			assertTrue(true);
		}

		// use type as paramether which contains unsupported java type as public field
		type = TypeFactory.create(getProjectName(), PCK + ".UsesUnsupportedAsField");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("ImplementsRemote");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}
	}

	//FIXME Uncomment when Eclipse 3.6 M4 is used in the WTP builds. Contains a fix for Bug# 158361
//	public void testValidateTypesIncompatibleImplementation() throws Exception
//	{
//		// use not implemented abstract class
//		IType type = TypeFactory.create(getProjectName(), PCK + ".BaseNotImplemented");
//		assertNotNull(type);
//		try
//		{
//			validator.validate(type);
//			fail("ImplementsRemote");
//		} catch (AbstractClassNotImplementedException e)
//		{
//			assertTrue(true);
//		}
//
//		// use implemented abstract class
//		type = TypeFactory.create(getProjectName(), PCK + ".BaseImplemented");
//		assertNotNull(type);
//		validator.validate(type);
//
//		// use interface
//		type = TypeFactory.create(getProjectName(), PCK + ".InterfaceImplemented");
//		assertNotNull(type);
//		try
//		{
//			// use implemented interface
//			validator.validate(type);
//		} catch (InterfacesNotSupportedException e)
//		{
//			assertTrue(true);
//		}
//	}

	public void testValidateTypesIncompatibleInheritance() throws Exception
	{
		// use class that extends custom class and custom interface
		IType type = TypeFactory.create(getProjectName(), PCK + ".MultyInheritanceUseClass");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("MultyInheritanceUseClass");
		} catch (InheritanceAndImplementationExecption e)
		{
			assertTrue(true);
		}

		// use multy inheritance with 2 implemented interfaces
		type = TypeFactory.create(getProjectName(), PCK + ".MultyInheritanceUseInterfaces");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("MultyInheritanceUseInterfaces");
		} catch (MultipleImplementationException e)
		{
			assertTrue(true);
		}

		// use class that extends class and implements 3 supported interfaces
		type = TypeFactory.create(getProjectName(), PCK + ".SupportedMultyInheritanceWithBaseClass");
		assertNotNull(type);
		validator.validate(type);

		// SupportedMultyInheritanceWithInterface
		type = TypeFactory.create(getProjectName(), PCK + ".SupportedMultyInheritanceWithInterface");
		assertNotNull(type);
		validator.validate(type);

		type = TypeFactory.create(getProjectName(), PCK + ".BaseImplementedNotCorrectly");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("MultyInheritanceUseClass");
		} catch (AbstractClassNotImplementedException e)
		{
			assertTrue(true);
		}
	}

	public void testValidateTypesUsingGenerics() throws Exception
	{
		// check supported type in list using generics
		IType type = TypeFactory.create(getProjectName(), PCK + ".ContainsList");
		assertNotNull(type);
		validator.validate(type);

		// check not supported type in list using generics
		type = TypeFactory.create(getProjectName(), PCK + ".ContainsNotSupportedInList");
		assertNotNull(type);
		try
		{
			validator.validate(type);
			fail("ContainsNotSupportedInList");
		} catch (NoDefaultConstructorException e)
		{
			assertTrue(true);
		}

		// check supported type in list using quote
		type = TypeFactory.create(getProjectName(), PCK + ".ContainsQuoteInList");
		assertNotNull(type);
		validator.validate(type);

		// check supported type in list using quote
		type = TypeFactory.create(getProjectName(), PCK + ".ContainsExtendsInList");
		assertNotNull(type);
		validator.validate(type);

		type = TypeFactory.create(getProjectName(), PCK + ".ContainsSuperInList");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateTypesCircularReference() throws Exception
	{
		// check circular reference via member varibles
		IType type = TypeFactory.create(getProjectName(), PCK + ".CircularAsParamA");
		assertNotNull(type);
		validator.validate(type);

		// check circular reference via return values
		type = TypeFactory.create(getProjectName(), PCK + ".CircularAsReturnValueB");
		assertNotNull(type);
		validator.validate(type);

		// check circular reference via member and return value
		type = TypeFactory.create(getProjectName(), PCK + ".CircularAsMixedParamReturnValueA");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateEnumType() throws Exception
	{
		IType type = TypeFactory.create(getProjectName(), PCK + ".EnumType");
		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateInterfaceInObjectFactory() throws Exception
	{
		final String PCK1 = "org.eclipse.test.implemented";
		final IPackageFragment pack = getTestProject().createPackage(PCK1);

		final IType type = getTestProject().createType(pack, "ImplementedInterface.java",  "public interface ImplementedInterface {}");
		getTestProject().createType(pack, "ObjectFactory.java",
			"public class ObjectFactory {" +
			"	public ImplementedInterface createImplementedInterface() {" +
			"		return null;" +
			"	}" +
			"}");

		assertNotNull(type);
		validator.validate(type);
	}

	public void testValidateParameterizedMethodParameterClass() throws JavaModelException, InadmissableTypeException
	{
		final IType parameterizedType = TypeFactory.create(getProjectName(), PCK + ".ParameterizedClass");
		assertNotNull(parameterizedType);
		final IType endpointType = TypeFactory.create(getProjectName(), PCK + ".ParameterizedMethodParameterEndpointClass");
		assertNotNull(endpointType);

		validator.validate(parameterizedType);
	}

	public void testValidateParameterizedReturnTypeClass() throws JavaModelException, InadmissableTypeException
	{
		final IType parameterizedType = TypeFactory.create(getProjectName(), PCK + ".ParameterizedClass");
		assertNotNull(parameterizedType);
		final IType endpointType = TypeFactory.create(getProjectName(), PCK + ".ParameterizedMethodReturnTypeEndpointClass");
		assertNotNull(endpointType);

		validator.validate(parameterizedType);
	}
}
