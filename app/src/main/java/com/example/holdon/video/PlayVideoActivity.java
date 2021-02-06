package com.example.holdon.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.holdon.R;
import com.example.holdon.databinding.ActivityPlayVideoBinding;

import java.util.ArrayList;

public class PlayVideoActivity extends AppCompatActivity {

    private ActivityPlayVideoBinding binding;
    private PlayVideoContentAdapter playVideoContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.playVideoRecyclerView.setLayoutManager(layoutManager);

        playVideoContentAdapter = new PlayVideoContentAdapter(this);
        playVideoContentAdapter.addItem(new PlayVideoContent(PlayVideoContent.TYPE_DESCRIPTION, "제목", "설명"));
        playVideoContentAdapter.addItem(new PlayVideoContent(PlayVideoContent.TYPE_SEPARATOR));
        playVideoContentAdapter.addItem(new PlayVideoContent(PlayVideoContent.TYPE_FOOTER));

        binding.playVideoRecyclerView.setAdapter(playVideoContentAdapter);

        binding.plusBookmarkFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}