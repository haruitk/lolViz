<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*" %>
<%@page import="twitter4j.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lol Viz</title>
</head>
<body>

<% 
Map<String, Map<String, List<Status>>> tweetsByChampion = (Map<String, Map<String, List<Status>>>)request.getAttribute("TWEETS_BY_CHAMPION"); 

for (String champion : tweetsByChampion.keySet()) {
	Map<String, List<Status>> resultByDate  = (Map<String, List<Status>>)tweetsByChampion.get(champion);
	 out.println ("----FOR----: "+ champion);
for (String date : resultByDate.keySet()) {
	 out.println ("KEY: " + date + " COUNT : "	+ resultByDate.get(date).size());
}
}

%>

</body>
</html>