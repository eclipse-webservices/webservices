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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.apt.core.internal.AptPlugin;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.WebServiceProblemsDecorator;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Tests for {@link WebServiceDecorator} class
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("restriction")
public class WebServiceDecoratorTest extends MockObjectTestCase
{
    private MyWebServiceProblemsDecorator decorator;
    private TestProject testProject;

    private IWebService ws;
    private IServiceEndpointInterface sei;
    private IWebServiceProject wsProject;
    private IWebMethod webMethod;
    private IWebParam webParam;
    private Mock<IWebServiceChildList> wsChildList;
    private Mock<ISEIChildList> seiChildList;
    private IPackageFragment testPackage;

    private IType seiType;
    private IType wsImplType;
    
    private JaxWsWorkspaceResource jaxwsResource;

    private static final int NO_ERROR = 0;

    private static final String SEI_CONTENT = "@javax.jws.WebService(name=\"{0}\")\n" + "public interface ISei '{'\n" + "@javax.jws.WebMethod(operationName=\"{1}\")\n"
    + "public void testMethod(@javax.jws.WebParam(name=\"{2}\") String s);}";
    private static final String IMPL_CONTENT = "@javax.jws.WebService(serviceName=\"{0}\", endpointInterface=\"test.ISei\")\n" + "public class EndpointClass '{'\n"
    + "public void testMethod(String s)'{'}}";

    @Override
    public void setUp() throws CoreException
    {
        decorator = new MyWebServiceProblemsDecorator();
        testProject = null;
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (testProject != null)
        {
            testProject.dispose();
    		jaxwsResource.stopSynchronizing();
        }
    }

