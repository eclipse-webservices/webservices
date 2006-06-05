/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060517   142324 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.wsil;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class HTMLHeadHandler extends DefaultHandler
{
  private final char START_TAG = '<';
  private final char END_TAG = '>';
  private final String HEAD_START_TAG = "<head>";
  private final String HEAD_END_TAG = "</head>";
  private final String ROOT_START_TAG = "<root>";
  private final String ROOT_END_TAG = "</root>";
  private final String UTF8 = "UTF-8";
  
  //HTML META tag information used to detect the charset.
  private final String HTML_CONTENT = "content";
  private final String HTTP_EQUIV = "http-equiv";
  private final String HTTP_EQUIV_CONTENT_TYPE = "Content-Type";
  private final String CHARSET = "charset";
  
  // WSIL tag information.
  private final String META = "meta";
  private final String NAME = "name";
  private final String SERVICE_INSPECTION = "serviceInspection";
  private final String CONTENT = "content";
  
  // DISCO tag information.
  private final String LINK = "link";
  private final String TYPE = "type";
  private final String TEXT_XML = "text/xml";
  private final String REL = "rel";
  private final String ALTERNATE = "alternate";
  private final String HREF = "href";
    
  private String baseURI_;
  private Vector wsils_;
  private Vector discos_;
  private String byteEncoding = UTF8; //Default to UTF-8.
  
  public HTMLHeadHandler(String baseURI)
  {
    super();
    baseURI_ = baseURI;
    wsils_ = new Vector();
    discos_ = new Vector();
  }
 
  public String[] getWsils()
  {
    String[] wsils = new String[wsils_.size()];
    wsils_.copyInto(wsils);
    return wsils;
  }
  
  public String[] getDiscos()
  {
    String[] discos = new String[discos_.size()];
    discos_.copyInto(discos);
    return discos;
  }
  
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    String qNameLC = qName.toLowerCase();
    if (qNameLC.equals(META))
    {
      String nameValue = attributes.getValue(NAME);
      if (SERVICE_INSPECTION.equals(nameValue))
      {
        String wsilURI = attributes.getValue(CONTENT);
        if (baseURI_ != null && wsilURI.indexOf(":/") == -1)
        {
          StringBuffer sb = new StringBuffer();
          sb.append(baseURI_.substring(0, baseURI_.lastIndexOf("/")+1));
          sb.append(wsilURI);
          wsilURI = sb.toString();
        }
        if (!wsils_.contains(wsilURI))
          wsils_.add(wsilURI);
      }
    }
    else if (qNameLC.equals(LINK))
    {
      // See http://msdn.microsoft.com/msdnmag/issues/02/02/xml/default.aspx for more details on DISCO.
      String type = attributes.getValue(TYPE);
      String rel = attributes.getValue(REL);
      String href = attributes.getValue(HREF);
      if (TEXT_XML.equals(type) && ALTERNATE.equals(rel) && href != null)
      {
        String discoURI = href;
        if (discoURI.indexOf(":/") == -1)
        {
          StringBuffer sb = new StringBuffer();
          sb.append(baseURI_.substring(0,baseURI_.lastIndexOf("/")+1));
          sb.append(discoURI);
          discoURI = sb.toString();
        }
        if (!discos_.contains(discoURI))
          discos_.add(discoURI);
      }
    }
  }
  
  public void error(SAXParseException e) throws SAXException
  {
  }
  
  public void fatalError(SAXParseException e) throws SAXException
  {
  }
  
  public void warning(SAXParseException e) throws SAXException
  {
  }
  
  /**
   * Appends the elements of the provided tag in the provided document to the provided StringBuffer.
   * @param target
   * @param document
   * @param tag
   * @param encoding
   * @return boolean false if the value of the encoding parameter matched the detected charset or if no charset was detected.
   * Returns true if a charset was detected and it did not equal the encoding parameter. If true is returned
   * the harvesting of the tags would have stopped at the point the charset was detected. The caller
   * should call this method again with the correct encoding.
   */
  private boolean harvestTags(StringBuffer target,String document,String tag, String encoding)
  {
	boolean changeEncoding = false;
    int index = document.indexOf(START_TAG);
    int documentLength = document.length();
    int tagLength = tag.length();
    while (index != -1 && (index+1+tagLength)<documentLength)
    {
      String str = document.substring(index+1,index+1+tagLength);
      if (str.toLowerCase().equals(tag))
      {
        str = document.substring(index,document.indexOf(END_TAG,index+1)+1);
        target.append(str);
        index += str.length();
        
        //If tag is META and declares the charset, find out what it is
        //and if it matches what was passed in. If it matches, continue 
        //with the parsing and return false when complete. 
        //If the detected charset is different from what was passed in, 
        //- change byteEncoding to equal the detected charset.
        //- stop parsing.
        //- return true.
        if (tag.equals(META))
        {
          int idxOfContent = str.indexOf(HTML_CONTENT);
          int idxOfHTTPEQUIV = str.indexOf(HTTP_EQUIV);
          if (idxOfHTTPEQUIV!= -1 && idxOfContent != -1)
          {
        	//Check if the http-equiv attribute is set to Content-Type.
          	int idxOfHTTPEQUIVOpenQuote = str.indexOf("\"", idxOfHTTPEQUIV+1);
        	int idxOfHTTPEQUIVClosingQuote = str.indexOf("\"", idxOfHTTPEQUIVOpenQuote+1);
        	String hTTPEQUIVValueUntrimmed = str.substring(idxOfHTTPEQUIVOpenQuote+1, idxOfHTTPEQUIVClosingQuote);
        	if (hTTPEQUIVValueUntrimmed.trim().equals(HTTP_EQUIV_CONTENT_TYPE))
        	{
        	  //This META tag contains the charset. Get the value of the content attribute
        	  int idxOfOpenQuote = str.indexOf("\"", idxOfContent+1);
        	  int idxOfClosingQuote = str.indexOf("\"", idxOfOpenQuote+1);
        	  String contentValue = str.substring(idxOfOpenQuote+1, idxOfClosingQuote);
        	  
        	  //Get the charset
        	  int idxOfCharSet = contentValue.indexOf(CHARSET);
        	  int idxOfEquals = contentValue.indexOf("=", idxOfCharSet+CHARSET.length());
        	  String detectedEncodingValueUntrimmed = contentValue.substring(idxOfEquals+1);
        	  String detectedEncodingValue = detectedEncodingValueUntrimmed.trim();
        	  if (!detectedEncodingValue.equals(encoding))
        	  {
        	    byteEncoding = detectedEncodingValue;
        	    changeEncoding = true;
        	    break;
        	  }
            }
          }
        }
      }
      else
        index++;
      index = document.indexOf(START_TAG,index);
    }
    
    return changeEncoding;
  }
  

  /**
   * If the provided byte array reperesents the contents of an HTML
   * document, this method will return a byte array in which
   * <ul>
   * <li>the opening and closing HEAD tags are removed and replaced with 
   * opening and closing <root> tags</li>
   * <li>only the META and LINK elements are in the HTML document
   * are included in the contents between the opening and closing
   * <root> tags.
   * </ul>
   * This method will modify the value of the byteEncoding String
   * attribute on this class if it is something other than
   * UTF-8. Callers of this method should call getByteEncoding()
   * after calling this method if they need to know the charset
   * value used by this method to decode/endcode the byte array.
   * @param b
   * @return byte[]
   */
  public byte[] harvestHeadTags(byte[] b)
  {
    String s;
    
    try
    {
    	//Assume the default byte encoding of UTF-8 for now.
    	s = new String(b, byteEncoding);
    }
    catch (UnsupportedEncodingException uee)
    {
      s = new String(b);
    }
    String head = s.toLowerCase();
    int headStartIndex = head.indexOf(HEAD_START_TAG);
    int headEndIndex = head.indexOf(HEAD_END_TAG);
    StringBuffer sb = new StringBuffer();
    sb.append(ROOT_START_TAG);
    if (headStartIndex != -1 && headEndIndex != -1)
    {
      head = s.substring(headStartIndex, headEndIndex+HEAD_END_TAG.length());
      boolean encodingChanged = harvestTags(sb,head,META, byteEncoding);
      if (encodingChanged)
      {
    	  //The harvestTags method detected a different charset
    	  //than the one that was passed in. Start from the beginning
    	  //with the correct charset.
    	    String s2;
    	    try
    	    {
    	    	s2 = new String(b, byteEncoding);
    	    }
    	    catch (UnsupportedEncodingException uee)
    	    {
    	      s2 = new String(b);
    	    }
    	    String head2 = s2.toLowerCase();
    	    int head2StartIndex = head2.indexOf(HEAD_START_TAG);
    	    int head2EndIndex = head2.indexOf(HEAD_END_TAG);
    	    sb = new StringBuffer();
    	    sb.append(ROOT_START_TAG);
    	    if (head2StartIndex != -1 && head2EndIndex != -1)
    	    {
    	      head2 = s2.substring(head2StartIndex, head2EndIndex+HEAD_END_TAG.length());
    	      harvestTags(sb,head2,META, byteEncoding);
    	      harvestTags(sb,head2,LINK,byteEncoding);
    	    }    	  
      }
      else
      {
        harvestTags(sb,head,LINK,byteEncoding);
      }
    }
    sb.append(ROOT_END_TAG);
    try
    {
    	return sb.toString().getBytes(byteEncoding);      
    } catch (UnsupportedEncodingException uee)
    {
      return sb.toString().getBytes();
    }    
    
  }
  
  public String getByteEncoding()
  {
	  return byteEncoding;
  }
}
