/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft, Beacon Information Technology Inc. and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *   BeaconIT - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.analyzer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.wsdl.WSDLException;

import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSIFileNotFoundException;
import org.eclipse.wst.wsi.internal.core.log.LogReader;
import org.eclipse.wst.wsi.internal.core.log.MessageEntryHandler;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.EnvelopeValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.UDDIValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl.WSDLValidatorImpl;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportContext;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;
import org.eclipse.wst.wsi.internal.core.report.impl.DefaultReporter;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;
import org.eclipse.wst.wsi.internal.core.util.WSIProperties;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.xml.XMLDocumentCache;
import org.uddi4j.transport.TransportFactory;


/**
 * Analyzes log files to confirm conformance to a profile.
 *
 * @version 1.0.1
 * @author Jim Clune
 * @author Peter Brittenham
 * @author Graham Turrell
 */
public class BasicProfileAnalyzer extends Analyzer
{
  /**
   * Tool information.
   */
  public static final String TOOL_NAME = "Analyzer";

  /**
   * WSDL document to analyze.
   */
  protected WSDLDocument wsdlDocument = null;

  /**
   * Basic profile analyzer.
   * @param args command line arguments.
   * @throws WSIException if unable to create a Basic profile analyzer.
   */
  public BasicProfileAnalyzer(String[] args) throws WSIException
  {
    super(args, new ToolInfo(TOOL_NAME));
    new XMLDocumentCache();
  }

  /**
   * Basic profile analyzer.
   * @param args command line arguments.
   * @param validate flag for command line argument validation.
   * @throws WSIException if unable to create a Basic profile analyzer.
   */
  public BasicProfileAnalyzer(String[] args, boolean validate) throws WSIException
  {
    super(args, new ToolInfo(TOOL_NAME), validate);
    new XMLDocumentCache();
  }

  /**
   * Basic profile analyzer.
   * @param analyzerConfigList a list of configurations for the analyzer.
   * @throws WSIException if unable to create a Basic profile analyzer.
   */
  public BasicProfileAnalyzer(List analyzerConfigList) throws WSIException
  {
    super(analyzerConfigList, new ToolInfo(TOOL_NAME));
    new XMLDocumentCache();
  }

  /**
   * Basic profile analyzer.
   * @param analyzerConfigList a list of configurations for the analyzer.
   * @param wsdlURI a wsdl document location.
   * @throws WSIException if unable to create a Basic profile analyzer.
   */
  public BasicProfileAnalyzer(List analyzerConfigList, String wsdlURI)
    throws WSIException
  {
    super(analyzerConfigList, new ToolInfo(TOOL_NAME));
    new XMLDocumentCache();
    
    try
    {
      // Get the WSDL document
      if (wsdlURI != null)
        this.wsdlDocument = new WSDLDocument(wsdlURI);
    }

    catch (WSDLException we)
    {
      throw new WSIException(we.getMessage(), we);
    }

  }

  private WSDLValidator wsdlValidator;

