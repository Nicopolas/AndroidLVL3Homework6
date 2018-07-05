package zakharov.nikolay.com.androidlvl3homework6;

import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 1 on 01.07.2018.
 */

public class Presenter {
    private final String TAG = getClass().getSimpleName();

    @Inject
    Call<List<Model>> call;
    @Inject
    boolean networkInfo;

    ListView mListView;
    List<Model> modelList;
    Endpoints restAPI;
    Realm realm;


    public Presenter(ListView view) {
        this.mListView = view;
        modelList = mListView.getModelList();
    }

    public void load() {
        if (networkInfo) {
            // запускаем
            try {
                mListView.setVisibilityProgressBar(true);
                downloadOneUrl(call);
            } catch (IOException e) {
                e.printStackTrace();
                mListView.setTextIntoTextView(e.getMessage());
            }
        } else {
            mListView.makeToast("Подключите интернет");
        }
    }

    // -------------------------------------------------------------- Sugar
    public void saveAllSugar() {
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
            setStat(tempList.size(), first, second);
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }

    public void selectAllSugar() {
        try {
            Date first = new Date();
            List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
            Date second = new Date();
            setStat(tempList.size(), first, second);
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }

    public void deleteAllSugar() {
        try {
            Date first = new Date();
            int count = SugarModel.deleteAll(SugarModel.class);
            Date second = new Date();
            setStat(count, first, second);
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }


    // -------------------------------------------------------------- Realm

    public void saveAllRealm() {
        try {
            String curLogin = "";
            String curUserID = "";
            String curAvatarUrl = "";
            realm = Realm.getDefaultInstance();
            Date first = new Date();
            realm.beginTransaction();
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
                    e.printStackTrace();
                    mListView.setTextIntoTextView(e.getMessage());
                }
            }
            Date second = new Date();
            long count = realm.where(RealmModel.class).count();
            setStat((int) count, first, second);
            realm.close();
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }

    public void selectAllRealm() {
        try {
            realm = Realm.getDefaultInstance();
            Date first = new Date();
            RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
            Date second = new Date();
            setStat(tempList.size(), first, second);
            realm.close();
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }

    public void deleteAllRealm() {
        try {
            realm = Realm.getDefaultInstance();
            final RealmResults<RealmModel> tempList = realm.where(RealmModel.class).findAll();
            Date first = new Date();
            realm.executeTransaction(realm -> tempList.deleteAllFromRealm());
            Date second = new Date();
            setStat(tempList.size(), first, second);
            realm.close();
        } catch (Exception e) {
            mListView.setVisibilityProgressBar(false);
            mListView.setVisibilityUsersList(false);
            mListView.setTextIntoTextView("ошибка БД: " + e.getMessage());
        }
    }


    private void setStat(int count, Date first, Date second) {
        mListView.makeToast("количество = " + count +
                "\n милисекунд = " + (second.getTime() - first.getTime()));
    }

    private void downloadOneUrl(Call<List<Model>> call) throws IOException {
        call.enqueue(new Callback<List<Model>>() {

            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        Log.e(TAG, "response.body().size()" + response.body().size());
                        Model curModel = null;
                        mListView.appendIntoTextView("\nколичество = " + response.body().size() +
                                "\n-----------------");
                        for (int i = 0; i < response.body().size(); i++) {
                            curModel = response.body().get(i);
                            modelList.add(curModel);
                        }
                    }
                } else {
                    Log.e(TAG, "response == null");
                    System.out.println("onResponse error: " + response.code());
                    mListView.setTextIntoTextView("onResponse error: " + response.code());
                }
                mListView.setVisibilityProgressBar(false);
                mListView.setVisibilityUsersList(true);
                mListView.initGUI();
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                System.out.println("onFailure " + t);
                mListView.setTextIntoTextView("onFailure " + t.getMessage());
                mListView.setVisibilityProgressBar(false);
            }
        });
    }
}
