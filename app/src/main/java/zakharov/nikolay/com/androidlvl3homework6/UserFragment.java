package zakharov.nikolay.com.androidlvl3homework6;

import android.support.v4.app.Fragment;

/**
 * Created by user on 04.07.2018.
 */

public class UserFragment extends Fragment implements OnBackPressedListener{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onBackPressed() {
        //обработка нажатия в Navigation Drawer
        ((MainActivity) getActivity()).startFragment(new ListFragment());
    }
}
