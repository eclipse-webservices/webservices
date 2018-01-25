<%@ page contentType="text/html; charset=UTF-8" import="java.io.*" %>
<jsp:useBean id="echoProxy" class="foo.EchoProxy" scope="request"/>
<html>
<%@ include file = "/outputFilePath.jspf" %>
<%
   // HTTP/POST parameter:
   // testInput - the String argument to call the proxy with.
   String testInput = request.getParameter("testInput");
   
   // Execute the Web service.
   String result = echoProxy.echoString(testInput);
   
   // Write the result to the outputFilePath.
   PrintWriter pw = null;
   try
   {
     pw = new PrintWriter(new FileOutputStream(outputFilePath));
     pw.print(result);
     pw.flush();
   }   
   catch (Exception e)
   {
   }
   finally
   {
     if (pw != null)
       pw.close();
   }
%>
</body>
<p>
testInput: <%=testInput%>
<p>
outputFilePath: <%=outputFilePath%>
<p>
The result is <%=result%>
</body>
</html>