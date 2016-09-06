package email.crappy.ssao.ruoka.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.ui.list.WeekAdapter;
import email.crappy.ssao.ruoka.ui.view.DateBoxView;
import io.github.prashantsolanki3.shoot.Shoot;
import io.github.prashantsolanki3.shoot.listener.OnShootListener;

/**
 * @author Santeri Elo
 */
public class HomeFragment extends MvpLceFragment<NestedScrollView, Map<Integer, List<FoodItem>>, HomeMvpView, HomePresenter> implements HomeMvpView {
    private Unbinder unbinder;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.recycler_weeks)
    RecyclerView recyclerView;

    @BindView(R.id.next_container)
    LinearLayout nextContainer;

    @BindView(R.id.next_empty)
    TextView nextEmpty;

    @BindView(R.id.next_date)
    DateBoxView dateBox;

    @BindView(R.id.next_food)
    TextView nextFood;

    @BindView(R.id.next_food_secondary)
    TextView nextFoodVeg;

    @BindView(R.id.ad_bottom)
    AdView bottomAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return SSKKYApplication.get(getContext()).component().presenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new WeekAdapter(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        // Load content
        loadData(false);

        // Show sexy Toast about update
        Shoot.once(Shoot.APP_VERSION, "WHAT_IS_THIS", new OnShootListener() {
            @Override
            public void onExecute(int i, String s, int i1) {
                Toast.makeText(getContext(), R.string.app_update, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(Map<Integer, List<FoodItem>> data) {
        ((WeekAdapter) recyclerView.getAdapter()).setItems(data);
    }


    @Override
    public void setNext(FoodItem next) {
        dateBox.setDay(next.date);
        dateBox.setDate(next.date);

        nextFood.setText(next.food);
        nextFoodVeg.setText(next.secondaryFood);

        TransitionManager.beginDelayedTransition(nextContainer);
        nextEmpty.setVisibility(View.GONE);
        dateBox.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNextEmpty() {
        TransitionManager.beginDelayedTransition(nextContainer);
        nextEmpty.setVisibility(View.VISIBLE);
        dateBox.setVisibility(View.GONE);
    }

    @Override
    public void loadAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("84275ACA55FEE01D25BACD7DC1A42F7A")
                .build();

        bottomAd.loadAd(adRequest);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.load();
    }
}
