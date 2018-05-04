package ict.com.expensemanager.ui.category;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.util.AppKey;


public class IconCategoryFragment extends Fragment {

    @BindView(R.id.image_back_category_icon)
    ImageView imageBack;
    @BindView(R.id.gridview_icon_category)
    GridView gvIcon;
    Unbinder unbinder;

    private Integer[] iconId;
    private IconAdapter adapter;
    private FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icon_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iconId = new Integer[]{R.drawable.ic_airplanes,
                R.drawable.ic_book,
                R.drawable.ic_fashion,
                R.drawable.ic_gas_station,
                R.drawable.ic_coffee,
                R.drawable.ic_pet,
                R.drawable.ic_taxi,
                R.drawable.ic_pacifier,
                R.drawable.ic_drops,
                R.drawable.ic_gas,
                R.drawable.ic_internet,
                R.drawable.ic_ping_pong,
                R.drawable.ic_smartphone,
                R.drawable.ic_pills,
                R.drawable.ic_care,
                R.drawable.ic_mortgage
        };
        adapter = new IconAdapter(getContext(), iconId);
        gvIcon.setAdapter(adapter);
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(AppKey.ICON, iconId[i]);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                manager = getFragmentManager();
                manager.popBackStack();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getFragmentManager();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
