/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.analyzer;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.analyzer.WSIAnalyzerException;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.eclipse.wst.wsi.internal.report.impl.ReportImpl;
import org.eclipse.wst.wsi.internal.URIEncoder;
import org.eclipse.wst.wsi.internal.WSIException;
import org.eclipse.wst.wsi.internal.WSIPreferences;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.analyzer.config.WSDLReference;
import org.eclipse.wst.wsi.internal.analyzer.config.impl.AssertionResultsOptionImpl;
import org.eclipse.wst.wsi.internal.analyzer.config.impl.WSDLElementImpl;
import org.eclipse.wst.wsi.internal.analyzer.config.impl.WSDLReferenceImpl;
import org.eclipse.wst.wsi.internal.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.report.AssertionResult;
import org.eclipse.wst.wsi.internal.report.Entry;
import org.eclipse.wst.wsi.internal.report.impl.FailureDetailImpl;
import org.eclipse.wst.wsi.internal.xml.dom.ElementLocation;

/**
 * MessageAnalyzer
 * 
 * A class that allows simple access to the WS-I Test Tools in order to analyze
 * messages in a log file.
 */

public class MessageAnalyzer
{
  public final static String PLUGIN_ID = "org.wsi.test.tools";
  
  public final static String TYPE_PORT = "port";
  public final static String TYPE_BINDING = "binding";
  public final static String TYPE_PORTTYPE = "porttype";
  public final static String TYPE_OPERATION = "operation";
  public final static String TYPE_MESSAGE = "message";

  // Test result Strings
  protected final String NOTPASSED = "notPassed";
  protected final String WARNING = "warning";
  protected final String FAILED = "failed";
  
  protected final String FILE = "file:";

  protected String filename;
  protected String wsdlfile = null;
  protected String elementname = null;
  protected String namespace = null;
  protected String parentname = null;
  protected boolean wsdlspecified = false;
  protected String type  = null;
  protected List assertionwarnings;
  protected List assertionerrors;
  protected WSIPreferences wsiPreferences = null;
  
  /**
   * Constructor for MessageAnalyzer.
   * @param filename log file name.
   */
  public MessageAnalyzer(String filename)
  {
    this.filename = filename;

    assertionwarnings = new Vector();
    assertionerrors = new Vector();
  	this.wsiPreferences = WSITestToolsProperties.checkWSIPreferences(filename);
  }
  
  /**
   * Constructor for MessageAnalyzer.
   * @param filename log file name.
   */
  public MessageAnalyzer(String filename, WSIPreferences wsiPreferences)
  {
    this.filename = filename;

    assertionwarnings = new Vector();
    assertionerrors = new Vector();
  	this.wsiPreferences = wsiPreferences;
  }

  public MessageAnalyzer(String filename, String wsdlfile, String elementname, String namespace, String parentname, String type)
  {
  	this(filename);
  	wsdlspecified = true;
  	
  	this.wsdlfile = wsdlfile;
  	this.elementname = elementname;
  	this.namespace = namespace;
  	this.parentname = parentname;
  	this.type = type;
  }

  public MessageAnalyzer(String filename, String wsdlfile, String elementname, String namespace, String parentname, String type, WSIPreferences wsiPreferences)
  {
  	this(filename, wsiPreferences);
  	wsdlspecified = true;
  	
  	this.wsdlfile = wsdlfile;
  	this.elementname = elementname;
  	this.namespace = namespace;
  	this.parentname = parentname;
  	this.type = type;
  }

