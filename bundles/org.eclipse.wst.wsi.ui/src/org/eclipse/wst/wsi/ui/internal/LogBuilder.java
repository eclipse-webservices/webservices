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
package org.eclipse.wst.wsi.ui.internal;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.internet.monitor.core.internal.provisional.Request;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogWriter;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.impl.LogImpl;
import org.eclipse.wst.wsi.internal.core.log.impl.LogWriterImpl;
import org.eclipse.wst.wsi.internal.core.log.impl.MessageEntryImpl;
import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.ManInTheMiddle;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfig;
import org.eclipse.wst.wsi.internal.core.monitor.config.impl.CommentImpl;
import org.eclipse.wst.wsi.internal.core.monitor.config.impl.ManInTheMiddleImpl;

/**
 * Given a list of RequestResponses from a TCPIP Monitor, 
 * this class builds a WS-I compliant Message Log file.
 * 
 * @author David Lauzon, IBM
 */
public class LogBuilder
{
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
   * IDs to identify requests and corresponding responses.
   */
  protected int conversationId = 1;

  /**
   * IDs to uniquely identify each and every message within the log file.
   */
  protected int id = 1;

  /**
   * The list of RequestResponces from a TCPIP Monitor used to generate the log file.
   */
  protected Request[] requestResponses;

  /**
   * The actual log object.
   */
  protected Log log = null;

  /**
   * The log file.
   */
  protected IFile ifile;

  /**
   * Tool information property values.
   */
  private static final String TOOL_NAME = "Monitor";
  private static final String TOOL_VERSION = "1.0";
  private static final String TOOL_RELEASE_DATE = "2003-03-20";
  private static final String TOOL_IMPLEMENTER = "IBM";
  private static final String TOOL_LOCATION = "";

  /**
   * Properties of a RequestResponse.
   */
  private static final String PROPERTY_REQUEST_HEADER = "request-header";
  private static final String PROPERTY_RESPONSE_HEADER = "response-header";
  private static final String HTTP_REQUEST_BODY = "http-request-body";
  private static final String HTTP_RESPONSE_BODY = "http-response-body";

  /**
   * Common timestamp format.
   */
  public static final SimpleDateFormat timestampFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  /**
   * Constructor.
   * @param ifile: the file handler for the log.
   */
  public LogBuilder(IFile ifile)
  {
    this.ifile = ifile;
  }

  /**
   * This builds and then returns a log based on the list of request-response pairs.
   * @param requestResponses: a list of messages in the form of request-response pairs.
   * @return a log based on a list of request-response pairs.
   */
  public Log buildLog(Request[] requestResponses)
  {
    this.requestResponses = requestResponses;

    log = new LogImpl();
    logMonitorInformation();

    // log the messages
    for (int i=0; i<requestResponses.length; i++)
    {
      Request rr = requestResponses[i];
      if ((rr != null) && (!omitRequestResponse(rr)))
      {
        logRequestResponse(rr);
      }
    }
    return log;
  }

  /**
   * Currently a no-op.
   */
  public void logMonitorInformation()
  {
  }

  /**
   * Log the request-response pair.
   *@param rr: a request-response pair.
   */
  protected void logRequestResponse(Request rr)
  {
    if (rr != null)
    {
      byte[] request = rr.getRequest(Request.ALL);
      byte[] response = rr.getResponse(Request.ALL);
        
      String requestHeader = new String(rr.getRequest(Request.TRANSPORT));
      String responseHeader = new String(rr.getResponse(Request.TRANSPORT));
      byte[] unchunkedRequestBody = rr.getRequest(Request.CONTENT);
      byte[] unchunkedResponseBody = rr.getResponse(Request.CONTENT);
      String requestBody = null;
      String responseBody = null;

      if (unchunkedRequestBody != null)
      {
        requestBody = new String(Base64.encode(unchunkedRequestBody)); 
      }

      if (unchunkedResponseBody != null)
      {
        responseBody = new String(Base64.encode(unchunkedResponseBody)); 
      }

      long timestamp = rr.getDate().getTime();
      String localHostAndPort = "localhost:" + rr.getLocalPort();
      String remoteHostAndPort = rr.getRemoteHost() + ":" + rr.getRemotePort();

      int requestId  = getNextAvailableId();
      int responseId = getNextAvailableId();
      int conversationId = getNextAvailableConversationId();

      MessageEntry messageEntryRequest = createMessageEntry(requestId, conversationId, 
                MessageEntry.TYPE_REQUEST, timestamp, localHostAndPort,
                remoteHostAndPort, requestBody, requestHeader);

      MessageEntry messageEntryResponse = createMessageEntry(responseId, conversationId, 
                MessageEntry.TYPE_RESPONSE, timestamp + rr.getResponseTime(), remoteHostAndPort,
                localHostAndPort, responseBody, responseHeader);
      try
      {
        if ((messageEntryRequest != null) && isMessageWithBrackets(requestBody))
        {
           if (messageEntryResponse != null)
           {
             log.addLogEntry(messageEntryRequest);
             log.addLogEntry(messageEntryResponse);
           }
        }
      }
      catch (Exception e)
      {
        // ignore the request response pair
      }
    }
  }

