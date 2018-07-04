package zakharov.nikolay.com.androidlvl3homework6;

import dagger.Component;

@Component(modules = {DaggerModelModule.class})
public interface ModelComponent {
    void injectsUserFragment(UserFragment userFragment);
}
