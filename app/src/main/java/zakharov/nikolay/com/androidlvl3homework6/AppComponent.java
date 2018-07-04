package zakharov.nikolay.com.androidlvl3homework6;


import dagger.Component;


@Component(modules = {DaggerNetModule.class})
public interface AppComponent {
    void injectsToListFragent(ListFragment listFragment);
}

