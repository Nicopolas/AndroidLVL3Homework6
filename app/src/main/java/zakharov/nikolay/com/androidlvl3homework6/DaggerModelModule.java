package zakharov.nikolay.com.androidlvl3homework6;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.Retrofit;

@Module
public class DaggerModelModule {
    private Model model;

    public DaggerModelModule(Model model) {
        this.model = model;
    }

    @Provides
    Call<List<ReposModel>> getCallRepos(Retrofit retrofit){
        Endpoints restAPI = retrofit.create(Endpoints.class);
        return restAPI.loadUsersRepos(model.getUserId());
    }

    @Provides
    public Model getModel() {
        return model;
    }
}
