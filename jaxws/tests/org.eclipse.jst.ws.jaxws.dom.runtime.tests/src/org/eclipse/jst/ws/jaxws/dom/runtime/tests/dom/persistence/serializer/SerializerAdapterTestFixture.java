/*******************************************************************************
 * Copyright (c) 2009, 2024 by SAP AG, Walldorf and others
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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer;

import junit.framework.TestCase;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.DomTestUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;

public abstract class SerializerAdapterTestFixture extends TestCase 
{
	protected static final String WS_ANNOTATION = "javax.jws.WebService";	
	protected static final String SERVICE_NAME = "MyService";
	protected static final String PORT_TYPE_NAME = "MyPortType";
	protected static final String PORT_NAME = "MyPort";
	protected static final String TARGET_NAMESPACE = "http://com.sap/test";
	protected static final String WSDL_LOCATION = "C:/test/wsdl/location";
	
	protected IWebService ws;
	protected IWebService wsImplicit;
	
	protected IServiceEndpointInterface sei;
	protected TestProject project;
	protected IType implBeanType;
	protected IType implBeanImplicit;
	protected IType seiType;
	protected IWebServiceProject wsProject;
	protected JaxWsWorkspaceResource resource;
	protected DomTestUtils testUtil = new DomTestUtils();
	protected DomUtil domUtil = new DomUtil();
	
	@Override
	public void setUp() throws Exception
	{
		project = new TestProject();
		final IPackageFragment pack = project.createSourceFolder("src").createPackageFragment("test", true, null);
		seiType = project.createType(pack, "SEI.java", "@javax.jws.WebService(name=\"MyPortType\", targetNamespace=\"http://com.sap/test\")\n" +
				"public interface SEI {}");
		implBeanType = project.createType(pack, "ImplBean.java", "@javax.jws.WebService(endpointInterface=\"test.SEI\", " +
				"serviceName=\"MyService\", portName=\"MyPort\", wsdlLocation=\"C:/test/wsdl/location\", targetNamespace=\"http://com.sap/test\")" +
				"public class ImplBean {}");
		
		implBeanImplicit = project.createType(pack, "ImplicitImplBean.java", "@javax.jws.WebService(name=\"ImplicitName\", " +
				"serviceName=\"MyService\", portName=\"MyPort\", wsdlLocation=\"C:/test/wsdl/location\", targetNamespace=\"http://com.sap/test\")" +
		"public class ImplicitImplBean {}");		
		
		assertNotNull(seiType);
		assertNotNull(implBeanType);
		resource = createTarget();	
		resource.load(null);
		
		wsProject = resource.getDOM().getWebServiceProjects().get(0);
		sei = domUtil.findSeiByImplName(wsProject, "test.SEI");
		ws = domUtil.findWsByImplName(wsProject, "test.ImplBean");
		wsImplicit = domUtil.findWsByImplName(wsProject, "test.ImplicitImplBean");
	}
	
	@Override
	public void tearDown()
	{
		try {
			project.dispose();
		} catch (Exception e) {
		}
	}
	
	private JaxWsWorkspaceResource createTarget()
	{
		return new JaxWsWorkspaceResource(project.getJavaProject().getJavaModel()) 
		{
			@Override
			public boolean approveProject(IJavaProject prj) 
			{
				if (prj.getElementName().equals(project.getJavaProject().getElementName()))
				{
					return true;
				}

				return false;
			}
		};
	}		
}
