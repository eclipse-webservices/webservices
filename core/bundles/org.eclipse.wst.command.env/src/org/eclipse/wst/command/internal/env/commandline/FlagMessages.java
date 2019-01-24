/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.commandline;

/**
  * This class is used to store constant string error messages
  * generated by CommandLine.java
  *
  *
  * @author Peter Moogk
  * @date   July 13, 2000
  * @see    org.eclipse.wst.command.internal.env.commandline.CommandLine
**/

public final class FlagMessages
{

  public static final String PARSER_ERROR_FLAG_NOT_FOUND = "PARSER_ERROR_FLAG_NOT_FOUND";

  public static final String PARSER_ERROR_AMBIGUOUS_FLAG = "PARSER_ERROR_AMBIGUOUS_FLAG";

  public static final String PARSER_ERROR_MISSING_POSITIONAL = "PARSER_ERROR_MISSING_POSITIONAL";

  public static final String PARSER_ERROR_TOO_MANY_POSITIONALS = "PARSER_ERROR_TOO_MANY_POSITIONALS";

  public static final String PARSER_ERROR_MISSING_REQUIRED_FLAG = "PARSER_ERROR_MISSING_REQUIRED_FLAG";

  public static final String PARSER_ERROR_DUPLICATE_FLAGS_NOT_ALLOWED = "PARSER_ERROR_DUPLICATE_FLAGS_NOT_ALLOWED";

  public static final String PARSER_ERROR_MISSING_FLAG_PARAMETER = "PARSER_ERROR_MISSING_FLAG_PARAMETER";

  // Error internal error messages.
  public static final String PARSER_ERROR_MESSAGE_MISSING_ARG = "PARSER_ERROR_MESSAGE_MISSING_ARG";

  // Internal command line error messages.
  public static final String PARSER_ERROR_NO_FLAGS_DATA_SPECIFIED = "PARSER_ERROR_NO_FLAGS_DATA_SPECIFIED";

  public static final String PARSER_ERROR_FIRST_FLAG_NOT_POSITIONAL = "PARSER_ERROR_FIRST_FLAG_NOT_POSITIONAL";

  public static final String PARSER_ERROR_NULL_FLAG_ROW = "PARSER_ERROR_NULL_FLAG_ROW";

  public static final String PARSER_ERROR_INCORRECT_ROW_SIZE = "PARSER_ERROR_INCORRECT_ROW_SIZE";

  public static final String PARSER_ERROR_NULL_IN_ROW = "PARSER_ERROR_NULL_IN_ROW";

  public static final String PARSER_ERROR_INCORRECT_DUP_STRING = "PARSER_ERROR_INCORRECT_DUP_STRING";

  public static final String PARSER_ERROR_INCORRECT_REQUIRED_STRING = "PARSER_ERROR_INCORRECT_REQUIRED_STRING";

  public static final String PARSER_ERROR_POSITIONAL_NOT_AT_BEGINNING = "PARSER_ERROR_POSITIONAL_NOT_AT_BEGINNING";

  public static final String PARSER_ERROR_HELP_FLAG_NOT_SPECIFIED = "PARSER_ERROR_HELP_FLAG_NOT_SPECIFIED";

  public static final String PARSER_ERROR_FLAGS_NOT_ORDERED = "PARSER_ERROR_FLAGS_NOT_ORDERED";

  public static final String PARSER_ERROR_FLAG_NOT_LOWERCASE = "PARSER_ERROR_FLAG_NOT_LOWERCASE";

  public static final String PARSER_ERROR_REQUIRED_POSITIONAL_NEEDS_NAME = "PARSER_ERROR_REQUIRED_POSITIONAL_NEEDS_NAME";

  public static final String PARSER_INFO_SYNTAX = "PARSER_SYNTAX";

  public static final String PARSER_FLAGS = "PARSER_FLAGS";
  
  public static final String PARSER_INFO_WHERE = "PARSER_WHERE";
}
