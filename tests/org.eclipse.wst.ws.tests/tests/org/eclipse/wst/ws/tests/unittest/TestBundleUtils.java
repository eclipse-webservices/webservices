package org.eclipse.wst.ws.tests.unittest;

import junit.framework.TestCase;

import org.eclipse.wst.ws.internal.common.BundleUtils;
import org.osgi.framework.Version;

public class TestBundleUtils extends TestCase {

	public void testGetVersion() {
		Version version = BundleUtils.getVersion("org.eclipse.wst.ws");
		assertNotNull("version should never be null", version);
	}

}
