package com.example.arttree.content_fragments.upload;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arttree.R;
import com.example.arttree.app.AppHelper;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class UploadMusicFragment extends Fragment {

    private PlayerControlView playerView;
    private SimpleExoPlayer player;
    private long currentPlayerPosition = 0L;

    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.upload_fragment_music, container, false);
        Bundle bundle = getArguments();

        uri = Uri.parse(getArguments().getString("uri"));

        playerView = root.findViewById(R.id.upload_music);

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
            player = builder.build();
            playerView.setPlayer(player);
        }
        try {
            MediaItem mediaItem = new MediaItem.Builder().setUri(uri).build();
            DefaultDataSourceFactory factory = new DefaultDataSourceFactory(getActivity());
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