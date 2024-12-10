package com.sak.myrecreativa.ui.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnClickListenner;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.adapters.PlayableGameAdapter;

import java.util.List;

public class ListadoJuegosFavFragment extends Fragment {

    public interface IOnAttachListenner{

        /**
         * Metodo para obtener un listado de los juegos favoriros
         * @return Lista de juegos
         */
        List<GameName> getGamesFav();
    }

    private List<GameName> games;
    private IOnClickListenner clickListenner;

    public ListadoJuegosFavFragment(){super(R.layout.fragment_list_games);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlayableGameAdapter adapter = new PlayableGameAdapter(games);
        RecyclerView recyclerView = view.findViewById(R.id.rvList);
        adapter.setListenner(clickListenner);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        clickListenner = (IOnClickListenner) context;
        ListadoJuegosFavFragment.IOnAttachListenner attachListenner = (ListadoJuegosFavFragment.IOnAttachListenner) context;
        games = attachListenner.getGamesFav();
    }
}
