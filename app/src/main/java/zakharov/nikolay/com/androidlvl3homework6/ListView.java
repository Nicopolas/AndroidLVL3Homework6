package zakharov.nikolay.com.androidlvl3homework6;

import java.util.List;

import retrofit2.Call;

/**
 * Created by user on 04.07.2018.
 */

public interface ListView {
    void appendIntoTextView(String str);
    void setTextIntoTextView(String str);
    void setVisibilityProgressBar(boolean visibility);
    void setVisibilityUsersList(boolean visibility);
    void makeToast(String string);
    void initGUI();
    List<Model> getModelList();
}
