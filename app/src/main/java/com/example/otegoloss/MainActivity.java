package com.example.otegoloss;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;
import static com.example.otegoloss.R.id.fragmentHome;
import static com.example.otegoloss.R.id.searchFragment;
import static com.example.otegoloss.R.id.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.otegoloss.databinding.ActivityMainBinding;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.otegoloss.home.ViewProduct;
import com.example.otegoloss.home.ViewSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_home);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_mypage, R.id.navigation_shipping, R.id.navigation_user)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        LinearLayout view = findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //メニューを生成
        getMenuInflater().inflate(R.menu.search_menu, menu);

        //MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.app_bar_search) {

//                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.nav_host_fragment_activity_search);
//                    NavController navController = navHostFragment.getNavController();
                    //navController.navigate(R.id.action_mainActivity_to_searchFragment);

                    //Navigation.findNavController().navigate(R.id.action_mainActivity_to_searchFragment);
//


                    //NavHostFragment.findNavController(new searchFragment());

//                    FragmentManager fm_item = getSupportFragmentManager();
//                    FragmentTransaction t_item = fm_item.beginTransaction();
//
//                    // 次のFragment
//                    Fragment secondFragment = new ViewSearch();
//                    // bundleを次のfragmentに設定
//                    // fragmentManagerに次のfragmentを追加
//                    t_item.replace(R.id.container, secondFragment);
//                    // 画面遷移戻りを設定
//                    t_item.addToBackStack(null);
//                    // 画面遷移
//                    t_item.commit();
//                    return true;
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // Fragmentを表示させるメソッドを定義（表示したいFragmentを引数として渡す）
    private void addFragment(Fragment fragment) {
        // フラグメントマネージャーの取得
        FragmentManager manager = getSupportFragmentManager();
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = manager.beginTransaction();
        // MainFragmentを追加
        transaction.add(R.id.fragmentUser, fragment);
        // フラグメントトランザクションのコミット。コミットすることでFragmentの状態が反映される
        transaction.commit();
    }

    // 戻るボタン「←」をアクションバー（上部バー）にセットするメソッドを定義
    public void setupBackButton(boolean enableBackButton) {
        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーに戻るボタン「←」をセット（引数が true: 表示、false: 非表示）
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(enableBackButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
