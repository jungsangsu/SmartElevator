package sang;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class update extends javax.swing.JFrame {
	
	static Connection conn = null; // DataBase와 연결을 위한 참조변수 선언.
	static Statement stmt = null; // DataBase에 Query 이용 변수.
	static ResultSet rs = null; // DataBase에 Query 이용 변수.
	static int r = 0;
	
    public update() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocation(600, 300);
        setTitle("수정");
        jLabel1.setFont(new java.awt.Font("굴림", 1, 18));
        jLabel1.setText("Search Data");
        jLabel2.setFont(new java.awt.Font("굴림", 1, 12));
        jLabel2.setText("수정할 이름, 호실을 입력하세요.");
        jLabel3.setFont(new java.awt.Font("굴림", 1, 12));
        jLabel3.setText("이름");
        jLabel4.setFont(new java.awt.Font("굴림", 1, 12));
        jLabel4.setText("호실");
        jButton1.setText("확인");
        
        
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
            	String totalList = new String("<html>");
            	
            	String name = String.valueOf(jTextField1.getText()).toLowerCase();
            	String roomNumber = String.valueOf(jTextField2.getText());
            	boolean condition = false;
            	try {
    				Class.forName("com.mysql.jdbc.Driver");
    				conn = DriverManager.getConnection(
    						"jdbc:mysql://ssdb.ccijo8xfuwup.ap-northeast-2.rds.amazonaws.com:3306/SSDB?verifyServerCertificate=false&useSSL=true",
    						"username", "password"); // DataBase와 연결-> RDS 사용하기.
    				
    				if(jTextField1.getText().isEmpty()||jTextField2.getText().isEmpty()) //사용자 이름정보를 입력안하고 사진을 찍으려고 할떄!
					{
							JOptionPane.showMessageDialog(null, "사용자 정보를 먼저 작성해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
					}
    				else{
        				stmt = (Statement) conn.createStatement();
        				rs = stmt.executeQuery("select * from info where  name  like '%" +  name+roomNumber + "%' ");
        				String name2 = new String();
        				String phoneNumber = new String();
        				String roomNumber2 = new String();
        				String EntranceTime = new String();
        				String ExitTime = new String();
        				String picture = new String();
        				char level;
        				
        				if (rs == null)
        					System.out.println("No such content found.");
        				while (rs.next()) {
        					name2 = rs.getString("name");
        					phoneNumber = rs.getString("phoneNumber");
        					roomNumber2 = rs.getString("roomNumber");
        					EntranceTime = rs.getString("EntranceTime");
        					ExitTime = rs.getString("ExitTime");
        					picture = rs.getString("picture");
        					level = roomNumber.charAt(0);
        					
        					totalList += (name2+"&nbsp;&nbsp;&nbsp;&nbsp;"+phoneNumber+"&nbsp;&nbsp;&nbsp;&nbsp;"+roomNumber2+"&nbsp;&nbsp;&nbsp;&nbsp;"+EntranceTime+"&nbsp;&nbsp;&nbsp;&nbsp;"+ExitTime+"<br/>");
        				}
        				totalList+="</html>";
        				
        				if(totalList.compareTo("<html></html>")==0)
        				{
        					JOptionPane.showMessageDialog(null, "수정할 정보가 없습니다", "알림", JOptionPane.WARNING_MESSAGE);
        				}
        				else{
        					new updateNextScen(name2+"/"+phoneNumber+"/"+roomNumber2+"/"+EntranceTime+"/"+ExitTime).setVisible(true);
        				}
    				}
    				
    			} catch (ClassNotFoundException cnfe) {
    				System.out.println("The class could not be found." + cnfe.getMessage());
    			} catch (SQLException se) {
    				System.out.println(se.getMessage());
    			}
    			try {
    				conn.close();
    			} catch (SQLException e2) {
    				System.out.println(e2.getMessage());
    			}
    			
            }
        });
        
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(136, 136, 136))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(38, 38, 38))
        );
        pack();
    }                       
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(delete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(delete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(delete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(delete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new delete().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration                   
}