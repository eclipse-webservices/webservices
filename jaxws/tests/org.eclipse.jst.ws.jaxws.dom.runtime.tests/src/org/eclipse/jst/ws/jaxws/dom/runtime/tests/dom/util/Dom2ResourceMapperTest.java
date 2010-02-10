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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.util;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;

/**
 * Tests for {@link Dom2ResourceMapper} class
 * 
 * @author Georgi Vachkov
 */
public class Dom2ResourceMapperTest extends TestCase
{
    private Dom2ResourceMapper mapper = Dom2ResourceMapper.INSTANCE;
    private TestProject testProject;
    private TestProject testProject1;

    private IType seiType;
    private IType wsType;
    private IDOM dom;

    @Override
    public void setUp() throws Exception
    {
        testProject = prepareProject();
        IPackageFragment pck = testProject.createPackage("test");
        seiType = testProject.createType(pck, "Sei.java", "@javax.jws.WebService public interface Sei { \n" +
                "@javax.jws.WebMethod public void test(int a); \n" +
        "}");

        testProject1 = prepareProject();
        pck = testProject1.createPackage("test");
        wsType = testProject1.createType(pck, "Ws.java", "@javax.jws.WebService public class Ws {}");


        final IWsDOMRuntimeExtension domRuntime = WsDOMRuntimeManager.instance().getDOMRuntime("org.eclipse.jst.ws.jaxws.dom.jee5");
        domRuntime.createDOM(null);
        dom = domRuntime.getDOM();
    }

    private TestProject prepareProject() throws Exception
    {
        TestEjb3Project tp = new TestEjb3Project("Ejb");
        return new TestProject(tp.getProject());
    }

    @Override
    public void tearDown()
    {
        try {
            testProject.dispose();
        } catch (Exception _) {}
        try {
            testProject1.dispose();
        } catch (Exception _) {}
    }

    public void testFindProject()
    {
        IWebServiceProject wsProject = DomUtil.INSTANCE.findProjectByName(dom, testProject.getProject().getName());
        IProject project = mapper.findProject(wsProject);
        assertEquals(testProject.getProject(), project);
    }

    public void testFindResourceSei() throws JavaModelException
    {
        IWebServiceProject wsProject = DomUtil.INSTANCE.findProjectByName(dom, testProject.getProject().getName());
        IServiceEndpointInterface sei = DomUtil.INSTANCE.findSeiByImplName(wsProject, "test.Sei");
        assertNotNull(sei);

        // find using SEI
        IType type = mapper.findType(sei);
        assertNotNull(type);
        assertEquals(seiType, type);
    }

	public void testFindResourceWs() throws JavaModelException
	{
		IWebServiceProject wsProject = DomUtil.INSTANCE.findProjectByName(dom, testProject1.getProject().getName());
		IWebService ws = DomUtil.INSTANCE.findWsByImplName(wsProject, "test.Ws");
		assertNotNull(ws);

		IType type = mapper.findType(ws);
		assertNotNull(type);
		assertEquals(wsType, type);
	}

    public void testFindResourceWebMethod() throws JavaModelException
    {
        IWebServiceProject wsProject = DomUtil.INSTANCE.findProjectByName(dom, testProject.getProject().getName());
        IServiceEndpointInterface sei = DomUtil.INSTANCE.findSeiByImplName(wsProject, "test.Sei");
        assertNotNull(sei);

        IType type = mapper.findType(sei.getWebMethods().get(0));
        assertNotNull(type);
        assertEquals(seiType, type);

        final IWebMethod webMethod = DomFactory.eINSTANCE.createIWebMethod();
        type = mapper.findType(webMethod);
        assertNull(type);
    }

    public void testFindResourceWebParam() throws JavaModelException
    {
        IWebServiceProject wsProject = DomUtil.INSTANCE.findProjectByName(dom, testProject.getProject().getName());
        IServiceEndpointInterface sei = DomUtil.INSTANCE.findSeiByImplName(wsProject, "test.Sei");
        assertNotNull(sei);

        IType type = mapper.findType(sei.getWebMethods().get(0).getParameters().get(0));
        assertNotNull(type);
        assertEquals(seiType, type);
    }
}
