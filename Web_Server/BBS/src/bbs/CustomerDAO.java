package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDAO {
	private Connection conn;
	private ResultSet rs;
	
	public CustomerDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://ssdb.ccijo8xfuwup.ap-northeast-2.rds.amazonaws.com:3306/SSDB?verifyServerCertificate=false&useSSL=true",
					"sangsu", "tkd1029718"); // DataBase와 연결-> RDS 사용하기.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	public int getNext() // 다음 게시글
	{
		String SQL = "SELECT customerID FROM Customer ORDER BY customerID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;// 첫번쨰 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(String customerName,String customerPhoneNumber,String customerRoomNumber
			,String userID, String customerPicture, String customerFloor) {
		String SQL = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, customerName);
			pstmt.setString(3, customerPhoneNumber);
			pstmt.setString(4, customerRoomNumber);
			pstmt.setString(5, userID);
			pstmt.setString(6, getDate());
			pstmt.setString(7, customerPicture);
			pstmt.setString(8, customerFloor);
			pstmt.setInt(9, 1);

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ArrayList<Customer> getList(int pageNumber) {
		String SQL = "SELECT * FROM Customer WHERE customerID < ? AND customerAvailable = 1 ORDER BY customerID DESC LIMIT 10";
		ArrayList<Customer> list = new ArrayList<Customer>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerID(rs.getInt(1));
				customer.setCustomerName(rs.getString(2));
				customer.setCustomerPhoneNumber(rs.getString(3));
				customer.setCustomerRoomNumber(rs.getString(4));
				customer.setUserID(rs.getString(5));
				customer.setCustomerET(rs.getString(6));
				customer.setCustomerPicture(rs.getString(7));
				customer.setCustomerFloor(rs.getString(8));
				customer.setCustomerAvailable(rs.getInt(9));
				list.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM Customer WHERE customerID < ? AND customerAvailable = 1 ORDER BY customerID DESC LIMIT 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Customer getCustomer(int customerID) {
		String SQL = "SELECT * FROM Customer WHERE customerID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, customerID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerID(rs.getInt(1));
				customer.setCustomerName(rs.getString(2));
				customer.setCustomerPhoneNumber(rs.getString(3));
				customer.setCustomerRoomNumber(rs.getString(4));
				customer.setUserID(rs.getString(5));
				customer.setCustomerET(rs.getString(6));
				customer.setCustomerPicture(rs.getString(7));
				customer.setCustomerFloor(rs.getString(8));
				customer.setCustomerAvailable(rs.getInt(9));
				return customer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int customerID,String customerName,String customerPhoneNumber,String customerRoomNumber
			, String customerPicture, String customerFloor) {
		String SQL = "UPDATE Customer SET customerName =?, customerPhoneNumber =?, customerRoomNumber =?"
				+ ",customerPicture =?,customerFloor =? WHERE customerID =?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, customerName);
			pstmt.setString(2, customerPhoneNumber);
			pstmt.setString(3, customerRoomNumber);
			pstmt.setString(4, customerPicture);
			pstmt.setString(5, customerFloor);
			pstmt.setInt(6, customerID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int delete(int customerID) {
		String SQL = " DELETE FROM Customer WHERE customerID = ?;";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, customerID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	
}