  /**
   * Method validateConformance.
   * 
   * Checks the conformance of the given logfile against the WS-I Basic Profile.
   * If there are conformance problems they can be obtained from getAssertionErrors.
   * 
   * @throws WSIAnalyzerException
   */
  public void validateConformance() throws WSIAnalyzerException
  {
    try
    {
	  WSITestToolsProperties.setLocal();
	  
	  if (wsiPreferences.getComplianceLevel() != WSITestToolsProperties.IGNORE_NON_WSI)
	  {
        DocumentFactory documentFactory = DocumentFactory.newInstance();
        // Initialize the BasicProfileAnalyzer using an analyzerconfig object
        AnalyzerConfig analyzerconfig = documentFactory.newAnalyzerConfig();
        if(wsdlspecified)
        {
      	  WSDLReference wsdlref = new WSDLReferenceImpl();
      	  wsdlref.setWSDLLocation(wsdlfile);
      	
      	  WSDLElement wsdlelem = new WSDLElementImpl();
      	  wsdlelem.setName(elementname);
      	  wsdlelem.setNamespace(namespace);
      	  wsdlelem.setParentElementName(parentname);
      	  wsdlelem.setType(type);
      	
      	  wsdlref.setWSDLElement(wsdlelem);
      	
      	  analyzerconfig.setWSDLReference(wsdlref);
        }

        AssertionResultsOption aro = new AssertionResultsOptionImpl();
        aro.setAssertionResultType(AssertionResultType.newInstance(NOTPASSED));
        aro.setShowAssertionDescription(false);
        aro.setShowFailureDetail(false);
        aro.setShowFailureMessage(false);
        aro.setShowMessageEntry(false);

        // although we don't use a stylesheet for our application it is required or the 
        // WS-I tools will fail
        AddStyleSheet addstylesheet = new AddStyleSheetImpl();

        analyzerconfig.setAssertionResultsOption(aro);
        analyzerconfig.setAddStyleSheet(addstylesheet);
      
        analyzerconfig.setCorrelationType("endpoint");
  	    try
		{
		  analyzerconfig.setLogLocation(URIEncoder.encode(filename, "UTF8"));
		  analyzerconfig.setTestAssertionsDocumentLocation(
			   URIEncoder.encode(wsiPreferences.getTADFile(), "UTF8"));
		}
		catch (UnsupportedEncodingException uee)
		{
		  analyzerconfig.setLogLocation(URIEncoder.encode(filename));
	      analyzerconfig.setTestAssertionsDocumentLocation(URIEncoder.encode(wsiPreferences.getTADFile()));
		}
        //analyzerconfig.setLogLocation(filename);
        //analyzerconfig.setTestAssertionsDocumentLocation(getBasicProfileTestAssertionsFile());
        analyzerconfig.setVerboseOption(false);
      

        List analyzerConfigs = new Vector();
        analyzerConfigs.add(analyzerconfig);
        // here's where the validation actually starts happening
        WSIBasicProfileAnalyzer bpanalyzer = new WSIBasicProfileAnalyzer(analyzerConfigs);

        bpanalyzer.validateAll();
        ReportImpl report = (ReportImpl) bpanalyzer.getReport();

        List entries = report.getEntries();

        // if there are report entries we iterate through them and add all
        // error and warning messages to the error list.
        if (entries != null)
        {
          Iterator ientry = entries.iterator();
          while (ientry.hasNext())
          {
            Entry entry = (Entry) ientry.next();
            Map assertionResults = entry.getAssertionResultList();

            if (assertionResults != null)
            {
              Iterator iassertionResults = assertionResults.values().iterator();

              while (iassertionResults.hasNext())
              {
                AssertionResult ar = (AssertionResult) iassertionResults.next();

                if (ar.getResult().equalsIgnoreCase(FAILED) && 
                	wsiPreferences.getComplianceLevel().equals(WSITestToolsProperties.STOP_NON_WSI))
                {
                  TestAssertion ta = ar.getAssertion();
                  if (ta.isEnabled())
                  {
                    Iterator errorMessages = ar.getFailureDetailList().iterator();
                    String error = "";
                    while (errorMessages.hasNext())
                    {
                      FailureDetailImpl fdi = (FailureDetailImpl)errorMessages.next();
                      error = (String) fdi.getFailureMessage();

                      int lineNumber = 0;
                      int columnNumber = 0;
                    
                      ElementLocation el = fdi.getElementLocation();

                      if (el != null)
                      {
                        lineNumber = el.getLineNumber();
                        columnNumber = el.getColumnNumber();
                      }

                      //TODO: get assertion locations in the log file
                      assertionerrors.add(
                        new AssertionError(
                        ta.getId(),
                        "WS-I Message ID " + entry.getReferenceID() + ":" + ta.getFailureMessage(),
                        lineNumber,
                        columnNumber));
                    }
                  }
                }
                else if (ar.getResult().equalsIgnoreCase(FAILED) && 
                         wsiPreferences.getComplianceLevel().equals(WSITestToolsProperties.WARN_NON_WSI))
                {
                  TestAssertion ta = ar.getAssertion();
                  if (ta.isEnabled())
                  {
                    Iterator errorMessages = ar.getFailureDetailList().iterator();
                    String error = "";
                    while (errorMessages.hasNext())
                    {
                      FailureDetailImpl fdi = (FailureDetailImpl)errorMessages.next();
                      error = (String) fdi.getFailureMessage();

                      int lineNumber = 0;
                      int columnNumber = 0;
                    
                      ElementLocation el = fdi.getElementLocation();

                      if (el != null)
                      {
                        lineNumber = el.getLineNumber();
                        columnNumber = el.getColumnNumber();
                      }
                      //TODO: get assertion locations in the log file
                      assertionwarnings.add(
                        new AssertionError(
                        ta.getId(),
                        "Message ID " + entry.getReferenceID() + ":" + ta.getFailureMessage(),
                        lineNumber,
                        columnNumber));
                    }
                  }
                }
              }
            }
          }
        } 
	  }
    }
    catch (WSIException e)
    {
      throw new WSIAnalyzerException("The MessageAnalyzer was unable to validate the given logfile.", e.getTargetException());
    }
    catch (Exception e)
    {
      throw new WSIAnalyzerException("The MessageAnalyzer was unable to validate the given logfile.");
    }

  }

  /**
   * Method getAssertionWarnings.
   * Returns a list of any assertion warnings generated from validateConformance.
   * @return List
   */
  public List getAssertionWarnings()
  {
    return assertionwarnings;
  }

  /**
   * Method getAssertionFailures.
   * Returns a list of any assertion errors generated from validateConformance.
   * @return List
   */
  public List getAssertionErrors()
  {
    return assertionerrors;
  }
}
