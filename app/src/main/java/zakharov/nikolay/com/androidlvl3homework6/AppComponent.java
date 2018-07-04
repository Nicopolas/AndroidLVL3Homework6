package zakharov.nikolay.com.androidlvl3homework6;


import android.content.Context;

import dagger.Component;


@Component(modules = {DaggerNetModule.class})
public interface AppComponent {
    void injectsToListFragment(ListFragment listFragment);
    void injectsToPresenter(Presenter presenter);
}

