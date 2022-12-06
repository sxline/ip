package com.example.db3;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import com.example.db3.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


@SuppressLint("SdCardPath")
public class MainActivity extends Activity {
    String DBName="lab2";

    public DecimalFormat df = new DecimalFormat("#.##");
    String [] data2=new String[50];
    public String[] sqlprop;
    public String[] wd;
    EditText et1,et2,et3;
    EditText txtData;
    Button btnWriteSDFile;
    Button btnReadSDFile;
    Button btnClearScreen;
    Button btnClose;
    FileOper tabel;
//    SQLiteDatabase: /data/user/0/com.example.db3/databases/CatPrSales
    SQLiteDatabase db;
    TextView tv;
    DBHelper dbHelper;
    String [] tableFields;
    String [][] cc=new String[20][20];
    String [][] cc1=new String[20][20];
    String [] ss, whereCV;
    String [] tablesContent=new String[20];
    String [] tablesStruct=new String[20];
    String [] tabNames=new String[20];
    String tabStruct,tabContent;
    int numberOfTables;
    public final int IPC_ID = 1122;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (EditText) findViewById(R.id.txtData);
        txtData.setHint("Enter some lines of data here...");
        tv = (TextView)findViewById(R.id.textView1);
        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        tv.setMovementMethod(new ScrollingMovementMethod());
        String whereC="  NF > ?  OR PF=? ";
        whereC="  PPrice = ?  OR SQuant > ? ";
        String whereV=" 6; 5; ";
        et1.setText(whereC);
        et2.setText("Editable Values for Restriction(s):  " );
        et3.setText(whereV);
        cc[1][1]="11"; cc[1][2]="12";
        cc1=cc;
        Log.d(" cc=cc1", "cc[1][1]=  " + cc1[1][1] + "cc[1][2]=  " + cc1[1][2]);


    }



    public void createDB(View v)
    {
        dbHelper = new DBHelper(this, DBName);
        db = dbHelper.getWritableDatabase();
        Log.d("Create DB=","The DB " +DBName+ "  was created OR Opened the exiting one!");
        //db.execSQL("DROP TABLE IF EXISTS detbord");
        //db.execSQL("DROP TABLE IF EXISTS bord");
    }
    public void createTAB(View v)
    {
        String aDataRow = "";
        String aBuffer = "";
        String tt;
        int nf;
        String [] fieldsN=new String[10];
        String [] fieldsT=new String[10];
        tabel=new FileOper();
        String tabN="tablesM";
        Log.d("Read tabN=",tabN);
        aBuffer=tabel.readTable(tabN);
        tt=aBuffer;
        aBuffer="";
        String [] tn=tt.toString().split("\n");
        int nt=tn.length;
        numberOfTables=nt;
        Log.d("","N="+String.valueOf(nt)+"  Nume tabel="+tn[0]+",  Nume tabel2="+tn[1]);
        for(int i=0;i<nt;i++) tabNames[i]=tn[i];
        for(int i=0;i<nt;i++)
        {
            boolean te=exists(tn[i]);
            if(!te)
            {
                tabStruct=tabel.readTable(tn[i]+"s");
                tabContent=tabel.readTable(tn[i]);
                String [] tfS=tabStruct.split("\n");// structura
                String [] tfC=tabContent.split("\n");//  Content
                nf=tfS.length;
                for(int j=0;j<nf;j++)
                {
                    String [] fields=tfS[j].split("\t");
                    fieldsN[j]=fields[0];
                    fieldsT[j]=fields[1];
                }
                Log.d("Tabelul : ",tn[i]);
                Log.d("Fields",fieldsN[0] + " , "+ fieldsT[0]+" , "+fieldsN[1]+ " , "+fieldsT[1]);

                // create table i

                boolean  tableExists=false;

                Log.d("before try Table:", tn[i]+ "   to create???   ");
                try
                {
                    // creating a table
                    dbHelper.createT(db, tn[i], fieldsN, fieldsT, nf);
                    tableExists = true;

                    Log.d("Table:", "The  "+ tn[i] + "   was created   ");
                }
                catch (Exception e) {
                    // /* fail */
                    Log.d("Table:", "The table "+ tn[i] + "  was existing, and was not created again   ");
                }
            }

        }


    }
    public boolean exists(String table) {
        Cursor c = null;
        boolean tableExists = false;
        /* get cursor on it */
        try
        {
            c = db.query(table, null,
                    null, null, null, null, null);
            tableExists = true;
            Log.d("About existing ", "The table "+table+"  exists! :))))");
        }
        catch (Exception e) {
            /* fail */
            Log.d("The table is missing", table+" doesn't exist :(((");

        }

        return tableExists;
    }

    public void fillTAB(View v)
    {
        String tabN;
        for(int i=0;i<numberOfTables;i++)
        {
            tabN=tabNames[i];
            boolean te=exists(tabN);

            Log.d("Before if", tabN);
            if(te)
            {  //if the tb exists then fill it
                Log.d("Inside  if", "The table:  "+tabN+ "   Exists");
                //the table exests:  	//clear the table
                db.delete(tabN,null,null);
                Log.d("After delete",tabN);
                //fill the table
                String tt, tabContent;
                int nf;
                tabel=new FileOper();
                tabContent=tabel.readTable(tabN);
                Log.d("After table content",tabN);
                String [] tfC=tabContent.split("\n");//  Content
                int nr=tfC.length;
                String [] fieldsN =tfC[0].split("\t");
                String [] fieldsT =tfC[1].split("\t");
                nf=fieldsN.length;
                // insert rows
                ContentValues cv = new ContentValues();
                int sw;double nnf;
                for (int j=2;j<nr;j++) //on rows nr=tfC.length;
                {
                    cv.clear();
                    if (tabN.equals("detbord"))
                    { String [] rcd=tfC[j].toString().split("\t");
                        nnf =Float.valueOf(rcd[2])*0.15  + Float.valueOf(rcd[3])*0.15
                                + Float.valueOf(rcd[4])*0.1+Float.valueOf(rcd[5])*0.2
                                +Float.valueOf(rcd[6])*0.4;
                        tfC[j]=tfC[j]+"\t"+String.valueOf(df.format(nnf));
                    }
                    String [] rcd=tfC[j].toString().split("\t");
                    for (int k=0;k<nf;k++)// on fields nf=fieldsN.length;
                    {
                        sw=Integer.valueOf(fieldsT[k]);
                        switch (sw)
                        {
                            case 1:
                                cv.put(fieldsN[k], Integer.valueOf(rcd[k].toString()));
                                break;
                            case 2:
                                cv.put(fieldsN[k], rcd[k].toString());
                                break;
                            default:
                                break;
                        }
                    }// end of fields
                    db.insert(tabN, null, cv);


                }  // end of rows
                // show the table content
                Log.d("Datele in tabel", "------" +tabN);
                Cursor cc=null;
                cc = db.query(tabN, null, null, null, null, null, null);
                logCursor(cc);
                cc.close();
                Log.d("Datele in tabel","--- ---");
            }// end of  if(te) (if the tb exists then fill it
        }//  end of for(int i=0;i<numberOfTables;i++)

    }
    public void studL(View v)
    {
        String studLista="SELECT latitude, longitude, COUNT(*) count"+
                "FROM tasks  "
                + " GROUP BY latitude, longitude "
                +  "HAVING count >1 1";

        dbHelper = new DBHelper(this, DBName);
        db = dbHelper.getWritableDatabase();
        Log.d("Create DB=","The DB " +DBName+ "  was created OR Opened the exiting one!");
        Log.d("Group By", "--- INNER JOIN with rawQuery---");

        String sqlQuery = "select PL.name as Name, PS.name as Position, salary as Salary "
                + "from people as PL "
                + "inner join position as PS "
                + "on PL.posid = PS.id "
                + "where salary > ?";
        int v1=7,v2=8;
        String vv1=String.valueOf(v1);
        String vv2=String.valueOf(v2);
        String vv3="prof3", vv4="prof2";

        // String whereV="Ex > " + vv1 + "  and  Re > " + vv2 + " and ( PF=" +vv3 + "  or PF = " + vv4 +")";
        //  String [] s1=et.getText().toString().split("\n");
        // String [] s2=s1[2].split(";");
        String  whereC=et1.getText().toString().trim();
        String  whereV=et3.getText().toString().trim();

        whereV=whereV.replace(" ", "");
        String [] s2=whereV.split(";");
        //vv1=s2[0].trim(); vv2=s2[1].trim(); vv3=s2[2].trim(); vv4=s2[3].trim();
        // String []  val =new String[4];
        int np=s2.length;
        //whereC=s1[0];
        Log.d("Numar de parametri","parametri= "+np);
        Log.d("WhereC=","whereC= "+whereC  );
        Log.d("WhereV=","whereV= "+whereV  );
        // Log.d("WhereC=","whereV= "+whereV+  "  , v1="+s2[0]+" ,  v2="+s2[1]);
        for (int j=0;j<np;j++)
            s2[j]=s2[j].trim();


        String studLista1= "select PR.prid as ProdID, PR.prn AS ProdN,PR.price AS PPrice, SM.quants as SQuant "
                + "from ProductsM as PR "
                + "inner join SalesM as SM "
                + "on SM.prid=PR.prid "
                + "where " +whereC;

        Cursor c;
        c=null;
        // c = db.rawQuery(studLista1, new String[] {"600"});
        c = db.rawQuery(studLista1, s2);
        // c = db.rawQuery(studLista1, null);
        logCursor1(c);
        c.close();
        Log.d("End Lista", "End Lista");
        txtData.setText("Example of Conditions: " +whereC );
        // txtData.append(whereV);

    }


    // afisare in LOG din Cursor
    void logCursor(Cursor c) {
        tv.setText(""); String str2="Results For the Fields : ";String cnn="",coln="";
        if (c != null) {
            if (c.moveToFirst()) {
                String str,str1;
                int klu=0,rr=0;
                do {
                    rr=0;  str = "";str1="";
                    if (klu==0) //  the first record
                    {
                        for (String cn : c.getColumnNames())
                        { //if(rr<6)
                            {//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                                str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
                                cnn=cnn.concat(cn+" ; ");
                                if(rr==2) cnn=cnn+"\n";
                            }

                            rr++;
                        }

                    }
                    // for the next records
                    rr=0;  str = "";str1="";
                    if (klu>0) //  the next records
                    {
                        for (String cn : c.getColumnNames())
                        { if(rr>2)
                        {//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                            str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
                            cnn=cnn.concat(cn+" ; ");

                        }

                            rr++;
                        }

                    }
                    if (klu==0) {str2=str2 + cnn;tv.setText(cnn+"\n");}
                    klu++;
                    rr++;
                    str1=str+"\n";
                    // Log.d(" Rindul=", str);
                    tv.setText(tv.getText()+str1);

                } while (c.moveToNext());

            }
            // txtData.setText(str2 + " with WHERE Conditions : " + "\n");
        } else
            Log.d("Rindul", "Cursor is null");
    }
    // afisare in LOG din Cursor
    void logCursor1(Cursor c) {
        Log.d("COLUMNS NR=","nc="+c.getColumnCount());

        tv.setText(""); String str2="Results For the Fields : ";String cnn="",coln="";
        if (c != null) {
            if (c.moveToFirst()) {
                String str,str1;

                int klu=0,rr=0;
                do {
                    rr=0; str = "";str1="";

                    // for the next records
                    for (String cn : c.getColumnNames())
                    {if(rr>-1)
                    {//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                        str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
                        cnn=cnn.concat(cn+" ; ");

                    }

                        rr++;
                        Log.d("COLUMNS NR=","nc="+c.getColumnCount()+", rr="+rr);
                    }

                    if (klu==0) {str2=str2 + cnn;tv.setText(cnn+"\n");}
                    klu++;
                    rr++;
                    str1=str+"\n";
                    Log.d(" Rindul=", str);
                    tv.setText(tv.getText()+str1);

                } while (c.moveToNext());

            }
            // txtData.setText(str2 + " with WHERE Conditions : " + "\n");
        } else
            Log.d("Rindul", "Cursor is null");
    }
    public void ReadWords(View v)
    {
        String aDataRow = "";
        String aBuffer = "";
        // fn="query4";
        try {
            File myFile = new File("/mnt/sdcard/download1/spinwd.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            //tv.setText(aBuffer);
            myReader.close();
            Toast.makeText(getBaseContext(),"Done reading SD 'wordkey.txt'",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        wd = aBuffer.split("\n");
        Log.d("words=","valoare=" +wd[0]+", "+wd[1]+", "+wd[2]+", "+wd[3]);

        // foll spinner2
        String [] data1 = {wd[0].toString(),wd[1].toString(),wd[2].toString(),wd[3].toString(),
                wd[4].toString(),wd[5].toString(),wd[6].toString(),wd[7].toString(),wd[8].toString(),
                wd[9].toString(),wd[10].toString(),wd[11].toString(),wd[12].toString()
        };
        data2=data1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);

        spinner.setPrompt("Title");

        Log.d("Hello","salut din lista inainte de seletion 2");
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                et1=(EditText)findViewById(R.id.editText1);


                String ss=data2[position];
                et1.setText(ss);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    public void ReadSQL(View v)
    {
        String aDataRow = "";
        String aBuffer = "";
        // fn="query4";
        try {

            //File myFile = new File("/storage/extSdCard/wherec.txt");
            File myFile = new File("/mnt/sdcard/download1/wherec.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            //tv.setText(aBuffer);
            myReader.close();
            Toast.makeText(getBaseContext(),"Done reading SD 'MMMM.txt'",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        sqlprop = aBuffer.split("\n");
        Log.d("SQL=","valoare=" +sqlprop[0]);
        // fill spinner2


    }
    public void lista(View v)
    {

        String [] data1 = {sqlprop[0].toString(),sqlprop[1].toString(),sqlprop[2].toString(),sqlprop[3].toString(),
                sqlprop[4].toString(),sqlprop[5].toString(),sqlprop[6].toString()};

        Log.d("Hello","salut din lista");

        Log.d("data[4]=",data1[4]);
        data2=data1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);

        spinner.setPrompt("Title");

        Log.d("Hello","salut din lista inainte de seletion 2");
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                et1=(EditText)findViewById(R.id.editText1);


                String ss=data2[position];
                et1.setText(ss);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.add("UpDate");
        menu.add("IneqGraphSol");
        menu.add("MovRotate");
        menu.add("Grid");
        menu.add("GraphFun");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            if(item.getTitle()=="Grid")
            {

            }
            return super.onOptionsItemSelected(item);
        }
    }





