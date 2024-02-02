package com.example.parentalcontrol.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.parentalcontrol.model.AppModel;
import com.example.parentalcontrol.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactUtils {

    public interface OnContactFetchedListener{
        void onContactsFetched(List<ContactModel> contactsList);
    }

    public static void getAllContactsListAsync(Context context, OnContactFetchedListener listener){
            new FetchContactListTask(context,listener).execute();
    }

    public static class FetchContactListTask extends AsyncTask<Void, Void,List<ContactModel>>{
        private Context context;
        private OnContactFetchedListener listener;

        public FetchContactListTask(Context context, OnContactFetchedListener listener){
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            List<ContactModel> contacts = new ArrayList<>();

            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    List<String> phoneNumbers = getContactPhoneNumbers(contentResolver, contactId);

                    ContactModel contact = new ContactModel(contactId, displayName, phoneNumbers);
                    contacts.add(contact);

                } while (cursor.moveToNext());

                cursor.close();
            }

            return contacts;
        }


        @Override
        protected void onPostExecute(List<ContactModel> contactList) {
            super.onPostExecute(contactList);
            if (listener != null) {
                listener.onContactsFetched(contactList);
            }
        }
    }

   /* public static List<ContactModel> getAllContacts(Context context) {
        List<ContactModel> contacts = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                List<String> phoneNumbers = getContactPhoneNumbers(contentResolver, contactId);

                ContactModel contact = new ContactModel(contactId, displayName, phoneNumbers);
                contacts.add(contact);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return contacts;
    }*/

    private static List<String> getContactPhoneNumbers(ContentResolver contentResolver, String contactId) {
        List<String> phoneNumbers = new ArrayList<>();

        Cursor phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumbers.add(phoneNumber);
            } while (phoneCursor.moveToNext());

            phoneCursor.close();
        }

        return phoneNumbers;
    }
}

