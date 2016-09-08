package email.crappy.ssao.ruoka.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.lukaspili.reactivebilling.ReactiveBilling;
import com.github.lukaspili.reactivebilling.model.PurchaseType;
import com.github.lukaspili.reactivebilling.response.GetPurchasesResponse;
import com.github.lukaspili.reactivebilling.response.PurchaseResponse;
import com.github.lukaspili.reactivebilling.response.Response;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.home.HomeFragment;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    @Inject
    DataManager dataManager;

    private Subscription billingSubscription;
    private static final String BILLING_ID = "feelgood";

    public static Intent createIntent(Context context, boolean newTask) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        if (dataManager.getPreferencesHelper().getIsMadde()) {
            // TODO: Make this easter egg more fun
            setTheme(R.style.Theme_SSKKY_Madde);
        }

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, HomeFragment.newInstance())
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();

            dataManager.setAlarm(this);
        }

        billingSubscription = ReactiveBilling.getInstance(this).purchaseFlow()
                .subscribe(response -> {
                    if (response.isSuccess()) {
                        Timber.i("Product purchased succesfully - $$$");
                        response.getPurchase();
                    } else {
                        Timber.e("Product purchase failed");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (billingSubscription != null) {
            billingSubscription.unsubscribe();
            billingSubscription = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(dataManager.getPreferencesHelper().getIsDebug() ? R.menu.activity_main_debug : R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                break;
            case R.id.action_donate:
                ReactiveBilling.getInstance(this)
                        .startPurchase(BILLING_ID, PurchaseType.PRODUCT, null, null)
                        .compose(dataManager.schedulers())
                        .subscribe(response -> {
                            if (response.isSuccess()) {
                                Timber.d("Purchase flow started");
                            } else {
                                Timber.e("Couldn't start purchase flow");
                            }
                        }, throwable -> {
                            Timber.e(throwable, "Couldn't start purchase flow");
                        });
                break;
            case R.id.action_clear:
                dataManager.getPreferencesHelper().clear();
                startActivity(createIntent(this, true));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
