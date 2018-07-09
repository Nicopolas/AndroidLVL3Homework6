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
public class DaggerNetModule {
    private Context context;
    private Model model;

    public DaggerNetModule(Context context){
        this.context = context;
    }

    public DaggerNetModule() {

    }

    @Provides
    Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Provides
    Call<List<Model>> getCall(Retrofit retrofit){
        Endpoints restAPI = retrofit.create(Endpoints.class);
        return restAPI.loadUsers();
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

