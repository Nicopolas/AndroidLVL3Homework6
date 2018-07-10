package zakharov.nikolay.com.androidlvl3homework6;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerModelModule {
    private Model model;
    private Context context;

    public DaggerModelModule(Context context, Model model) {
        this.context = context;
        this.model = model;
    }

    @Provides
    Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    Call<List<ReposModel>> getCall(Retrofit retrofit){
        Endpoints restAPI = retrofit.create(Endpoints.class);
        return restAPI.loadUsersRepos(model.getLogin());
    }

    @Provides
    public Model getModel() {
        return model;
    }

    @Provides
    public Context provideContext(){
        return context;
    }

    @Provides
    public boolean getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }
}
