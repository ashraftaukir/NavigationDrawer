package info.androidhive.navigationdrawer.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.fragment.BottomFragment;
import info.androidhive.navigationdrawer.fragment.HomeFragment;
import info.androidhive.navigationdrawer.fragment.MoviesFragment;
import info.androidhive.navigationdrawer.fragment.NotificationsFragment;
import info.androidhive.navigationdrawer.fragment.PhotosFragment;
import info.androidhive.navigationdrawer.fragment.SettingsFragment;
import info.androidhive.navigationdrawer.fragment.TopFragment;
import info.androidhive.navigationdrawer.myinterface.DataCallback;

public class MainActivity extends AppCompatActivity implements DataCallback, View.OnClickListener {

    private static final String TAG = "abc";
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    TopFragment topFragment;
    BottomFragment bottomFragment;
    FragmentTransaction transaction;
    FrameLayout frame, firstfragment, secondfragment;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private String[] activityTitles;
    private LinearLayout linearLayout;
    private ImageView menuicon;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        initListeners();
        orientationCheck();
        fragmentTransition();
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        setNavHeader();
        setUpNavigationView();

    }

    private void orientationCheck() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams firstparams = firstfragment.getLayoutParams();
            firstparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            firstparams.width = 0;
            firstfragment.setLayoutParams(firstparams);
            ViewGroup.LayoutParams secondparams = secondfragment.getLayoutParams();
            secondparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            secondparams.width = 0;
            firstfragment.setLayoutParams(secondparams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    private void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frame = (FrameLayout) findViewById(R.id.frame);
        firstfragment = (FrameLayout) findViewById(R.id.firstfragment);
        secondfragment = (FrameLayout) findViewById(R.id.secondfragment);
        menuicon = (ImageView) findViewById(R.id.iv_menuicon);
    }

    private void initListeners() {
        menuicon.setOnClickListener(this);

    }

    private void fragmentTransition() {
        topFragment = new TopFragment();
        bottomFragment = new BottomFragment();
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.firstfragment, topFragment, "Frag_Top_tag");
        transaction.add(R.id.secondfragment, bottomFragment, "Frag_Bottom_tag");
        transaction.commit();

    }


    private void setNavHeader() {
        txtName.setText(getResources().getString(R.string.username));
        txtWebsite.setText(getResources().getString(R.string.websitename));

    }

    private void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        firstfragment.setVisibility(View.GONE);
        secondfragment.setVisibility(View.GONE);
        frame.setVisibility(View.VISIBLE);

        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new PhotosFragment();
            case 2:
                return new MoviesFragment();
            case 3:
                return new NotificationsFragment();

            case 4:
                return new SettingsFragment();
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;

                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //this code should be reuse
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//
//        Intent intent = new Intent(this, GridViewActivity.class);
//        startActivity(intent);
//        return super.onMenuOpened(featureId, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int itemid=item.getItemId();
//        if(itemid==R.id.action_gridview){
//            Intent intent = new Intent(this, GridViewActivity.class);
//            this.startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setViewforLandscapemode();

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setViewforPotraitmode();

        } else {
            Log.d("other", "onConfigurationChanged: " + "other");

        }

    }

    private void setViewforPotraitmode() {

        ViewGroup.LayoutParams firstparams = firstfragment.getLayoutParams();
        firstparams.height = 0;
        firstparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        firstfragment.setLayoutParams(firstparams);
        ViewGroup.LayoutParams secondparams = secondfragment.getLayoutParams();
        secondparams.height = 0;
        secondparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        firstfragment.setLayoutParams(secondparams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void setViewforLandscapemode() {
        ViewGroup.LayoutParams firstparams = firstfragment.getLayoutParams();
        firstparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        firstparams.width = 0;
        firstfragment.setLayoutParams(firstparams);
        ViewGroup.LayoutParams secondparams = secondfragment.getLayoutParams();
        secondparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        secondparams.width = 0;
        firstfragment.setLayoutParams(secondparams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void callback(String value) {
        bottomFragment.displayValue(value);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, GridViewActivity.class);
        startActivity(intent);
    }
}
