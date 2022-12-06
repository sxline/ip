package com.example.db3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import android.util.Log;

public class FileOper {
    String tablesN;
    int numberOfTables;
    public FileOper()
    {
    }
    public FileOper(String tablesN)
    {
        this.tablesN=tablesN;
    }
    public FileOper(int n,String tablesN)
    {
        this.tablesN=tablesN;
        this.numberOfTables=n;
    }
    public  String readTable(String  tablesN)
    {	String aDataRow = "";
        String aBuffer = "";
        final String rez;
        {// read from ListOfTables
            try {
                //File myFile = new File("/storage/extSdCard/"+tablesN+".txt");
                File myFile = new File("/storage/emulated/ip/lab4/"+tablesN+".txt");
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                //tv.setText(aBuffer);
                myReader.close();
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),
                //Toast.LENGTH_SHORT).show();
                Log.d("ERROR","Eloare la citire TablesM");
            }

        }
        rez=aBuffer;
        return rez;
    }

    public ArrayList<String> readT(String table) {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            String path = "/storage/emulated/ip/lab4/" + table + ".txt";
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                dataList.add(myReader.nextLine());
            }
            myReader.close();
            return dataList;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }


    public int proba()
    {
        int i=3;
        i=i+2;
        return i;
    }
}

