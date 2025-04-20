package com.cen4910c.ipaccessproject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<User> getAllUsers(){
        String executeString = "SELECT u FROM User u";
        Query query = entityManager.createQuery(executeString);
        List<User> queryUser = query.getResultList();

        if (queryUser.isEmpty()) {
            System.out.println("No users found");
        }
        return queryUser;
    }

    public User getFirstUser() {
        String executeQuery = "SELECT u FROM User u";
        Query query = entityManager.createQuery(executeQuery);
        List<User> queryUser = query.getResultList();
        return queryUser.getFirst();
    }
    /**
     * getUserByID
     * @param ID int user ID
     * @return Retrieves the user by querying the database for their user ID
     */
    public User getUserByID(int ID) {
        User user = entityManager.find(User.class, ID);
        System.out.println("User: " + user);
        return user;
    }

    /**
     * getUserByEmail
     * @param email String email of the user account
     * @return User with matching email
     */
    public User getUserByEmail(String email) {
        String executeString = "SELECT d FROM User d WHERE d.email = :email";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("email", email);
        List<User> queryUser = query.getResultList();
        if (queryUser.isEmpty()) {
            System.out.println("No User found for email: " + email);
            return null;
        } else {
            System.out.println("User found for email: " + email);
        }
        return queryUser.getFirst();
    }

    /**
     * getUserByName
     * @param firstName String firstName of the user
     * @param lastName String lastName of the user
     * @return User with matching first and last name
     */
    public User getUserByName(String firstName, String lastName) {
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

    /**
     * addUser - creates a user with the following parameters and adds it to the database.
     * @param firstName
     * @param lastName
     * @param zip
     * @param email
     * @param password
     * @param phoneNumber
     * @return The user created and added to the database.
     */
    @Transactional
    public User addUser(String firstName, String lastName, String zip, String email, String password, String phoneNumber) {
        User user = new User(firstName, lastName, zip, email, password, phoneNumber);
        entityManager.persist(user);

        System.out.println("User created successfully: " + user);
        return user;
    }

    @Transactional
    public User addUser(User user) {
        entityManager.persist(user);
        return user;
    }

    /**
     * deleteUserByID Finds a user in the database by their ID and deletes the user from the database.
     * @param ID int ID of the user.
     */
    @Transactional
    public void deleteUserByID(int ID) {
        System.out.println("Deleting User with ID: " + ID);
        User user = getUserByID(ID);

        // This is here to affect the foreign key constraint
        deleteLoanByUserID(ID);

        if (user != null) {
            entityManager.remove(user);
            System.out.println("User deleted successfully: " + user);
        } else {
            System.out.println("User not found");
        }
    }

    /**
     * deleteUserByName - Finds a user by their first and last name and deletes
     * the user with the matching first and last name.
     * @param firstName String firstName of the user
     * @param lastName String lastName of the user
     */
    @Transactional
    public void deleteUserByName(String firstName, String lastName) {
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
     * Does not include deleteLoanByUserID as a User can have multiple loans.
     */
    public Loan getLoanByID(int ID) {
        Loan loan = entityManager.find(Loan.class, ID);
        System.out.println("Loan: " + loan);
        return loan;
    }

    public Loan getLoanByDeviceID(int ID) {
        String executeString = "SELECT l FROM Loan l WHERE l.deviceID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            return null;
        }
        return queryLoan.getFirst();
    }

    public String getLoanStatusByID(int ID) {
        String executeString = "SELECT l FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();
        String status = queryLoan.getFirst().getLoanStatus();

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

    public void changeLoanStatusByID(int ID, String newStatus) {
        String executeQuery = "SELECT l FROM Loan l WHERE l.loanID = :ID";
        Query query = entityManager.createQuery(executeQuery);
        query.setParameter("ID", ID);
        List<Loan> queryLoan = query.getResultList();

        if (queryLoan.isEmpty()) {
            System.out.println("No active loans found for ID: " + ID);
        } else {
            System.out.println("Updating status for loan: " + ID);
            Loan loan = queryLoan.getFirst();
            loan.setLoanStatus(newStatus);
            entityManager.merge(loan);

            System.out.println("Loan status updated successfully");
        }
    }
    /**
     * updateLoanStatus - Finds a loan by its ID and updates the loanStatus to a given status.
     * @param loanID int - ID of the loan.
     * @param newStatus Values can be: ACTIVE, OVERDUE, RESERVED, REVIEW
     * @return boolean - whether the loan is found and updated successfully.
     */
    @Transactional
    public boolean updateLoanStatus(int loanID, String newStatus) {
        Loan loan = entityManager.find(Loan.class, loanID);
        if (loan == null) {
            System.out.println("Loan not found with ID: " + loanID);
            return false;
        }

        loan.setLoanStatus(newStatus.toUpperCase());
        entityManager.merge(loan);

        System.out.println("Loan status updated: #" + loanID + " -> " + newStatus);
        return true;
    }

    public List<Loan> retrieveActiveLoansByUser(int ID) {
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

    /**
     * addLoan - creates a loan and adds it to the database. Sets device as unavailable and adds the user ID to the device's renterID field.
     * @param userID ID of the user account the device is being loaned to.
     * @param deviceID ID of the device being loaned.
     * @param startDate Start date of the loan.
     * @param endDate end date of the loan.
     * @param loanStatus enum Status of the loan. Values: ACTIVE, OVERDUE, RESERVED, REVIEW
     * @return Loan that is created and added to the database.
     */
    @Transactional
    public Loan addLoan(int userID, int deviceID, LocalDate startDate, LocalDate endDate, String loanStatus) {
        Loan loan = new Loan(userID, deviceID, startDate, endDate, loanStatus);
        Device device = getDeviceByID(deviceID);
        device.setAvailability(false);
        device.setRenterID(userID);
        entityManager.persist(device);
        entityManager.persist(loan);

        System.out.println("Loan created successfully: " + loan);
        return loan;
    }

    /**
     * addLoan - adds a Loan object to the database. Sets loaned device as unavailable with the renting user ID as the renterID.
     * @param loan Loan object.
     * @return Loan that is added to the database.
     */
    @Transactional
    public Loan addLoan(Loan loan) {
        Device device = getDeviceByID(loan.getDeviceID());
        device.setAvailability(false);
        device.setRenterID(loan.getUserID());
        entityManager.persist(device);
        entityManager.persist(loan);

        System.out.println("Loan created successfully: " + loan);
        return loan;
    }

    public List<Loan> getOverdueLoans() {
        String executeString = "SELECT l FROM Loan l WHERE l.endDate < :today AND l.loanStatus <> 'OVERDUE'";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("today", LocalDate.now());

        List<Loan> overdueLoans = query.getResultList();
        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans found.");
            return List.of();
        } else {
            System.out.println(overdueLoans.size() + " overdue loans found.");
            return overdueLoans;
        }
    }

    @Transactional
    public void checkAndUpdateOverdueLoans() {
        List<Loan> overdueLoans = getOverdueLoans();
        for (Loan loan : overdueLoans) {
            loan.setLoanStatus("OVERDUE");
            entityManager.merge(loan);
            System.out.println("Updated loan to OVERDUE: " + loan.getLoanID());
        }
    }

    @Transactional
    public List<Loan> getAllLoans() {
        String executeString = "SELECT l FROM Loan l";
        Query query = entityManager.createQuery(executeString);
        List<Loan> loans = query.getResultList();

        if (loans.isEmpty()) {
            System.out.println("No loans found.");
        }
        return loans;
    }


    @Transactional
    public void deleteLoanByID(int ID) {
        Loan loan = getLoanByID(ID);
        entityManager.remove(loan);
        Device device = getDeviceByID(loan.getDeviceID());
        device.setAvailability(true);
        device.setRenterID(null);
        entityManager.persist(device);
    }

    @Transactional
    public void deleteLoanByUserID(int ID) {
        Loan loan = getLoanByID(ID);
        if (loan != null) {
            entityManager.remove(loan);
            System.out.println("Loan deleted successfully: " + loan);
        } else
            System.out.println("Loan not found");
    }

    @Transactional
    public void deleteLoanByDeviceID(int ID) {
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
    @Transactional
    public Device addDevice(String deviceName, boolean availability, int locationID) {
        Location location = entityManager.find(Location.class, locationID);

        if (location == null) {
            throw new IllegalArgumentException("Invalid location ID: " + locationID);
        }

        Device device = new Device(deviceName, availability);
        device.setLocation(location);
        entityManager.persist(device);

        System.out.println("Device added successfully: " + device);
        return device;
    }

    @Transactional
    public void assignDeviceToLocation(int deviceID, int locationID) {
        Device device = entityManager.find(Device.class, deviceID);
        Location location = entityManager.find(Location.class, locationID);

        if (device == null || location == null) {
            throw new IllegalArgumentException("Device or location not found.");
        }

        device.setLocation(location);
        System.out.println("Assigned device " + deviceID + " to location " + locationID);
    }

    public List<Map<String, Object>> getDeviceInventoryMap(boolean availableOnly) {
        StringBuilder queryStr = new StringBuilder(
                "SELECT d.deviceID, d.deviceType, d.deviceStatus, d.availability, d.location.locationName " +
                        "FROM Device d"
        );

        if (availableOnly) {
            queryStr.append(" WHERE d.availability = TRUE");
        }

        Query query = entityManager.createQuery(queryStr.toString());
        List<Object[]> resultList = query.getResultList();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Object[] row : resultList) {
            Map<String, Object> map = new HashMap<>();
            map.put("deviceID", row[0]);
            map.put("deviceType", row[1]);
            map.put("deviceStatus", row[2]);
            map.put("availability", row[3]);
            map.put("locationName", row[4] != null ? row[4] : "Unassigned");
            responseList.add(map);
        }

        return responseList;
    }



    public Device getDeviceByID(int ID) {
        Device device = entityManager.find(Device.class, ID);
        System.out.println("Device: " + device);
        return device;
    }

    public Device getDeviceByUserID(int ID) {
        String executeString = "SELECT d FROM Device d WHERE d.renterID = :ID";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("ID", ID);
        List<Device> queryDevice = query.getResultList();

        if (queryDevice.isEmpty()) {
            System.out.println("No device found for user: " + ID);
            return null;
        } else {
            System.out.println("Device found under user: " + ID);
            return queryDevice.getFirst();
        }
    }

    public List<Device> getAvailableDevices() {
        String executeString = "SELECT d FROM Device d WHERE d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        List<Device> queryDevice = query.getResultList();

        if (queryDevice.isEmpty()) {
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

        if (queryDevice.isEmpty()) {
            System.out.println("No available devices found");
            return null;
        }
        return queryDevice;
    }

    public List<Device> getAvailableDeviceByType(String type) {
        String executeString = "SELECT d FROM Device d WHERE d.deviceType = :type AND d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("type", Device.DeviceType.valueOf(type.toUpperCase()));
        List<Device> queryDevice = query.getResultList();

        if (queryDevice.isEmpty()) {
            System.out.println("No available " + type + "s found");
            return null;
        } else {
            System.out.println("Available devices found: " + queryDevice.size());
            return queryDevice;
        }
    }


    public Device getFirstAvailableDeviceByType(String type) {
        String executeString = "SELECT d FROM Device d WHERE d.deviceType = :type AND d.availability = TRUE";
        Query query = entityManager.createQuery(executeString);
        query.setParameter("type", Device.DeviceType.valueOf(type.toUpperCase()));
        List<Device> queryDevice = query.getResultList();

        if (queryDevice.isEmpty()) {
            System.out.println("No available " + type + "s found");
            return null;
        } else {
            System.out.println("Available devices found: " + queryDevice.size());
            return queryDevice.getFirst();
        }
    }


    @Transactional
    public Device addDevice(String deviceName, boolean availability) {
        Device device = new Device(deviceName, availability);
        entityManager.persist(device);

        System.out.println("Device added successfully: " + device);
        return device;
    }

    @Transactional
    public void removeDeviceByID(int ID) {
        Device device = getDeviceByID(ID);
        if (device != null) {
            entityManager.remove(device);
            System.out.println("Device removed successfully: " + device);
        } else {
            System.out.println("Device not found");
        }
    }
}
