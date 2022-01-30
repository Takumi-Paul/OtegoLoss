package com.example.otegoloss.shipping;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class uploadImage {

    byte[] byteArray;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    public void upload(Bitmap bitmap, URL _url, String id) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();

        HttpURLConnection con = (HttpURLConnection)_url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
        con.setRequestProperty("Accept-Charset", "UTF-8");
        con.setUseCaches(false);

        DataOutputStream  outputStream = new DataOutputStream(con.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        // ファイル名の送信
        outputStream.writeBytes("Content-Disposition: form-data; name=\"filename\";" + lineEnd);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(id);
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        // データの送信
        outputStream.writeBytes("Content-Disposition: form-data; name=\"upfile\";filename=\"upfile.jpg\"" + lineEnd);
        outputStream.writeBytes(lineEnd);
        for(int i =  0 ; i < byteArray.length;i++){
            outputStream.writeByte(byteArray[i]);
        }
        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        con.connect();

        InputStream in = con.getInputStream();
        InputStreamReader objReader = new InputStreamReader(in);
        BufferedReader objBuf = new BufferedReader(objReader);
        StringBuilder objStr = new StringBuilder();
        String sLine;
        while ((sLine = objBuf.readLine()) != null) {
            objStr.append(sLine);
        }
        objStr.toString();//返り値
         System.out.println(objStr.toString());
        in.close();
        objBuf.close();
    }


}
