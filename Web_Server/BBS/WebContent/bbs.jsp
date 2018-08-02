<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.CustomerDAO" %>
<%@ page import="bbs.Customer" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width", initial-scale="1">
<link rel="stylesheet" href = "css/bootstrap.css">
<title>Smart Elevator</title>
<style type="text/css">
	a,a:hover{
		color: #000000;
		text-decoration: none;
	}
</style>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID")!=null)
		{
			userID = (String) session.getAttribute("userID");
		}
		int pageNumber =1; //기본 페이지
		if(request.getParameter("pageNumber")!=null)
		{
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));		
		}
	%>


	<nav class="navbar navbar-default">
		<div class = "navbar-header">
			<button type = "button" class="navbar-toggle collapsed"
				data-toggle ="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">Smart Elevator</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">Main</a></li>
				<li class="active"><a href="bbs.jsp">Customer</a></li>
			</ul>
			<%
				if(userID==null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>
			</ul>
			<%
				}else{
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%
				}
			%>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align: center; border:1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee;text-align:center;">번호</th>
						<th style="background-color: #eeeeee;text-align:center;">이름</th>
						<th style="background-color: #eeeeee;text-align:center;">작성자</th>
						<th style="background-color: #eeeeee;text-align:center;">작성일</th>
					</tr>
				</thead>
				<tbody>
					<%
						CustomerDAO customerDAO = new CustomerDAO();
						ArrayList<Customer> list = customerDAO.getList(pageNumber);
						for(int i=0; i<list.size();i++){
					%>
					<tr>
						<td><%= list.get(i).getCustomerID() %></td>
						<td><a href="view.jsp?customerID=<%= list.get(i).getCustomerID()%>"><%= list.get(i).getCustomerName() %></a></td>
						<td><%= list.get(i).getUserID() %></td>
						<td><%= list.get(i).getCustomerET().substring(0,11)+list.get(i).getCustomerET().substring(11,13)+"시"+list.get(i).getCustomerET().substring(14,16)+"분" %></td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			<%
				if(pageNumber!=1){
			%>
				<a href="bbs.jsp?pageNumber=<%=pageNumber -1 %>" class="btn btn-success btn-arraw-left">다음</a>
			<%
				}if(customerDAO.nextPage(pageNumber+1)){
			%>
				<a href="bbs.jsp?pageNumber=<%=pageNumber +1 %>" class="btn btn-success btn-arraw-left">다음</a>
			<%
				}
			%>
			
			<a href="write.jsp" class="btn btn-primary pull-right">고객추가</a>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>