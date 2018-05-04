package ict.com.expensemanager.ui.transaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.base.BaseDialogFragment;
import ict.com.expensemanager.ui.event.AddEventActivity;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsDialogFragment extends BaseDialogFragment implements
        View.OnClickListener,
        EventsAdapter.OnCreateNewItemClick,
        EventsAdapter.OnMyEventSelected {


    @BindView(R.id.recyclerview_events)
    RecyclerView recyclerviewEvents;
    @BindView(R.id.button_accept)
    Button buttonAccept;
    @BindView(R.id.button_cancel)
    Button buttonCancel;
    Unbinder unbinder;

    private List<Event> events = null;
    private EventsAdapter eventsAdapter = null;
    private InteractionWithEventsDialog interaction = null;
    private int eventPosCurrentSelected = -1;
    private int eventPostPreviousSelected = -1;
    private int userId;


    public EventsDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        userId = sharedPreferences.getInt(AppKey.KEY_USER_ID,0);
    }

    public static EventsDialogFragment newInstance() {

        Bundle args = new Bundle();

        EventsDialogFragment fragment = new EventsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof TransactionAddingActivity) {
            interaction = (InteractionWithEventsDialog) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUiEvents();
        EventsTask eventsTask = new EventsTask(this);
        eventsTask.execute(userId);
    }

    public void setupEventsRecyclerView(List<Event> datas) {
        if (datas != null) {
            events = datas;
        } else {
            events = new ArrayList<>();

        }
        recyclerviewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewEvents.setHasFixedSize(true);

        eventsAdapter = new EventsAdapter(events, this);

        if (events.isEmpty()) {
            eventsAdapter.setOnCreateNewItemClick(this);
        }
        recyclerviewEvents.setAdapter(eventsAdapter);

    }

    private void setupUiEvents() {

        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        Commons.setSizeDialog(getDialog());
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_accept:
                onClickButtonAccept();
                break;

            case R.id.button_cancel:
                onClickButtonCancel();
                break;
        }
    }

    private void onClickButtonAccept() {
        if (eventPosCurrentSelected != -1) {

            interaction.setEventSelected(events.get(eventPosCurrentSelected));
            showMessage("Chọn thành công");
            dismiss();
        } else {
            showMessage("Chưa có sự kiện nào được chọn, mời chọn lại!");
        }
    }

    private void onClickButtonCancel() {
        dismiss();
    }

    @Override
    public void onCategorySelected(View view, boolean isChecked, int position) {

        if (isChecked) {

            if (eventPostPreviousSelected == -1) {
                eventPostPreviousSelected = position;
                eventPosCurrentSelected = position;
                events.get(position).setSelected(true);
                eventsAdapter.notifyItemChanged(position);

            } else {

                events.get(eventPostPreviousSelected).setSelected(false);
                eventsAdapter.notifyItemChanged(eventPostPreviousSelected);

                eventPosCurrentSelected = position;
                events.get(eventPosCurrentSelected).setSelected(true);
                eventsAdapter.notifyItemChanged(eventPosCurrentSelected);

                eventPostPreviousSelected = eventPosCurrentSelected;
            }

        } else {
            events.get(position).setSelected(false);
            eventsAdapter.notifyItemChanged(position);

            eventPostPreviousSelected = -1;
            eventPosCurrentSelected = -1;
        }

    }

    @Override
    public void onClickButtonCreate(View view) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        startActivity(intent);
        dismiss();
    }

    public interface InteractionWithEventsDialog {

        void setEventSelected(Event event);
    }

    static class EventsTask extends AsyncTask<Integer, Void, List<Event>> {

        private WeakReference<EventsDialogFragment> dialogRef;

        public EventsTask(EventsDialogFragment fragment) {
            this.dialogRef = new WeakReference<EventsDialogFragment>(fragment);

        }

        @Override
        protected List<Event> doInBackground(Integer... integers) {
            int userID = integers[0];
            AppDatabase appDatabase = dialogRef.get().getAppDatabase();
            List<Event> events = null;

            if (appDatabase != null) {
                events = appDatabase.eventDao().getEventsStarting(userID);
            }

            return events;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            dialogRef.get().setupEventsRecyclerView(events);
        }
    }
}
