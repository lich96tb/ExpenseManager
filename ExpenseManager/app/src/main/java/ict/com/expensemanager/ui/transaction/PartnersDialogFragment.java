package ict.com.expensemanager.ui.transaction;


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.tokenautocomplete.TokenCompleteTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Partner;
import ict.com.expensemanager.ui.base.BaseDialogFragment;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnersDialogFragment extends BaseDialogFragment implements View.OnClickListener,TokenCompleteTextView.TokenListener{


    @BindView(R.id.edit_text_partner_name)
    PartnerCompletionView editTextPartnerName;
    @BindView(R.id.button_accept)
    Button buttonAccept;
    @BindView(R.id.button_cancel)
    Button buttonCancel;
    Unbinder unbinder;


    private InteractionWithPartnersDialog interaction = null;
    private List<Partner> partnersSelected = null;
    private List<Partner> partners = null;
    private ArrayAdapter<Partner> adapter = null;

    public PartnersDialogFragment() {
        // Required empty public constructor
    }

    public static PartnersDialogFragment newInstance(List<Partner> partnersSelected) {

        Bundle args = new Bundle();
        args.putSerializable(AppKey.KEY_LIST_PARTNER_SELECTED, (Serializable) partnersSelected);

        PartnersDialogFragment fragment = new PartnersDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransactionAddingActivity) {
            interaction = (InteractionWithPartnersDialog) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partners = getPartnersFromContacts();
        //

        Bundle bundle = getArguments();
        if (bundle != null) {
            partnersSelected  = (List<Partner>) bundle.getSerializable(AppKey.KEY_LIST_PARTNER_SELECTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partners_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUiEvents();
        setupUis();

    }

    private void setupUis() {
        if (partners == null) {
            partners = new ArrayList<>();
        }

        adapter = new ArrayAdapter<Partner>(getActivity(), android.R.layout.simple_list_item_1, partners);
        editTextPartnerName.setAdapter(adapter);
        editTextPartnerName.allowDuplicates(false);
        //
        if (partnersSelected != null && !partnersSelected.isEmpty()) {

            for(Partner partner : partnersSelected) {
                editTextPartnerName.addObject(partner);
            }
        }

    }

    private void setupUiEvents() {
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        editTextPartnerName.setTokenListener(this);
        //



    }




    private List<Partner> getPartnersFromContacts() {
        List<Partner> myPartners = new ArrayList<>();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);


                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId));
                    Uri photoUri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.DISPLAY_NAME);


                    Partner partner = new Partner();
                    partner.setPartnerName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));


                    myPartners.add(partner);
                }
            }
        }

        cursor.close();

        return myPartners;

    }

    @Override
    public void onResume() {
        Commons.setSizeDialog(getDialog(),0.85);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interaction = null;
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

        if (partnersSelected == null || partnersSelected.isEmpty()) {

            showMessage("Chưa có đối tác nào được chọn, mời chọn lại!");
        } else {

            interaction.setPartners(partnersSelected);
            showMessage("Chọn thành công");
            dismiss();
        }

    }

    private void onClickButtonCancel() {
        dismiss();
    }


    @Override
    public void onTokenAdded(Object token) {

        if (partnersSelected == null) {
            partnersSelected = new ArrayList<>();
        }

        Partner partner = (Partner) token;

        if (!partnersSelected.contains(partner)) {
            partnersSelected.add(partner);
        }



    }

    @Override
    public void onTokenRemoved(Object token) {

        if (partnersSelected != null) {

            Partner partner = (Partner) token;
            partnersSelected.remove(partner);


        }
    }

    public interface InteractionWithPartnersDialog {

        void setPartners(List<Partner> partners);
    }
}
