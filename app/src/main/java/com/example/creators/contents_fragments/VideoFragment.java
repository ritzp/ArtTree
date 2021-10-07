package com.example.creators.contents_fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.jsp.JspHelper;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

public class VideoFragment extends Fragment {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private long currentPlayerPosition = 0L;

    private String contentsId, extension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contents_fragment_video, container, false);
        Bundle bundle = getArguments();
        contentsId = bundle.getString("contentsId");
        extension = bundle.getString("extension");

        playerView = root.findViewById(R.id.condetailVdo_player);

        initPlayer();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            currentPlayerPosition = player.getCurrentPosition();
        } catch (Exception e) {
            AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
        releasePlayer();
    }

    private void initPlayer() {
        if (player == null) {
            SimpleExoPlayer.Builder builder = new SimpleExoPlayer.Builder(getActivity());
            if (currentPlayerPosition > 0L) {
                builder.setSeekBackIncrementMs(currentPlayerPosition);
            }
            TrackSelector trackSelector = new DefaultTrackSelector(getActivity());
            player = builder.setTrackSelector(trackSelector).build();
            playerView.setPlayer(player);
        }
        try {
            MediaItem mediaItem = new MediaItem.Builder().setUri(Uri.parse(JspHelper.getVideoURL(contentsId, extension))).build();
            DefaultHttpDataSource.Factory factory = new DefaultHttpDataSource.Factory();
            MediaSource source = new ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem);

            player.setMediaSource(source);
        } catch (Exception e) {
            AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }

    private void releasePlayer() {
        try {
            player.release();
            player = null;
        } catch (Exception e) {
            AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }
}