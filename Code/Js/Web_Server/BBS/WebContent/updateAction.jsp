<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bbs.CustomerDAO"%>
<%@ page import="bbs.Customer"%>
<%@ page import="java.io.PrintWriter"%>
<%
	request.setCharacterEncoding("UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Smart Elevator</title>
</head>
<body>
	<%
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if (userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp");
			script.println("</script>");
		}
		int customerID = 0;
		if (request.getParameter("customerID") != null) {
			customerID = Integer.parseInt(request.getParameter("customerID"));
		}
		if (customerID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		Customer customer = new CustomerDAO().getCustomer(customerID);
		if (!userID.equals(customer.getUserID())) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		} else {
			if (request.getParameter("customerName") == null || request.getParameter("customerPhoneNumber") == null
					||request.getParameter("customerRoomNumber") == null|| request.getParameter("customerName").equals("")
					|| request.getParameter("customerPhoneNumber").equals("")|| request.getParameter("customerRoomNumber").equals("")) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력이 안 된 사항이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
			} else {
				CustomerDAO customerDAO = new CustomerDAO();
				int result = customerDAO.update(customerID, request.getParameter("customerName"),
						request.getParameter("customerPhoneNumber"),request.getParameter("customerRoomNumber"),request.getParameter("customerPicture")
						,request.getParameter("customerFloor"));
				if (result == -1) {
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글 수정에 실패했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				} else {
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