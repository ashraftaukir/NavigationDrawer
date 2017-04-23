package info.androidhive.navigationdrawer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.adapter.GridViewAdapter;
import info.androidhive.navigationdrawer.asynctask.Getdata;
import info.androidhive.navigationdrawer.asynctask.ParseDataTask;
import info.androidhive.navigationdrawer.model.MyDataModel;
import info.androidhive.navigationdrawer.myinterface.GetDataCallBack;
import info.androidhive.navigationdrawer.myinterface.GetDataNextPageCallBack;
import info.androidhive.navigationdrawer.utils.InternetConnection;
import info.androidhive.navigationdrawer.utils.PaginationScrollListener;


public class GridViewActivity extends AppCompatActivity implements GetDataCallBack, GetDataNextPageCallBack, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final int PAGE_START = 1;
    private static final String TAG = "ACD";
    RecyclerView gridview;
    GridLayoutManager gridLayoutManager;
    GridViewAdapter gridViewAdapter;
    int pageNumber;
    private boolean itemsetupChecker = false;
    private ArrayList<MyDataModel> informationArraylist;
    private int currentPage = PAGE_START;
    private SwipeRefreshLayout swipe_refresh_layout;
    private int TOTAL_PAGES = 20;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitygridview);
        init();
        initListener();
        ApiCall(currentPage);

        gridview.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                if (currentPage <= TOTAL_PAGES) {
                    nextPageApiCall(currentPage);
                }
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void initListener() {
        swipe_refresh_layout.setOnRefreshListener(this);
    }

    private void ApiCall(int pagenumber) {
        Log.d(TAG, "ApiCall: " + "apicall");
        swipe_refresh_layout.setRefreshing(true);
        pageNumber = pagenumber;

        if (InternetConnection.checkConnection(this)) {
            if (currentPage <= TOTAL_PAGES) {
                new ParseDataTask(GridViewActivity.this, pageNumber).execute();
            } else {

                swipe_refresh_layout.setRefreshing(false);
            }
        } else {
            swipe_refresh_layout.setRefreshing(false);
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        informationArraylist = new ArrayList<>();
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        gridview = (RecyclerView) findViewById(R.id.gridview);
        gridLayoutManager = new GridLayoutManager(this, 3);
        gridview.setLayoutManager(gridLayoutManager);
        gridViewAdapter = new GridViewAdapter(informationArraylist);
        gridview.setAdapter(gridViewAdapter);

    }

    @Override
    public void callback(ArrayList<MyDataModel> datalist) {
        if (datalist != null) {
            gridViewAdapter.addAll(datalist);
            itemsetupChecker = true;
            swipe_refresh_layout.setRefreshing(false);
        } else {
            Toast.makeText(this, "Dataset is empty", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void nextpagecallback(ArrayList<MyDataModel> datalist) {

        if (datalist != null) {
            isLoading = false;
            itemsetupChecker = true;
            gridViewAdapter.addAll(datalist);
            if (currentPage > TOTAL_PAGES) {
                isLastPage = true;
            }

            swipe_refresh_layout.setRefreshing(false);

        } else {
            Toast.makeText(this, "Dataset is empty", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onRefresh() {
        isLoading = true;
        if (itemsetupChecker) {
            currentPage += 1;
            nextPageApiCall(currentPage);
        } else {
            ApiCall(currentPage);
        }
    }

    private void nextPageApiCall(int pagenumber) {

        swipe_refresh_layout.setRefreshing(true);
        pageNumber = pagenumber;
        if (InternetConnection.checkConnection(this)) {
            if (currentPage <= TOTAL_PAGES) {
                new Getdata(this, pageNumber).execute();
            } else {
                swipe_refresh_layout.setRefreshing(false);
            }
        } else {
            swipe_refresh_layout.setRefreshing(false);
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