    public void testComputeAdornmentForObject()
    {
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(new Object()));
    }

    public void testComputeAdornmentNoValidationErrors() throws CoreException, WsDOMLoadCanceledException, IOException
    {
        createWsContent("MyPort", "myMethod", "myParam", "MyService");
        initWsDomObjects();
        checkNoErrorsAdornment();
    }

	public void testComputeAdornmentForSeiError() throws CoreException, WsDOMLoadCanceledException, AnnotationGeneratorException, IOException
	{
		createWsContent("", "myMethod", "myParam", "MyService");
		waitForAptMarkersAppear();
		initWsDomObjects();
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(wsChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(seiChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(ws));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(sei));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webMethod));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webParam));

		fixAptErrors();
		checkNoErrorsAdornment();
	}

	public void testComputeAdornmentForWebMethodError() throws CoreException, WsDOMLoadCanceledException, AnnotationGeneratorException, IOException
	{
		createWsContent("MyPort", "", "myParam", "MyService");
		waitForAptMarkersAppear();
		initWsDomObjects();
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(wsChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(seiChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(ws));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(sei));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(webMethod));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webParam));

		fixAptErrors();
		checkNoErrorsAdornment();
	}

	public void testComputeAdornmentForWebParamError() throws CoreException, WsDOMLoadCanceledException, AnnotationGeneratorException, IOException
	{
		createWsContent("MyPort", "MyMethod", "", "MyService");
		waitForAptMarkersAppear();
		initWsDomObjects();
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(wsChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(seiChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(ws));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(sei));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(webMethod));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(webParam));

		fixAptErrors();
		checkNoErrorsAdornment();
	}

	public void testComputeAdornmentForWsImplError() throws CoreException, WsDOMLoadCanceledException, AnnotationGeneratorException, IOException
	{
		createWsContent("MyPort", "MyMethod", "myParam", "");
		waitForAptMarkersAppear();
		initWsDomObjects();
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(wsChildList.proxy()));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(seiChildList.proxy()));
		assertEquals("Unexpected adornment", JavaElementImageDescriptor.ERROR, decorator.computeAdornmentFlags(ws));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(sei));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webMethod));
		assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webParam));

		fixAptErrors();
		checkNoErrorsAdornment();
	}

	private void createWsContent(final String seiName, final String webMethodName, final String paramName, final String serviceName) throws CoreException, WsDOMLoadCanceledException, IOException
	{
		final IProject webP = TestProjectsUtils.createWeb25Project("WebTester" + System.currentTimeMillis());
		testProject = new TestProject(webP);
		testProject.setAptProcessingEnabled(true, false);

		jaxwsResource = new JaxWsWorkspaceResource(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
		jaxwsResource.load(null);
		jaxwsResource.startSynchronizing();

		testPackage = testProject.createPackage("test");
		seiType = testProject.createType(testPackage, "ISei.java", MessageFormat.format(SEI_CONTENT, seiName, webMethodName, paramName));
		wsImplType = testProject.createType(testPackage, "EndpointClass.java", MessageFormat.format(IMPL_CONTENT, serviceName));
		testProject.build(IncrementalProjectBuilder.CLEAN_BUILD);
	}

    private void initWsDomObjects() throws WsDOMLoadCanceledException
    {
		this.wsProject = DomUtil.INSTANCE.findProjectByName(jaxwsResource.getDOM(), testProject.getProject().getName());
		assertNotNull(this.wsProject);

        ws = wsProject.getWebServices().iterator().next();
        sei = wsProject.getServiceEndpointInterfaces().iterator().next();
        webMethod = sei.getWebMethods().iterator().next();
        webParam = webMethod.getParameters().iterator().next();

        wsChildList = mock(IWebServiceChildList.class);
        wsChildList.stubs().method("getWSChildList").will(returnValue(elist(ws)));
        seiChildList = mock(ISEIChildList.class);
        seiChildList.stubs().method("getSEIChildList").will(returnValue(elist(sei)));
    }

    private void waitForAptMarkersAppear()
    {
        Assertions.waitAssert(new IWaitCondition()
        {
            public boolean checkCondition() throws ConditionCheckException
            {
                try
                {
                    return findAptMarkers().size() > 0;
                } catch (CoreException e)
                {
                    throw new ConditionCheckException(e);
                }
            }
        }, "APT markers did not appear");
    }
    
    private Collection<IMarker> findAptMarkers() throws CoreException
    {
		final Collection<IMarker> result = new ArrayList<IMarker>();
		for(final IMarker m : testProject.getProject().findMarkers(AptPlugin.APT_COMPILATION_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE))
		{
			result.add(m);
		}
		
		for(final IMarker m : testProject.getProject().findMarkers(AptPlugin.APT_NONRECONCILE_COMPILATION_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE))
		{
			result.add(m);
		}
		
		return result;
    }

    private void waitForNoAptMarkers()
    {
        Assertions.waitAssert(new IWaitCondition()
        {
            public boolean checkCondition() throws ConditionCheckException
            {
                try
                {
                    return testProject.getProject().findMarkers(AptPlugin.APT_NONRECONCILE_COMPILATION_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE).length == 0;
                } catch (CoreException e)
                {
                    throw new ConditionCheckException(e);
                }
            }
        }, "APT markers did not disappear");
    }

    private EList<EObject> elist(final EObject... elements)
    {
        final EList<EObject> result = new BasicEList<EObject>();
        for (EObject eObj : elements)
        {
            result.add(eObj);
        }

        return result;
    }

    private void fixAptErrors() throws CoreException, AnnotationGeneratorException
    {
        final AnnotationWriter annWriter = AnnotationWriter.getInstance();

        final IAnnotationInspector seiInspector = AnnotationFactory.createAnnotationInspector(seiType);
        final IAnnotation<IType> seiWsAnnotation = seiInspector.inspectType("javax.jws.WebService");
        seiWsAnnotation.getParamValuePairs().clear();
        annWriter.update(seiWsAnnotation);

        final IMethod seiMethod = seiType.getMethods()[0];
        final IAnnotation<IMethod> seiWebMethodAnnotation = seiInspector.inspectMethod(seiMethod, "javax.jws.WebMethod");
        annWriter.remove(seiWebMethodAnnotation);

        final ITypeParameter seiMethodParam = seiMethod.getTypeParameter(seiMethod.getParameterNames()[0]);
        final IAnnotation<ITypeParameter> seiWebParamAnnotation = seiInspector.inspectParam(seiMethodParam, "javax.jws.WebParam");
        annWriter.remove(seiWebParamAnnotation);

        final IAnnotationInspector wsImplInspector = AnnotationFactory.createAnnotationInspector(wsImplType);
        final IAnnotation<IType> wsImplWsAnnotation = wsImplInspector.inspectType("javax.jws.WebService");
        final Set<IParamValuePair> newAnnValues = new HashSet<IParamValuePair>();
        for (IParamValuePair pair : wsImplWsAnnotation.getParamValuePairs())
        {
            if (pair.getParam().equals("serviceName"))
            {
                newAnnValues.add(AnnotationFactory.createParamValuePairValue("serviceName", AnnotationFactory.createStringValue("MyServiceName")));
            } else
            {
                newAnnValues.add(pair);
            }
        }
        wsImplWsAnnotation.getParamValuePairs().clear();
        wsImplWsAnnotation.getParamValuePairs().addAll(newAnnValues);
        annWriter.update(wsImplWsAnnotation);
        waitForNoAptMarkers();
    }

    private void checkNoErrorsAdornment()
    {
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(wsChildList.proxy()));
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(seiChildList.proxy()));
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(ws));
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(sei));
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webMethod));
        assertEquals("Unexpected adornment", NO_ERROR, decorator.computeAdornmentFlags(webParam));
    }

    private class MyWebServiceProblemsDecorator extends WebServiceProblemsDecorator
    {
        @Override
        public int computeAdornmentFlags(Object obj)
        {
            return super.computeAdornmentFlags(obj);
        }
    }
}
