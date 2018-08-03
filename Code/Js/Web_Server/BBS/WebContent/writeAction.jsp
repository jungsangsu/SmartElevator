<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="bbs.CustomerDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8");%>

<jsp:useBean id="customer" class="bbs.Customer" scope="page"/>
<jsp:setProperty name="customer" property="customerName"/>
<jsp:setProperty name="customer" property="customerPhoneNumber"/>
<jsp:setProperty name="customer" property="customerRoomNumber"/>
<jsp:setProperty name="customer" property="customerPicture"/>
<jsp:setProperty name="customer" property="customerFloor"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Smart Elevator</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") !=null)
		{
			userID = (String) session.getAttribute("userID");
		}
		if(userID==null)
		{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp");
			script.println("</script>");
		}else{
			if(customer.getCustomerName()==null||customer.getCustomerPhoneNumber()==null||customer.getCustomerRoomNumber()==null)
					{
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('입력이 안 된 사항이 있습니다.')");
						script.println("history.back()");
						script.println("</script>");
					}
					else{
						CustomerDAO customerDAO = new CustomerDAO();
						int result = customerDAO.write(customer.getCustomerName(),customer.getCustomerPhoneNumber(),customer.getCustomerRoomNumber()
								,userID, customer.getCustomerPicture(),customer.getCustomerFloor());
						if(result ==-1)
						{
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("alert('글쓰기에 실패했습니다.')");
							script.println("history.back()");
							script.println("</script>");
						}
						else{
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("location.href = 'bbs.jsp'");
							script.println("</script>");
						}
					}
		}
			
	%>
</body>
</html>