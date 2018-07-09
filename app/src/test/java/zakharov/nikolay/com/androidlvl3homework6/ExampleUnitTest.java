package zakharov.nikolay.com.androidlvl3homework6;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void presenterTest() throws Exception {
        ListFragment listFragment = new ListFragment();
        //ListView listView = mock(ListView.class);
        //Model model = mock(Model.class);

        DaggerNetModule daggerAppComponent = new DaggerNetModule();
        Presenter presenter = new Presenter(listFragment);
        presenter.networkInfo = daggerAppComponent.getNetworkInfo();
        presenter.call = daggerAppComponent.getCall(daggerAppComponent.getRetrofit());

        presenter.downloadResponse();

        try {
            presenter.readResponse(presenter.response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.verifyNoMoreInteractions(listFragment);
        presenter.load();
        try {
            Mockito.verify(presenter).downloadOneUrl(presenter.call);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.verify(listFragment).appendIntoTextView("\nколичество = " + 30 +
                "\n-----------------");
    }
}