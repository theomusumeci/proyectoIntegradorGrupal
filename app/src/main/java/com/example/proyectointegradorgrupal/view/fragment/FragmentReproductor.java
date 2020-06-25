package com.example.proyectointegradorgrupal.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegradorgrupal.LoginActivity;
import com.example.proyectointegradorgrupal.R;
import com.example.proyectointegradorgrupal.model.Track;
import com.example.proyectointegradorgrupal.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragmentReproductor extends Fragment {


    private MediaPlayer mediaPlayer;

    private ImageView imageViewPlayPause;
    private TextView currentTime;
    private TextView duration;
    private SeekBar seekBar;
    private ImageButton botonFavoritos;
    private MaterialTextView nombreTrack;
    private MaterialTextView nombreArtista;
    private Handler handler = new Handler();
    private Uri uriTrack;
    private Track track;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public FragmentReproductor() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reproductor, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imageViewPlayPause = view.findViewById(R.id.fragmentReproductorPlayPause);
        currentTime = view.findViewById(R.id.fragmentReproductorCurrentTime);
        duration = view.findViewById(R.id.fragmentReproductorDuration);
        nombreTrack = view.findViewById(R.id.fragmentReproductorNombreTrack);
        nombreArtista = view.findViewById(R.id.fragmentReproductorNombreArtista);
        seekBar = view.findViewById(R.id.fragmentReproductorSeekBar);
        botonFavoritos = view.findViewById(R.id.reproductorFavoritos);


        Bundle bundle = getArguments();
        track = (Track) bundle.get("track");
        final String preview = track.getPreview();
        uriTrack = Uri.parse(preview);


        nombreArtista.setText(track.getArtist().getName());
        nombreTrack.setText(track.getTitleShort());


        mediaPlayer = new MediaPlayer();
        seekBar.setMax(100);

        imageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    imageViewPlayPause.setImageResource(R.drawable.ic_play_circle_filled);
                } else {
                    mediaPlayer.start();
                    imageViewPlayPause.setImageResource(R.drawable.ic_pause_circle_filled);
                    updateSeekBar();

                }
            }
        });


        prepareMediaPlayer();


        botonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    db.collection(LoginActivity.DATOS_USUARIO)
                            .document(currentUser.getUid())
                            .update("tracksFavoritos", FieldValue.arrayUnion(track))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Track gregado a favoritos", Toast.LENGTH_SHORT).show();
                                    botonFavoritos.setImageResource(R.drawable.ic_favorite);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Salio mal agregar favorito", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Accede a tu cuenta para agregar favoritos", Toast.LENGTH_SHORT).show();
                }


            }
        });


        /**
         * Esto hace que puedas adelantar la cancion con la barra
         * */
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar newSeekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * newSeekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                currentTime.setText(millisecondsToTimer(mediaPlayer.getCurrentPosition()));

                return false;
            }
        });


        /**
         * Este metodo hace que se almacene la cancion en el buffer
         * */
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);

            }
        });


        return view;
    }


    /**
     * Se setea el media player con los datos de la cancion clickeada
     */
    private void prepareMediaPlayer() {
        try {


            mediaPlayer.setDataSource(getContext(), uriTrack);
            mediaPlayer.prepare();
            long durationMediaPlayer = mediaPlayer.getDuration();
            duration.setText(millisecondsToTimer(durationMediaPlayer));

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Algo salio mal", Toast.LENGTH_SHORT).show();
        }

    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();

            currentTime.setText(millisecondsToTimer(currentDuration));

        }
    };

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress((int) ((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration() * 100));

            handler.postDelayed(updater, 1000);

        }
    }

    /**
     * Metodo para pasar millisegundos a segundos
     */
    private String millisecondsToTimer(long milliSeconds) {
        String timerStrings;
        String secondsString;


        int seconds = (int) (milliSeconds / 1000);

        if (seconds < 10) {
            secondsString = "0" + seconds;

        } else {
            secondsString = "" + seconds;
        }

        timerStrings = "0" + ":" + secondsString;
        return timerStrings;


    }
}
