package com.bsobat.github.guiView;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsobat.github.R;
import com.bsobat.github.databinding.TemItemBinding;
import com.bsobat.github.dto.Repo;

public class ItemView implements GuiView {
    private TemItemBinding binding;
    private View rootView;


    public ItemView(LayoutInflater inflater, ViewGroup vg, Listener listener){
        binding = DataBindingUtil.inflate(inflater, R.layout.tem_item, vg, false);
        rootView = binding.getRoot();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public void bind(Repo repo) {
        binding.setRepo(repo);
    }


    public interface Listener{

    }
}

