/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

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
  
  private void harvestTags(StringBuffer target,String document,String tag)
  {
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
      }
      else
        index++;
      index = document.indexOf(START_TAG,index);
    }
  }
  
  public byte[] harvestHeadTags(byte[] b)
  {
    String s;
    try
    {
      s = new String(b, UTF8);
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
      harvestTags(sb,head,META);
      harvestTags(sb,head,LINK);
    }
    sb.append(ROOT_END_TAG);
    return sb.toString().getBytes();
  }
}