package com.example.semestral_android_1sf142;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsFragment extends Fragment
{
    // Views
    private ListView listView;

    private ImageButton backBtn;

    // Integrantes
    private ArrayList<String[]> integrantesData;

    public SettingsFragment()
    {
        super(R.layout.fragment_settings);
        this.integrantesData = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.listView = requireActivity().findViewById(R.id.integrante_listview);
        parseIntegrantesTxtFile();

        this.listView.setAdapter(new IntegranteListViewAdapter(this.integrantesData, requireActivity()));

        this.backBtn = requireActivity().findViewById(R.id.settings_back_btn);
        this.backBtn.setOnClickListener(v -> handleBackBtn());
    }

    private void handleBackBtn()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }

    private void parseIntegrantesTxtFile()
    {
        InputStream in_s = requireActivity().getResources().openRawResource(R.raw.integrantes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
        String line;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                String[] res = line.split("[,]", 0);
                if (Character.isSpaceChar(res[1].charAt(0)))
                {
                    res[1] = res[1].substring(1);
                }

                this.integrantesData.add(res);
            }

            in_s.close();
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
