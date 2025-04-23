package com.cen4910c.ipaccessproject;

import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
public class WebHandling {

    @Autowired
    private DataHandling dataHandling;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "redirect:/Login.html";
    }

    //    ================================
    //    User endpoints
    //    ================================
    @PostMapping("/IPaccess/addUser")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String zipCode = user.getZipCode();
        String email = user.getEmail();
        String password = passwordEncoder.encode(user.getPassword());
        String phoneNumber = user.getPhoneNumber();

        dataHandling.addUser(firstName, lastName, zipCode, email, password, phoneNumber);
        redirectAttributes.addFlashAttribute("message", "User created successfully!");
        return "redirect:/IPaccess/getAllUsers";
    }

    @GetMapping("/IPaccess/getUserByID")
    public String getUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByID(userID);
        if (user != null)
            redirectAttributes.addFlashAttribute("message", user.toString());
        else
            redirectAttributes.addFlashAttribute("message", "User not found!");
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getUserByName")
    public String getUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByName(firstName, lastName);
        if (user != null)
            redirectAttributes.addFlashAttribute("message", user.toString());
        else
            redirectAttributes.addFlashAttribute("message", "User not found!");
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getAllUsers")
    public String getAllUsers(Model model){
        List<User> users = dataHandling.getAllUsers();
        model.addAttribute("users", users);
        return "user-profile";
    }

    @GetMapping("/IPaccess/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            redirectAttributes.addFlashAttribute("message", "User logged in successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid credentials");
        }
        assert user != null;
        if(user.getRole() == User.Role.ADMIN)
            return "admin-dashboard";
        return "redirect:/Home.html";
    }

    @GetMapping("IPaccess/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/IPaccess/register")
    public String register(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dataHandling.addUser(
                user.getFirstName(),
                user.getLastName(),
                user.getZipCode(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber()
        );

        return "registerSuccess";
    }

    @PostMapping("/IPaccess/updateUserRole")
    public String updateUserRole(@RequestParam int userID, @RequestParam String role, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByID(userID);
        user.setRole(role);
        dataHandling.addUser(user);
        return "redirect:/IPaccess/admin-dashboard";
    }

    @PostMapping("/IPaccess/deleteUserByID")
    public String deleteUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByID(userID);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. ID: " + userID);
        return "redirect:/IPaccess/getAllUsers";
    }

    @PostMapping("/IPaccess/deleteUserByName")
    public String deleteUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByName(firstName, lastName);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. User: " + firstName + " " + lastName);
        return "redirect:/IPaccess/getAllUsers";
    }

    //    ================================
    //    Device endpoints
    //    ================================

    // Created this method to ALL and/or Available devices
    // If true only returns available devices .. if false returns all devices
    // Returns list into JSON and sends to response body

    @GetMapping("/IPaccess/inventory")
    public String getAllDevices(Model model) {
        List<Device> devices = dataHandling.getAllDevices();
        model.addAttribute("devices", devices);
        return "inventory";
    }

/*
    @GetMapping("/IPaccess/getAllDevices")
    @ResponseBody
    public List<Device> getAllDevices(@RequestParam(required = false) Boolean available) {
        return (available != null && available) ? dataHandling.getAvailableDevices() : dataHandling.getAllDevices();
    }*/

    @PostMapping("/IPaccess/addDevice")
    public String addDevice(@RequestParam String deviceName,
                            @RequestParam(name = "availability", defaultValue = "false") boolean availability,
                            @RequestParam int locationID,
                            RedirectAttributes redirectAttributes) {
        try {
            Device device = dataHandling.addDevice(deviceName, availability, locationID);
            redirectAttributes.addFlashAttribute("message", "Device added successfully. Device: " + device);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add device: " + e.getMessage());
        }
        return "redirect:/IPaccess/inventory";
    }

    @GetMapping("/IPaccess/getDeviceByID")
    public String getDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.getDeviceByID(deviceID);
        if (device != null)
            redirectAttributes.addFlashAttribute("message", device.toString());
        else
            redirectAttributes.addFlashAttribute("message", "Device not found!");
        return "redirect:/IPaccess/getAllDevices";
    }


    @PostMapping("/IPaccess/removeDeviceByID")
    public String removeDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        dataHandling.removeDeviceByID(deviceID);
        redirectAttributes.addFlashAttribute("message", "Device removed successfully. ID: " + deviceID);
        return "redirect:/IPaccess/inventory";
    }

