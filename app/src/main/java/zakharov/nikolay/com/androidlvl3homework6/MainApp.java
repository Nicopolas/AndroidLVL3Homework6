package zakharov.nikolay.com.androidlvl3homework6;


import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orm.SugarContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);

        stechoInit();
    }

    private void stechoInit() {
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}

