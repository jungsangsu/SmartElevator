package com.example.ice305.server2;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class Elevator_RunningActivity extends AppCompatActivity {    //메인 activity 시작!

    Intent i;
    SpeechRecognizer mRecognizer;
    TextView textView;
    boolean condition = true;
    boolean isEndOfSpeech ;

    private static String TAG = "MainActivity";

    private static final String TAG_JSON="ElevatorInfo"; //제이슨 이름
    private static final String TAG_floor = "floor"; //층수 인자
    private static final String TAG_Contents = "Contents"; //내용 인자

    private TextView mTextViewResult;
    private TextView mTextViewFloor;
    private TextView mTextViewContents;

    String mJsonString;


    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    TextView output1;        //화면구성
    TextView output2;        //화면구성
    TextView output3;        //화면구성
    TextView output4;        //화면구성
    TextView output5;        //화면구성
    TextView output6;        //화면구성
    TextView button_UP;
    TextView button_DOWN;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        층수 관련 가이드정보를 보여주는 선언
         */
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Bold.ttf");    //폰트 지정
        TextView myTextview = (TextView)findViewById(R.id.textView_list_floor);
        TextView myTextview2 = (TextView)findViewById(R.id.textView_list_Contents);     //폰트 사용 textview
        myTextview.setTypeface(myTypeface);
        myTextview2.setTypeface(myTypeface);


        mTextViewResult = (TextView)findViewById(R.id.textView_main_result); //json Text
        mTextViewFloor = (TextView)findViewById(R.id.textView_list_floor);
        mTextViewContents = (TextView)findViewById(R.id.textView_list_Contents);


        /*
        엘리베이터 버튼 ON/OFF 및 해당 층수를 받아온다.
         */
        //start
        output1 = (TextView) findViewById(R.id.output1); // 글자출력칸을 찾는다.
        output2 = (TextView) findViewById(R.id.output2); // 글자출력칸을 찾는다.
        output3 = (TextView) findViewById(R.id.output3); // 글자출력칸을 찾는다.
        output4 = (TextView) findViewById(R.id.output4); // 글자출력칸을 찾는다.
        output5 = (TextView) findViewById(R.id.output5); // 글자출력칸을 찾는다.
        output6 = (TextView) findViewById(R.id.output6); // 글자출력칸을 찾는다.

        output1.setText("1F");
        output2.setText("2F");
        output3.setText("3F");
        output4.setText("4F");
        output5.setText("5F");
        output6.setText("6F");

        output1.setTextColor(Color.BLACK);
        output2.setTextColor(Color.BLACK);
        output3.setTextColor(Color.BLACK);
        output4.setTextColor(Color.BLACK);
        output5.setTextColor(Color.BLACK);
        output6.setTextColor(Color.BLACK);

        mTextViewFloor.setText("");
        mTextViewContents.setText("");

        /*
        음성인식 관련 선언
         */
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        textView = (TextView) findViewById(R.id.textView);

        mRecognizer.startListening(i); //음성인식 리스너 실행.

        GetData task = new GetData();
        task.execute("http://220.67.124.178/php/getjson.php"); //가이드정보 실행.

        Thread worker = new Thread() {    //worker 를 Thread 로 생성
            public void run() { //스레드 실행구문
                try {
                    //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                    socket = new Socket("220.67.124.178", 10000); //소켓생성
                    out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여      //전송한다.
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream())); //데이터 수신시 stream을 받아들인다.
                    out.println("U");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //소켓에서 데이터를 읽어서 화면에 표시한다.
                try {
                    while (true) {
                        data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                        output1.post(new Runnable() {
                            public void run() {

                                if(data.contains("1F")==true)
                                {
                                    output1.setText("1F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output1.setTextColor(Color.RED);
                                    output1.setSelected(!output1.isSelected());
                                    showResult(0);
                                }
                                else{
                                    output1.setText("1F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output1.setTextColor(Color.BLACK);
                                }
                                if(data.contains("2F")==true)
                                {
                                    output2.setText("2F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output2.setTextColor(Color.RED);
                                    output2.setSelected(!output2.isSelected());
                                    showResult(1);
                                }
                                else{
                                    output2.setText("2F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output2.setTextColor(Color.BLACK);
                                }
                                if(data.contains("3F")==true)
                                {
                                    output3.setText("3F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output3.setTextColor(Color.RED);
                                    output3.setSelected(!output3.isSelected());
                                    showResult(2);
                                }
                                else{
                                    output3.setText("3F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output3.setTextColor(Color.BLACK);
                                }
                                if(data.contains("4F")==true)
                                {
                                    output4.setText("4F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output4.setTextColor(Color.RED);
                                    output4.setSelected(!output4.isSelected());
                                    showResult(3);
                                }
                                else{
                                    output4.setText("4F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output4.setTextColor(Color.BLACK);
                                }
                                if(data.contains("5F")==true)
                                {
                                    output5.setText("5F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output5.setTextColor(Color.RED);
                                    output5.setSelected(!output5.isSelected());
                                    showResult(4);
                                }
                                else{
                                    output5.setText("5F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output5.setTextColor(Color.BLACK);
                                }
                                if(data.contains("6F")==true)
                                {
                                    output6.setText("6F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output6.setTextColor(Color.RED);
                                    output6.setSelected(!output6.isSelected());
                                    showResult(5);
                                }
                                else{
                                    output6.setText("6F"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                                    output6.setTextColor(Color.BLACK);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();  //onResume()에서 실행.
    }


    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(Bundle params) {
            condition=true;
            Log.d("onReadyForSpeech","onReadyForSpeech");
        }
        @Override
        public void onBeginningOfSpeech() {
            Log.d("onBeginningOfSpeech","onBeginningOfSpeech");
        }
        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d("onRmsChanged","onRmsChanged");
        }
        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("onBufferReceived","onBufferReceived");
        }
        @Override
        public void onEndOfSpeech() {
            Log.d("onEndOfSpeech","onEndOfSpeech");
            isEndOfSpeech = true;
        }
        @Override
        public void onError(int error) {
            String er = new String();
            er.valueOf(error);
            Log.d("onError","onError");
            if (!isEndOfSpeech) return;
            if(condition)
            {
                mRecognizer.startListening(i);
            }
        }
        @Override
        public void onResults(Bundle results) {
            ///
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            textView.setText("" + rs[0]);
            if(rs[0].contains("1층"))
            {
                if(output1.getCurrentTextColor()==Color.BLACK){
                    output1.setSelected(!output1.isSelected());
                    output1.setTextColor(Color.RED);
                    showResult(0);
                    out.println("1");
                }
            }
            else if(rs[0].contains("2층"))
            {
                if(output2.getCurrentTextColor()==Color.BLACK){
                    output2.setSelected(!output2.isSelected());
                    output2.setTextColor(Color.RED);
                    showResult(1);
                    out.println("2");
                }
            }
            else if(rs[0].contains("3층"))
            {
                if(output3.getCurrentTextColor()==Color.BLACK){
                    output3.setSelected(!output3.isSelected());
                    output3.setTextColor(Color.RED);
                    showResult(2);
                    out.println("3");
                }
            }
            else if(rs[0].contains("4층"))
            {
                if(output4.getCurrentTextColor()==Color.BLACK){
                    output4.setSelected(!output4.isSelected());
                    output4.setTextColor(Color.RED);
                    showResult(3);
                    out.println("4");
                }
            }
            else if(rs[0].contains("5층"))
            {
                if(output5.getCurrentTextColor()==Color.BLACK){
                    output5.setSelected(!output5.isSelected());
                    output5.setTextColor(Color.RED);
                    showResult(4);
                    out.println("5");
                }
            }
            else if(rs[0].contains("6층"))
            {
                if(output6.getCurrentTextColor()==Color.BLACK){
                    output6.setSelected(!output6.isSelected());
                    output6.setTextColor(Color.RED);
                    showResult(5);
                    out.println("6");
                }
            }
            mRecognizer.startListening(i);

        }
        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d("onPartialResults","onPartialResults");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d("onEvent","onEvent");
        }
    };

    public void btn_bottonUp(View v) //올라가기 버튼
    {
        output1.setTextColor(Color.BLACK);
        output2.setTextColor(Color.BLACK);
        output3.setTextColor(Color.BLACK);
        output4.setTextColor(Color.BLACK);
        output5.setTextColor(Color.BLACK);
        output6.setTextColor(Color.BLACK);
        out.println("U");
    }
    public void btn_bottonClose(View v)
    {

        output1.setTextColor(Color.BLACK);
        output2.setTextColor(Color.BLACK);
        output3.setTextColor(Color.BLACK);
        output4.setTextColor(Color.BLACK);
        output5.setTextColor(Color.BLACK);
        output6.setTextColor(Color.BLACK);
        out.println("C");
        finish();
    }
    public void btn_botton1(View v)
    {
        output1.setSelected(!output1.isSelected());
        if(output1.getCurrentTextColor()==Color.RED){
            output1.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output1.setTextColor(Color.RED);
            showResult(0);
        }
        out.println("1");

    }
    public void btn_botton2(View v)
    {
        output2.setSelected(!output2.isSelected());
        if(output2.getCurrentTextColor()==Color.RED){
            output2.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output2.setTextColor(Color.RED);
            showResult(1);
        }
        out.println("2");

    }
    public void btn_botton3(View v)
    {
        output3.setSelected(!output3.isSelected());
        if(output3.getCurrentTextColor()==Color.RED){
            output3.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output3.setTextColor(Color.RED);
            showResult(2);
        }
        out.println("3");
    }
    public void btn_botton4(View v)
    {
        output4.setSelected(!output4.isSelected());
        if(output4.getCurrentTextColor()==Color.RED){
            output4.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output4.setTextColor(Color.RED);
            showResult(3);
        }
        out.println("4");
    }
    public void btn_botton5(View v)
    {
        output5.setSelected(!output5.isSelected());
        if(output5.getCurrentTextColor()==Color.RED){
            output5.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output5.setTextColor(Color.RED);
            showResult(4);
        }
        out.println("5");
    }
    public void btn_botton6(View v)
    {
        output6.setSelected(!output6.isSelected());
        if(output6.getCurrentTextColor()==Color.RED){
            output6.setTextColor(Color.BLACK);
            showResult(9999);
        }
        else{
            output6.setTextColor(Color.RED);
            showResult(5);
        }
        out.println("6");
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            mRecognizer.destroy();
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null; //에러 스트링

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Elevator_RunningActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // mTextViewResult.setText(result); //제이슨 텍스트에!!
            Log.d(TAG, "response  - " + result);

            if (result == null) {
                // mTextViewResult.setText(errorString);
            } else {
                mJsonString = result;
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }

    }

    private void showResult(int number){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString); //제이슨 텍스트를 가지고온다.
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON); //제이슨 ElevatorInfo

            if(number!=9999)
            {
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    String id = item.getString(TAG_floor);
                    String name = item.getString(TAG_Contents);

                    if(i==number)
                    {
                        mTextViewFloor.setText(id+"층");
                        mTextViewContents.setText(name);
                    }
                }
            }
            else {
                mTextViewFloor.setText("");
                mTextViewContents.setText("");
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}