package zakharov.nikolay.com.androidlvl3homework6;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 05.07.2018.
 */

public class UserFragmentPresenter {
    private final String TAG = getClass().getSimpleName();
    UserView userView;

    public UserFragmentPresenter(UserView userView) {
        this.userView = userView;
    }

    @Inject
    Call<List<ReposModel>> call;
    @Inject
    boolean networkInfo;

    public void load() {
        if (networkInfo) {
            // запускаем
            try {
                userView.setVisibilityProgressBar(true);
                downloadOneUrl(call);
            } catch (IOException e) {
                e.printStackTrace();
                userView.setTextIntoReposTextView(e.getMessage());
            }
        } else {
            userView.makeToast("Подключите интернет");
        }
    }

    private void downloadOneUrl(Call<List<ReposModel>> call) throws IOException {
        call.enqueue(new Callback<List<ReposModel>>() {

            @Override
            public void onResponse(Call<List<ReposModel>> call, Response<List<ReposModel>> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        Log.e(TAG,  "response.body().size()" + response.body().size());
                        ReposModel curModel = null;
                        userView.appendIntoReposTextView("\nколичество = " + response.body().size() +
                                "\n-----------------");
                        for (int i = 0; i < response.body().size(); i++) {
                            curModel = response.body().get(i);
                            userView.appendIntoReposTextView("\n"+ String.valueOf(i+1) + ") " +curModel.getName());
                        }
                    }
                } else {
                    Log.e(TAG,  "response == null");
                    System.out.println("onResponse error: " + response.code());
                    userView.setTextIntoReposTextView("onResponse error: " + response.code());
                }
                userView.setVisibilityProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<ReposModel>> call, Throwable t) {
                System.out.println("onFailure " + t);
                userView.setTextIntoReposTextView("onFailure " + t.getMessage());
                userView.setVisibilityProgressBar(false);
            }
        });
    }

}
