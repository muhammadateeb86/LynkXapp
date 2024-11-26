package classes;

import java.util.List;

public class Group {
    private String groupName;
    private List<Student> members;

    // Constructor
    public Group(String groupName, List<Student> members) {
        this.groupName = groupName;
        this.members = members;
    }

    // Getter for groupName
    public String getGroupName() {
        return groupName;
    }

    // Getter for members
    public List<Student> getMembers() {
        return members;
    }

    // Add a member to the group
    public void addMember(Student student) {
        members.add(student);
    }
}

