<%
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
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   int calendarType = Integer.parseInt(request.getParameter(ActionInputs.CALENDAR_TYPE));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title><%=controller.getMessage("FRAME_TITLE_CALENDAR_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/calendar.css"))%>">
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript">
  var days = new Array("<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_SUNDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_MONDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_TUESDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_WEDNESDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_THURSDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_FRIDAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SH_SATURDAY"))%>");
  var months = new Array("<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_JANUARY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_FEBRUARY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_MARCH"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_APRIL"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_MAY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_JUNE"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_JULY"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_AUGUST"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_SEPTEMBER"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_OCTOBER"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_NOVEMBER"))%>","<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_DECEMBER"))%>");
  var engMonths = new Array("January","February","March","April","May","June","July","August","September","October","November","December");
  var currentDate;
  var calendarBodySet = false;
  var calendarBody;
  var savedYearTextValue = "";

<%
   // Regular expressions
   // time       - HH:MM:SS[.SSS]
   // year       - Y*
   // date       - YYYY+-MM-DD
   // dateTime   - YYYY+-MM-DDTHH:MM:SS[.SSS]Z
   // gYearMonth - YYYY+-MM 
   // gDay       - ---DD
   // gMonthDay  - --MM-DD
%>
  var timeRegExp = /^(\d{2}):(\d{2}):(\d{2})(\.\d{3})?$/;
  var yearRegExp = /^(\d*)$/;
  var dateRegExp = /^(\d{4,})-(\d{2})-(\d{2})$/;
  var dateTimeRegExp = /^(\d{4,})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(\.\d{3})?Z$/;
  var gYearMonthRegExp = /^(\d{4,})-(\d{2})$/;
  var gDayRegExp = /^-{3}(\d{2})$/;
  var gMonthDayRegExp = /^-{2}(\d{2})-(\d{2})$/;

  function validateYearString(yearString)
  {
    return yearRegExp.test(yearString);
  }

  function getTimeParameters(timeString)
  {
    return timeRegExp.exec(timeString);
  }

  function getDateParameters(dateString)
  {
    return dateRegExp.exec(dateString);
  }

  function getDateTimeParameters(dateTimeString)
  {
    return dateTimeRegExp.exec(dateTimeString);
  }
  
  function getGYearMonthParameters(gYearMonthString)
  {
    return gYearMonthRegExp.exec(gYearMonthString);
  }
  
  function getGDayParameters(gDayString)
  {
    return gDayRegExp.exec(gDayString);
  }
  
  function getGMonthDayParameters(gMonthDayString)
  {
    return gMonthDayRegExp.exec(gMonthDayString);
  }
  
<%
   // HH:MM:SS[.SSS]
%>
  function serializeTime(hours,minutes,seconds,milliseconds)
  {
    var timeString = "";
    if (hours < 10)
      timeString += "0";
    timeString += hours + ":";
    if (minutes < 10)
      timeString += "0";
    timeString += minutes + ":";
    if (seconds < 10)
      timeString += "0";
    timeString += seconds;
    if (milliseconds > 0)
    {
      timeString += ".";
      if (milliseconds < 100)
      {
        if (milliseconds < 10)
          timeString += "00";
        else
          timeString += "0";
      }
      timeString += milliseconds;
    }
    return timeString;
  }

<%
   // YYYY+-MM
%>   
  function serializeGYearMonth(fullYear,month)
  {
    var gYearMonthString = "";
    if (fullYear < 1000)
    {
      if (fullYear < 100)
      {
        if (fullYear < 10)
          gYearMonthString += "000";
        else
          gYearMonthString += "00";
      }
      else
        gYearMonthString += "0";
    }
    gYearMonthString += fullYear+"-";
    if (month < 10)
      gYearMonthString += "0";
    gYearMonthString += month;
    return gYearMonthString;    
  }
  
<%
   // YYYY+-MM-DD
