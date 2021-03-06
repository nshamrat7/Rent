package com.example.rent.screen.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rent.adapter.RequestAdapter;
import com.example.rent.pojo.BookingPoJo;
import com.example.rent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements RequestAdapter.RequestClickListener {

    DatabaseReference databaseReference;
    String userId;
    RecyclerView history_rv;
    ArrayList<BookingPoJo>bookingPoJos = new ArrayList<>();
    RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setTitle("Booking History");

        history_rv = findViewById(R.id.history_rv);

        try {
            userId = getIntent().getStringExtra("userId");
        } catch (Exception e) {
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        history_rv.setLayoutManager(layoutManager);
        requestAdapter = new RequestAdapter(this,bookingPoJos,this,1000);
        history_rv.setAdapter(requestAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Booking");

        databaseReference.child("History").orderByChild("bookerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    bookingPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        BookingPoJo bookingPoJo = data.getValue(BookingPoJo.class);
                        bookingPoJos.add(bookingPoJo);
                    }
                    requestAdapter.updateBookinglList(bookingPoJos);
                }
                else {
                    Toast.makeText(HistoryActivity.this, "No history exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HistoryActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(BookingPoJo bookingPoJo) {

    }
}
