package zakharov.nikolay.com.androidlvl3homework6;

/**
 * Created by user on 05.07.2018.
 */

public interface UserView {

    void setVisibilityProgressBar(boolean visibility);

    void setTextIntoReposTextView(String string);

    void appendIntoReposTextView(String string);

    void makeToast(String string);

}
