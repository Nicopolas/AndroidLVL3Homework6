package zakharov.nikolay.com.androidlvl3homework6;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("zakharov.nikolay.com.androidlvl3homework6", appContext.getPackageName());
    }

    @Test(expected = NullPointerException.class)
    public void serviceTestWithConnection() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ListFragment listFragment = mock(ListFragment.class);
        //ListView listView = mock(ListView.class);
        //Model model = mock(Model.class);

        AppComponent appComponent = DaggerAppComponent.builder()
                .daggerNetModule(new DaggerNetModule(appContext))
                .build();
        Presenter presenter = new Presenter(listFragment);
        appComponent.injectsToPresenter(presenter);
        try {
            presenter.readResponse(presenter.response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.verifyNoMoreInteractions(listFragment);
        presenter.load();
        try {
            Mockito.verify(presenter).readResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.verify(listFragment).appendIntoTextView("\nколичество = " + 30 +
                "\n-----------------");
    }
}
