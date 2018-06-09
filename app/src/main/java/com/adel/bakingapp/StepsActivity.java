package com.adel.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import junit.framework.Assert;

import org.junit.Test;

import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

public class StepsActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    ImageView videoRes;
    TextView tvDes;
    int DEFAULT_POSITION = -1;
    int recipePosition = -1;
    private int stepPosition = -1;
    private long playerPos = 0;
    private boolean isPlaying = true;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = "ExoPlayer";

    public static final String PLAYBACK_STATE_KEY = "PLAYBACK_STATE_KEY";
    public static final String VIDEO_ID_KEY = "VIDEO_ID_KEY";
    public static final String PLAY_STATE_KEY = "PLAY_STATE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        videoRes = findViewById(R.id.iv_video_res);
        tvDes = findViewById(R.id.tv_Des);
        mPlayerView = findViewById(R.id.playerView);

        Intent intent = getIntent();
        if (intent != null){
            recipePosition = intent.getIntExtra("extra_position_recipe", DEFAULT_POSITION);
        }

        if (savedInstanceState != null){
            stepPosition = savedInstanceState.getInt(VIDEO_ID_KEY);
            boolean isPlaying = savedInstanceState.getBoolean(PLAY_STATE_KEY);
            long currentPos = savedInstanceState.getLong(PLAYBACK_STATE_KEY);
            initializeMediaSession();
            initializePlayer(Uri.parse(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL()));
            mExoPlayer.seekTo(currentPos);
            if (isPlaying){
                mExoPlayer.setPlayWhenReady(true);
            }else {
                mExoPlayer.setPlayWhenReady(false);
            }
            setTitle(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmShortDescription());
            tvDes.setText(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmDescription());
        }else {
            Intent intent1 = getIntent();
            if (intent == null) {
                closeOnError();
            }else {
                stepPosition = intent.getIntExtra("extra_position_step", DEFAULT_POSITION);
            }

            setTitle(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmShortDescription());

            initializeMediaSession();
            if (!listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL().equals("")){
                videoRes.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL()));
            }else {
                mPlayerView.setVisibility(View.GONE);
                videoRes.setVisibility(View.VISIBLE);
            }

            tvDes.setText(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmDescription());
        }
    }

    public void BtnBackClick(View view) {
        int oldValue = stepPosition;
        --stepPosition;
        if (stepPosition < 0){
            stepPosition = listRecipes.get(recipePosition).getmRecipeSteps().size() - 1;

            BackFuncTest(stepPosition+1, stepPosition);
        }else {
            BackFuncTest(oldValue, stepPosition);
        }

        backNextFunc();
    }

    public void BtnNextClick(View view) {
        int oldValue = stepPosition;
        ++stepPosition;
        if (stepPosition == listRecipes.get(recipePosition).getmRecipeSteps().size()){
            stepPosition = 0;

            NextFuncTest(stepPosition-1, stepPosition);
        }else {
            NextFuncTest(oldValue, stepPosition);
        }

        backNextFunc();
    }

    private void backNextFunc(){
        if (mExoPlayer != null){
            releasePlayer();
        }

        setTitle(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmShortDescription());

        if (!listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL().equals("")){
            videoRes.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL()));
        }else {
            mPlayerView.setVisibility(View.GONE);
            videoRes.setVisibility(View.VISIBLE);
        }

        tvDes.setText(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmDescription());
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StepsActivity.this, RecipeDetails.class);
                intent.putExtra("extra_position", recipePosition);
                intent.putExtra("title", listRecipes.get(recipePosition).getmName());
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Test
    public void BackFuncTest(int oldValue, int newValue) {
        Assert.assertTrue("TRUE", oldValue > newValue);
    }

    @Test
    public void NextFuncTest(int oldValue, int newValue) {
        Assert.assertTrue("TRUE", oldValue < newValue);
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(this, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
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

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
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
        mMediaSession.setPlaybackState(mStateBuilder.build());
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
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong(PLAYBACK_STATE_KEY, playerPos);
        savedInstanceState.putInt(VIDEO_ID_KEY, stepPosition);
        savedInstanceState.putBoolean(PLAY_STATE_KEY, isPlaying);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        stepPosition = savedInstanceState.getInt(VIDEO_ID_KEY);
        isPlaying = savedInstanceState.getBoolean(PLAY_STATE_KEY);
        playerPos = savedInstanceState.getLong(PLAYBACK_STATE_KEY);
        initializePlayer(Uri.parse(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL()));
        mExoPlayer.seekTo(playerPos);
        if (isPlaying){
            mExoPlayer.setPlayWhenReady(true);
        }else {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mExoPlayer != null){
            playerPos = mExoPlayer.getCurrentPosition();
            isPlaying = mExoPlayer.getPlayWhenReady();
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initializePlayer(Uri.parse(listRecipes.get(recipePosition).getmRecipeSteps().get(stepPosition).getmVideoURL()));
        mExoPlayer.seekTo(playerPos);
        if (isPlaying){
            mExoPlayer.setPlayWhenReady(true);
        }else {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
