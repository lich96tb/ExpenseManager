package ict.com.expensemanager.ui.slide_menu;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.RepeatType;
import ict.com.expensemanager.ui.ListTransactionAdapter;
import ict.com.expensemanager.ui.ListTransactionFragment;
import ict.com.expensemanager.ui.category.ListCategoryFragment;
import ict.com.expensemanager.ui.event.EventFragment;
import ict.com.expensemanager.ui.report.ReportActivity;
import ict.com.expensemanager.ui.transaction.TransactionAddingActivity;
import ict.com.expensemanager.ui.wallet.ListWalletFragment;
import ict.com.expensemanager.util.AppKey;

public class Home extends AppCompatActivity {
    public Toolbar homeToolbar;
    DrawerLayout homeDrawer;
    NavigationView homeNavigation;
    ListView lv;
    ArrayList<Slinemenu> arrayListMenu;
    SlineMenuAdapter adapter;
    int id;
    String nameUser;
    TextView txtNameUser;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        homeToolbarSetup();
        Event();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (homeToolbar.getVisibility() == View.GONE) {
            homeToolbar.setVisibility(View.VISIBLE);
        }
    }


    private void init() {
        txtNameUser = findViewById(R.id.txtNameUser);
        homeDrawer = findViewById(R.id.home_Drawer);
        homeToolbar = findViewById(R.id.home_Toolbar);
        setSupportActionBar(homeToolbar);
        homeNavigation = findViewById(R.id.home_Navigation);
        lv = findViewById(R.id.lv);
        Bundle bundle = getIntent().getBundleExtra(AppKey.KEY_BUNDLE);
        id = bundle.getInt(AppKey.KEY_USER_ID);
        nameUser = bundle.getString("name");

        FragmentManager manager = getSupportFragmentManager();
        ListTransactionFragment listTransactionFragment = new ListTransactionFragment();
        listTransactionFragment.setIdUser(id);
        manager.beginTransaction()
                .replace(R.id.Frame_Content, listTransactionFragment, AppKey.LIST)
                .commit();
        addRepeatType();



    }


    private void addRepeatType() {
        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, AppKey.NAME_DATABASE).allowMainThreadQueries().build();
        int count = db.repeatTypeDao().getCountRepeattype();
        if (count == 0) {
            RepeatType repeatType = new RepeatType(1, "Tháng");
            RepeatType repeatType2 = new RepeatType(2, "Tuần");
            RepeatType repeatType3 = new RepeatType(3, "Ngày");

            db.repeatTypeDao().addRepeatType(repeatType, repeatType2, repeatType3);
        }

    }

    private void Event() {
        txtNameUser.setText(nameUser);
        arrayListMenu = new ArrayList<>();
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_transactions, "Thêm giao dịch"));
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_category, "Danh mục"));
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_wallet, "Ví"));
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_event, "Sự kiện"));
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_report, "Báo cáo"));
        arrayListMenu.add(new Slinemenu(R.drawable.drawable_sign_out, "Đăng xuất"));
        adapter = new SlineMenuAdapter(this, R.layout.item_menu, arrayListMenu);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (i) {
                    case 0:
                        Intent intent = new Intent(Home.this, TransactionAddingActivity.class);
                        intent.putExtra(AppKey.KEY_USER_ID, id);
                        startActivity(intent);
                        break;
                    case 1:
                        fragmentManager.beginTransaction()
                                .replace(R.id.Frame_Content, new ListCategoryFragment(), AppKey.LIST)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 2:

                        fragmentManager.beginTransaction()
                                .replace(R.id.Frame_Content, new ListWalletFragment(),AppKey.LIST)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 3:

                        fragmentManager.beginTransaction()
                                .replace(R.id.Frame_Content, new EventFragment(),AppKey.LIST)
                                .addToBackStack(null)
                                .commit();

                        break;
                    case 4:
                        Intent intent1 = new Intent(Home.this, ReportActivity.class);
                        startActivity(intent1);
                        break;
                    case 5:
                        SharedPreferences sharedPreferences = getSharedPreferences(AppKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(AppKey.USER_NAME, "");
                        editor.putString(AppKey.USER_PASS, "");
                        editor.apply();
                        finish();
                        break;
                }
                homeDrawer.closeDrawers();

            }
        });
    }

    private void homeToolbarSetup() {
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(AppKey.NAME_DATABASE);
        homeToolbar.setNavigationIcon(R.drawable.ic_navigation);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeDrawer.openDrawer(Gravity.START);
            }
        });
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            Toast.makeText(this, "Xin hãy đăng xuất", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
        Fragment fragment = fragmentManager.findFragmentByTag(AppKey.LIST);{
            if(fragment != null){
                if(fragment instanceof ListTransactionFragment){
                    getSupportActionBar().setTitle("Danh sách giao dịch");
                }else if(fragment instanceof ListCategoryFragment){
                    getSupportActionBar().setTitle("Danh sách danh mục");
                }else if(fragment instanceof ListWalletFragment){
                    getSupportActionBar().setTitle("Danh sách ví");
                }else if(fragment instanceof EventFragment){
                    getSupportActionBar().setTitle("Danh sách sự kiện");
                }
            }
        }


    }

}
