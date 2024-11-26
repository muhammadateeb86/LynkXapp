package classes;

import java.util.ArrayList;
import java.util.List;

import database.MySql;

public class BatchService {
    private final List<BatchObserver> observers = new ArrayList<>();

    // Add an observer
    public void addObserver(BatchObserver observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(BatchObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers when a student joins a batch
    private void notifyObservers(String studentId, String batchName) {
        for (BatchObserver observer : observers) {
            observer.onBatchJoin(studentId, batchName);
        }
    }

    // Add a student to a batch
    public boolean addUserToBatch(String studentID, String batchName) {
        
    	MySql mySql = new MySql();
    	boolean added = mySql.addStudentToBatch(studentID, batchName);
        return added;
    }
}
