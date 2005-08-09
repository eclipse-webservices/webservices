/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

import javax.servlet.http.*;
import java.util.*;

public final class HTMLUtils
{
  public static final String UTF8_ENCODING = "UTF-8";
  public static final String LINE_BREAK = "<br>";
  public static final String LINE_SEPARATOR = System.getProperties().getProperty("line.separator");

  /**
  * Get the HTML tag for an image.
  * @return String The HTML tag for this image.
  * @param HttpServletResponse To encode the src attribute.
  * @param String src attribute defining the path to the image file. This must include the context path.
  * @param String Optional alt text to be displayed with the image. Set this to null to disable the attribute.
  * @param String Optional width of the rendered image. Set this to null to disable the attribute.
  * @param String Optional height of the rendered imag. Set this to null to disable the attribute.
  * @param Hashtable Optional key-value pairs of additional strings to be added as-is to the tag. Set this to null to disable.
  * @return String The image tag.
  */
  public static final String getHTMLImageTag(HttpServletResponse response,String src,String alt,String width,String height,Hashtable additionalAttributes)
  {
    StringBuffer tag = new StringBuffer("<img src=\"");
    tag.append(response.encodeURL(src)).append('\"');
    if (alt != null)
    {
      tag.append(" alt=\"").append(alt).append('\"');
      tag.append(" title=\"").append(alt).append('\"');
    }
    if (width != null)
      tag.append(" width=").append(width);
    if (height != null)
      tag.append(" height=").append(height);
    tag.append(" border=0");
    if (additionalAttributes != null)
    {
      Enumeration keys = additionalAttributes.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        tag.append(' ').append(key).append("=\"").append((String)additionalAttributes.get(key)).append('\"');
      }
    }
    tag.append('>');
    return tag.toString();
  }

  /**
  * Get the HTML tag for this link. The complete tag consists of the open tag <a> followed by a subelement and, finally, by an end tag </a>.
  * @return String The HTML tag of the link.
  * @param HttpServletResponse To encode the href attribute.
  * @param String The href attribute of the link. This must include the context path.
  * @param String target Optional target attribute for the link. Set this to null to disable the attribute.
  * @param String Optional name for the link. Set this to null to disable the attribute.
  * @param String The label which acts as the subelement. i.e. <a>label</a>.
  * @param Hashtable Optional key-value pairs of additional string attributes to be added as-is to the open tag. Set this to null to disable.
  */
  public static final String getHTMLLinkTag(HttpServletResponse response,String href,String target,String name,String label,Hashtable additionalAttributes)
  {
    StringBuffer tag = new StringBuffer("<a href=\"");
    tag.append(response.encodeURL(href)).append('\"');
    if (target != null)
      tag.append(" target=\"").append(target).append('\"');
    if (name != null)
      tag.append(" name=\"").append(name).append('\"');
    if (additionalAttributes != null)
    {
      Enumeration keys = additionalAttributes.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        tag.append(' ').append(key).append("=\"").append((String)additionalAttributes.get(key)).append('\"');
      }
    }
    tag.append('>');
    tag.append(label);
    tag.append("</a>");
    return tag.toString();
  }

  /**
  * Get the HTML tag for a red asterist.
  * @return String The HTML tag for the red asterisk.
  */
  public static final String redAsterisk()
  {
    return "<font color=\"#ff0000\">*</font>";
  }

  /**
  * Get the Javascript mangled version of a given input String.
  * @return String The Javascript mangled String.
  */
  public static final String JSMangle(String input)
  {
    if (input == null)
      return "";

    StringBuffer mangledOutput = new StringBuffer();
    for (int i=0;i<input.length();i++)
    {
      char c = input.charAt(i);
      switch (c)
      {
        case '\n':
          mangledOutput.append("\\n");
          break;
        case '\r':
          mangledOutput.append("\\r");
          break;
        case '\\':
        case '\"':
        case '\'':
          mangledOutput.append('\\');
        default:
          mangledOutput.append(c);
      }
    }
    return mangledOutput.toString();
  }

  private static final String LESS_THAN = "<";
  private static final String LESS_THAN_HTML_ENTITY = "&lt;";
  private static final String GREATER_THAN = ">";
  private static final String GREATER_THAN_HTML_ENTITY = "&gt;";
  private static final String SPACE = " ";
  private static final String SPACE_HTML_ENTITY = "&nbsp;";
  private static final String AMPERSAND = "&";
  private static final String AMPERSAND_HTML_ENTITY = "&amp;";
  private static final String QUOTATION = "\"";
  private static final String QUOTATION_HTML_ENTITY = "&quot;";

  /**
  * Replace special characters with HTML entities representing these characters.
  * @return String The converted String. Note: Order is important so that corrected entity references are not re-mangled.
  */
  public static final String charactersToHTMLEntities(String s) {
    s = stringReplace(s, AMPERSAND, AMPERSAND_HTML_ENTITY);
    s = stringReplace(s, LESS_THAN, LESS_THAN_HTML_ENTITY);
    s = stringReplace(s, GREATER_THAN, GREATER_THAN_HTML_ENTITY);
    s = stringReplace(s, SPACE, SPACE_HTML_ENTITY);
    s = stringReplace(s, QUOTATION, QUOTATION_HTML_ENTITY);
    return s;
  }

  /**
  * Replace HTML character entities with their associated characters.
  * @return String The converted String.
  */
  public static final String htmlEntitiesToCharacters(String s) {
    s = stringReplace(s, LESS_THAN_HTML_ENTITY, LESS_THAN);
    s = stringReplace(s, GREATER_THAN_HTML_ENTITY, GREATER_THAN);
    s = stringReplace(s, SPACE_HTML_ENTITY, SPACE);
    s = stringReplace(s, AMPERSAND_HTML_ENTITY, AMPERSAND);
    s = stringReplace(s, QUOTATION_HTML_ENTITY, QUOTATION);
    return s;
  }

  private static final String stringReplace(String s, String oldString, String newString) {
    String sCopy = s;
    int fromIndex = 0;
    int oldStringIndex = sCopy.indexOf(oldString, fromIndex);
    StringBuffer sb = new StringBuffer();
    while (oldStringIndex != -1) {
      sb.setLength(0);
      sb.append(sCopy.substring(0, oldStringIndex));
      sb.append(newString);
      sb.append(sCopy.substring(oldStringIndex + oldString.length(), sCopy.length()));
      sCopy = sb.toString();
      fromIndex = oldStringIndex + newString.length();
      oldStringIndex = sCopy.indexOf(oldString, fromIndex);
    }
    return sCopy;
  }
}
