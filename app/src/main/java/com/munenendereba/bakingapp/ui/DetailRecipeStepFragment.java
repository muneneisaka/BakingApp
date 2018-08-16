package com.munenendereba.bakingapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.models.RecipeStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailRecipeStepFragment extends Fragment {

    private OnDetailRecipeStepInteractionListener mListener;
    private TextView textViewDescription;
    private int numOfSteps;
    private int positionOfStep;
    private ArrayList<RecipeStep> recipeSteps = new ArrayList<>();
    private Button buttonNext, buttonPrevious;
    private String recipeVideoToPlay;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ImageView imageViewNoThumbnail;

    private View.OnClickListener OnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            //if the button pressed is Next, add 1 to position, else subtract 1
            if (v.getId() == R.id.buttonNext)
                onButtonPressed(positionOfStep + 1);
            else onButtonPressed(positionOfStep - 1);
        }
    };
    private long playerPosition;

    public DetailRecipeStepFragment() {
        // Required empty public constructor
    }

    boolean isPlayWhenReady;

    //learnt how to enter full screen with exoplayer using this resource https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
    private void openFullscreenDialog() {
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        Dialog mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mFullScreenDialog.addContentView(simpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    //check the position of steps and hide "Next" and "Previous" button
    private void checkPositionOfStep() {
        numOfSteps = recipeSteps.size();
        if (numOfSteps - 1 == positionOfStep)
            buttonNext.setVisibility(View.GONE);
        else if (positionOfStep == 0)
            buttonPrevious.setVisibility(View.GONE);
    }

    // when next button is pressed, get the next recipe
    public void onButtonPressed(int nextPosition) {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (mListener != null) {
            mListener.onFragmentInteraction(nextPosition);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailRecipeStepInteractionListener) {
            mListener = (OnDetailRecipeStepInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailRecipeStepInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_recipe_step, container, false);
        textViewDescription = view.findViewById(R.id.textViewStepDescription);
        simpleExoPlayerView = view.findViewById(R.id.exoPlayerViewer);
        imageViewNoThumbnail = view.findViewById(R.id.thumbnailNoImage);

        recipeSteps = getArguments().getParcelableArrayList("recipeSteps");
        positionOfStep = 0;
        positionOfStep = getArguments().getInt("positionOfStep");
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        //if this is a tablet, show the two-pane layout
        if (isTablet) {
            RecipeStep recipeStep = getArguments().getParcelable("recipeStep");
            textViewDescription.setText(recipeStep.getDescription());
            recipeVideoToPlay = recipeStep.getVideoURL();

            //if no video to display, display a thumbnail
            if (TextUtils.isEmpty(recipeVideoToPlay)) {
                simpleExoPlayerView.setVisibility(View.GONE);

                String recipeThumbnail = recipeStep.getThumbnailURL();
                if (TextUtils.isEmpty(recipeThumbnail))
                    recipeThumbnail = "https://flic.kr/p/7gyEeU"; //placeholder image

                //load the thumbnail
                Picasso.get()
                        .load(recipeThumbnail)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageViewNoThumbnail);
                imageViewNoThumbnail.setVisibility(View.VISIBLE);
            }
        }


        //phone stuff down here
        //if it's a phone, show normal view
        else {
            buttonNext = view.findViewById(R.id.buttonNext);
            buttonPrevious = view.findViewById(R.id.buttonPrevious);
            buttonPrevious.setOnClickListener(OnButtonClickListener);
            buttonNext.setOnClickListener(OnButtonClickListener);
            //check the position of step, and hide "Next" and "Previous" button as appropriate
            checkPositionOfStep();

            RecipeStep recipeStep;
            recipeStep = recipeSteps.get(positionOfStep);

            textViewDescription.setText(recipeStep.getDescription());
            recipeVideoToPlay = recipeStep.getVideoURL();

            //if no video to display, display a thumbnail
            if (TextUtils.isEmpty(recipeVideoToPlay)) {
                simpleExoPlayerView.setVisibility(View.GONE);

                String recipeThumbnail = recipeStep.getThumbnailURL();
                if (TextUtils.isEmpty(recipeThumbnail))
                    recipeThumbnail = "https://flic.kr/p/7gyEeU"; //placeholder image

                //load the thumbnail
                Picasso.get()
                        .load(recipeThumbnail)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageViewNoThumbnail);
                imageViewNoThumbnail.setVisibility(View.VISIBLE);
            }

            if (savedInstanceState != null) {
                playerPosition = savedInstanceState.getLong("playerPosition");
                isPlayWhenReady = savedInstanceState.getBoolean("isPlayWhenReady");
            }

            //if the orientation is landscape on a mobile, hide the rest of the widgets and make the exoplayer full screen
            int orientation = this.getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
                FrameLayout frameLayout = view.findViewById(R.id.linearLayoutForButtons);
                frameLayout.setVisibility(View.GONE);
                ScrollView scrollView = view.findViewById(R.id.scrollStepDescription);
                scrollView.setVisibility(View.GONE);

                openFullscreenDialog();
            }

            getActivity().setTitle(recipeStep.getShortDescription());
        }

        return view;
    }

    //called once the view becomes visible
    @Override
    public void onStart() {
        if (!TextUtils.isEmpty(recipeVideoToPlay))
            initializeExoPlayer(recipeVideoToPlay);
        super.onStart();
    }

    //learnt how to use ExoPlayer from https://cloudinary.com/blog/exoplayer_android_tutorial_easy_video_delivery_and_editing
    private void initializeExoPlayer(String video) {
        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        //Initialize simpleExoPlayerView

        simpleExoPlayerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingAppVideo"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(video);
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        //when restoring the state, check there is a saved position, seek to it, and continue playing video automatically
        if (playerPosition != 0) {
            player.seekTo(playerPosition); //seek to new position
            player.prepare(videoSource, false, true); //do NOT reset position to default
            //if (playerPlaybackState)
            player.setPlayWhenReady(isPlayWhenReady); //continue playing video
        } else
            player.prepare(videoSource);
    }

    //should be used for persisting; called when the user is leaving fragment
    @Override
    public void onPause() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            isPlayWhenReady = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        if (!TextUtils.isEmpty(recipeVideoToPlay))
            initializeExoPlayer(recipeVideoToPlay);

        super.onResume();
    }

    //save a bundle that includes the position of the player and playback state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("playerPosition", playerPosition);
        outState.putBoolean("isPlayWhenReady", isPlayWhenReady);
    }

    public interface OnDetailRecipeStepInteractionListener {
        void onFragmentInteraction(int nextPosition);
    }
}
