package scienceWork.objects;

import java.util.List;

public class GroupDescr {
    private List<Integer> descr;
    private int group;
    private String pictureName;

    public GroupDescr(String pictureName, int group, List<Integer> descr) {
        this.descr = descr;
        this.group = group;
        this.pictureName = pictureName;
    }

    public List<Integer> getDescr() {
        return descr;
    }

    public int getGroup() {
        return group;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "GroupDescr{" +
                "pictureName='" + pictureName  +
                ", group=" + group +
                ", descr=" + descr +
                '}';
    }
}
