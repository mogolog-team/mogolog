package com.team.mogolog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_SELECTED_TAB = "selected_tab";
    private static final String TAG_HOME = "tab_home";
    private static final String TAG_EXAM_LIST = "tab_exam_list";
    private static final String TAG_COMMUNITY = "tab_community";

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        applyWindowInsets();

        bottomNavigation.setOnItemSelectedListener(item -> showTab(item.getItemId()));
        int selectedTab = savedInstanceState == null
                ? R.id.nav_home
                : savedInstanceState.getInt(STATE_SELECTED_TAB, R.id.nav_home);
        bottomNavigation.setSelectedItemId(selectedTab);
        // Register after the initial selection: the menu already checks Home on inflation.
        // Reselecting a tab afterwards leaves its current screen untouched.
        bottomNavigation.setOnItemReselectedListener(item -> { });
    }

    private void applyWindowInsets() {
        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets safeArea = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout());
            // The root owns the top/side insets; the navigation bar owns the bottom.
            view.setPadding(safeArea.left, safeArea.top, safeArea.right, 0);
            return insets;
        });

        int navigationHeight = getResources().getDimensionPixelSize(R.dimen.bottom_nav_height);
        // Replace Material's default inset listener so bottom spacing is applied once.
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigation, (view, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout()).bottom;
            view.setPadding(0, 0, 0, bottomInset);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params.height != navigationHeight + bottomInset) {
                params.height = navigationHeight + bottomInset;
                view.setLayoutParams(params);
            }
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    private boolean showTab(int itemId) {
        String targetTag;
        if (itemId == R.id.nav_home) {
            targetTag = TAG_HOME;
        } else if (itemId == R.id.nav_records) {
            targetTag = TAG_EXAM_LIST;
        } else if (itemId == R.id.nav_community) {
            targetTag = TAG_COMMUNITY;
        } else {
            return false;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.isStateSaved()) {
            return false;
        }

        Fragment target = fragmentManager.findFragmentByTag(targetTag);
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true);
        for (String tag : new String[]{TAG_HOME, TAG_EXAM_LIST, TAG_COMMUNITY}) {
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null && fragment != target) {
                transaction.hide(fragment);
                transaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
            }
        }

        if (target == null) {
            if (itemId == R.id.nav_home) {
                target = new HomeFragment();
            } else if (itemId == R.id.nav_records) {
                target = new ExamListFragment();
            } else {
                target = new CommunityFragment();
            }
            transaction.add(R.id.fragment_container, target, targetTag);
        } else {
            transaction.show(target);
        }
        transaction.setMaxLifecycle(target, Lifecycle.State.RESUMED);
        transaction.setPrimaryNavigationFragment(target);
        // Tabs have no back stack. Commit immediately to avoid duplicate fragments on rapid taps.
        transaction.commitNow();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_TAB, bottomNavigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }
}
