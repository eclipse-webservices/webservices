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
package org.eclipse.wst.command.internal.env.commandline;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.BitSet;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.wst.command.env.common.StringUtils;


/**
  * <p>This is a generic command line parsing class.  The programmer need only specify
  * the characteristics of their command line(ie. positional parameters, flags, etc)
  * and this class will perform the rest of the parsing work.  As an added bonus this
  * class will also create formated help text that displays the syntax of this
  * command line.
  *
  * <p>The main parameter to the CommandLine constructor is a two dimensional String
  * array.  There are 5 columns in this array and as many rows as needed for each
  * flag or positional parameter.  The five columns are:
  *
  * <ol>
  * <li>The name of the flag(eg. "help" or "a", etc)
  *     Note: no dash should be in the name here.  The user would specify "-help" on the
  *           command line though.
  * <li>The name of the flag parameter(if any)
  * <li>A string indicating whether this flag can appear more than once on the
  *     command line.
  * <li>A string indicating whether this flag must be specified on the command line.
  * <li>A help text string which describes this particular flag or positional parameter.
  * </ol>
  *
  * <p>Each row in the string array is associate with either a positional parameter
  * or with a flag.  For example, consider this command line:
  * <ul>
  * <li>My_prog required_parm1 -ab parm_for_ab required_parm2 -help optional_parms
  * </ul>
  *
  * <p>In this example My_prog is the name of the program.  required_parm1 and
  * required_parm2 must be specified on the command line.  Notice that they do not
  * have to be adjacent to each other on the command line. optional_parms may optionally
  * specified.  optional_parms can be 0 or more parameters.  They does not need to be
  * adjacent to the required parameters, but they do need to follow the required
  * parameters. Two flags are specified "-ab" and "-help".  The "-ab" flag has a
  * parameter "parm_for_ab" associated with it.  Note: a flag can not have an optional
  * parameter. (ie. a flag either never have a parameter or it always has a parameter)
  * In this example, the first two rows in the string array would describe required_parm1
  * and required_parm2.  The third entry would describe optional_parms.  The fourth and
  * fifth rows would describe "-ab" and "-help".  For this command line the string array
  * would look like this:
  *
  * <ul>
  * <li>{ { CommandLine.POSITIONAL, "parm1", CommandLine.NO_DUPS,
  *       CommandLine.REQUIRED, "parm1 help text" },
  * <li>{ CommandLine.POSITIONAL, "parm2", CommandLine.NO_DUPS,
  *       CommandLine.REQUIRED, "parm2 help text" },
  * <li>{ CommandLine.POSITIONAL, "optional_parms", CommandLine.DUPS_OK,
  *       CommandLine.REQUIRED, "optional help text" },
  * <li>{ "ab", "parm_for_ab", CommandLine.NO_DUPS,
  *       CommandLine.OPTIONAL, "ab help text" },
  * <li>{ "help", CommandLine.NO_PARM, CommandLine.NO_DUPS,
  *       CommandLine.OPTIONAL, "displays this help text" } }
  * </ul>
  *
  * <p>String array rules:
  *
  * <ol>
  * <li>Positional rows must always come first in the array.  These rows must
  *     have a name specified in column 2, except for one case.  If you want to indicate that
  *     there are no optional positional parameters you would put CommandLine.NO_PARM
  *     in column 2 of the positional row.  You would also need to put CommandLine.OPTIONAL
  *     in column 4 since this row is for optional parameters(or the non-existence of
  *     optional parameters as the case may be)
  * <li>Once a positional row specifies that it is optional, no further positional rows
  *     may be specified.
  * <li>At least one positional row must be specified, even if it is to just to specify that
  *     there are no optional parameters.
  * <li>Flag names must be in lower case.  Note: the user can specify upper or lower case
  *     on the command line.
  * <li>If a flag has a parameter the name of the parameter should be put into column 2.
  *     otherwise CommandLine.NO_PARM should be put in column 2.
  * <li>If a flag is allowed to appear more than once on the command line then
  *     CommandLine.DUPS_OK should be put into column 3, otherwise CommandLine.NO_DUPS
  *     should be put into column 3.
  * <li>If a flag must be specified on the command line then CommandLine.REQUIRED should
  *     be specified in column 4, otherwise CommandLine.OPTIONAL should be specified.
  * <li>A help flag must be specified.
  * <li>The strings specified in the flags column, the parameter name column, and the
  *     help column are always translated using the resource bundle specified.  Of
  *     course the special string values such as POSITIONAL and NO_PARM are not
  *     translated.
  * </ol>
  *
  * <p> Flag matching rules:
  * <ol>
  * <li>The parser will attempt to match a user specified flag with the minimum number
  *     of programmer specified flag characters.  For example, if the programmer has
  *     specified that the flag is "help" the user could specify "-h", "-he", "-hel", or
  *     "-help".  If the programmer has also specified a flag called "hello", the
  *     user would get an error message for specifing either "-h", "-he" or "-hel", since
  *     it is ambiguous which flag is being refered to.  Both "-hell" and "-hello"
  *     would be ok for the user to specify the hello flag on the command line.
  * <li>If a flag has a parameter, the parameter may be immediately adjacent to the flag.
  *     For example if "abcd" is specified as a flag with a parameter, the user could
  *     specify the following assuming there are no other ambiguous flags. "-abcdparm",
  *     "-abcparm", "-abparm", or "-aparm" where parm is the parameter for the flag.
  *     (Of course the user can always put a white space between the flag and the
  *     parameter.)
  * <li>If a flag does not have a parameter and a parameter is adjacent to it then this
  *     parameter is interpreted as a positional parameter.  For example if "abcb" is
  *     specified as a flag and the user specifies "-abhello", then the first "ab" will be
  *     interpreted as the flag and "hello" will be interpreted as a positional
  *     parameter.
  * </ol>
  *
  * @author Peter Moogk
  * @date   July 13, 2000
**/