//    @GetMapping("/IPaccess/admin-dashboard")
//    public String redirectToDashboard() {
//        return "admin-dashboard";
//    }


    @GetMapping("/IPaccess/admin-dashboard")
    public String updateDeviceStatusForm(Model model) {
        Device device = new Device();
        model.addAttribute("device", device);
        return "admin-dashboard";
    }


    @PostMapping("/IPaccess/updateDeviceStatus")
    public String updateDeviceStatus(@ModelAttribute Device device) {
        dataHandling.setDeviceStatus(device.getDeviceID(), device.getDeviceStatus());
        return "redirect:/IPaccess/admin-dashboard";
    }

    //    ================================
    //    Loan endpoints
    //    ================================
    @PostMapping("/IPaccess/addLoan")
    public String addLoan(@RequestParam int userID, @RequestParam int deviceID, @RequestParam String loanStatus, RedirectAttributes redirectAttributes) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(14);

        Loan loan = dataHandling.addLoan(userID, deviceID, start, end, loanStatus);
        redirectAttributes.addFlashAttribute("message", loan.toString());
        return "redirect:/IPaccess/getAllLoans";
    }

    @GetMapping("/IPaccess/getAllLoans")
    public String getAllLoans(Model model) {
        List<Loan> loans = dataHandling.getAllLoans();
        model.addAttribute("loans", loans);
        return "loan-list";
    }


    @GetMapping("/IPaccess/getLoanByID")
    public String getLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributes) {
        Loan loan = dataHandling.getLoanByID(loanID);
        if (loan != null)
            redirectAttributes.addFlashAttribute("message", loan.toString());
        else
            redirectAttributes.addFlashAttribute("message", "Loan not found!");
        return "redirect:loan-list";
    }

    @GetMapping("/IPaccess/deleteLoanByID")
    public String deleteLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributes) {
        dataHandling.deleteLoanByID(loanID);
        redirectAttributes.addFlashAttribute("message", "Loan deleted successfully. ID: " + loanID);
        return "redirect:/IPaccess/getAllLoans";
    }

    @PostMapping("/IPaccess/requestLoan")
    public String requestLoan(@RequestParam String deviceType,
                              @RequestParam String location,
                              @RequestParam String startDate,
                              @RequestParam String endDate,
                              @RequestParam String email,
                              RedirectAttributes redirectAttributes) {

        Loan loan = null;

        User user = dataHandling.getUserByEmail(email);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User not found. Please check your name and try again.");
            return "redirect:/Home.html";
        }

        // Get all available devices of the selected type
        List<Device> availableDevices = dataHandling.getAvailableDeviceByType(deviceType);

        if (availableDevices != null && !availableDevices.isEmpty()) {
            Optional<Device> matchingDevice = availableDevices.stream()
                    .filter(device -> device.getLocation().getLocationName().equalsIgnoreCase(location))
                    .findFirst();

            if (matchingDevice.isPresent()) {
                Device device = matchingDevice.get();
                loan = dataHandling.addLoan(new Loan(
                        user.getUserID(),
                        device.getDeviceID(),
                        startDate,
                        endDate,
                        "RESERVED"
                ));

                System.out.println("Loan reserved for device: " + device.getDeviceID());
            } else {
                System.out.println("No available device found at the selected location.");
            }
        }

        redirectAttributes.addFlashAttribute("message", loan != null
                ? "Loan successfully reserved for " + user.getEmail()
                : "No devices available at the selected location.");

        return "redirect:/Home.html";
    }


    @PostMapping("/IPaccess/updateLoanStatus")
    public String updateLoanStatus(@RequestParam int loanID,
                                   @RequestParam String status,
                                   RedirectAttributes redirectAttributes) {
        boolean updated = dataHandling.updateLoanStatus(loanID, status);

        if (updated) {
            redirectAttributes.addFlashAttribute("message", "Loan #" + loanID + " status updated to: " + status.toUpperCase());
        } else {
            redirectAttributes.addFlashAttribute("message", "Loan ID #" + loanID + " not found.");
        }

        return "redirect:/IPaccess/getAllLoans";
    }

}
