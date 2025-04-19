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
        return "index";
    }

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
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/getUserByID")
    public String getUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByID(userID);
        if (user != null)
            redirectAttributes.addFlashAttribute("message", user.toString());
        else
            redirectAttributes.addFlashAttribute("message", "User not found!");
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/getUserByName")
    public String getUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByName(firstName, lastName);
        if (user != null)
            redirectAttributes.addFlashAttribute("message", user.toString());
        else
            redirectAttributes.addFlashAttribute("message", "User not found!");
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            redirectAttributes.addFlashAttribute("message", "User logged in successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid credentials");
        }
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/register")
    public String registrationPage(@ModelAttribute("user") User user) {
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

    @PostMapping("/IPaccess/deleteUserByID")
    public String deleteUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByID(userID);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. ID: " + userID);
        return "redirect:/user.html";
    }

    @PostMapping("/IPaccess/deleteUserByName")
    public String deleteUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByName(firstName, lastName);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. User: " + firstName + " " + lastName);
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/getAllDevices")
    @ResponseBody
    public List<Device> getAllDevices(@RequestParam(required = false) Boolean available) {
        return (available != null && available) ? dataHandling.getAvailableDevices() : dataHandling.getAllDevices();
    }

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
        return "redirect:/device.html";
    }

    @GetMapping("/IPaccess/getDeviceByID")
    public String getDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.getDeviceByID(deviceID);
        if (device != null)
            redirectAttributes.addFlashAttribute("message", device.toString());
        else
            redirectAttributes.addFlashAttribute("message", "Device not found!");
        return "redirect:/device.html";
    }

    @PostMapping("/IPaccess/removeDeviceByID")
    public String removeDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        dataHandling.removeDeviceByID(deviceID);
        redirectAttributes.addFlashAttribute("message", "Device removed successfully. ID: " + deviceID);
        return "redirect:/device.html";
    }

    @PostMapping("/IPaccess/assignDeviceToLocation")
    public String assignDeviceToLocation(@RequestParam int deviceID,
                                         @RequestParam int locationID,
                                         RedirectAttributes redirectAttributes) {
        try {
            dataHandling.assignDeviceToLocation(deviceID, locationID);
            redirectAttributes.addFlashAttribute("message", "Device assigned to location successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error assigning device: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getDeviceInventory")
    @ResponseBody
    public List<Map<String, Object>> getDeviceInventory(@RequestParam(required = false) Boolean available) {
        return dataHandling.getDeviceInventoryMap(available != null && available);
    }

    @PostMapping("/IPaccess/addLoan")
    public String addLoan(@RequestParam int userID, @RequestParam int deviceID, @RequestParam String loanStatus, RedirectAttributes redirectAttributes) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(14);

        Loan loan = dataHandling.addLoan(userID, deviceID, start, end, loanStatus);
        redirectAttributes.addFlashAttribute("message", loan.toString());
        return "redirect:/loan.html";
    }

    @GetMapping("/IPaccess/getLoanByID")
    public String getLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributes) {
        Loan loan = dataHandling.getLoanByID(loanID);
        if (loan != null)
            redirectAttributes.addFlashAttribute("message", loan.toString());
        else
            redirectAttributes.addFlashAttribute("message", "Loan not found!");
        return "redirect:/loan.html";
    }

    @GetMapping("/IPaccess/deleteLoanByID")
    public String deleteLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributes) {
        dataHandling.deleteLoanByID(loanID);
        redirectAttributes.addFlashAttribute("message", "Loan deleted successfully. ID: " + loanID);
        return "redirect:/loan.html";
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

        return "redirect:/loan-list.html";
    }
}
