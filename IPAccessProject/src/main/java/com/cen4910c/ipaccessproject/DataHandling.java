package com.cen4910c.ipaccessproject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataHandling {
    /**
     * The {@code DataHandling} class manages the CRUD operations for
     * Users, Loans, and Devices in the database. It interacts with the database to allow
     * for adding, retrieving, and removing records for these entities.
     *
     * <p>This class provides a set of methods to perform the following actions:</p>
     * <ul>
     *     <li>Retrieve Users by ID or first + last name</li>
     *     <li>Create and delete User records</li>
     *     <li>Retrieve Loans by ID or device ID, and manage loan statuses</li>
     *     <li>Manage Device availability and associated rentals</li>
     * </ul>
     */

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Methods for managing User data: getting, adding, deleting users.
     */

    public User getUserByID(int ID) {
        User user = entityManager.find(User.class, ID);

        if (user == null) {
            System.out.println("User not found, ID: " + ID);
            return null;
        }

        return user;
    }

    public User getUserByName(String firstName, String lastName){
        String executeString = "SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        List<User> queryUser = query.getResultList();

        if (queryUser.isEmpty()) {
            System.out.println("User not found, firstName: " + firstName + " lastName: " + lastName);
            return null;
        }
        return queryUser.getFirst();
    }

    @Transactional
    public User addUser(String firstName, String lastName, String zip, String email, String phoneNumber){
        User user = new User(firstName, lastName, zip, email, phoneNumber);
        entityManager.persist(user);

        System.out.println("User created successfully: " + user);
        return user;
    }

    @Transactional
    public void deleteUserByID(int ID){
        System.out.println("Deleting User with ID: " + ID);

        // This is here to affect the foreign key constraint
        deleteLoanByUserID(ID);

        String executeString = "DELETE FROM User u WHERE u.userID = :userID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("userID", ID);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }

    @Transactional
    public void deleteUserByName(String firstName, String lastName){
        System.out.println("Deleting User: " + firstName + " " + lastName);

        // This is here to affect the foreign key constraint
        deleteLoanByUserID(getUserByName(firstName, lastName).getUserID());

        String executeString = "DELETE FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }

    /**
     * Methods for managing Loan data: getting, adding, deleting loans.
     */
    public Loan getLoanByID(int ID){
        String executeString = "SELECT l FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            return null;
        }
        return queryLoan.getFirst();
    }

    public Loan getLoanByDeviceID(int ID){
        String executeString = "SELECT l FROM Loan l WHERE l.deviceID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            return null;
        }
        return queryLoan.getFirst();
    }

    public Loan getLoanByUserID(int ID){
        String executeString = "SELECT l FROM Loan l WHERE l.userID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            return null;
        }
        return queryLoan.getFirst();
    }

    public String getLoanStatusByID(int ID){
        String executeString = "SELECT l FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();
        String status = queryLoan.getFirst().getLoanStatus().name();

        if (queryLoan.isEmpty()) {
            System.out.println("No active loans found for device: " + ID);
            return null;
        } else {
            System.out.println(status == null || status.isEmpty() ?
                    "No active status for loan: " + ID :
                    "Active status: " + status);
            return status;
        }
    }

    public void changeLoanStatusByID(int ID, String newStatus){
        String executeQuery = "SELECT l FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeQuery);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No active loans found for ID: " + ID);
        } else {
            System.out.println("Updating status for loan: " + ID);
            Loan loan = queryLoan.getFirst();
            ApplicationEnums.LoanStatus status = ApplicationEnums.LoanStatus.valueOf(newStatus.toUpperCase());
            loan.setLoanStatus(status);

            entityManager.merge(loan);

            System.out.println("Loan status updated successfully");
        }
    }

    public List<Loan> getActiveLoans() {
        String executeString = "SELECT l FROM Loan l WHERE l.loanStatus = :status";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("status", ApplicationEnums.LoanStatus.ACTIVE);

        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No active loans found");
            return null;
        }
        return queryLoan;
    }

    public List<Loan> getReviewLoans() {
        String executeString = "SELECT l FROM Loan l WHERE l.loanStatus = :status";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("status", ApplicationEnums.LoanStatus.REVIEW);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No loans under review found");
        }
        
        return queryLoan;
    }

    public List<Loan> getOverdueLoans() {
        String executeString = "SELECT l FROM Loan l WHERE l.loanStatus = :status";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("status", ApplicationEnums.LoanStatus.OVERDUE);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No loans under review found");
        }

        return queryLoan;
    }

    public List<Loan> getLoansByUser(int ID){
        String executeString = "SELECT l FROM Loan l WHERE l.userID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);

        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No active loans found for User ID: " + ID);
            return null;
        } else {
            System.out.println(queryLoan.size() + " Active loans found for User ID: " + ID);
            return queryLoan;
        }
    }

    @Transactional
    public Loan addLoan(int userID, int deviceID, LocalDate startDate, LocalDate endDate, ApplicationEnums.LoanStatus loanStatus){
        Loan loan = new Loan(userID, deviceID, startDate, endDate, loanStatus);
        if (getLoanByUserID(userID) != null) {

        }


        entityManager.persist(loan);

        System.out.println("Loan created successfully: " + loan);
        return loan;
    }

    @Transactional
    public void deleteLoanByID(int ID){
        String executeString = "DELETE FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }

    @Transactional
    public void deleteLoanByUserID(int ID){
        System.out.println("Deleting User with ID: " + ID);

        String executeString = "DELETE FROM Loan l WHERE l.userID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }

    @Transactional
    public void deleteLoanByDeviceID(int ID){
        String executeString = "DELETE FROM Loan l WHERE l.deviceID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }


    /**
     * Methods for managing Device data: getting, adding, deleting devices.
     */
    public Device getDeviceByID(int ID){
        String executeString = "SELECT d FROM Device d WHERE d.deviceID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No device found for ID: " + ID);
            return null;
        } else {
            System.out.println("Device found for ID: " + ID);
            return queryDevice.getFirst();
        }
    }

    public Device getDeviceByUserID(int ID){
        String executeString = "SELECT d FROM Device d WHERE d.renterID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No device found for user: " + ID);
            return null;
        } else {
            System.out.println("Device found under user: " + ID);
            return queryDevice.getFirst();
        }
    }

    public List<Device> getAvailableDevices(){
        String executeString = "SELECT d FROM Device d WHERE d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No available devices found");
            return null;
        } else {
            System.out.println("Available devices found: " + queryDevice.size());
            return queryDevice;
        }
    }

    public List<Device> getAllDevices() {
        String executeString = "SELECT d FROM Device d";
        Query query = entityManager.createQuery(executeString);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No available devices found");
        }
        return queryDevice;
    }

    public List<Device> getAvailableDeviceByType(ApplicationEnums.DeviceType type){
        String executeString = "SELECT d FROM Device d WHERE d.deviceType = :type AND d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("type", type);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No available " + type + "s found");
            return null;
        } else {
            System.out.println("Available devices found: " + queryDevice.size());
            return queryDevice;
        }
    }

    public Device getFirstAvailableDeviceByType(ApplicationEnums.DeviceType type){
        String executeString = "SELECT d FROM Device d WHERE d.deviceType = :type AND d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("type", type);
        List<Device> queryDevice = query.getResultList();

        if(queryDevice.isEmpty()){
            System.out.println("No available " + type + "s found");
            return null;
        } else {
            System.out.println("Available devices found: " + queryDevice.size());
            return queryDevice.getFirst();
        }
    }

    @Transactional
    public Device addDevice(ApplicationEnums.DeviceType deviceType, boolean availability, int renterID){
        Device device = new Device(deviceType, availability, renterID);
        entityManager.persist(device);

        System.out.println("Device added successfully: " + device);
        return device;
    }

    @Transactional
    public void removeDeviceByID(int ID){
        String executeString = "DELETE FROM Device d WHERE d.deviceID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Rows affected: " + rowsAffected);
        } else {
            System.out.println("No rows affected");
        }
    }

    /**
     * Methods for managing alert data: getting, adding, and deleting alerts.
     */
    public List<Alert> getAlerts(){
        String executeString = "SELECT d FROM Alert d";
        Query query = entityManager.createQuery(executeString);
        List<Alert> queryAlert = query.getResultList();

        if(queryAlert.isEmpty()){System.out.println("No alerts found");}
        return queryAlert;
    }

    public List<Alert> getUnreadAlerts(){
        String executeString = "SELECT d FROM Alert d WHERE d.isRead = FALSE";
        Query query = entityManager.createQuery(executeString);
        List<Alert> queryAlert = query.getResultList();

        if(queryAlert.isEmpty()){System.out.println("No unread alerts found");}
        return queryAlert;
    }

    public List<Alert> getReadAlerts(){
        String executeString = "SELECT d FROM Alert d WHERE d.isRead = TRUE";
        Query query = entityManager.createQuery(executeString);
        List<Alert> queryAlert = query.getResultList();

        if(queryAlert.isEmpty()){System.out.println("No read alerts found");}
        return queryAlert;
    }

    public List<Alert> getAlertsByPriority(ApplicationEnums.AlertPriority priority){
        String executeString = "SELECT d FROM Alert d WHERE d.alertPriority = :priority";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("priority", priority);
        List<Alert> queryAlert = query.getResultList();

        if(queryAlert.isEmpty()){System.out.println("No alerts found");}
        return queryAlert;
    }

    public List<Alert> getAlertsByType(ApplicationEnums.AlertType type){
        String executeString = "SELECT d FROM Alert d WHERE d.alertType = :type";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("type", type);
        List<Alert> queryAlert = query.getResultList();

        if(queryAlert.isEmpty()){System.out.println("No {" + type + "} priority alerts found");}
        return queryAlert;
    }
}
