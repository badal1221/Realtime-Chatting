package com.example.chatt;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryData {
    public static void saveData(String data, Context context){
          try{
              FileOutputStream fileOutputStream=context.openFileOutput("datata.txt",context.MODE_PRIVATE);
              fileOutputStream.write(data.getBytes());
              fileOutputStream.close();
          }catch(IOException e){
              e.printStackTrace();
          }
    }
    public static void saveName(String data, Context context){
        try{
            FileOutputStream fileOutputStream=context.openFileOutput("namee.txt",context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void savelastmsgTs(String data,String chatid, Context context){
        try{
            FileOutputStream fileOutputStream=context.openFileOutput(chatid+".txt",context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public  static  String getData(Context context){
        String data="";
        try{
            FileInputStream fis=context.openFileInput("datata.txt");
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader bufferedReader=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data=sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public  static  String getName(Context context){
        String data="";
        try{
            FileInputStream fis=context.openFileInput("namee.txt");
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader bufferedReader=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data=sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public  static  String getlastmsgTs(Context context,String chatid){
        String data="0";
        try{
            FileInputStream fis=context.openFileInput(chatid+".txt");
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader bufferedReader=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data=sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }
}
