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
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.interfaces.IOnGameModeSelectedListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.conecta4.ConectaCuatro;
import com.sak.myrecreativa.ui.fragments.ModeFragment;
import com.sak.myrecreativa.ui.fragments.battleship.BattleshipFragment;
import com.sak.myrecreativa.ui.fragments.buscaminasGame.MinesweeperFragment;
import com.sak.myrecreativa.ui.fragments.conecta4.ConectaCuatroFragment;
import com.sak.myrecreativa.ui.fragments.memoryGame.MemoryGameFragment;
import com.sak.myrecreativa.ui.fragments.menu.AjustesFragment;
import com.sak.myrecreativa.ui.fragments.menu.ListadoJuegosBloqueadosFragment;
import com.sak.myrecreativa.ui.fragments.menu.ListadoJuegosFragment;
import com.sak.myrecreativa.ui.fragments.menu.MisionesFragment;
import com.sak.myrecreativa.ui.fragments.ScoreFragment;
import com.sak.myrecreativa.ui.fragments.sudoku.SudokuGameFragment;
import com.sak.myrecreativa.ui.fragments.trivialGame.TrivialFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListadoJuegosFragment.IOnAttachListenner,
        IOnClickListenner, IOnGameModeSelectedListener, IOnGameEndListener {
    private DrawerLayout drawer;
    private List<GameName> gameNames;
    private GameName currentGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createGames();
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

    /**
     * Obtener un listado de los juegos que tenemos.
     * @return listado de GameName
    */
    @Override
    public List<GameName> getGames() {
        return gameNames;
    }
    public List<GameName> createGames() {
        //! indica el orden de los juegos.
        gameNames = new ArrayList<>();
        gameNames.add(new GameName("Minesweeper"));
        gameNames.add(new GameName("Trivial"));
        gameNames.add(new GameName("Memory"));
        gameNames.add(new GameName("Sudoku"));
<<<<<<< HEAD
        gameNames.add(new GameName("BattleShip"));
=======
        gameNames.add(new GameName("Conecta4"));
>>>>>>> b06111817e12e231c179af04f1d7866aa9c7b407
        return gameNames;
    }


    private GameName getCurrentGame(){
        return currentGame;
    }

    private Fragment modeFragment(GameName gameName){
        Fragment f;
        Bundle args = new Bundle();
        args.putParcelable("GAME", currentGame);
        if(gameName.getName().equalsIgnoreCase("Trivial")){
            args.putStringArray("MODES", new String[]{"Movies", "Series", "Anime"});
        } else {
            args.putStringArray("MODES", new String[]{"Easy", "Medium", "Hard"});
        }
        f = new ModeFragment();
        f.setArguments(args);
        setTitle(gameName.getName());

        return f;
    }

    @Override
    public void onClick(int position) {
        //TODO: PASAR TAMBIEN EL ARRAY POR PARAMETRO
        Fragment f = null;
        if (position == -1) {
            if (currentGame.getName().equalsIgnoreCase("Trivial")) {
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Minesweeper")) {
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Memory")) {
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Sudoku")) {
                f = modeFragment(currentGame);
            }
<<<<<<< HEAD
            if (currentGame.getName().equalsIgnoreCase("BattleShip")) {
=======
            if(currentGame.getName().equalsIgnoreCase("conecta4")){
>>>>>>> b06111817e12e231c179af04f1d7866aa9c7b407
                f = modeFragment(currentGame);
            }
        } else if(position == -2){
            f = new ListadoJuegosFragment();
            setTitle("MyRecreativa");

        }else{
            currentGame = gameNames.get(position);
            if (currentGame.getName().equalsIgnoreCase("Minesweeper")){
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Trivial")) {
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Memory")){
                f = modeFragment(currentGame);
            }
            if (currentGame.getName().equalsIgnoreCase("Sudoku")){
                f = modeFragment(currentGame);
            }
<<<<<<< HEAD
            if (currentGame.getName().equalsIgnoreCase("BattleShip")){
=======
            if (currentGame.getName().equalsIgnoreCase("conecta4")){
>>>>>>> b06111817e12e231c179af04f1d7866aa9c7b407
                f = modeFragment(currentGame);
            }
        }

        if(f != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
        }
    }


    @Override
    public void onGameModeSelected(String mode, GameName gameName) {
        Fragment f = null;

        Bundle arg = new Bundle();
        arg.putString("MODE", mode);
        arg.putParcelable("GAME", gameName);

        if (gameName.getName().equalsIgnoreCase("Trivial")){
            f = new TrivialFragment();
            f.setArguments(arg);
            setTitle(gameName.getName());
        }
        if (gameName.getName().equalsIgnoreCase("Minesweeper")){
            f = new MinesweeperFragment();
            f.setArguments(arg);
            setTitle("Minesweeper");
        }
        if (gameName.getName().equalsIgnoreCase("Memory")){
            f = new MemoryGameFragment();
            f.setArguments(arg);
            setTitle("Card Memory");
        }
        if (gameName.getName().equalsIgnoreCase("Sudoku")){
            f = new SudokuGameFragment();
            f.setArguments(arg);
            setTitle("Sudoku");
        }
<<<<<<< HEAD
        if (gameName.getName().equalsIgnoreCase("BattleShip")){
            f = new BattleshipFragment(); // cambiar el fragment
            f.setArguments(arg);
            setTitle("BattleShip");
=======
        if (gameName.getName().equalsIgnoreCase("conecta4")){
            f = new ConectaCuatroFragment();
            f.setArguments(arg);
            setTitle("Conecta4");
>>>>>>> b06111817e12e231c179af04f1d7866aa9c7b407
        }
        if (f != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcvMain, f)
                    .commit();
        }
    }

    @Override
    public void onGameEnd(int score, GameName name, boolean isWin) {
        Fragment f;
        f = new ScoreFragment();
        Bundle arg = new Bundle();
        arg.putInt("SCORE", score);
        arg.putParcelable("GAME", name);
        arg.putBoolean("IS_WIN", isWin);
        f.setArguments(arg);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvMain, f)
                .addToBackStack(null)
                .commit();
    }

}