%>
  function serializeDate(fullYear,month,date)
  {
    var dateString = serializeGYearMonth(fullYear,month) + "-";
    if (date < 10)
      dateString += "0";
    dateString += date;
    return dateString;
  }
  
  function serializeGDay(date)
  {
    var gDayString = "---";
    if (date < 10)
      gDayString += "0";
    gDayString += date;
    return gDayString;
  }
  
  function serializeGMonthDay(month,date)
  {
    var gMonthDayString = "--";
    if (month < 10)
      gMonthDayString += "0";
    gMonthDayString += month+"-";
    if (date < 10)
      gMonthDayString += "0";
    gMonthDayString += date;
    return gMonthDayString;
  }    

  function setDateUsingGDayParameters(gDayParameters,dateObj)
  {
    if (gDayParameters != null)
      dateObj.setDate(gDayParameters[1]);
  }
  
  function setDateUsingGMonthDayParameters(gMonthDayParameters,dateObj)
  {
    if (gMonthDayParameters != null)
    {
      dateObj.setMonth(gMonthDayParameters[1]-1);
      dateObj.setDate(gMonthDayParameters[2]);
    }
  }
  
  function setDateUsingGYearMonthParameters(gYearMonthParameters,dateObj)
  {
    if (gYearMonthParameters != null)
    {
      dateObj.setFullYear(gYearMonthParameters[1]);
      dateObj.setMonth(gYearMonthParameters[2]-1);
    }
  }
  
  function setDateUsingDateParameters(dateParameters,dateObj)
  {
    if (dateParameters != null)
    {
      setDateUsingGYearMonthParameters(dateParameters,dateObj);
      dateObj.setDate(dateParameters[3]);
    }
  }

  function setDateUsingDateTimeParameters(dateTimeParameters,dateObj)
  {
    if (dateTimeParameters != null)
    {
      setDateUsingDateParameters(dateTimeParameters,dateObj);
      dateObj.setUTCHours(dateTimeParameters[4]);
      dateObj.setUTCMinutes(dateTimeParameters[5]);
      dateObj.setUTCSeconds(dateTimeParameters[6]);
      var milliseconds = dateTimeParameters[7];
      if (milliseconds.length > 0)
<%
   // Eliminate the decimal point
%>
        dateObj.setUTCMilliseconds(milliseconds.substr(1));
      else
        dateObj.setUTCMilliseconds(0);
    }
  }

  function setDateUsingTimeParameters(timeParameters,dateObj)
  {
    if (timeParameters != null)
    {
      dateObj.setHours(timeParameters[1]);
      dateObj.setMinutes(timeParameters[2]);
      dateObj.setSeconds(timeParameters[3]);
      var milliseconds = timeParameters[4];
      if (milliseconds.length > 0)
<%
   // Eliminate the decimal point
%>
        dateObj.setMilliseconds(milliseconds.substr(1));
      else
        dateObj.setMilliseconds(0);
    }
  }

  function init()
  {
    var d = new Date();
<%
   switch (calendarType)
   {
     case ActionInputs.CALENDAR_TYPE_DATE:
%>
    setDateUsingDateParameters(getDateParameters(top.opener.calendarTarget.value),d);
<%
       break;
     case ActionInputs.CALENDAR_TYPE_DATETIME:
%>
    setDateUsingDateTimeParameters(getDateTimeParameters(top.opener.calendarTarget.value),d);
<%
       break;
     case ActionInputs.CALENDAR_TYPE_GYEARMONTH:
%>
    setDateUsingGYearMonthParameters(getGYearMonthParameters(top.opener.calendarTarget.value),d);
<%     
     case ActionInputs.CALENDAR_TYPE_GDAY:
%>
    setDateUsingGDayParameters(getGDayParameters(top.opener.calendarTarget.value),d);
<%   
     case ActionInputs.CALENDAR_TYPE_GMONTHDAY:
%>
    setDateUsingGMonthDayParameters(getGMonthDayParameters(top.opener.calendarTarget.value),d);
<%       
     default:
       break;
   }
%>
    generateCalendar(d);
    var monthSelector = document.getElementById("monthSelector");
    for (var i=0;i<months.length;i++)
      monthSelector.options[i] = new Option(months[i],i);
    generateCalendar(d);
  }

  function initCalendarHeader(table)
  {
    calendarBody = document.createElement("TBODY");
    var headerRow = document.createElement("tr");
    for (var i=0;i<days.length;i++)
    {
      var header = document.createElement("th");
      header.appendChild(document.createTextNode(days[i]));
      header.className="headercolor";
      headerRow.appendChild(header);
    }
    calendarBody.appendChild(headerRow);
    table.appendChild(calendarBody);
    calendarBodySet = true;
  }

  function generateCalendar(date)
  {
    var table = document.getElementById("calendar");
    var dateDate = date.getDate();
    var dateMonth = date.getMonth();
    var dateFullYear = date.getFullYear();
    var dateIterator = new Date();
    dateIterator.setTime(Date.parse(engMonths[dateMonth]+" 1, "+dateFullYear));
    var startingDate = dateIterator.getDate(); // 1,2,3...
    var startingDay = dateIterator.getDay(); // Sun,Mon,Tues...
    clearCalendar();
    for (var rows=0;rows<6;rows++)
    {
      var row = document.createElement("tr");
      for (var columns=0;columns<days.length;columns++)
      {
        var column = document.createElement("td");
        var dateIteratorDate = dateIterator.getDate();
        var dateIteratorMonth = dateIterator.getMonth();
        var dateIteratorFullYear = dateIterator.getFullYear();
        column.width = 24;
        column.height = 24;
        column.align = "center";
        column.valign = "center";
        column.className = "calendartablecells";
        if ((dateIteratorDate == startingDate && columns < startingDay) || (dateIteratorMonth != dateMonth))
          column.innerHTML = "&nbsp;";
        else if (dateIteratorMonth == dateMonth)
        {
          var link = document.createElement("a");
          switch (<%=calendarType%>)
          {
            case <%=ActionInputs.CALENDAR_TYPE_DATE%>:
              link.href = "javascript:transferDate("+dateIteratorDate+")";
              break;
            case <%=ActionInputs.CALENDAR_TYPE_DATETIME%>:
              link.href = "javascript:transferDateTime("+dateIteratorDate+")";
              break;
            case <%=ActionInputs.CALENDAR_TYPE_GYEARMONTH%>:
              link.href = "javascript:transferGYearMonth()";
              break;
            case <%=ActionInputs.CALENDAR_TYPE_GDAY%>:
              link.href = "javascript:transferGDay("+dateIteratorDate+")";
              break;
            case <%=ActionInputs.CALENDAR_TYPE_GMONTHDAY%>:
              link.href = "javascript:transferGMonthDay("+dateIteratorDate+")";
              break;
          }
          link.appendChild(document.createTextNode(dateIteratorDate));
          column.appendChild(link);
          if (dateIteratorDate == dateDate && dateIteratorFullYear == dateFullYear)
            column.className = "todaytablecell";
          dateIteratorDate++;
          dateIterator.setTime(Date.parse(engMonths[dateIteratorMonth]+" "+dateIteratorDate+", "+dateIterator.getFullYear()));
        }
        row.appendChild(column);
      }
      calendarBody.appendChild(row);
    }
    var monthSelector = document.getElementById("monthSelector");
    for (var i=0;i<monthSelector.options.length;i++)
    {
      if (monthSelector.options[i].value == dateMonth)
      {
        monthSelector.selectedIndex = i;
        break;
      }
    }
    var yearText = document.getElementById("yearText");
    yearText.value = dateFullYear;
    savedYearTextValue = yearText.value;
<%
   if (calendarType == ActionInputs.CALENDAR_TYPE_DATETIME)
   {
%>
    var timeText = document.getElementById("timeText");
    timeText.value = serializeTime(date.getHours(),date.getMinutes(),date.getSeconds(),date.getMilliseconds());
<%
   }
%>
    currentDate = date;
  }

  function clearCalendar()
  {
    var table = document.getElementById("calendar");
    if (!calendarBodySet)
      initCalendarHeader(table);
    for (var i=1;i<table.rows.length;i++)
    {
      table.deleteRow(i);
      i--;
    }
  }

  function updateCalendar(newMonth,newFullYear)
  {
    var d = new Date();
    if (newMonth > months.length)
    {
      newFullYear++;
      newMonth = 0;
    }
    else if (newMonth < 0)
    {
      newFullYear--;
      newMonth = months.length-1;
    }
    d.setMonth(newMonth);
    d.setFullYear(newFullYear);
<%
   if (calendarType == ActionInputs.CALENDAR_TYPE_DATETIME)
   {
%>
    setDateUsingTimeParameters(getTimeParameters(document.getElementById("timeText").value),d);
<%
   }
%>
    generateCalendar(d);
  }

  function jumpToPreviousYear()
  {
    updateCalendar(currentDate.getMonth(),currentDate.getFullYear()-1)
  }

  function jumpToPreviousMonth()
  {
    updateCalendar(currentDate.getMonth()-1,currentDate.getFullYear());
  }

  function jumpToNextMonth()
  {
    updateCalendar(currentDate.getMonth()+1,currentDate.getFullYear());
  }

  function jumpToNextYear()
  {
    updateCalendar(currentDate.getMonth(),currentDate.getFullYear()+1);
  }

  function jumpToToday()
  {
    generateCalendar(new Date());
  }

  function setNewDate()
  {
    var monthSelector = document.getElementById("monthSelector");
    var newMonth = monthSelector.options[monthSelector.selectedIndex].value;
    var newFullYear = document.getElementById("yearText").value;
<%
   // YYYY+
%>
    var d = new Date();
    if (validateYearString(newFullYear))
    {
      // The year string contains only digits and is hence a valid year.
      d.setFullYear(newFullYear);
      d.setMonth(newMonth);
    }
    generateCalendar(d);
  }

  function handleYearTextKeyUpEvent()
  {
    var currentYearTextValue = document.getElementById("yearText").value;
    if (currentYearTextValue == savedYearTextValue)
      return false;
    setNewDate();
  }