  /**
   * Returns true if the content of the message has at least
   * one left and one right bracket.
   * @param message: a message content.
   * @return true if the content of the message has at least
   *         one left and one right bracket.
   */
  public boolean isMessageWithBrackets(String message)
  {
    return ((message != null) && 
            (message.indexOf("<")!= -1) && 
            (message.indexOf(">") != -1));
  }
      
  /**
   * Create a log entry.
   * @param id: unique message id within the log.
   * @param conversationId: conversation id to identify request-response pairs.
   * @param type: type indicating a request or response.
   * @param timestamp: the date and time of the message.
   * @param senderHostAndPort: the host and port of the sender.
   * @param receiverHostAndPort: the host and port of the receiver.
   * @param messageContent: the content or body of the message.
   * @param header: the header of the message.
   * @return a log entry.
   */
  protected MessageEntry createMessageEntry(int id, int conversationId, String type, long timestamp, 
        String senderHostAndPort, String receiverHostAndPort, String messageContent, String header) 
  {
 	// Create log entry
    MessageEntry messageEntry = new MessageEntryImpl();
	messageEntry.setHTTPHeaders(header);
	messageEntry.setMessage(messageContent);
    messageEntry.setId(String.valueOf(id));
    messageEntry.setConversationId(String.valueOf(conversationId));
    messageEntry.setType(type);
    messageEntry.setTimestamp(getTimestamp(new Date(timestamp)));
    messageEntry.setSenderHostAndPort(senderHostAndPort);
    messageEntry.setReceiverHostAndPort(receiverHostAndPort);
            
    return messageEntry;
  }

  /**
   * Returns the next available id, then increments the id value.
   * @return the next available id.
   */
  protected int getNextAvailableId()
  {
    return id++;
  }

  /**
   * Returns the next available conversation id, then increments the conversation id value.
   * A conversation id identfies a request and its corresponding response.
   * @return the next available converasationid.
   */
  protected int getNextAvailableConversationId()
  {
    return conversationId++;
  }

  /**
   * Check for HTTP messages that should not be logged.
   * @param rr: a request-response pair.
   * @return true if the request-response pair should be omitted from the log.
   */
  private boolean omitRequestResponse(Request rr) 
  {
    boolean omit = false;
    if (rr != null)
    {
      String request = rr.getRequest(Request.TRANSPORT).toString();
      if ((request != null) &&
          ((request.startsWith("CONNECT")) ||
           (request.startsWith("TRACE")) || 
           (request.startsWith("DELETE")) || 
           (request.startsWith("OPTIONS")) || 
           (request.startsWith("HEAD"))))
      { 
        omit = true;
      }
    }
    return omit;
  }

  /**
   * Write log out to file.
   * @param log: log to be written to file.
   */

  public void writeLog(Log log)
  {
    try
    {
      // Get log writer
      LogWriter logWriter = new LogWriterImpl();
    
      logWriter.setWriter(ifile.getLocation().toOSString());
      
      // Write start of log file
      logWriter.write(new StringReader(log.getStartXMLString("")));
    
      // Write monitor tool information
      String monitorInfo = generateMonitorToolInfo();
      logWriter.write(new StringReader(monitorInfo));

      for (int i=0; i<log.getEntryCount(); i++)
      {
        MessageEntry me = log.getLogEntry(i);
        logWriter.write(new StringReader(me.toXMLString("")));
      }
      logWriter.write(new StringReader(log.getEndXMLString("")));
      logWriter.close();
    }
    catch (Exception e)
    {
      System.out.println("Exception thrown when printing log file.");
    }
  }


  /**
   * Returns XML string representation of the Monitor tool.
   * @return XML string representation of the Monitor tool.
   */
  protected String generateMonitorToolInfo()
  {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw); 

    try
    {
      // Tool information
      ToolInfo toolInfo = new ToolInfo(TOOL_NAME, TOOL_VERSION, TOOL_RELEASE_DATE, TOOL_IMPLEMENTER, TOOL_LOCATION);

      DocumentFactory documentFactory = DocumentFactory.newInstance();
      MonitorConfig monitorConfig = documentFactory.newMonitorConfig();
    
      Comment comment = new CommentImpl();
      comment.setText("Comment");
      monitorConfig.setComment(comment);
      monitorConfig.setLogLocation("URL");
      monitorConfig.setReplaceLog(true);

      AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
      monitorConfig.setAddStyleSheet(addStyleSheet);

      monitorConfig.setLogDuration(600);
      monitorConfig.setTimeout(3);
     
      ManInTheMiddle manInTheMiddle = new ManInTheMiddleImpl();
      monitorConfig.setManInTheMiddle(manInTheMiddle);

      monitorConfig.setLocation("documentURI");
      monitorConfig.setVerboseOption(false);

      // Start     
      pw.print(toolInfo.getStartXMLString(""));
    
      // Config
      pw.print(monitorConfig.toXMLString(WSIConstants.NS_NAME_WSI_MONITOR_CONFIG));

      // End
      pw.println(toolInfo.getEndXMLString(""));
    }
    catch (Exception e)
    {
    }
    return sw.toString();
  }

  /**
   * Get the given date and time as a timestamp.
   * @param date: a date object.
   * @return the given date and time as a timestamp.
   */
  public static String getTimestamp(Date date)
  {
    timestampFormat.setTimeZone(TimeZone.getDefault());
    return timestampFormat.format(date);
  }
}
