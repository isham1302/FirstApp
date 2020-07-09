package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class recycle extends AppCompatActivity {
    RecyclerView recyclerView;
    String s1[], s2[];
    int images[]= {R.drawable.cplusplus,R.drawable.csharp,R.mipmap.logo,R.mipmap.javascript,R.mipmap.python,R.mipmap.ruby,R.mipmap.kotlin,R.mipmap.mysql};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        recyclerView= findViewById(R.id.recyclerView);
        s1= getResources().getStringArray(R.array.ProgrammingLanguages);
        s2= getResources().getStringArray(R.array.description);

        MyAdapter myAdapter= new MyAdapter(this,s1,s2,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}