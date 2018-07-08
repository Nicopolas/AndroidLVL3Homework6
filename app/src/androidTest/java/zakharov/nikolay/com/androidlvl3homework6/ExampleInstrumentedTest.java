package zakharov.nikolay.com.androidlvl3homework6;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

/*      InfoView view = Mockito.mock(InfoView.class);
        InfoModel model = Mockito.mock(InfoModel.class);
        Mockito.when(model.lifecycle()).thenReturn(Observable.empty());
        InfoPresenter presenter = new InfoPresenterImpl(model);
        presenter.attachView(view);
        Mockito.verifyZeroInteractions(view);*/


        Context appContext = InstrumentationRegistry.getTargetContext();
        ListFragment listFragment = new ListFragment();
        DaggerNetModule daggerNetModule = new DaggerNetModule(appContext);
        Presenter presenter = new Presenter(listFragment);
        checkCallNull(presenter);
        checkCallStatusCode(presenter, daggerNetModule.getCall(daggerNetModule.getRetrofit()), listFragment);
    }

    private void checkCallNull(Presenter presenter) {
        try {
            presenter.downloadOneUrl(null);
        } catch (NullPointerException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkCallStatusCode(Presenter presenter, Call call, ListView listView) {
            presenter.networkInfo = true;
            try {
                presenter.downloadOneUrl(call);
                thrown.expect(IllegalStateException.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        while (call.isExecuted()) {
            call.enqueue(new Callback<List<Model>>() {
                @Override
                public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                    if (response.code() != 200) {
                        Mockito.verify(listView).setTextIntoTextView("onResponse error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Model>> call, Throwable t) {
                    Mockito.verify(listView).setTextIntoTextView("onFailure " + t.getMessage());
                }
            });
        }
    }

    @Test
    public void serviceTestWithoutConnecting() {
        Comparable c = mock(Comparable.class);
    }
}