<%
   // YYYY+-MM-DD
%>
  function transferDate(date)
  {
    var fullYear = document.getElementById("yearText").value;
    var month = document.getElementById("monthSelector").selectedIndex+1;
    top.opener.calendarTarget.value = serializeDate(fullYear,month,date);
    top.opener.closeCalendarBrowser();
  }

<%
   // YYYY+-MM-DDTHH:MM:SS[.SSS]Z
%>
  function transferDateTime(date)
  {
    var fullYear = document.getElementById("yearText").value;
    var month = document.getElementById("monthSelector").selectedIndex+1;
    var serializedDate = serializeDate(fullYear,month,date);
    var d = new Date();
    setDateUsingDateParameters(getDateParameters(serializedDate),d);
    var timeValue = document.getElementById("timeText").value;
    var timeParameters = getTimeParameters(timeValue);
    if (timeParameters == null)
    {
      if (!confirm("<%=HTMLUtils.JSMangle(controller.getMessage("MSG_ERROR_INVALID_TIME_FORMAT"))%>"))
        return;
    }
    else
      setDateUsingTimeParameters(timeParameters,d);
    top.opener.calendarTarget.value = serializedDate + "T" + serializeTime(d.getUTCHours(),d.getUTCMinutes(),d.getUTCSeconds(),d.getUTCMilliseconds()) + "Z";
    top.opener.closeCalendarBrowser();
  }
  
