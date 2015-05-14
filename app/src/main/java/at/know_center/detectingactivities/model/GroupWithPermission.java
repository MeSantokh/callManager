package at.know_center.detectingactivities.model;

/**
 * Created by santokh on 14.05.15.
 */
public class GroupWithPermission {
    private int groupId;
    private boolean permission;

    public GroupWithPermission() {}

    public boolean hasPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public GroupWithPermission(int groupId, boolean permission) {
        this.groupId = groupId;
        this.permission = permission;

    }
}
