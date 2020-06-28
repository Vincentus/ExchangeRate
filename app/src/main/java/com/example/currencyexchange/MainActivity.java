package com.example.currencyexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Document doc;
private Thread secondThread;
private Runnable runnable;
private ListView listView;
private TextView dateText;
private CustomArrayAdapter adapter;
private String formateDate;
private List<ListItemClass> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }
    private void init() {
        listView= findViewById(R.id.listview);
        arrayList= new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_item1,arrayList,getLayoutInflater());
        listView.setAdapter(adapter);
Date date = Calendar.getInstance().getTime();
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
String formateDate = simpleDateFormat.format(date);
dateText= findViewById(R.id.dateText);
dateText.setText(formateDate);

        runnable= new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };

        secondThread = new Thread(runnable);
        secondThread.start();


    }

    private void getWeb(){

        try {
            doc = Jsoup.connect("https://www.x-rates.com/table/?from=USD&amount=1").get();
            Elements tables = doc.getElementsByTag("tbody");
            Element mainTable = tables.get(0);
            Elements elements =mainTable.children();
            Element euro =elements.get(0);
            Elements euro_elements = euro.children();

            for ( int i =0;i<10;i++){
                ListItemClass item = new ListItemClass();
                item.setData_1(mainTable.children().get(i).child(0).text());
                item.setData_2(mainTable.children().get(i).child(1).text().substring(0,7));
                item.setData_3(mainTable.children().get(i).child(2).text().substring(0,7));
                arrayList.add(item);


            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}