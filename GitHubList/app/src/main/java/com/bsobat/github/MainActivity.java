package com.bsobat.github;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.bsobat.github.dto.RepoResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.guiView.MainView;
import com.bsobat.github.viewmodel.MainActivityViewModel;

public class MainActivity extends LifecycleActivity {
    public static final int LIMIT = 15;
    private int page = 1;
    private MainView view;
    private MainActivityViewModel modelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
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

        //get an instance of our viewmodel...
        modelView = MainActivityViewModel.create(this);
        MyApplication.getAppComponent().inject(modelView);

        modelView.getResult().observe(this, new Observer<Resource<RepoResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<RepoResponse> repoResponseResource) {
                view.loading(false);

                switch (repoResponseResource.getStatus()){
                    case LOADING: //loading
                        break;
                    case ERROR:
                        Log.e("Error", repoResponseResource.getException().getMessage(), repoResponseResource.getException());
                        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        RepoResponse data = repoResponseResource.getData();
                        view.bind(data.getList(), data.getPage(), data.getLimit());
                        break;
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
