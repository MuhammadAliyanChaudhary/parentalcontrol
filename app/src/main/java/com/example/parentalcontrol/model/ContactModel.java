package com.example.parentalcontrol.model;

import java.util.List;

public class ContactModel {

    private String contactId;
    private String displayName;
    private List<String> phoneNumbers;

    public ContactModel(String contactId, String displayName, List<String> phoneNumbers) {
        this.contactId = contactId;
        this.displayName = displayName;
        this.phoneNumbers = phoneNumbers;
    }

    public String getContactId() {
        return contactId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}