<%
   // YYYY+-MM
%>
  function transferGYearMonth()
  {
    var fullYear = document.getElementById("yearText").value;
    var month = document.getElementById("monthSelector").selectedIndex+1;
    var serializedGYearMonth = serializeGYearMonth(fullYear,month);
    var d = new Date();
    setDateUsingGYearMonthParameters(getGYearMonthParameters(serializedGYearMonth),d);
    top.opener.calendarTarget.value = serializedGYearMonth;
    top.opener.closeCalendarBrowser();
  }     
  
<%
   // ---DD
%>
  function transferGDay(date)     
  {
    top.opener.calendarTarget.value = serializeGDay(date);
    top.opener.closeCalendarBrowser();
  }
<%
   // --MM-DD
%>  
  function transferGMonthDay(date)
  {
    var month = document.getElementById("monthSelector").selectedIndex+1;
    top.opener.calendarTarget.value = serializeGMonthDay(month,date);
    top.opener.closeCalendarBrowser();
  }
</script>
</head>
<body class="contentbodymargin">
  <div id="contentborder">
    <table border=0>
      <tr>
        <td align="center" valign="top">
          <table>
            <tr>
              <td align="left" valign="top" class="labels">
                <label for="monthSelector"><%=controller.getMessage("FORM_LABEL_MONTH")%></label>
              </td>
              <td width="20%">&nbsp;</td>
              <td align="left" valign="top" class="labels">
                <label for="yearText"><%=controller.getMessage("FORM_LABEL_YEAR")%></label>
              </td>
            </tr>
            <tr>
              <td align="left" valign="center">
                <select id="monthSelector" onChange="setNewDate()" class="selectlist">
                </select>
              </td>
              <td width="20%">&nbsp;</td>
              <td width="30%" align="left" valign="center">
                <input id="yearText" size=6 onkeyup="handleYearTextKeyUpEvent()" class="tabletextenter">
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td align="center" valign="top">
          <table id="calendar" border=0 cellspacing=0 cellpadding=3 class="tableborder">
          </table>
        </td>
      </tr>
