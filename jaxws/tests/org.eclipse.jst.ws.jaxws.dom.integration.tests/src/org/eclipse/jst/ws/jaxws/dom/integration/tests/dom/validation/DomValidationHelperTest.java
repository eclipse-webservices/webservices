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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.dom.validation;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jst.ws.jaxws.dom.integration.validation.DomValidationHelper;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;

/**
 * Test class for {@link DomValidationHelper}.
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("restriction")
public class DomValidationHelperTest extends MockObjectTestCase
{
    private DomValidationHelper helper;
    @Override
    public void setUp() {
        helper = new DomValidationHelper();
    }

    public void testDomValidationHelper() {
        assertTrue(helper.isRegistered("webServiceProject"));
    }

	public void testGetWebServiceProject() throws Exception
	{
		TestEjb3Project ejbProject = new TestEjb3Project("EjbProject");
		TestProject testProject = new TestProject(ejbProject.getProject());
		IPackageFragment pack = testProject.getSourceFolder().createPackageFragment("org.elcipse.test", true, null);
		testProject.createType(pack, "Sei.java", "@javax.jws.WebService public interface Sei {}");

		helper.setProject(testProject.getProject());
		assertNotNull("DOM project not retrieved", helper.getWebServiceProject("org.eclipse.jst.ws.jaxws.dom.jee5"));

		try {
			testProject.dispose();
		} catch (Exception _) {
		}
	}

    public void testGetWebServiceProjectDOMLoadingCanceled() throws Exception
    {
        final Mock<IWsDOMRuntimeExtension> extMock = mock(IWsDOMRuntimeExtension.class);
        extMock.expects(once()).method("getDOM").will(throwException(new WsDOMLoadCanceledException("", "")));
        helper = new DomValidationHelper() {
            @Override
            protected IWsDOMRuntimeExtension getDomRuntime(final String domId) {
                return extMock.proxy();
            }
        };
        assertNull("The helper returned dom instance", helper.getWebServiceProject("some.dom"));
    }
}
