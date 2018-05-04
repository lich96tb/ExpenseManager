package ict.com.expensemanager.ui.event;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/19/2018.
 */

public class FinishEventFragment extends Fragment implements IDataBack {
    @BindView(R.id.recycler_view_finish_event)
    RecyclerView recyclerViewFinishEvent;
    Unbinder unbinder;
    private View rootView;
    private LinearLayoutManager llm;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private AppDatabase db;
    TestRunning testRunning;
    private boolean check = false;
    int idUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_finish_event, container, false);
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
        if (check) {
            updateData update = new updateData();
            update.execute();
        }
        check = true;


    }

    private void initialize() {
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        idUser = sharedPreferences.getInt(AppKey.KEY_USER_ID,0);
        testRunning = new TestRunning();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppKey.ACTION_UPDATE);
        getActivity().registerReceiver(testRunning, intentFilter);
        llm = new LinearLayoutManager(getContext());
        eventList = new ArrayList<>();
        recyclerViewFinishEvent.setLayoutManager(llm);
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .build();

        updateData update = new updateData();
        update.execute();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(testRunning);
        unbinder.unbind();
    }

    @Override
    public void dataBack(int position) {
        int event = eventList.get(position).getIdEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(AppKey.ID_EVENT, event);
        DetainEventFragment detainEventFragment = new DetainEventFragment();
        detainEventFragment.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(android.R.id.content, detainEventFragment)
                .addToBackStack(null)
                .commit();
    }

    private class updateData extends AsyncTask<Void, List<Event>, List<Event>> {


        @Override
        protected List<Event> doInBackground(Void... voids) {
            eventList = db.eventDao().getFinishEvent(idUser);
            Collections.reverse(eventList);
            return eventList;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            eventAdapter = new EventAdapter(getContext(), events);
            eventAdapter.setDataBack(FinishEventFragment.this);
            recyclerViewFinishEvent.setAdapter(eventAdapter);

        }
    }

    public class TestRunning extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                switch (action) {
                    case AppKey.ACTION_UPDATE:
                        updateData update = new updateData();
                        update.execute();

                        break;
                    default:
                        break;
                }
            }
        }
    }
}
