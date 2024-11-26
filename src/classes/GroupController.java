package classes;

import java.util.*;

public class GroupController {
    private GroupService groupService;
    private NotificationService notificationService;

    // Constructor
    public GroupController() {
        this.groupService = new GroupService();
        this.notificationService = new NotificationService();
    }

    // Method to handle the group action selection
    public void handleGroupAction(Student student, String action) {
        Scanner scanner = new Scanner(System.in);

        if (action.equals("1")) { // Option to create a group
            System.out.print("Enter group name: ");
            String groupName = scanner.nextLine();

            // Prompt for group members
            List<Student> members = new ArrayList<>();
            System.out.print("Enter number of members: ");
            int numMembers = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numMembers; i++) {
                System.out.print("Enter student ID for member " + (i + 1) + ": ");
                String studentId = scanner.nextLine();
                System.out.print("Enter student name for member " + (i + 1) + ": ");
                String studentName = scanner.nextLine();
                System.out.print("Enter student batch for member " + (i + 1) + ": ");
                String studentBatch = scanner.nextLine();

                // Create a new Student object and add to the list
                members.add(new Student(studentId, studentName, null, studentBatch));
            }

            /*
            // Attempt to create the group
            Group createdGroup = groupService.createGroup(groupName, members);
            if (createdGroup != null) {
                System.out.println("Group " + groupName + " created successfully!");
                // Notify all members about the creation
                Group group = new Group(groupName, members);
                notificationService.notifyMembers(group, "Group " + groupName + " has been created successfully.");
            } else {
                System.out.println("Group creation failed. Please try again.");
            }*/

        } /*else if (action.equals("2")) { // Option to join an existing group
            System.out.print("Enter group name to join: ");
            String groupName = scanner.nextLine();

            // Join the group
            if (groupService.joinGroup(groupName, student)) {
                System.out.println("Successfully joined the group " + groupName + "!");
                
                // Get all members of the group to notify them about the new join
                List<Student> groupMembers = groupService.getMembers(groupName);
                
                // Notify all members
                notificationService.notifyGroupMembers(
                    groupMembers, "A new member (" + student.getName() + ") has joined the group " + groupName + ".");
            }
             else {
                System.out.println("Group " + groupName + " does not exist or join request failed.");
            }
        } else {
            System.out.println("Invalid action. Please select '1' to create a group or '2' to join a group.");
        }*/
    }
}
