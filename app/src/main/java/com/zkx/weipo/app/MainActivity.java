package com.zkx.weipo.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private List<TestData> testDatas;

    private void initData(){
        testDatas=new ArrayList<TestData>();
        for (int i=0;i<10;i++){
            testDatas.add(new TestData("TestData"+i));
        }
    }

    private void initAdapter(){
        mRecyclerViewAdapter=new MyRecyclerViewAdapter(testDatas);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void initViews(){
        mDrawerLayout=(DrawerLayout)findViewById(R.id.id_DrawerLayout);
        mToolbar=(Toolbar)findViewById(R.id.id_Toolbar);
        mNavigationView=(NavigationView)findViewById(R.id.id_NavigationView);
        mRecyclerView=(RecyclerView)findViewById(R.id.id_RecyclerView);
    }

    private void confViews(){
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mNavigationView.inflateHeaderView(R.layout.navi_header);
        mNavigationView.inflateMenu(R.menu.menu_nav);
        onNavigationViewMenuItemSelected(mNavigationView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        confViews();
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNavigationViewMenuItemSelected(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

}
