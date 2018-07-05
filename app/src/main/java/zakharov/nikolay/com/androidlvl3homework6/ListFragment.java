package zakharov.nikolay.com.androidlvl3homework6;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 04.07.2018.
 */

public class ListFragment extends Fragment implements ListView {
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

    List<Model> modelList = new ArrayList<>();
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_fragment, container, false);
        mPresenter = new Presenter(this);
        appComponent = buildComponent();
        appComponent.injectsToListFragment(this);
        appComponent.injectsToPresenter(mPresenter);
        initGUI();
        SugarContext.init(getActivity());
        mPresenter.load();
        return v;
    }

    public AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .daggerNetModule(new DaggerNetModule(getActivity()))
                .build();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
    }

    @Override
    public void initGUI() {
        mInfoTextView = (TextView) v.findViewById(R.id.tvLoad);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        btnLoad = (Button) v.findViewById(R.id.btnLoad);
        btnSaveAllSugar = (Button) v.findViewById(R.id.btnSaveAllSugar);
        btnSelectAllSugar = (Button) v.findViewById(R.id.btnSelectAllSugar);
        btnDeleteAllSugar = (Button) v.findViewById(R.id.btnDeleteAllSugar);
        btnSaveAllRealm = (Button) v.findViewById(R.id.btnSaveAllRealm);
        btnSelectAllRealm = (Button) v.findViewById(R.id.btnSelectAllRealm);
        btnDeleteAllRealm = (Button) v.findViewById(R.id.btnDeleteAllRealm);

        btnLoad.setOnClickListener(v1 -> mPresenter.load());

        btnSaveAllSugar.setOnClickListener(view -> mPresenter.saveAllSugar());
        btnSelectAllSugar.setOnClickListener(view -> mPresenter.selectAllSugar());
        btnDeleteAllSugar.setOnClickListener(view -> mPresenter.deleteAllSugar());

        btnSaveAllRealm.setOnClickListener(view -> mPresenter.saveAllRealm());
        btnSelectAllRealm.setOnClickListener(view -> mPresenter.selectAllRealm());
        btnDeleteAllRealm.setOnClickListener(view -> mPresenter.deleteAllRealm());

        //RecyclerView и Adapter
        usersList = (RecyclerView) v.findViewById(R.id.list_recycler_view);
        adapter = new UserAdapter(modelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager(linearLayoutManager);
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
            holder.avatar.setOnClickListener(view -> {
                startUserFragment(model);
            });
            holder.usersName.setOnClickListener(view -> {
                startUserFragment(model);
            });
        }


        @Override
        public int getItemCount() {
            if (models != null && models.size() != 0) {
                return models.size();
            }
            return 0;
        }
    }

    private void startUserFragment(Model model) {
        ModelComponent modelComponent = DaggerModelComponent.builder()
                .daggerModelModule(new DaggerModelModule(getActivity(), model))
                .build();

        UserFragment fragment = new UserFragment();
        UserFragmentPresenter userFragmentPresenter = new UserFragmentPresenter(fragment);
        fragment.setPresenter(userFragmentPresenter);
        modelComponent.injectsUserFragment(fragment);
        modelComponent.injectsToUserFragmentPresenter(userFragmentPresenter);
        ((MainActivity) getActivity()).startFragment(fragment);
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
    public void makeToast(String string) {
        Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public List<Model> getModelList() {
        return modelList;
    }
}
