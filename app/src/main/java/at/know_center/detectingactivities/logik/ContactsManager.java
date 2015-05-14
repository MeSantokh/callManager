package at.know_center.detectingactivities.logik;

import android.content.Context;

import at.know_center.detectingactivities.contact.ContactGroup;

/**
 * Created by santokh on 14.05.15.
 */
public class ContactsManager {

    private ContactGroup contactGroup;
    private Context context;
    public ContactsManager(Context context) {
        this.context = context;
        this.contactGroup = new ContactGroup(context);
    }



}
