package sang;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class server extends JPanel {
	BufferedImage image; // 이미지를 저장할 이미지 버퍼.
	static Connection conn = null; // DataBase와 연결을 위한 참조변수 선언.
	static Statement stmt = null; // DataBase에 Query 이용 변수.
	static ResultSet rs = null; // DataBase에 Query 이용 변수.

	public static void main(String[] args) throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://ssdb.ccijo8xfuwup.ap-northeast-2.rds.amazonaws.com:3306/SSDB?verifyServerCertificate=false&useSSL=true",
					"username", "password"); // DataBase와 연결-> RDS 사용하기.
			System.out.println("=====================================================================================================================================================================================");
			ShowData(); // 데이터 베이스에 저장되어 있는 사용자 정보를 보여준다.
			System.out.println("=====================================================================================================================================================================================\n\n");
			
			ServerSocket serverARDUINO = new ServerSocket(10001);
			System.out.println("<  엘리베이터 작동부분[아두이노] 연결 대기중   >");
			Socket sockARDUINO = serverARDUINO.accept();
			InetAddress inetaddrARDUINO = sockARDUINO.getInetAddress();
			System.out.println("< 엘리베이터 작동[아두이노] 연결 > IP :" + inetaddrARDUINO.getHostAddress() + "\n\n");
			/*
			 * 아두이노 접속하기 위한 스트림 설정.
			 */
			OutputStream outARDUINO = sockARDUINO.getOutputStream();
			PrintWriter pwARDUINO = new PrintWriter(new OutputStreamWriter(outARDUINO));

			while (true) {
				
				ServerSocket serverANDROID = new ServerSocket(10000);
				System.out.println("<  엘리베이터 작동부분[안드로이드] 연결 대기중   >");
				Socket sockANDROID = serverANDROID.accept();
				InetAddress inetaddrANDROID = sockANDROID.getInetAddress();
				System.out.println("< 엘리베이터 작동[안드로이드] 연결 > IP :" + inetaddrANDROID.getHostAddress() + "\n\n");
				/*
				 * 안드로이드 접속하기 위한 스트림 설정.
				 */
				OutputStream outANDROID = sockANDROID.getOutputStream();
				PrintWriter pwANDROID = new PrintWriter(new OutputStreamWriter(outANDROID));
				BufferedReader inANDROID = new BufferedReader(new InputStreamReader(sockANDROID.getInputStream()));

				
				String tranString = new String();
				ArrayList<Integer> array = new ArrayList<Integer>(); // 유사한 사람의 갯수 리스트.			
				
				String inputButton = new String(); // 이미지파일에 이름을 받는다.
				try {

					while ((inputButton = inANDROID.readLine()) != null) {
						
						if(inputButton.compareTo("U")==0)
						{
							tranString = new String(); // 초기화.
							array = new ArrayList<Integer>(); // 초기화.

							System.out.println("안드로이드부터 받은 버튼 : " + inputButton);

							String tempImage = new String();
							while (tempImage.isEmpty() == true) {
								tempImage = Take_Picture(); // 사진 한장을 찍는다. 저장된 파일 경로를 리턴함
							}

							// 전체 이미지를 -> 얼굴이미지만
							int FaceNumber = pictureTOface("C:\\Users\\ice305\\Desktop\\server\\");

							System.out.println("엘리베이터에서 찍힌 사진중 사람얼굴 수 : " + FaceNumber);	

							for (int i = 0; i < FaceNumber; i++) {
								String tempString = new String();
								tempString = SelectElevatorFloor(
										"C:\\Users\\ice305\\Desktop\\server\\detectingFace" + (i + 1) + ".png");
								System.out.println("인식된 "+(i + 1) + "번째 사람 층수  : " + tempString);
								if (!tempString.isEmpty()) // 반환 값이 null 아닐때 : 유사한 사람이 있을때!!!
								{
									tranString += (tempString + "F"); // 안드로이드에게 보내주기 위한 스트링.
									int tempNumber = Integer.parseInt(tempString);
									array.add(tempNumber);
								}
							}
							if (array.size() == 0) // 인식된 얼굴이 하나도 없을때.
							{
								continue;
							} else {
								pwANDROID.println(tranString);
								pwANDROID.flush();
								System.out.println("---------------------------------------------------\n\n\n");
							}
						}
						else if(inputButton.compareTo("C")==0)
						{
							int transArray[] = convertIntegers(array); // 솔트를 하기 위해서 배열 변환.
							java.util.Arrays.sort(transArray); // 순서대로 전송하기 위해 정렬한다.
							for (int i = 0; i < transArray.length; i++) {
								pwARDUINO.println(transArray[i]);
								System.out.println("아두이노 보내는 수 : " + transArray[i]);
								pwARDUINO.flush();
							}
							tranString = new String(); //보내면서 초기화.
							array = new ArrayList<Integer>(); //보내면서 초기화.
							
						}
						else
						{
							int temp = Integer.parseInt(inputButton);
							if(array.contains(temp))
							{
								for (int i = 0; i < array.size(); i++) {
									if(array.get(i)==temp)
									{
										array.remove(i);
									}
								}
								System.out.println(array.toString());
							}
							else{
								array.add(temp);
								System.out.println(array.toString());
							}
						}

					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				sockANDROID.close();
				// ois.close();
				serverANDROID.close();
				System.out.println("< 엘리베이터 안드로이드 종료 >");
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("The class could not be found." + cnfe.getMessage());
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public server() {
	}

	public server(BufferedImage img) {
		image = img;
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public static void ShowData() throws SQLException {
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery("select * from info");
		if (rs == null)
			System.out.println("No such content found.");
		System.out.println("name   \t\tphoneNumber\troomNumber\tEntranceTime\t\t\t\tExitTime\t\t\tpicture");
		while (rs.next()) {
			String name = rs.getString("name");
			String phoneNumber = rs.getString("phoneNumber");
			String roomNumber = rs.getString("roomNumber");
			String EntranceTime = rs.getString("EntranceTime");
			String ExitTime = rs.getString("ExitTime");
			String picture = rs.getString("picture");
			System.out.printf("%s\t%s\t%s\t\t%s\t\t%s\t%s", name, phoneNumber, roomNumber, EntranceTime, ExitTime,
					picture);
			System.out.println("");
		}
	}

	public static String SelectElevatorFloor(String address) throws SQLException { //디비에서 해당 사진에 대한 층수를 반환.
		String floor = new String();
		int result = 0;
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery("select * from info");
		if (rs == null)
			System.out.println("No such content found.");

		while (rs.next()) {
			String name = rs.getString("name");
			int tempNumber = ComparePicture(address, "C:\\Users\\ice305\\Desktop\\Customerphotos\\" + name + ".png");
			if (tempNumber > result) {
				result = tempNumber; // 가장 유사율이 큰것을 찾아야된다.
				floor = rs.getString("level"); // 가장 유사율이 큰것의 층수(이름)을 추출.
			}
		}
		System.out.println("");

		return floor;
	}

	public static String Take_Picture() { // 사진찍고 폴더에 저장.
		String result = new String(); // 사진파일을 리턴!!
		System.loadLibrary("opencv_java2412"); // 라이브러리 로드.
		server t = new server(); // 캡쳐이미지 객체 생성해준다.
		VideoCapture camera = new VideoCapture(0); // 비디오를 캡쳐할 카메라 객체를 생성해준다.
		Mat frame = new Mat(); // 행렬을 생성해준다
		if (!camera.isOpened()) { // 카메라가 닫혀있을때.
			System.out.println("카메라를 열수 없습니다.");
		} else {
			while (true) {
				if (camera.read(frame)) { // 카메라에서 이미지 데이터를 행렬단위로 읽어 성공하면.
					BufferedImage image = t.MatToBufferedImage(frame); // 행렬이미지
																		// 데이터를
																		// 이미지
																		// 데이터로.
					t.grayscale(image);
					t.saveImage(image);
					result = "C:\\\\\\\\Users\\\\\\\\ice305\\\\\\\\Desktop\\\\\\\\server\\\\\\\\PicturesTakenFromElevator.png";
					break;
				}
			}
		}
		camera.release();
		return result; // 사진파일의 경로를 리턴한다.
	}

	public static int pictureTOface(String address) { //전체사진을 얼굴사진만 추출해서 저장
		int detectingFace = 0; // 사진에서 얼굴이 몇개 잡혔는지 정수값.
		System.loadLibrary("opencv_java2412"); // 라이브러리 로드.
		CascadeClassifier faceDetector = new CascadeClassifier(
				"C:\\Users\\ice305\\Downloads\\opencv\\build\\share\\OpenCV\\lbpcascades\\lbpcascade_frontalface.xml");
		int imageNumber = 0;
		while (true) {
			Mat Mat_image = Highgui.imread(address + "\\PicturesTakenFromElevator.png");
			// 이미지에서 얼굴검출
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(Mat_image, faceDetections);
			detectingFace = faceDetections.toArray().length;
			Mat temp = new Mat();
			// 그림그려준다 네모박스!!
			for (Rect rect : faceDetections.toArray()) {
				temp = new Mat(Mat_image, rect);
				imageNumber++; // 디텍딩 갯수만큼 저장하기 위해서. -> 카운터에서는 필요없다.
				String filename = "detectingFace" + imageNumber + ".png";
				Highgui.imwrite("C:\\Users\\ice305\\Desktop\\server\\" + filename, temp);
			}
			break;
		}
		return detectingFace; // 검출된 얼굴의 수를 리턴한다.
	}

	// 이미지를 윈도우상에 보여준다.
	public void window(BufferedImage img, String text, int x, int y) {
		JFrame frame0 = new JFrame();
		frame0.getContentPane().add(new server(img));
		frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame0.setTitle(text); // tool action bar 이름 정해주는거.
		frame0.setSize(img.getWidth(), img.getHeight() + 30); // tool 크기.
		frame0.setLocation(x, y);// 윈도우상에 어디에 위치해주는지.
		frame0.setVisible(true); // false 해주면 윈도우상에 안나온다.
	}

	// Load an image 이미지를 로드한다.
	public BufferedImage loadImage(String file) {
		BufferedImage img;
		try {
			File input = new File(file);
			img = ImageIO.read(input);
			return img;
		} catch (Exception e) {
			System.out.println("erro");
		}

		return null;
	}

	// 이미지 데이터를 window에 저장한다.
	public void saveImage(BufferedImage img) {
		try {
			File outputfile = new File("C:\\Users\\ice305\\Desktop\\server\\PicturesTakenFromElevator.png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	// 회색필터 적용.
	public BufferedImage grayscale(BufferedImage img) {
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));

				int red = (int) (c.getRed() * 0.299);
				int green = (int) (c.getGreen() * 0.587);
				int blue = (int) (c.getBlue() * 0.114);

				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);

				img.setRGB(j, i, newColor.getRGB());
			}
		}

		return img;
	}

	// 행렬데이터를 받아서 이미지 데이터형식으로 바꿔주는 함수.
	public BufferedImage MatToBufferedImage(Mat frame) {
		// Mat() to BufferedImage
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);

		return image;
	}

	public static int ComparePicture(String address1, String address2) { //얼굴 이미지 비교분석
		int resultRatio = 0;

		System.loadLibrary("opencv_java2412");

		String picture = address1; // 비교대상
		String basePicture = address2; // 비교기준

		// System.out.println("\n\n\n\n------------프로그램 시작-------------");
		// System.out.println("1. 이미지 로드실행");

		Mat objectPictureImage = Highgui.imread(picture, Highgui.CV_LOAD_IMAGE_COLOR); // 이미지를
																						// 행렬로
																						// 만든다.
		Mat objectBasePictureImage = Highgui.imread(basePicture, Highgui.CV_LOAD_IMAGE_COLOR); // 이미지를
																								// 행렬로
																								// 만든다.

		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint(); // 행렬에 키포인트를 저장할 객체
																// 생성.
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);// 특징점
																						// 알고리즘
																						// 객체생성.

		// System.out.println("2. 비교대상 이미지 키포인트 감지(행렬이용)"); //키포인트 감지 실시!!
		featureDetector.detect(objectPictureImage, objectKeyPoints);

		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		descriptorExtractor.compute(objectPictureImage, objectKeyPoints, objectDescriptors);

		// 비교이미지와 기준이미지를 비교 실시.
		MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
		// System.out.println("4. 기준 이미지의 주요 지점 감지");
		featureDetector.detect(objectBasePictureImage, sceneKeyPoints);
		// System.out.println("5. 기준 이미지의 설명자 계산");
		descriptorExtractor.compute(objectBasePictureImage, sceneKeyPoints, sceneDescriptors);

		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
		// System.out.println("6. 비교대상 및 기준 이미지 매칭시키기");
		descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

		// System.out.println("7. 매칭 계산");
		LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

		float nndrRatio = 0.7f;

		for (int i = 0; i < matches.size(); i++) {
			MatOfDMatch matofDMatch = matches.get(i);
			DMatch[] dmatcharray = matofDMatch.toArray();
			DMatch m1 = dmatcharray[0];
			DMatch m2 = dmatcharray[1];

			if (m1.distance <= m2.distance * nndrRatio) {
				goodMatchesList.addLast(m1);
			}
		}
		System.out.println("유사율 : " + goodMatchesList.size()); // 얼마나 비슷한것이 있는지
																// 출력!!

		if (goodMatchesList.size() >= 10) {
			// System.out.println(" (동일인물)Object Found");
			resultRatio = goodMatchesList.size();
			// Mat img = Highgui.imread(basePicture,
			// Highgui.CV_LOAD_IMAGE_COLOR);
			// System.out.println("8. 이미지와 일치하는 그림 그리기");
			// MatOfDMatch goodMatches = new MatOfDMatch();
			// goodMatches.fromList(goodMatchesList);
			//
			// Highgui.imwrite("C:\\Users\\rtr45\\Desktop\\image\\sang5.png",
			// img);
		} else {
			// System.out.println("다른인물(Object Not Found)");
		}
		// System.out.println("-------------프로그램 종료-------------");

		return resultRatio;
	}
}