  /**
   * Process all conformance validation functions.
   * @return status code.
   * @throws WSIException if conformance validation process failed.
   */
  public int validateConformance() throws WSIException
  {
    int statusCode = 0;

    Report report = null;
    ReportArtifact reportArtifact = null;
    String wsdlURI = null;
    //WSDLDocument wsdlDocument = null;

    // Set up initial analyzer context based on entries in the config file
    this.analyzerContext =
      new AnalyzerContext(new ServiceReference(getAnalyzerConfig()));

    try
    {
      this.profileAssertions = WSITestToolsProperties.getProfileAssertions(
          getAnalyzerConfig().getTestAssertionsDocumentLocation());

      if (this.profileAssertions == null)
      {
 		 throw new WSIException(messageList.getMessage(
 				 "config20",
 		         "The WS-I Test Assertion Document (TAD)document was either not found or could not be processed."));  
      }

      // Create report from document factory
      report = documentFactory.newReport();
      report.setLocation(getAnalyzerConfig().getReportLocation());

      // Create report context
      ReportContext reportContext =
        new ReportContext(
          WSIConstants.DEFAULT_REPORT_TITLE,
          profileAssertions,
          this);
      report.setReportContext(reportContext);

      // Create report writer
      ReportWriter reportWriter = documentFactory.newReportWriter();
      // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT 
      //reportWriter.setWriter(new FileWriter(analyzerConfig.getReportLocation()));
      reportWriter.setWriter(getAnalyzerConfig().getReportLocation());

      // Create reporter
      this.reporter = new DefaultReporter(report, reportWriter);

      // Start writing report
      this.reporter.startReport();

      // --------------------------------
      // DISCOVERY ARTIFACT:
      // --------------------------------

      // Set current artifact
      reportArtifact = setCurrentArtifact(ArtifactType.ARTIFACT_TYPE_DISCOVERY);

      // If UDDI options specified, then process UDDI test assertions
      if ((wsdlURI = validateUDDI(reportArtifact, factory.newUDDIValidator()))
        == null)
      {
        if (getAnalyzerConfig().isWSDLReferenceSet())
        {
          // Otherwise use the WSDL options to get the WSDL location
          wsdlURI = getAnalyzerConfig().getWSDLLocation();
        }
      }

      // Indicate that we are done with this artifact
      this.reporter.endCurrentArtifact();

      // --------------------------------
      // DESCRIPTION ARTIFACT:
      // --------------------------------

      // Set current artifact
      reportArtifact =
        setCurrentArtifact(ArtifactType.ARTIFACT_TYPE_DESCRIPTION);

      // Call WSDLValidator
      if (wsdlValidator == null)
      {
        wsdlValidator = factory.newWSDLValidator();
      }
      wsdlDocument =
        validateWSDL(
          reportArtifact,
          wsdlValidator,
          wsdlURI,
          wsdlDocument);

      // If WSDL document object is null, then throw exception
      if ((wsdlDocument == null) && (getAnalyzerConfig().isWSDLReferenceSet()))
      {
        throw new WSIException(
          messageList.getMessage(
            "config05",
            "WSDL document was either not found or could not be processed."));
      }

      // Indicate that we are done with this artifact
      this.reporter.endCurrentArtifact();

      // REMOVE:
      // If processing log entries and there isn't a WSDL URI, 
      // then throw an exception since it is required to process the log entries
      //if ((wsdlURI == null) && (analyzerConfig.getLogLocation() != null)) {
      //  throw new WSIException("Must specify the WSDL document location to validate message log.");
      //}

      // --------------------------------
      // MESSAGE ARTIFACT:
      // --------------------------------

      // Set current artifact
      reportArtifact = setCurrentArtifact(ArtifactType.ARTIFACT_TYPE_MESSAGE);

      // Process test assertions for the messages
      validateMessages(
        reportArtifact,
        factory.newMessageValidator(),
        wsdlDocument);

      // Indicate that we are done with this artifact
      this.reporter.endCurrentArtifact();

      // --------------------------------
      // ENVELOPE ARTIFACT:
      // --------------------------------

      // If that is not the Basic Profile 1.0, then process
      // the envelope artifact
      if (!profileAssertions.getTADName()
        .equals(WSIConstants.BASIC_PROFILE_TAD_NAME))
      {

        // Set current artifact
        reportArtifact = setCurrentArtifact(ArtifactType.ARTIFACT_TYPE_ENVELOPE);

        // Process test assertions for the envelopes
        validateEnvelopes(
          reportArtifact,
          factory.newEnvelopeValidator(),
          wsdlDocument);

        // Indicate that we are done with this artifact
        this.reporter.endCurrentArtifact();
      }

      // Finish the conformance report
      reporter.finishReport();
    }

    catch (Exception e)
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);

      String message =
        messageList.getMessage(
          "error03",
          "The conformance validation process failed.");

      // Finish report
      if (reporter != null)
      {
        pw.println(message);
        pw.println(
          messageList.getMessage("exception01", "Exception: ")
            + e.getClass().getName());
        e.printStackTrace(pw);
        reporter.finishReportWithError(sw.toString());
      }