public class CommandLine
{
  // Constant strings that the user should use in their flags_info input array.
  /**
    * Specifies that this flag or positional parameter is required.
  **/
  public static final String REQUIRED   = "required";
  /**
    * Specifies that this flag or positional parameter is optional.
  **/
  public static final String OPTIONAL   = "optional";
  /**
    * Specifies that this flag may be specified more than once on the command line.
    * Note: This string has no meaning for positional parameters.
  **/
  public static final String DUPS_OK    = "dups_ok";
  /**
    * Specifies that this flag may not be specified more than once on the command line.
    * Note: This string has no meaning for positional parameters.
  **/
  public static final String NO_DUPS    = "no_dups";
  /**
    * Specifies that a row is a positional parameter row.
  **/
  public static final String POSITIONAL = "";
  /**
    * Specifies that a flag has no parameter.  For positional parameters this
    * string indicates that there are no optional parameters.
  **/
  public static final String NO_PARM    = "";
  
  /**
    * @param flags_info This parameter specifies the characteristics of the
    *                   command line parser.  See the class description for a
    *                   definition of what this string array should look like.
    * @param help_flag  This string indicates which flag in the flags_info
    *                   array is the help flag.
    * @param tool_name  This string indicates the name of the tool that this
    *                   command line parser is being used for.  This string
    *                   is only used when creating the help text.
  **/
  public CommandLine( String[][]     flags_info,
                       String         help_flag,
                       String         tool_name,
                       ResourceBundle flagMessages )
                   
    throws InternalErrorExc
  {
    this.flags_info   = flags_info;
    this.help_flag    = help_flag;
    this.tool_name    = tool_name;
    this.flagMessages = flagMessages;
    
    messages = ResourceBundle.getBundle( "org.eclipse.wst.command.internal.env.commandline.commandline" );
    verify_flags_info();
  }

