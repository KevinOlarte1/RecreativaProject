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
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.adapters.GameAdapter;

import java.util.List;

public class MisionesFragment extends Fragment {

    public interface IOnAttachListenner{
        List<GameName> getGames();
    }

    private List<GameName> games;
    public MisionesFragment(){super(R.layout.mission_fragment);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GameAdapter adapter = new GameAdapter(games);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        games  = ((IOnAttachListenner) context).getGames();
    }
}
