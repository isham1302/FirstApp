package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class horizontal extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);

        recyclerView= findViewById(R.id.recycler_view);
        Integer[] langLogo= {R.drawable.cplusplus,R.drawable.csharp,R.mipmap.logo,R.mipmap.javascript,R.mipmap.kotlin,R.mipmap.ruby,R.mipmap.python,R.mipmap.mysql};
        String[] langName={"C++","C#","Java","Javascript","Kotlin","Ruby","Python","Mysql"};

        mainModels= new ArrayList<>();
        for (int i=0; i<langLogo.length;i++){
            MainModel model= new MainModel(langLogo[i],langName[i]);
            mainModels.add(model);
        }
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(horizontal.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator() );
        mainAdapter= new MainAdapter(horizontal.this,mainModels);
        recyclerView.setAdapter(mainAdapter);
    }
}