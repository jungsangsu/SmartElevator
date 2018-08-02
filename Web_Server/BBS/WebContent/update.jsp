<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.Customer" %>
<%@ page import="bbs.CustomerDAO" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width", initial-scale="1">
<link rel="stylesheet" href = "css/bootstrap.css">
<title>Smart Elevator</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID")!=null)
		{
			userID = (String) session.getAttribute("userID");
		}
		if(userID==null)
		{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		int customerID =0;
		if(request.getParameter("customerID")!=null)
		{
			customerID = Integer.parseInt(request.getParameter("customerID"));
		}
		if(customerID==0)
		{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		Customer customer = new CustomerDAO().getCustomer(customerID);
		if(!userID.equals(customer.getUserID())){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
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
		</div>
	</nav>
	<div class="container">
		<div class="row">
		<form method="post" action="updateAction.jsp?customerID=<%= customerID %>">
			<table class="table table-striped" style="text-align: center; border:1px solid #dddddd">
				<thead>
					<tr>
						<th colspan ="2" style="background-color: #eeeeee;text-align:center;">고객 정보 수정</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" class="form-control" placeholder="이름" name="customerName" maxlength="50" value="<%= customer.getCustomerName() %>"></td>
					</tr>
					<tr>
						<td><input type="text" class="form-control" placeholder="휴대 전화 번호" name="customerPhoneNumber" maxlength="50" value="<%= customer.getCustomerPhoneNumber() %>"></td>
					</tr>
					<tr>
						<td><input type="text" class="form-control" placeholder="호실 번호" name="customerRoomNumber" maxlength="50" value="<%= customer.getCustomerRoomNumber() %>"></td>
					</tr>
					<tr>
						<td><input type="text" class="form-control" placeholder="사진 경로" name="customerPicture" maxlength="200" value="<%= customer.getCustomerPicture()%>"></td>
					</tr>
					<tr>
						<td><input type="text" class="form-control" placeholder="층" name="customerFloor" maxlength="50" value="<%= customer.getCustomerFloor() %>"></td>
					</tr>				

				</tbody>
				
			</table>
			<input type="submit" class="btn btn-primary pull-right" value="글 수정">
		</form>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>