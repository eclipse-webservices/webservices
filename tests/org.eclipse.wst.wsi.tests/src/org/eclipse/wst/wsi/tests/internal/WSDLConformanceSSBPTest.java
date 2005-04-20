/* Copyright (c) 2001, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.wst.wsi.tests.internal;
/**
 * JUnit tests that test wsdl documents against 
 * the WS-I Simple Soap Binding profile.
 * 
 * @author lauzond
 */
public class WSDLConformanceSSBPTest extends CoreWSDLConformanceTest 
{
  public WSDLConformanceSSBPTest(String name)
  {
    super(name);
  }
  public void test_wsiSampleAppCatalog() { runConformanceWSDLTest("wsiSampleAppCatalog", TAD_ID_SSBP); }  
  public void test_wsiSampleAppManufacturer() { runConformanceWSDLTest("wsiSampleAppManufacturer", TAD_ID_SSBP); }  
  public void test_wsiSampleAppWarehouse() { runConformanceWSDLTest("wsiSampleAppWarehouse", TAD_ID_SSBP); }  
}