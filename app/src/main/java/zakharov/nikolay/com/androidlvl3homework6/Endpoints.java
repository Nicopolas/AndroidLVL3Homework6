package zakharov.nikolay.com.androidlvl3homework6;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface Endpoints {

    @GET("users")
    Call<List<Model>> loadUsers();

    @GET("users/{user}/repos")
    Call<List<ReposModel>> loadUsersRepos(@Path("user") String user);
}
