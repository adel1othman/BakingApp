package com.adel.bakingapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

public class RecipeDetailsDesFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String PLAYBACK_STATE_KEY_TAB = "PLAYBACK_STATE_KEY_TAB";
    public static final String VIDEO_ID_KEY_TAB = "VIDEO_ID_KEY_TAB";
    public static final String PLAY_STATE_KEY_TAB = "PLAY_STATE_KEY_TAB";

    View rootView;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSessionTab;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = "ExoPlayer";
    private int stepPosition = 0;
    private long playerPos;
    private boolean isPlaying;

    private Context context;
    private String STATE_KEY = "STATE_KEY";

    public static RecipeDetailsDesFragment newInstance(int currentStep) {
        RecipeDetailsDesFragment myFragment = new RecipeDetailsDesFragment();

        Bundle args = new Bundle();
        args.putInt("currentStep", currentStep);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details_des, container, false);

        context = getContext();

        initializeMediaSession();

        TextView tvDes = rootView.findViewById(R.id.tv_step_instruction);
        ImageView videoRes = rootView.findViewById(R.id.iv_video_res_tab);
        mPlayerView = rootView.findViewById(R.id.playerViewDetails);

        if (!listRecipes.get(stepPos).getmRecipeSteps().get(getArguments().getInt("currentStep")).getmVideoURL().equals("")){
            videoRes.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(listRecipes.get(stepPos).getmRecipeSteps().get(getArguments().getInt("currentStep")).getmVideoURL()));
        }else {
            mPlayerView.setVisibility(View.GONE);
            videoRes.setVisibility(View.VISIBLE);
        }

        tvDes.setText(listRecipes.get(stepPos).getmRecipeSteps().get(getArguments().getInt("currentStep")).getmDescription());

        stepPosition = getArguments().getInt("currentStep");

        return rootView;
    }

    private void initializeMediaSession() {
        mMediaSessionTab = new MediaSessionCompat(context, TAG);
        mMediaSessionTab.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSessionTab.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSessionTab.setPlaybackState(mStateBuilder.build());

        mMediaSessionTab.setCallback(new MySessionCallback());

        mMediaSessionTab.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(context, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSessionTab.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSessionTab, intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong(PLAYBACK_STATE_KEY_TAB, playerPos);
        savedInstanceState.putInt(VIDEO_ID_KEY_TAB, stepPosition);
        savedInstanceState.putBoolean(PLAY_STATE_KEY_TAB, isPlaying);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null){
            stepPosition = savedInstanceState.getInt(VIDEO_ID_KEY_TAB);
            boolean isPlaying = savedInstanceState.getBoolean(PLAY_STATE_KEY_TAB);
            long currentPos = savedInstanceState.getLong(PLAYBACK_STATE_KEY_TAB);
            initializePlayer(Uri.parse(listRecipes.get(stepPos).getmRecipeSteps().get(stepPosition).getmVideoURL()));
            mExoPlayer.seekTo(currentPos);
            if (isPlaying){
                mExoPlayer.setPlayWhenReady(true);
            }else {
                mExoPlayer.setPlayWhenReady(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mExoPlayer != null){
            playerPos = mExoPlayer.getCurrentPosition();
            isPlaying = mExoPlayer.getPlayWhenReady();
            releasePlayer();
            mMediaSessionTab.setActive(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mExoPlayer != null){
            playerPos = mExoPlayer.getCurrentPosition();
            isPlaying = mExoPlayer.getPlayWhenReady();
            releasePlayer();
            mMediaSessionTab.setActive(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}