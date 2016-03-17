package email.crappy.ssao.ruoka.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import butterknife.Bind;
import email.crappy.ssao.ruoka.BuildConfig;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import timber.log.Timber;

/**
 * Container for all list fragments
 *
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    @Inject
    DataManager dataManager;
    @Bind(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) { // Printing preferences for debug purposes
            Timber.i("Should show notifications: %s", dataManager.getPreferencesHelper().getNotificationsEnabled());
            Timber.i("Should show ads: %s", dataManager.getPreferencesHelper().getShowAds());
            Timber.i("Should debug: %s", dataManager.getPreferencesHelper().getIsDebug());
            Timber.i("Layout to show: %s", dataManager.getPreferencesHelper().getMenuLayout().name());
        }

        if (dataManager.getPreferencesHelper().getIsDebug()) {
            dataManager.getWeeks().subscribe(weeks -> {
                Timber.i("getWeeks returned next");
                for (Week week : weeks) {
                    Timber.i("week %s with title %s", weeks.indexOf(week), week.title);
                }
            }, throwable -> Timber.e(throwable, "getWeeks error"), () -> Timber.i("getWeeks completed"));
        }

        if (savedInstanceState == null) {
            // TODO: Content fragment
        }

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
