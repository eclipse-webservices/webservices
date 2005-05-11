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
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.wst.common.ui.internal.UIPlugin;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class ValidateHelper
{
  // XML Lang can have many different valid formats
  // 1) xx      ie. en, fr, de
  // 2) xx-xx   ie. en-US, en-FR
  // 3) I-xx    ie. I-en
  // 4) X-xx    ie. X-en
  public static String checkXMLLang(String lang)
  {
    if (lang.length() == 0) 
      return null;
    
    if (lang.length() == 1) 
    {
      char ch0 = lang.charAt(0);
      if ((ch0 >= 'a' && ch0 <= 'z') || (ch0 >= 'A' && ch0 <= 'Z'))
        return UIPlugin.getResourceString("_WARN_LANG_TOO_SHORT");
      else
        return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch0 + 
        UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");          
    }
    
    int offset;

    char ch0 = lang.charAt(0);
    if (lang.charAt(1) == '-') 
    {
      if (ch0 == 'i' || ch0 == 'I' || ch0 == 'x' || ch0 == 'X') 
        offset = 1;
      else
        return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch0 + 
        UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");
    }
    else
    {
      char ch1 = lang.charAt(1);
      if ((ch0 >= 'a' && ch0 <= 'z') || (ch0 >= 'A' && ch0 <= 'Z'))
        if ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))
          offset = 2;
        else
          return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch1 + 
          UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");
      else
        return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch0 + 
        UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");
    }

    if (lang.length() > offset) 
    {
      char ch = lang.charAt(offset++);
      if (ch != '-') 
        return UIPlugin.getResourceString("_WARN_HYPHEN_NEEDED") + Integer.toString(offset-1);        
      else 
      {
        while (true) 
        {
          if (ch == '-') 
          {
            if (lang.length() == offset) 
              return UIPlugin.getResourceString("_WARN_HYPHEN_ENDING");
            
            ch = lang.charAt(offset++);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) 
              return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch + 
              UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");
            
            if (lang.length() == offset)
              return null;
          } 
          else if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) 
            return UIPlugin.getResourceString("_WARN_NAME_INVALID_CHAR") + ch + 
            UIPlugin.getResourceString("_UI_NAME_INVALID_CHAR_END");             
          else if (lang.length() == offset)
            return null;
          ch = lang.charAt(offset++);
        }
      }
    }

    return null;
  }
  
  /**
   * Check to see if the min value is correct.
   * A minimum value must be non-negative and < maxValue
   */
  public static String isValidMinValue(String minValue, String maxValue)
  {
    int min;

    if (minValue == null || minValue.equals(""))
    {
      // Nothing to check
      return null;
    }

    try
    {
      min = Integer.parseInt(minValue);
    }
    catch (NumberFormatException ex)
    {
      return UIPlugin.getResourceString("_ERROR_MIN_NOT_POSITIVE");
    }

    if (min < 0) 
    {
      return UIPlugin.getResourceString("_ERROR_MIN_NOT_POSITIVE");
    }

    try
    {
      int max = Integer.parseInt(maxValue);
      if (min > max) 
      {
        return UIPlugin.getResourceString("_ERROR_MIN_VALUE");
      }
    }
    catch (NumberFormatException ex)
    {
      // Max is not accurate. Don't compare it.
    }
    return null;
  }

  /**
   * Check to see if the max value is correct.
   * A maximum value must be non-negative and > minValue
   *
   * It can also be set the string "unbounded"
   */
  public static String isValidMaxValue(String maxValue, String minValue)
  {
    int max;

    if (maxValue == null || maxValue.equals("")) 
    {
      // Nothing to check
      return null;
    }

    if (maxValue.equals("unbounded"))
    {
      return null;
    }

    try
    {
      max = Integer.parseInt(maxValue);
    }
    catch (NumberFormatException ex)
    {
      return UIPlugin.getResourceString("_ERROR_MAX_NOT_POSITIVE");
    }

    if (max < 0) 
    {
      return UIPlugin.getResourceString("_ERROR_MAX_NOT_POSITIVE");
    }

    try
    {
      int min = Integer.parseInt(minValue);
      if (max < min) 
      {
        return UIPlugin.getResourceString("_ERROR_MAX_VALUE");
      }
    }
    catch (NumberFormatException ex)
    {
      // Min is not accurate. Don't compare it.
    }
    return null;
  }

  /**
   * Validate the name conforms to the XML spec
   */
  public static String checkXMLName(String name, boolean allowEntityRef)
  {
    int length = name.length();
    char character;

    if (length == 0) 
    {
      return WSDLEditorPlugin.getWSDLString("_WARN_NAME_MUST_CONTAIN_AT_LEAST_ONE_CHAR");
    }
    
    if (name.indexOf(" ") >= 0)
    {
      return(WSDLEditorPlugin.getWSDLString("_WARN_NAME_HAS_SPACE"));
    }

    int index = 0;
    if (length > 0 &&
        name.charAt(0) == '%')
    {
      if (allowEntityRef) 
      {
        // skip over the first character 
        index++;
      } // end of if ()
      else
      {
        return WSDLEditorPlugin.getWSDLString("_WARN_NAME_INVALID_FIRST");
      } // end of else
    }
    
    for(; index < length; index++)
    {
      character = name.charAt(index);

      if(index == 0)
      {
        if( !isXMLNameStart(character) )
        {
          return WSDLEditorPlugin.getWSDLString("_WARN_NAME_INVALID_FIRST");
        }
      }
      else
      {
        if(!isXMLNameChar(character))
        {
          if ((index == length - 1) && //check if the last character is a ';'
              allowEntityRef &&
              character == ';')
          {
            // we're still ok then
            continue;
          } // end of if ()
          else 
          {
            return WSDLEditorPlugin.getWSDLString("_WARN_NAME_INVALID_CHAR") + character + 
            WSDLEditorPlugin.getWSDLString("_UI_NAME_INVALID_CHAR_END");
          } // end of else
        }
      }
    }
    return null;
  }

  /**
   * Validate the name conforms to the XML spec
   */
  public static String checkXMLName(String name)
  {
    return checkXMLName(name, false);
  }

  /**
   * isXMLNameStart
   **/
  private static boolean isXMLNameStart(char ch)
  {
    return (ch == '_' || ch == ':' || Character.isLetter(ch) );
  }

  /**
   * isXMLNameChar
   **/
  private static boolean isXMLNameChar(char ch)
  {
    return (Character.isLetterOrDigit(ch) || ch == '.' || ch == '-' || ch == '_' || ch == ':');
  }

  /**
   * isXMLPrefixStart
   **/
  private static boolean isXMLPrefixStart(char ch)
  {
    return (ch == '_' || Character.isLetter(ch) );
  }

  /**
   * isXMLPrefixChar
   **/
  private static boolean isXMLPrefixChar(char ch)
  {
    return (Character.isLetterOrDigit(ch) || ch == '.' || ch == '-' || ch == '_');
  }

  /**
   * parseElementText
   */
  public static String parseElementText(String text)
  {
    if (text.indexOf('<') != -1 || text.indexOf('>') != -1)
    {
      return UIPlugin.getResourceString("_WARN_ELEMENT_INVALID_CHAR");
    }
    return null;
  }

  /**
   * parseAttributeValue
   */
  public static String parseAttributeValue(String value)
  {
    if (value.indexOf('"') != -1 || value.indexOf('<') != -1 || value.indexOf('>') != -1)
    {
      return UIPlugin.getResourceString("_WARN_ATTRIBUTE_INVALID_CHAR");

    }
    return null;
  }

  /**
   * parseADATASection
   */
  public static String parseCDATASection(String section)
  {
    if (section.indexOf("]]>") != -1)
    {
      return UIPlugin.getResourceString("_WARN_CDATA_INVALID_STRING");

    }
    return null;
  }

  /**
   * parseProcessiingInstruction
   */
  public static String parseProcessingInstructionData(String data)
  {
    if (data.indexOf("?>") != -1)
    {
      return UIPlugin.getResourceString("_WARN_PROCESSING_INVALID_STRING");

    }
    return null;
  }

  /**
   * parseProcessingInstructionTarget
   */
  public static String parseProcessingInstructionTarget(String target)
  {
    if (target.length() == 3 && (target.indexOf("XML") != -1 || target.indexOf("XMl") != -1 || target.indexOf("XmL") != -1 ||
        target.indexOf("xML") != -1 || target.indexOf("Xml") != -1 || target.indexOf("xMl") != -1 ||
        target.indexOf("xmL") != -1 || target.indexOf("xml") != -1))
    {
      return UIPlugin.getResourceString("_WARN_PROCESSING_TARGET_INVALID_STRING");

    }
    return checkXMLName(target);
  }

  /**
   * parsComment
   */
  public static String parseComment(String comment)
  {
    if (comment.indexOf("--") != -1)
    {
      return UIPlugin.getResourceString("_WARN_COMMENT_INVALID_STRING");
    }
    return null;
  }
  
  /**
   * Validate the prefix conforms to the XML spec
   */
  public static String checkXMLPrefix(String prefix)
  {
    int length = prefix.length();
    if (length == 0)
    {
      return null;
    }
    char character;
    
    if (prefix.indexOf(" ") >= 0)
    {
      return(UIPlugin.getResourceString("_WARN_PREFIX_HAS_SPACE"));
    }
    
    for(int index = 0; index < length; index++)
    {
      character = prefix.charAt(index);

      if(!isXMLPrefixChar(character))
      {
        return UIPlugin.getDefault().getString("_WARN_PREFIX_INVALID_CHAR", String.valueOf(character));
      }
    }
    return null;
  }

}

