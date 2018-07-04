package zakharov.nikolay.com.androidlvl3homework6;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orm.SugarContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 04.07.2018.
 */

public class ListFragment extends Fragment implements View.OnClickListener, ListView {
    private final String TAG = getClass().getSimpleName();
    AppComponent appComponent;

    private TextView mInfoTextView;
    private ProgressBar progressBar;
    RecyclerView usersList;
    UserAdapter adapter;
    Presenter mPresenter;
    Button btnLoad;
    Button btnSaveAllSugar;
    Button btnSelectAllSugar;
    Button btnDeleteAllSugar;
    Button btnSaveAllRealm;
    Button btnSelectAllRealm;
    Button btnDeleteAllRealm;

    @Inject
    Call<List<Model>> call;
    Realm realm;

    Endpoints restAPI;
    List<Model> modelList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        mPresenter = new Presenter(this);
        appComponent = MainApp.getComponent();
        appComponent.injectsToListFragent(this);
        initGUI(v);
        mPresenter.load();
        SugarContext.init(getActivity());
        return v;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");

        super.onStart();
    }

    private void initGUI(View v) {
        mInfoTextView = (TextView) v.findViewById(R.id.tvLoad);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        btnLoad = (Button) v.findViewById(R.id.btnLoad);
        btnSaveAllSugar = (Button) v.findViewById(R.id.btnSaveAllSugar);
        btnSelectAllSugar = (Button) v.findViewById(R.id.btnSelectAllSugar);
        btnDeleteAllSugar = (Button) v.findViewById(R.id.btnDeleteAllSugar);
        btnSaveAllRealm = (Button) v.findViewById(R.id.btnSaveAllRealm);
        btnSelectAllRealm = (Button) v.findViewById(R.id.btnSelectAllRealm);

        btnLoad.setOnClickListener(v1 -> mPresenter.load());

        //RecyclerView и Adapter
        btnDeleteAllRealm = (Button) v.findViewById(R.id.btnDeleteAllRealm);
        usersList = (RecyclerView) v.findViewById(R.id.list_recycler_view);
        adapter = new UserAdapter(modelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager(linearLayoutManager);
        usersList.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoad:
                mInfoTextView.setText("");

                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();

                if (networkinfo != null && networkinfo.isConnected()) {
                    // запускаем
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        downloadOneUrl(call);
                        usersList.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mInfoTextView.setText(e.getMessage());
                    }
                } else {
                    Toast.makeText(getActivity(), "Подключите интернет", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSaveAllSugar:
                Single<Bundle> singleSaveAll = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            String curLogin = "";
                            String curUserID = "";
                            String curAvatarUrl = "";
                            Date first = new Date();
                            for (Model curItem : modelList) {
                                curLogin = curItem.getLogin();
                                curUserID = curItem.getUserId();
                                curAvatarUrl = curItem.getAvatar();
                                SugarModel sugarModel = new SugarModel(curLogin, curUserID, curAvatarUrl);
                                sugarModel.save();
                            }
                            Date second = new Date();
                            List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", tempList.size());
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                singleSaveAll.subscribeWith(CreateObserver());
                break;
            case R.id.btnSelectAllSugar:
                Single<Bundle> singleSelectAll = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            Date first = new Date();
                            List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
                            Date second = new Date();
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", tempList.size());
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                singleSelectAll.subscribeWith(CreateObserver());
                break;
            case R.id.btnDeleteAllSugar:
                Single<Bundle> singleDeleteAll = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            Date first = new Date();
                            int count = SugarModel.deleteAll(SugarModel.class);
                            Date second = new Date();
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", count);
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                singleDeleteAll.subscribeWith(CreateObserver());
                break;

            case R.id.btnSaveAllRealm:
                Single<Bundle> singleSaveAllRealm = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            String curLogin = "";
                            String curUserID = "";
                            String curAvatarUrl = "";
                            realm = Realm.getDefaultInstance();
                            Date first = new Date();

                            for (Model curItem : modelList) {
                                curLogin = curItem.getLogin();
                                curUserID = curItem.getUserId();
                                curAvatarUrl = curItem.getAvatar();
                                try {
                                    realm.beginTransaction();
                                    RealmModel realmModel = realm.createObject(RealmModel.class);
                                    realmModel.setUserID(curUserID);
                                    realmModel.setLogin(curLogin);
                                    realmModel.setAvatarUrl(curAvatarUrl);
                                    realm.commitTransaction();
                                } catch (Exception e) {
                                    realm.cancelTransaction();
                                    emitter.onError(e);
                                }
                            }
                            Date second = new Date();
                            long count = realm.where(RealmModel.class).count();
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", (int) count);
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                            realm.close();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                singleSaveAllRealm.subscribeWith(CreateObserver());
                break;
            case R.id.btnSelectAllRealm:
                Single<Bundle> singleSelectAllRealm = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            realm = Realm.getDefaultInstance();
                            Date first = new Date();
                            RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
                            Date second = new Date();
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", tempList.size());
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                            realm.close();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                singleSelectAllRealm.subscribeWith(CreateObserver());
                break;
            case R.id.btnDeleteAllRealm:
                Single<Bundle> singleDeleteAllRealm = Single.create(new SingleOnSubscribe<Bundle>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                        try {
                            realm = Realm.getDefaultInstance();
                            final RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
                            Date first = new Date();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    tempList.deleteAllFromRealm();
                                }
                            });
                            Date second = new Date();
                            Bundle bundle = new Bundle();
                            bundle.putInt("count", tempList.size());
                            bundle.putLong("msek", second.getTime() - first.getTime());
                            emitter.onSuccess(bundle);
                            realm.close();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                singleDeleteAllRealm.subscribeWith(CreateObserver());
                break;
        }
    }

    private DisposableSingleObserver<Bundle> CreateObserver() {
        return new DisposableSingleObserver<Bundle>() {

            @Override
            protected void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
                mInfoTextView.setText("");
            }

            @Override
            public void onSuccess(@NonNull Bundle bundle) {
                progressBar.setVisibility(View.GONE);
                usersList.setVisibility(View.GONE);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.append("количество = " + bundle.getInt("count") +
                        "\n милисекунд = " + bundle.getLong("msek"));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                progressBar.setVisibility(View.GONE);
                usersList.setVisibility(View.GONE);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("ошибка БД: " + e.getMessage());
            }
        };
    }

    private void downloadOneUrl(Call<List<Model>> call) throws IOException {
        call.enqueue(new Callback<List<Model>>() {

            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        Model curModel = null;
                        mInfoTextView.setVisibility(View.VISIBLE);
                        mInfoTextView.append("\n количество = " + response.body().size() +
                                "\n-----------------");
                        for (int i = 0; i < response.body().size(); i++) {
                            curModel = response.body().get(i);
                            modelList.add(curModel);
                        }
                    }
                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setVisibility(View.GONE);
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                System.out.println("onFailure " + t);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        List<Model> models;

        public UserAdapter(List<Model> dictionaries) {
            this.models = dictionaries;
            notifyDataSetChanged();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

            return new UserHolder(listItemView);
        }

        @Override
        public void onBindViewHolder(final UserHolder holder, int position) {
            final Model model = models.get(holder.getAdapterPosition());
            setImage(holder.avatar, model.getAvatar());
            holder.usersName.setText(model.getLogin());
            holder.avatar.setOnClickListener(view -> ((MainActivity) getActivity()).startFragment(new UserFragment()));
            holder.usersName.setOnClickListener(view -> ((MainActivity) getActivity()).startFragment(new UserFragment()));
        }


        @Override
        public int getItemCount() {
            if (models != null && models.size() != 0) {
                return models.size();
            }
            return 0;
        }
    }

    static class UserHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView usersName;

        public UserHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_list_item);
            usersName = (TextView) itemView.findViewById(R.id.user_name_list_item);
        }
    }

    private void setImage(ImageView image, String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(image);
    }

    @Override
    public SQLiteHelper getSQLiteHelper() {
        return new SQLiteHelper(getActivity());
    }

    @Override
    public void appendIntoTextView(String str) {
        mInfoTextView.append(str);
    }

    @Override
    public void setTextIntoTextView(String str) {
        mInfoTextView.setText("");
        mInfoTextView.setText(str);
    }

    @Override
    public void setVisibilityProgressBar(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
            return;
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setVisibilityUsersList(boolean visibility) {
        if (visibility) {
            usersList.setVisibility(View.VISIBLE);
            return;
        }
        usersList.setVisibility(View.GONE);
    }

    @Override
    public Call<List<Model>> gelCall() {
        return call;
    }

    @Override
    public void makeToast(String string) {
        Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public List<Model> getModelList() {
        return modelList;
    }

    @Override
    public boolean getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }
}