      if (e instanceof WSIException)
        throw (WSIException) e;
      else
        throw new WSIException(message, e);
    }

    if (report != null)
    {
      statusCode =
        (report.getSummaryResult().equals(AssertionResult.RESULT_PASSED)
          ? 0
          : 1);
    }

    return statusCode;
  }

  /**
   * Run UDDI test assertions.
   */
  private String validateUDDI(
    ReportArtifact reportArtifact,
    UDDIValidator uddiValidator)
    throws WSIException
  {
    String wsdlURI = null;

    // Init UDDIValidator
    uddiValidator.init(
      this.analyzerContext,
      this.profileAssertions.getArtifact(ArtifactType.TYPE_DISCOVERY),
      reportArtifact,
      getAnalyzerConfig().getUDDIReference(),
      this.reporter);

    // Call UDDIValidator 
    if (getAnalyzerConfig().isUDDIReferenceSet())
    {
      wsdlURI = uddiValidator.validate();

      // Cleanup
      uddiValidator.cleanup();
    }

    else
    {
      // Set all missingInput
      uddiValidator.setAllMissingInput();
    }

    return wsdlURI;
  }

  /**
   * Run WSDL test assertions.
   */
  private WSDLDocument validateWSDL(
    ReportArtifact reportArtifact,
    WSDLValidator wsdlValidator,
    String wsdlURI,
    WSDLDocument document)
    throws WSIException
  {
    WSDLDocument returnWSDLDocument = null;

    // TODO: The instanceof check is needed to avoid an API change
    // in the WTP 1.5.1 release. We should clean this up later.
    // Init WSDLValidator
    if (wsdlValidator instanceof WSDLValidatorImpl)
    {
      ((WSDLValidatorImpl)wsdlValidator).init(
        this.analyzerContext,
        this.profileAssertions.getArtifact(ArtifactType.TYPE_DESCRIPTION),
        reportArtifact,
        wsdlURI,
        document,
        this.reporter,
        getAnalyzerConfigIndex() == 0);
    }
    else
    {
      wsdlValidator.init(
        this.analyzerContext,
        this.profileAssertions.getArtifact(ArtifactType.TYPE_DESCRIPTION),
        reportArtifact,
        wsdlURI,
        document,
        this.reporter);
    }

    // If a WSDL URI was specified or located in a UDDI registry, then process the WSDL tests
    if (wsdlURI != null || document != null)
    {
      // Call WSDLValidator 
      returnWSDLDocument = wsdlValidator.validate();

      // Cleanup
      wsdlValidator.cleanup();
    }

    else
    {
      // Set all missingInput
      wsdlValidator.setAllMissingInput();
    }

    return returnWSDLDocument;
  }

  /**
   * Run message test assertions.
   */
  private void validateMessages(
    ReportArtifact reportArtifact,
    MessageValidator messageValidator,
    WSDLDocument document)
    throws WSIException
  {
    //Log log = null;
    //MessageEntry logEntry = null;

    // Init MessageValidator
    messageValidator.init(
      this.analyzerContext,
      this.profileAssertions.getArtifact(ArtifactType.TYPE_MESSAGE),
      reportArtifact,
      document,
      this.reporter);

    // If the log file location was specified, then process test assertions for the messages
    if (getAnalyzerConfig().getLogLocation() != null)
    {
      // Get the log file reader
      LogReader logReader = documentFactory.newLogReader();

      // Create log reader callback
      MessageProcessor messageProcessor =
        new MessageProcessor(messageValidator);

      // Start reading the log file
      logReader.readLog(getAnalyzerConfig().getLogLocation(), messageProcessor);

      // Cleanup
      messageValidator.cleanup();
    }

    // Otherwise all are missingInput
    else
    {
      // Set all missingInput
      messageValidator.setAllMissingInput();
    }
  }

  /**
   * Run envelope test assertions.
   */
  private void validateEnvelopes(
    ReportArtifact reportArtifact,
    EnvelopeValidator envelopeValidator,
    WSDLDocument document)
    throws WSIException
  {
    // Init envelopeValidator
    envelopeValidator.init(
      this.analyzerContext,
      this.profileAssertions.getArtifact(ArtifactType.TYPE_ENVELOPE),
      reportArtifact,
      document,
      this.reporter);

    // If the log file location was specified, then process test assertions for the messages
    if (getAnalyzerConfig().getLogLocation() != null)
    {
      // Get the log file reader
      LogReader logReader = documentFactory.newLogReader();

      // Create log reader callback
      EnvelopeProcessor envelopeProcessor =
        new EnvelopeProcessor(envelopeValidator);

      // Start reading the log file
      logReader.readLog(getAnalyzerConfig().getLogLocation(), envelopeProcessor);

      // Cleanup
      envelopeValidator.cleanup();
    }

    // Otherwise all are missingInput
    else
    {
      // Set all missingInput
      envelopeValidator.setAllMissingInput();
    }
  }

  /**
   * Command line interface for the analyzer tool.
   * @param args command line arguments.
   * @throws IOException if IO problems occur.
   */
  public static void main(String[] args) throws IOException
  {
    int statusCode = 0;
    Analyzer analyzer = null;

    try
    {
      // Set document builder factory class
      System.setProperty(
        WSIProperties.PROP_JAXP_DOCUMENT_FACTORY,
        WSIProperties.getProperty(WSIProperties.PROP_JAXP_DOCUMENT_FACTORY));

      // Set the system property for UDDI4J transport
      System.setProperty(
        TransportFactory.PROPERTY_NAME,
        WSIProperties.getProperty(TransportFactory.PROPERTY_NAME));

      // Create the analyzer object
      analyzer = new BasicProfileAnalyzer(args);

      // Have it process the conformance validation functions
      statusCode = analyzer.validateConformance();

      // Display message
      analyzer.printMessage(
        "created01",
        null,
        "Conformance report has been created.");
    }

    catch (Exception e)
    {
      statusCode = 1;

      String messageID;
      String defaultMessage;
      String messageData;

      if ((e instanceof WSIFileNotFoundException)
        || (e instanceof IllegalArgumentException))
      {
        //printStackTrace = false;
        messageID = "error01";
        defaultMessage = "Analyzer Error:";
        messageData = e.getMessage();
      }

      else
      {
        //printStackTrace = true;
        messageID = "error02";
        defaultMessage = "Analyzer Stopped By Exception:";
        messageData = e.toString();
      }

      if (analyzer != null)
        analyzer.printMessage(messageID, messageData, defaultMessage);
      else
        Analyzer.staticPrintMessage(messageID, messageData, defaultMessage);

      if (analyzer != null
        && analyzer.getAnalyzerConfig() != null
        && analyzer.getAnalyzerConfig().getVerboseOption())
        dump(e);
    }

    // Exit
    System.exit(statusCode);
  }

  /**
   * Set current artifact.
   * @param artifactType an ArtifactType object.
   * @return a ReportArtifact object.
   * @throws WSIException if problems creating report artifact.
   */
  protected ReportArtifact setCurrentArtifact(ArtifactType artifactType)
    throws WSIException
  {
    // Create artifact
    ReportArtifact reportArtifact = reporter.createArtifact();
    reportArtifact.setType(artifactType);

    // Add artifact to report
    this.reporter.setCurrentArtifact(reportArtifact);

    return reportArtifact;
  }

  /**
   * Print exception.
   * @param t a Throwable object.
   */
  public static void dump(Throwable t)
  {
    while (t instanceof WSIException)
    {
      Throwable nested = ((WSIException) t).getTargetException();
      if (nested == null)
        break;
      else
        t = nested;
    }
    t.printStackTrace();
  }

  /**
   * Message processor class.
   */
  class MessageProcessor implements MessageEntryHandler
  {
    MessageValidator messageValidator = null;

    /**
     * Create message processor as a log reader callback function.
     */
    MessageProcessor(MessageValidator messageValidator)
    {
      this.messageValidator = messageValidator;
    }

    /**
     * Process artifact reference.
     */
    public void processArtifactReference(ArtifactReference artifactReference)
      throws WSIException
    {
      reporter.addArtifactReference(artifactReference);
    }

    /**
     * Process a single log entry.
     */
    public void processLogEntry(EntryContext entryContext) throws WSIException
    {
      // Validate message
      messageValidator.validate(entryContext);
    }

  }

  /**
   * Envelope processor class.
   */
  class EnvelopeProcessor implements MessageEntryHandler
  {
    EnvelopeValidator envelopeValidator = null;

    /**
     * Create envelope processor as a log reader callback function.
     */
    EnvelopeProcessor(EnvelopeValidator envelopeValidator)
    {
      this.envelopeValidator = envelopeValidator;
    }

    /**
     * Process artifact reference.
     */
    public void processArtifactReference(ArtifactReference artifactReference)
      throws WSIException
    {
      reporter.addArtifactReference(artifactReference);
    }

    /**
     * Process a single log entry.
     */
    public void processLogEntry(EntryContext entryContext) throws WSIException
    {
      // Validate envelopes
      envelopeValidator.validate(entryContext);
    }

  }
}