  /**
    * Call this method to parse a command line.
    * @param args This is the string array from the command line.
    * @throws Flag_error If the user has specified the command line incorrectly
    *                    a Flag_error exception will be thrown.  Use getMessage
    *                    to get a description of the user error.
    *
    * @throws Help_specified If the user specifies the help flag this exception
    *                        will be thrown.  Use getMessage to get a fully
    *                        formatted syntax description of this command line.
  **/
  public void check_flags( String[] args ) throws Flag_error
  {
    flags_specified = new Vector[flags_info.length];

    for( int index = 0; index < args.length; index++ )
    {
      boolean processed_the_next_arg = process_arg( args, index );
      if( processed_the_next_arg == true ) index++;
    }

    // If help was specified we will not check the regular rules.
    if( !flag_specified( help_flag ) )
    {
      // Check for flags breaking the rules specified in flags_info.
      check_rules();

      // Call subclassed method to see if more processing is required.
      more_processing();
    }
  }

  /**
    * This method is for subclasses of this class.  This method is called at
    * the very end of check_flags method.  It is intended that subclasses would
    * perform additional command line checking here.
  **/
  protected void more_processing() throws Flag_error, InternalErrorExc
  {
  }

  /**
    * Once the command line has been parsed by calling check_flags a
    * call can be made to this method to get the parameters for this flag.
    * If the flag was not specified on the command line null will be returned.
    * If the flag was specified, but has no parameters a valid vector will be
    * returned containing a null.
  **/
  public String[] get_flag_parms( String flag )
  {
    int     row        = 0;
    boolean flag_found = false;

    while( flag_found == false && row < flags_info.length )
    {
      if( flags_info[row][FLAG_COL].equals( flag ) )
      {
        flag_found = true;
      }
      else
      {
        row++;
      }
    }

    if( flag_found == true ) 
    {
      Vector parms = flags_specified[row];
      return parms == null ? null : (String[])(parms.toArray( new String[0] ));
    }

    return null;
  }

  /**
    * Call this method to get all of the positional parameters.
    * This method returns both the required positionals and the
    * optional positionals in that order.  If no positional parameters
    * were specified null will be returned.
  **/
  public String[] get_positionals()
  {
    return (String[])flags_specified[POSITIONAL_ROW].toArray( new String[0] );
  }

  /**
    * This method returns true if this flag was specified by the user
    * otherwise false is returned.
  **/
  public boolean flag_specified( String flag )
  {
    return get_flag_parms( flag ) != null;
  }

  /**
    * This method returns fully formated help text syntax for this
    * command line.
  **/
  public String get_help_text()
  {
    StringBuffer help_text  = new StringBuffer(400);
    int          help_width = HELP_LINE_LEN -
                              2 -              // blanks
                              1 -              // dash
                              max_flag_size -  // flag field
                              1 -              // blank
                              max_name_size -  // name field
                              1;               // blank

    help_text.append( messages.getString( FlagMessages.PARSER_INFO_SYNTAX ) +
                      ": " + tool_name + " " );

    // Display the required positionals if any.
    for( int index = 0; index < required_positionals; index++ )
    {
      help_text.append( flagMessages.getString( flags_info[index][NAME_COL] ) +
                        " " );
    }

    help_text.append( "[" + messages.getString( FlagMessages.PARSER_FLAGS ) +
                      "] " );

    if( optional_positionals_allowed == true )
    {
      help_text.append( flagMessages.getString( flags_info[optional_list_index][NAME_COL] ) );
    }

    help_text.append( "\n  " + messages.getString( FlagMessages.PARSER_INFO_WHERE ) + "\n" );

    // Create one line of description for each parameter
    for( int row = 0; row < flags_info.length; row++ )
    {
      String columnId   = flags_info[row][NAME_COL];
      String columnName = columnId == NO_PARM ? "" : flagMessages.getString( columnId );

      String flagId     = flags_info[row][FLAG_COL];
      String flagName   = flagId == POSITIONAL ? POSITIONAL : flagMessages.getString(flagId);

      int flag_padding = max_flag_size - flagName.length();
      int name_padding = max_name_size - columnName.length();

      // Skip this row if this is a marker for no optional positionals.
      if( flags_info[row][FLAG_COL] == POSITIONAL &&
          flags_info[row][NAME_COL] == NO_PARM ) continue;

      if( flagName == POSITIONAL )
      {
        help_text.append( getBlanks(3 ) );
      }
      else
      {
        help_text.append( "  -" + flagName );
      }

      help_text.append( getBlanks( flag_padding + 1 ) );
      help_text.append( columnName );
      help_text.append( getBlanks( name_padding + 1 ) );

      String   columnHelp = flagMessages.getString( flags_info[row][HELP_COL] );
      String[] split_help_text
        = StringUtils.splitter( columnHelp, help_width );

      if( split_help_text.length > 0 )
        help_text.append( split_help_text[0] + "\n" );
      else
        help_text.append( "\n" );

      for( int index = 1; index < split_help_text.length; index++ )
      {
        help_text.append( getBlanks( HELP_LINE_LEN - help_width ) );
        help_text.append( split_help_text[index] + "\n" );
      }
      
      help_text.append( "\n" );
    }

    return "" + help_text;
  }
  
