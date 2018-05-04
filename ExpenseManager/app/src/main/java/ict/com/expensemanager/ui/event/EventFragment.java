package ict.com.expensemanager.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/19/2018.
 */

public class EventFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tab_layout_event)
    TabLayout tabLayoutEvent;
    @BindView(R.id.view_pager_event)
    ViewPager viewPagerEvent;
    Unbinder unbinder;
    @BindView(R.id.fabtn_add)
    FloatingActionButton fabtnAdd;
    private View rootView;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Home)getActivity()).getSupportActionBar().setTitle("Danh sách sự kiện");

    }

    private void initialize() {
        FragmentManager fragmentManager = getChildFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPagerEvent.setAdapter(viewPagerAdapter);
        tabLayoutEvent.setupWithViewPager(viewPagerEvent);
        fabtnAdd.setOnClickListener(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_add:
                addEvent();
                break;
            default:
                break;
        }
    }

    private void addEvent() {
        Intent intent = new Intent(getContext(), AddEventActivity.class);
        startActivity(intent);
    }
}
