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
 * the WS-I Attachments profile.
 * 
 * @author lauzond
 */
public class WSDLConformanceAPTest extends CoreWSDLConformanceTest 
{
  public WSDLConformanceAPTest(String name)
  {
    super(name);
  }
//ws-i wsdls
  public void test_addressBook() { runTest("wsi", "addressBook", TAD_ID_AP); }
  public void test_addressBook_rpc() { runTest("wsi", "addressBook-rpc", TAD_ID_AP); }
  public void test_sampleAppCatalog() { runTest("wsi", "sampleAppCatalog", TAD_ID_AP); }  
  public void test_sampleAppManufacturer() { runTest("wsi", "sampleAppManufacturer", TAD_ID_AP); }  
  public void test_sampleAppWarehouse() { runTest("wsi", "sampleAppWarehouse", TAD_ID_AP); } 

  // bp tests
  public void test_2012_1() { runTest("bp", "2012-1", TAD_ID_AP); } 
  public void test_2013_1() { runTest("bp", "2013-1", TAD_ID_AP); }
  public void test_2014_1() { runTest("bp", "2014-1", TAD_ID_AP); }
  public void test_2014_2() { runTest("bp", "2014-2", TAD_ID_AP); }
  public void test_2014_3() { runTest("bp", "2014-3", TAD_ID_AP); }
  public void test_2017_1() { runTest("bp", "2017-1", TAD_ID_AP); }
  public void test_2017_2() { runTest("bp", "2017-2", TAD_ID_AP); }
  public void test_2018_1() { runTest("bp", "2018-1", TAD_ID_AP); }
  public void test_2018_2() { runTest("bp", "2018-2", TAD_ID_AP); }
  public void test_2018_3() { runTest("bp", "2018-3", TAD_ID_AP); }
  public void test_2018_4() { runTest("bp", "2018-4", TAD_ID_AP); }
  public void test_2019_1() { runTest("bp", "2019-1", TAD_ID_AP); }
  public void test_2019_2() { runTest("bp", "2019-2", TAD_ID_AP); }
  public void test_2019_3() { runTest("bp", "2019-3", TAD_ID_AP); }
  public void test_2019_4() { runTest("bp", "2019-4", TAD_ID_AP); }
  public void test_2020_1() { runTest("bp", "2020-1", TAD_ID_AP); }
  public void test_2020_2() { runTest("bp", "2020-2", TAD_ID_AP); }
  public void test_2105_1() { runTest("bp", "2105-1", TAD_ID_AP); }
  public void test_2105_2() { runTest("bp", "2105-2", TAD_ID_AP); }
  public void test_2117_1() { runTest("bp", "2117-1", TAD_ID_AP); }
  public void test_2117_2() { runTest("bp", "2117-2", TAD_ID_AP); }
  public void test_2117_3() { runTest("bp", "2117-3", TAD_ID_AP); }
  public void test_2406_1() { runTest("bp", "2406-1", TAD_ID_AP); }

  // redundant tests -- assertions failures caught by base wsdl validator.
  public void test_2010_1() { runTest("redundant", "2010-1", TAD_ID_AP); }
  public void test_2011_1() { runTest("redundant", "2011-1", TAD_ID_AP); } 
  public void test_2011_2() { runTest("redundant", "2011-2", TAD_ID_AP); } 
  public void test_2011_3() { runTest("redundant", "2011-3", TAD_ID_AP); } 
  public void test_2011_4() { runTest("redundant", "2011-4", TAD_ID_AP); } 
  public void test_2011_5() { runTest("redundant", "2011-5", TAD_ID_AP); } 
  public void test_2021_1() { runTest("redundant", "2021-1", TAD_ID_AP); } 
  public void test_2021_2() { runTest("redundant", "2021-2", TAD_ID_AP); } 
  public void test_2021_3() { runTest("redundant", "2021-3", TAD_ID_AP); } 
  public void test_2021_4() { runTest("redundant", "2021-4", TAD_ID_AP); } 
  public void test_2021_5() { runTest("redundant", "2021-5", TAD_ID_AP); } 
  public void test_2021_6() { runTest("redundant", "2021-6", TAD_ID_AP); }
  public void test_2022_1() { runTest("redundant", "2022-1", TAD_ID_AP); } 
  public void test_2022_2() { runTest("redundant", "2022-2", TAD_ID_AP); }
  public void test_2032_1() { runTest("redundant", "2032-1", TAD_ID_AP); } 
  public void test_2032_2() { runTest("redundant", "2032-2", TAD_ID_AP); } 
}