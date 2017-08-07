package com.bsobat.github;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.guiView.MainView;
import com.bsobat.github.viewmodel.MainActivity2ViewModel;

public class MainActivity2  extends LifecycleActivity {
    public static final int LIMIT = 15;
    private int page = 1;
    private MainView view;
    private MainActivity2ViewModel modelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //All view related things are in MainView, this pattern makes it easy to apply a/b testing or changing the design easily
        view = new MainView(getLayoutInflater(), null, new MainView.Listener() {
            @Override
            public void loadMore() {
                loadMoreObjects();
            }

            @Override
            public void clearCache() {
                modelView.clearCache();
            }
        });
        setContentView(view.getRootView());

        //get an instance of our viewmodel and get things injected...
        modelView = MainActivity2ViewModel.create(this);
        MyApplication.getAppComponent().inject(modelView);

        //Let's observe the result and update our UI upon changes
        modelView.getResult().observe(this, new Observer<Resource<GitHubResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<GitHubResponse> repoResponseResource) {
                view.loading(false);
                Log.d("Status ", ""+repoResponseResource.getStatus());
                switch (repoResponseResource.getStatus()){
                    case LOADING: {
                        view.refreshing(true);
                        GitHubResponse data = repoResponseResource.getData();
                        view.bind(data.getList(), data.getPage(), data.getLimit());
                        break;
                    }
                    case ERROR:
                        view.refreshing(false);
                        Log.e("Error", repoResponseResource.getException().getMessage(), repoResponseResource.getException());
                        Toast.makeText(MainActivity2.this, R.string.error, Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS: {
                        view.refreshing(false);
                        GitHubResponse data = repoResponseResource.getData();
                        view.bind(data.getList(), data.getPage(), data.getLimit());
                        break;
                    }
                }
            }
        });
        view.loading(true);
        modelView.load(page, LIMIT);
    }

    private void loadMoreObjects() {
        view.loading(true);

        modelView.load(++page, LIMIT);
    }


}