  /**
   *  Return a string with the specified number of blanks.
   */
  private String getBlanks( int count )
  {
  	char[] blanks = new char[count];
  	Arrays.fill( blanks, 0, count, ' ' );
  	
  	return new String( blanks );
  }

  /**
    * This class is the base for all command line exception classes.
  **/
  static public class ErrorExc extends Exception
  {
  	public ErrorExc( String message, String[] args )
  	{
  	  super( MessageFormat.format( message, args ) );
  	}
  }

  /**
    * This class will be thrown when an internal error is detected.
    * This usually happens if the flag description information was
    * specified incorrectly.
  **/
  static public class InternalErrorExc extends IllegalArgumentException
  {  	
  	public InternalErrorExc( String message, String[] args )
  	{
  	  super( MessageFormat.format( message, args ) );
  	}
  	
  	public InternalErrorExc( String message )
  	{
  	  this( message, (String[])null );
  	}
  	
  	public InternalErrorExc( String message, String arg )
  	{
  	  this( message, new String[]{ arg } );
  	}
  }

  /**
    * This class will be thrown if a user error occurs while parsing the command line.
  **/
  static public class Flag_error extends ErrorExc
  {
    public Flag_error( String message, String[] args )
    {
      super( message, args );
    }

    public Flag_error( String message, String arg )
    {
      super( message, new String[]{ arg } );
    }

    public Flag_error( String message )
    {
      super( message, null );
    }
  }

  /**
    * Tries to process a single flag on the command line.
    * @param  args      All the command line parameters.
    * @param  arg_index This is the index of the argument that is to be processed.
    * @return returns true if the argument we are processing is a flag that has a
    *         parameter and the parameter is specified in the next argument.
  **/
  private boolean process_arg( String[] args, int arg_index ) throws Flag_error
  {
    boolean processed_next_arg = false;

    if( args[arg_index].charAt(0) == '-' )
    {
      // This is the start of a flag.
      int flag_index = 1;
      int info_index = get_info_index( args[arg_index], flag_index );
      int max_index  = get_max_index( args[arg_index], flag_index, info_index );

      if( flags_info[info_index][NAME_COL] != NO_PARM )
      {
        // This flag takes a parameter so check if it is stuck to this
        // arg.
        if( max_index < args[arg_index].length() )
        {
          add_flag_parm( info_index,
                         args[arg_index].substring( max_index,
                                                    args[arg_index].length() ) );
        }
        else
        {
          // This flag has a parameter and it wasn't stuck to the flag,
          // so we will try to get it from the next arg.
          if( arg_index+1 < args.length &&
              args[arg_index+1].charAt(0) != '-' )
          {
            // We found an parameter in the next string so we will use this
            // one as a parameter for this flag.  Note: if was no parameter
            // after the flag at all, the error will be caught in the
            // check_rules method.
            add_flag_parm( info_index, args[arg_index+1] );
            processed_next_arg = true;
          }
          else
          {
            // Error missing paramater. This error is caught by check_rules.
            add_flag_parm( info_index, null );
          }
        }
      }
      else
      {
        // The flag does not have a parameter so we will add a null.
        add_flag_parm( info_index, null );

        // Check to see if a positional parameter is stuck to this flag.
        if( max_index < args[arg_index].length() )
        {
          add_positional( args[arg_index].substring( max_index,
                                                     args[arg_index].length() ) );
        }
      }
    }
    else
    {
      // This is a positional parameter.
      add_positional( args[arg_index] );
    }

    return processed_next_arg;
  }

