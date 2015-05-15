package at.know_center.detectingactivities.contact;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by santokh on 14.05.15.
 */
public class ContactGroup {

    private Context context;
    private LinkedHashMap<Item, ArrayList<Item>> groupList;

    private LinkedHashMap<String, ArrayList<Item>> memberBasedGroupList;

    public ContactGroup(Context context) {
        this.context = context;
        memberBasedGroupList =  new LinkedHashMap<String,ArrayList<Item>>();
    }

    public LinkedHashMap<Item, ArrayList<Item>> getGroupList() {
        return groupList;
    }

    public LinkedHashMap<String, ArrayList<Item>> getMemberBasedGroupList() {
        return memberBasedGroupList;
    }

    public void initContactList(){
        groupList = new LinkedHashMap<Item,ArrayList<Item>>();
        ArrayList<Item> groupsList = fetchGroups();
        for(Item item:groupsList){
            String[] ids = item.id.split(",");
            ArrayList<Item> groupMembers =new ArrayList<Item>();
            for(int i=0;i<ids.length;i++){
                String groupId = ids[i];
                groupMembers.addAll(fetchGroupMembers(groupId));
            }
            item.name = item.name +" ("+groupMembers.size()+")";
            groupList.put(item,groupMembers);
        }
        createMemberBasedGroupList();
    }

    private void createMemberBasedGroupList() {
        Set<Item> contactGroups = groupList.keySet();

        for(Item group : contactGroups){
            Log.d("ContactGroup", "groupId " + group.id + ", groupName " + group.name);
            ArrayList<Item> groupMembers = groupList.get(group);
            for(Item member : groupMembers) {

                if(member.phNo != null) {
                    Log.d("ContactGroup   ", " memberName " + member.name + ", memberPhoneNr " + member.phNo);
                    String key = member.phNo;
                    key = key.replaceAll("\\s","");
                    ArrayList<Item> groupsOfMember = new ArrayList<Item>();
                    if(memberBasedGroupList.containsKey(key)) {
                        groupsOfMember.addAll(memberBasedGroupList.get(key));
                    }
                    groupsOfMember.add(group);
                    memberBasedGroupList.put(key, groupsOfMember);

                }
            }
        }
    }


    public ArrayList<Item> getContactGroupsByPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\s","");
        if(!memberBasedGroupList.containsKey(phoneNumber)) {
            return null;
        }
        return memberBasedGroupList.get(phoneNumber);
    }

    private ArrayList<Item> fetchGroups(){
        ArrayList<Item> groupList = new ArrayList<Item>();
        String[] projection = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                projection, null, null, null);
        ArrayList<String> groupTitle = new ArrayList<String>();
        while(cursor.moveToNext()){
            Item item = new Item();
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String groupName =      cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if(groupName.contains("Group:"))
                groupName = groupName.substring(groupName.indexOf("Group:")+"Group:".length()).trim();

            if(groupName.contains("Favorite_"))
                groupName = "Favorite";

            if(groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
                continue;
            if(groupName.contains("mstillalive"))
                continue;

            if(groupTitle.contains(groupName)){
                for(Item group:groupList){
                    if(group.name.equals(groupName)){
                        group.id += ","+item.id;
                        break;
                    }
                }
            }else{
                groupTitle.add(groupName);
                item.name = groupName;
                groupList.add(item);
            }

        }

        cursor.close();
        Collections.sort(groupList, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return item2.name.compareTo(item1.name) < 0
                        ? 0 : -1;
            }
        });
        return groupList;
    }

    private ArrayList<Item> fetchGroupMembers(String groupId){
        ArrayList<Item> groupMembers = new ArrayList<Item>();
        String where =  ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID +"="+groupId
                +" AND "
                +ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE+"='"
                +ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where,null,
                ContactsContract.Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
        while(cursor.moveToNext()){
            Item item = new Item();
            item.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID));
            Cursor phoneFetchCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.TYPE},
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID+"="+item.id,null,null);
            while(phoneFetchCursor.moveToNext()){
                item.phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                item.phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                item.phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            }
            phoneFetchCursor.close();
            groupMembers.add(item);
        }
        cursor.close();
        return groupMembers;
    }

}
