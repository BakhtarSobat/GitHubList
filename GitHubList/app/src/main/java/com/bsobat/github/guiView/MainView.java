package com.bsobat.github.guiView;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsobat.github.R;
import com.bsobat.github.databinding.ActivityMainBinding;
import com.bsobat.github.dto.GitHubDto;
import com.bsobat.github.ui.listeners.ExtendableList;
import com.bsobat.github.ui.listeners.RecyclerViewLoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainView implements GuiView {
    private View rootView;
    private ActivityMainBinding binding;
    private RecyclerView listView;
    private EventHandler handler;


    public MainView(LayoutInflater inflater, ViewGroup vg, final Listener listener){
        this.handler = new EventHandler(listener);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main, vg, false);
        binding.setHandler(handler);
        rootView = binding.getRoot();
        listView = (RecyclerView) rootView.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(new RepoAdapter(inflater));

        RecyclerViewLoadMoreScrollListener loadMoreScrollListener = new RecyclerViewLoadMoreScrollListener(new ExtendableList() {
            @Override
            public void loadMore() {
                if (listener != null) {
                    listener.loadMore();
                }
            }
        }, 100, 3);
        listView.addOnScrollListener(loadMoreScrollListener);
        loadMoreScrollListener.loadMoreIfneeded(listView);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public void loading(boolean loading){
        binding.setShowLoadMoreSpinner(loading);

    }


    public void bind(List<GitHubDto> list, int page, int limit){
        if(list == null) return;

        ((RepoAdapter) listView.getAdapter()).getGitHubDtoList().addAll(list);

        int position = page * limit;
        ((RepoAdapter) listView.getAdapter()).notifyItemRangeChanged(position, limit);
        if(((RepoAdapter) listView.getAdapter()).getGitHubDtoList().size() != ((RepoAdapter) listView.getAdapter()).getItemCount()){
            ((RepoAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }
    public interface Listener{

        void loadMore();

        void clearCache();
    }

    public class EventHandler{
        final  private Listener listener;

        public EventHandler(Listener listener) {
            this.listener = listener;
        }

        public void onClearCacheClicked(View v){
            if(listener != null) listener.clearCache();
        }
    }

    private class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        final private LayoutInflater inflater;
        private List<GitHubDto> gitHubDtoList = new ArrayList<>();

        public List<GitHubDto> getGitHubDtoList() {
            return gitHubDtoList;
        }

        public void setGitHubDtoList(List<GitHubDto> gitHubDtoList) {
            this.gitHubDtoList = gitHubDtoList;
        }

        private RepoAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView view = new ItemView(inflater, parent, new ItemView.Listener() {
            });
            ItemHolder holder = new ItemHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GitHubDto gitHubDto = gitHubDtoList.get(position);
            ((ItemHolder) holder).bind(gitHubDto);
        }

        @Override
        public int getItemCount() {
            return gitHubDtoList.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {
            ItemView holder;

            public ItemHolder(ItemView holder) {
                super(holder.getRootView());
                this.holder = holder;
            }

            public void bind(GitHubDto gitHubDto) {
                holder.bind(gitHubDto);
            }
        }
    }
}
