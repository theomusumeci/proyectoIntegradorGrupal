package com.example.proyectointegradorgrupal.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.proyectointegradorgrupal.LoginActivity;
import com.example.proyectointegradorgrupal.R;
import com.example.proyectointegradorgrupal.controller.AlbumController;
import com.example.proyectointegradorgrupal.controller.SearchController;
import com.example.proyectointegradorgrupal.model.Album;
import com.example.proyectointegradorgrupal.model.Playlist;
import com.example.proyectointegradorgrupal.model.Recomendados;
import com.example.proyectointegradorgrupal.model.SearchContainer;
import com.example.proyectointegradorgrupal.model.Track;
import com.example.proyectointegradorgrupal.util.ResultListener;
import com.example.proyectointegradorgrupal.view.fragment.BottomNavigationFragment;
<<<<<<< HEAD
import com.example.proyectointegradorgrupal.view.fragment.FavoritosFragment;
import com.example.proyectointegradorgrupal.view.fragment.PlaylistFragment;
import com.example.proyectointegradorgrupal.view.fragment.RecomendadosFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
=======
import com.example.proyectointegradorgrupal.view.fragment.FragmentListadoCanciones;
import com.example.proyectointegradorgrupal.view.fragment.FragmentPrincipal;
import com.example.proyectointegradorgrupal.view.fragment.FragmentSearch;
>>>>>>> master
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity implements FragmentPrincipal.FragmentPrincipalListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
//TODO me parece que el FavoritosFragment deveria ser un FragmentPantallaInicio y dentro de ese poner 3 recyclerview, uno de favoritos, otro de
//tus playlists y otro de recomendados, todos dentro del mismo fragment
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //Para autenticacion de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

<<<<<<< HEAD
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        findViewsById();

        toolbar.setTitle("Jaxoo");
        setSupportActionBar(toolbar);

        pegarFragmentsMainActivity();

        //Esta parte iria en el fragment, aca está para probar el pedido
        AlbumController albumController = new AlbumController();
        albumController.getAlbumTracks("12047960", new ResultListener<Track>() {
            @Override
            public void onFinish(Track result) {

            }
        });
    }

=======



        findViewsById();

        configuracionToolbar();


        pegarFragmentsMainActivity();

    }

    private void configuracionToolbar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawers, R.string.close_drawers);
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // actionBarDrawerToggle.syncState();
    }

    /**
     * Esto es para que al apretar atras no se cierre la app
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();

        } else {
            super.onBackPressed();

        }

    }
>>>>>>> master

    /**
     * Método para los FindViewsByIds
     */
    private void findViewsById() {
        bottomNavigationView = findViewById(R.id.fragmentBottomNavigation);
        drawerLayout = findViewById(R.id.activityMainDrawerLayout);
        toolbar = findViewById(R.id.activityMainToolbar);
    }

    /**
     * Metodo para pegar fragments
     */
    private void pegarFragmentsMainActivity() {

        pegarFragment(new FragmentPrincipal(), R.id.activityMainContenedorPrincipal);

        //Este siempre esta, va cambiando el de arriba
        pegarFragment(new BottomNavigationFragment(), R.id.activityMainContenedorFragmentBottomNavigation);

    }

    private void pegarFragment(Fragment favoritosFragment, int id) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(id, favoritosFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(Album album) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("favorito", album);


        FragmentListadoCanciones fragmentListadoCanciones = new FragmentListadoCanciones();
        fragmentListadoCanciones.setArguments(bundle);
        pegarFragment(fragmentListadoCanciones, R.id.activityMainContenedorPrincipal);


    }

    @Override
    public void onClick(Playlist playlist) {
        //abrir fragment con temas de playlist
    }

    @Override
    public void onClick(Recomendados recomendados) {
        //abrir fragment con recomendados
    }

    /**
     * Configuracion del Appbar superior, Buscador y Configuracion - CHEQUEAR color
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);

        final MenuItem cerrarItem = menu.findItem(R.id.menuGeneralCerrar);
        final MenuItem buscarItem = menu.findItem(R.id.menuGeneralBuscar);

        final SearchView searchView = (SearchView) buscarItem.getActionView();

        //Boton cerrar sesion
        cerrarItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuGeneralCerrar:
                        logOutFirebaseUser();
                        logOutGoogle();

                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(i);
                        break;
                }

                return false;
            }
        });


        //Boton Busqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Boton Buscar", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                FragmentSearch fragmentSearch = new FragmentSearch();
                fragmentSearch.setArguments(bundle);
                pegarFragment(fragmentSearch, R.id.activityMainContenedorPrincipal);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void logOutFirebaseUser() {
        mAuth.signOut();
        Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
    }

    private void logOutGoogle() {
        mGoogleSignInClient.signOut().addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this, "Gracias por quedarte", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Volvé pronto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
