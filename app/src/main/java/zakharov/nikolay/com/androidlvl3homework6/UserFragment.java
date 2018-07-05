package zakharov.nikolay.com.androidlvl3homework6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by user on 04.07.2018.
 */

public class UserFragment extends Fragment implements OnBackPressedListener, UserView {
    private final String TAG = getClass().getSimpleName();
    private View v;
    private ImageView avatar;
    private TextView name;
    private TextView repos;
    private ProgressBar progressBar;
    private UserFragmentPresenter mPresenter;

    @Inject
    Model model;
    @Inject
    Call<List<ReposModel>> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        if (model == null) {
            Log.e(TAG, "Инекция модели не отработала");
            onBackPressed();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_fragment, container, false);
        avatar = v.findViewById(R.id.user_fragment_avatar);
        name = v.findViewById(R.id.user_fragment_name);
        repos = v.findViewById(R.id.user_fragment_repos);
        progressBar = v.findViewById(R.id.progressBar);

        setImage(avatar, model.getAvatar());
        name.setText(model.getLogin());
        mPresenter.load();

        return v;
    }

    public void setPresenter(UserFragmentPresenter userFragmentPresenter) {
        this.mPresenter = userFragmentPresenter;
    }

    private void setImage(ImageView image, String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(image);
    }

    @Override
    public void onBackPressed() {
        //обработка нажатия в Navigation Drawer
        ((MainActivity) getActivity()).startFragment(new ListFragment());
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
    public void setTextIntoReposTextView(String string) {
        repos.setText("");
        repos.setText(string);
    }

    @Override
    public void appendIntoReposTextView(String string) {
        repos.append(string);
    }

    @Override
    public void makeToast(String string) {
        Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        toast.show();
    }
}
