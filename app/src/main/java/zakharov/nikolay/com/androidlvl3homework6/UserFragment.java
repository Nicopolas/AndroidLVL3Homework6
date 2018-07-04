package zakharov.nikolay.com.androidlvl3homework6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by user on 04.07.2018.
 */

public class UserFragment extends Fragment implements OnBackPressedListener{
    private final String TAG = getClass().getSimpleName();
    View v;

    @Inject
    Model model;

/*    @Inject
    Call<List<ReposModel>> callRepos;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_fragment, container, false);

        if (model != null){
            Log.e(TAG, "Все ок");
        }

        return v;
    }

    @Override
    public void onBackPressed() {
        //обработка нажатия в Navigation Drawer
        ((MainActivity) getActivity()).startFragment(new ListFragment());
    }
}