  /**
    * Finds the row for the flag that uniquely matched this flag str.
    * @return returns the flags_info index of the matching flag.
  **/
  private int get_info_index( String flag_str, int start_index )
    throws Flag_error
  {
    //int info_start  = 1;
    //int info_end    = flags_info.length;
    int info_index;
    int found_count = 0;
    int info_col_index = 0;
    int last_info_index = 0;

    BitSet flags_rejected = new BitSet();

    // Loop over each char in flag_str.  Note: we will probably bail early.
    for( int flag_col_index = start_index; flag_col_index < flag_str.length(); flag_col_index++ )
    {
      // Loop over each flag in flag_info
      for( info_index = 0; info_index < flags_info.length; info_index++ )
      {
        String flagId   = flags_info[info_index][FLAG_COL];
        String flagName = flagId == POSITIONAL ? POSITIONAL : flagMessages.getString( flagId );

        if( flags_rejected.get( info_index ) == false &&
            info_col_index < flagName.length() &&
              Character.toLowerCase(flag_str.charAt(flag_col_index)) ==
              flagName.charAt(info_col_index) )
        {
          found_count++;
          last_info_index = info_index;
        }
        else
        {
          flags_rejected.set( info_index );
        }
      }

      if( found_count == 1 )
      {
        // We have a match.
        return last_info_index;
      }
      else if( found_count == 0 )
      {
        // Flag not found at all.
        throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_FLAG_NOT_FOUND ),
                              flag_str.substring( start_index ) );
      }
      else
      {
        // More than one flag was found with this char so we will go to the
        // next char to uniquely match it.
        info_col_index++;
        //info_start = last_info_index - found_count + 1;
        //info_end   = last_info_index + 1;
        found_count = 0;
      }
    }

    // The only way to get to this code is if the loop exited with
    // the found_count greater than 1.  Therefore, the flag specified is
    // ambiguous.
    throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_AMBIGUOUS_FLAG ),
                          flag_str.substring( start_index) );
  }

  /**
    * This method tries to match as many characters as possible of the user
    * specified flag with the matched flag string.
    * @return returns the position of the last matching flag chararacter.
  **/
  private int get_max_index( String flag_str, int flag_start, int info_index )
  {
    int flag_col_index = flag_start;
    int info_col_index = 0;

    String info_str = flagMessages.getString( flags_info[info_index][FLAG_COL] );

    while( flag_col_index < flag_str.length() &&
           info_col_index < info_str.length() )
    {
      if( Character.toLowerCase( flag_str.charAt(flag_col_index) ) !=
          info_str.charAt(info_col_index) )
        break;

      flag_col_index++;
      info_col_index++;
    }

    return flag_col_index;
  }

  /**
    * Adds a positional parameter to flags_specified.
  **/
  private void add_positional( String positional_parm )
  {
    if( flags_specified[POSITIONAL_ROW] == null )
      flags_specified[POSITIONAL_ROW] = new Vector(3);

    flags_specified[POSITIONAL_ROW].add( positional_parm );
  }

  /**
    * Adds a flag and its parameter to flags_specified.  If only the
    * flag was specified, then a null should be passed to flag_parm.
  **/
  private void add_flag_parm( int flag_index, String flag_parm )
    throws Flag_error
  {
    if( flags_info[flag_index][DUP_COL] == NO_DUPS &&
        flags_specified[flag_index] != null )
      throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_DUPLICATE_FLAGS_NOT_ALLOWED ),
                             flagMessages.getString( flags_info[flag_index][FLAG_COL] ) );

    if( flags_specified[flag_index] == null )
      flags_specified[flag_index] = new Vector(3);

    flags_specified[flag_index].add( flag_parm );
  }

  /**
    * Checks that the user hasn't broken any command line rules.
  **/
  private void check_rules() throws Flag_error
  {
    // Check that all of the required positionals were specified.
    int positional_count = 0;

    if( flags_specified[POSITIONAL_ROW] != null )
    {
      positional_count = flags_specified[POSITIONAL_ROW].size();
    }

    if( required_positionals > positional_count )
      throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_MISSING_POSITIONAL ),
                             flagMessages.getString( flags_info[required_positionals-1][NAME_COL] ) );

    else if( required_positionals < positional_count &&
             optional_positionals_allowed == false )
      throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_TOO_MANY_POSITIONALS ) );

    for( int row = 0; row < flags_info.length; row++ )
    {
      if( flags_info[row][FLAG_COL] == POSITIONAL ) continue;

      if( flags_specified[row] == null &&
          flags_info[row][REQUIRED_COL] == REQUIRED )
        throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_MISSING_REQUIRED_FLAG ),
                               flagMessages.getString( flags_info[row][FLAG_COL] ) );

      int parm_count = 0;

      if( flags_specified[row] != null ) parm_count = flags_specified[row].size();

      // Check for too many flag parameters.
      if( flags_info[row][DUP_COL] == NO_DUPS && parm_count > 1 )
        throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_DUPLICATE_FLAGS_NOT_ALLOWED ),
                               flagMessages.getString( flags_info[row][FLAG_COL] ) );

      // Check for missing flag parameter.
      if( parm_count > 0 && flags_info[row][NAME_COL] != NO_PARM )
      {
        for( int index = 0; index < flags_specified[row].size(); index++ )
        {
          if( flags_specified[row].elementAt(index) == null )
            throw new Flag_error( messages.getString( FlagMessages.PARSER_ERROR_MISSING_FLAG_PARAMETER ),
                                  new String[]
                                    { flagMessages.getString( flags_info[row][FLAG_COL] ),
                                      flagMessages.getString( flags_info[row][NAME_COL] ) } );

        }
      }
    }
  }

  /**
    * This method verifies that a proper flags_info string array was passed
    * to us by the programmer.
  **/
  private void verify_flags_info() throws InternalErrorExc
  {
    boolean done_positionals = false;
    boolean help_specified   = false;

    required_positionals = 0;
    optional_positionals_allowed = false;
    optional_list_index = -1;
    max_flag_size = 0;
    max_name_size = 0;

    if( flags_info == null || flags_info.length == 0 )
      throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_NO_FLAGS_DATA_SPECIFIED  ) );

    if( flags_info[POSITIONAL_ROW] != null &&
        flags_info[POSITIONAL_ROW].length > 0 &&
        flags_info[POSITIONAL_ROW][FLAG_COL] != POSITIONAL )
      throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_FIRST_FLAG_NOT_POSITIONAL ) );

    for( int row = 0; row < flags_info.length; row++ )
    {
      if( flags_info[row] == null )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_NULL_FLAG_ROW ) );

      if( flags_info[row].length != 5 )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_INCORRECT_ROW_SIZE ) );

      if( flags_info[row][FLAG_COL] == null ||
          flags_info[row][NAME_COL] == null ||
          flags_info[row][HELP_COL] == null )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_NULL_IN_ROW ) );

      if( flags_info[row][FLAG_COL].equals( help_flag ) ) help_specified = true;

      String flagId   = flags_info[row][FLAG_COL];
      String nameId   = flags_info[row][NAME_COL];

      String flag_col = flagId == POSITIONAL ? POSITIONAL : flagMessages.getString( flagId );
      String name_col = nameId == NO_PARM ? NO_PARM : flagMessages.getString( nameId );

      if( flag_col.length() > max_flag_size )
        max_flag_size = flag_col.length();

      if( name_col != NO_PARM && name_col.length() > max_name_size )
        max_name_size = name_col.length();

      // Ensure that flags are all in lowercase.
      if( !flag_col.equals( flag_col.toLowerCase() ) )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_FLAG_NOT_LOWERCASE ),
                                    flag_col );

      // Ensure that only the predefined dup strings are used.
      // Note: it's ok to use != when comparing constant strings literals.
      if( flags_info[row][DUP_COL] == null ||
          ( flags_info[row][DUP_COL] != NO_DUPS &&
            flags_info[row][DUP_COL] != DUPS_OK ) )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_INCORRECT_DUP_STRING ),
                                 flags_info[row][DUP_COL] );

      // Ensure that only the predefined required strings are used.
      if( flags_info[row][REQUIRED_COL] == null ||
          ( flags_info[row][REQUIRED_COL] != REQUIRED &&
            flags_info[row][REQUIRED_COL] != OPTIONAL ) )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_INCORRECT_REQUIRED_STRING ),
                                 flags_info[row][REQUIRED_COL] );

      // Count the number of required positionals.
      if( flags_info[row][FLAG_COL] == POSITIONAL )
      {
        if( flags_info[row][REQUIRED_COL] == REQUIRED )
        {
          if( flags_info[row][NAME_COL] == NO_PARM )
            throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_REQUIRED_POSITIONAL_NEEDS_NAME ) );

          required_positionals++;
        }
        else if( flags_info[row][NAME_COL] != NO_PARM )
        {
          // We use the NAME_COL field for an optional positional to denote
          // whether additional positionals are allowed or not.
          optional_positionals_allowed = true;
          optional_list_index = row;
        }
      }

      // Ensure that positionals are at the beginning of the info.
      if( flags_info[row][FLAG_COL] != POSITIONAL ||
          ( done_positionals == false &&
            flags_info[row][FLAG_COL] == POSITIONAL &&
            flags_info[row][REQUIRED_COL] == OPTIONAL ) )
      {
        done_positionals = true;
      }
      else if( done_positionals == true )
        throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_POSITIONAL_NOT_AT_BEGINNING ) );
    }

    // Ensure that a help flag was specified in the info list.
    if( help_specified == false )
      throw new InternalErrorExc( messages.getString( FlagMessages.PARSER_ERROR_HELP_FLAG_NOT_SPECIFIED ) );
  }

  /**
    * This method is used to dump internal information about a parsed
    * command line.
  **/
  public String toString()
  {
    StringBuffer b = new StringBuffer(100);

    for( int row = 0; row < flags_info.length; row++ )
    {
      b.append( "\n" );

      if( flags_info[row][FLAG_COL] == POSITIONAL )
        b.append( "Positional:" );
      else
        b.append( flagMessages.getString( flags_info[row][FLAG_COL] ) + ":" );


      if( flags_specified[row] == null )
        b.append( "no parameters" );
      else
        for( int parm_index = 0; parm_index < flags_specified[row].size(); parm_index++ )
        {
          String parm = (String)flags_specified[row].elementAt(parm_index);
          b.append( parm + ":" );
        }
      b.append( "\n" );
    }

    return ""+b;
  }

  private static final int FLAG_COL     = 0;
  private static final int NAME_COL     = 1;
  private static final int DUP_COL      = 2;
  private static final int REQUIRED_COL = 3;
  private static final int HELP_COL     = 4;

  private static final int POSITIONAL_ROW = 0;
  private static final int HELP_LINE_LEN  = 75;

  private String[][] flags_info;
  private String     help_flag;
  private String     tool_name;
  private Vector[]   flags_specified;
  private int        required_positionals;
  private boolean    optional_positionals_allowed;
  private int        optional_list_index;
  private int        max_flag_size;
  private int        max_name_size;

  protected ResourceBundle messages;
  private   ResourceBundle flagMessages;
}
