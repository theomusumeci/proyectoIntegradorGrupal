package com.example.proyectointegradorgrupal.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectointegradorgrupal.LoginActivity;
import com.example.proyectointegradorgrupal.R;
import com.example.proyectointegradorgrupal.controller.AlbumController;
import com.example.proyectointegradorgrupal.model.Album;
import com.example.proyectointegradorgrupal.model.Track;
import com.example.proyectointegradorgrupal.util.ResultListener;
import com.example.proyectointegradorgrupal.view.MainActivity;
import com.example.proyectointegradorgrupal.view.adapter.TrackAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlbumTracks extends Fragment implements TrackAdapter.TrackAdapterListener {

    private RecyclerView recyclerViewListaCanciones;
    private FragmentListaCancionesListener fragmentListaCancionesListener;
    private AlbumController albumController;
    private TrackAdapter trackAdapter;
    private ImageButton albumFavoritoButtom;

    private Album album;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.fragmentListaCancionesListener = (FragmentListaCancionesListener) context;
    }

    public FragmentAlbumTracks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album_tracks, container, false);

        albumFavoritoButtom = view.findViewById(R.id.albumBotonFavoritos);
        recyclerViewListaCanciones = view.findViewById(R.id.fragmentListadoCancionesRecycler);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        album = (Album) bundle.getSerializable(MainActivity.FAVORITO);
        ImageView fragmentListadoCancionesImagen = view.findViewById(R.id.fragmentlistadoCancionesImagen);
        TextView fragmentListadoCancionesArtista = view.findViewById(R.id.fragmentListadoCancionesArtista);

        fragmentListadoCancionesArtista.setText(album.getTitle());

        albumController = new AlbumController();
        albumController.getAlbumTracks(album.getId().toString(), new ResultListener<Track>() {
            @Override
            public void onFinish(Track result) {
                trackAdapter = new TrackAdapter(result.getData(), FragmentAlbumTracks.this);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerViewListaCanciones.setLayoutManager(linearLayoutManager);
                recyclerViewListaCanciones.setAdapter(trackAdapter);

            }
        });
        Glide.with(fragmentListadoCancionesImagen.getContext())
                .load(album.getCoverXL())
                .into(fragmentListadoCancionesImagen);

        albumFavoritoButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    db.collection(LoginActivity.DATOS_USUARIO)
                            .document(currentUser.getUid())
                            .update("albumesFavoritos", FieldValue.arrayUnion(album))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Album gregado a favoritos", Toast.LENGTH_SHORT).show();
                                    albumFavoritoButtom.setImageResource(R.drawable.ic_favorite);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Salio mal agregar album", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Accede a tu cuenta para agregar favoritos", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;


    }

    @Override
    public void onClick(Track track) {
        fragmentListaCancionesListener.onClickTrackDesdeAlbum(track);
    }

    public interface FragmentListaCancionesListener {
        public void onClickTrackDesdeAlbum(Track track);
    }

}