<%
   if (calendarType == ActionInputs.CALENDAR_TYPE_DATETIME)
   {
%>
      <tr>
        <td align="center" valign="top">
          <table border=0>
            <td align="center" valign="center" class="labels">
              <label for="timeText"><%=controller.getMessage("FORM_LABEL_TIME")%></label>
            </td>
            <td align="center" valign="center" width="80%">
              <input id="timeText" type="text" class="tabletextenter" size=12 maxlength=12>
            </td>
            <td width="10%">&nbsp;</td>
          </table>
        </td>
      </tr>
<%
   }
%>
      <tr>
        <td align="center" valign="top">
          <table border=0>
            <tr>
              <td>
                <a href="javascript:jumpToPreviousYear()" title="<%=controller.getMessage("ALT_PREVIOUS_YEAR")%>"><%=controller.getMessage("FORM_LINK_PREVIOUS_YEAR")%></a>
              </td>
              <td>
                <a href="javascript:jumpToPreviousMonth()" title="<%=controller.getMessage("ALT_PREVIOUS_MONTH")%>"><%=controller.getMessage("FORM_LINK_PREVIOUS_MONTH")%></a>
              </td>
              <td>
                <a href="javascript:jumpToToday()" title="<%=controller.getMessage("ALT_TODAY")%>"><%=controller.getMessage("FORM_LINK_TODAY")%></a>
              </td>
              <td>
                <a href="javascript:jumpToNextMonth()" title="<%=controller.getMessage("ALT_NEXT_MONTH")%>"><%=controller.getMessage("FORM_LINK_NEXT_MONTH")%></a>
              </td>
              <td>
                <a href="javascript:jumpToNextYear()" title="<%=controller.getMessage("ALT_NEXT_YEAR")%>"><%=controller.getMessage("FORM_LINK_NEXT_YEAR")%></a>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
<script language="javascript">
  init();
</script>
</body>
</html>
