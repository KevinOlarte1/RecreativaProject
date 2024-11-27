package com.sak.myrecreativa.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnClickListenner;
import com.sak.myrecreativa.models.ConectaCuatro;
import com.sak.myrecreativa.models.abstracts.Game;
import com.sak.myrecreativa.ui.fragments.menu.AjustesFragment;
import com.sak.myrecreativa.ui.fragments.menu.ListadoJuegosBloqueadosFragment;
import com.sak.myrecreativa.ui.fragments.menu.ListadoJuegosFragment;
import com.sak.myrecreativa.ui.fragments.menu.MisionesFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListadoJuegosFragment.IOnAttachListenner,
        IOnClickListenner{
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                /*
                 * Si el usuario pulsa el botón atrás mientras está mostrándose el menú del NavigationView,
                 * hacemos que se cierre dicho menú, ya que el comportamiento por defecto es cerrar la
                 * Activity.
                 */
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        View headerView = navigationView.getHeaderView(0);
        ImageView ivUser = headerView.findViewById(R.id.ivProfile);
        TextView tvUser = headerView.findViewById(R.id.tvUser);
        tvUser.setText(R.string.nav_header_title);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
        tvEmail.setText(R.string.nav_header_subtitle);


    }

    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment f;
        // Se ha hecho click en algún item del NavigationView
        int id = item.getItemId();

        if (id == R.id.nav_ajustes) {
            f = new AjustesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
            setTitle(R.string.ajustes);
        } else if (id == R.id.nav_misiones) {
            f = new MisionesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
            setTitle(R.string.misiones);
        } else if (id == R.id.nav_game_blocked) {
            f = new ListadoJuegosBloqueadosFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
            setTitle(R.string.blocked);
        } else if (id == R.id.nav_allGame){
            f = new ListadoJuegosFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
            setTitle(R.string.allGame);
        }else if (id == R.id.nav_favoritos){
            f = new ListadoJuegosFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
            setTitle(R.string.favoritos);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public List<Game> getGames() {
        //TODO:
        List<Game> games = new ArrayList<>();
        games.add(new ConectaCuatro("conectaCuatro"));
        return games;
    }

    @Override
    public void onClick(int position) {
        //TODO: PASAR TAMBIEN EL ARRAY POR PARAMETRO
    }
}