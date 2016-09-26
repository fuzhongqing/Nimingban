package com.fuzho.nimingban.send.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.fuzho.nimingban.R;

import java.util.ArrayList;

/**
 * Created by fuzho on 2016/9/19.
 */

public class Emotion extends Fragment {

    ArrayList<Button> emotions = new ArrayList<>();
    public Emotion() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0;i < 20; ++i) {
            Button b = new Button(getActivity());
            b.setText(i + "");
            b.setGravity(View.TEXT_ALIGNMENT_CENTER);
            emotions.add(b);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.emotion_grid);
        gridLayout.setColumnCount(4);
        for (TextView t : emotions) {
            gridLayout.addView(t);
        }
        return view;
    }
}
