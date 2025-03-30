package com.cen4910c.ipaccessproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class WebHandling {

    @Autowired
    private DataHandling dataHandling;

    @GetMapping("/")
    public String home() {
        return "index";
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
        String password = user.getPassword();
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
        if (user != null) {
            if (user.getPassword().equals(password)) {
                redirectAttributes.addFlashAttribute("message", "User logged in successfully!");
            } else
                redirectAttributes.addFlashAttribute("message", "Invalid credentials");
        }
        return "redirect:/user.html";
    }

    @GetMapping("/IPaccess/checkOverdue")
    public String checkOverdue(RedirectAttributes redirectAttributes) {
        dataHandling.checkAndUpdateOverdueLoans();
        redirectAttributes.addFlashAttribute("message", "Overdue loans updated successfully.");
        return "redirect:/admin.html";
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

    //    ================================
    //    Device endpoints
    //    ================================
    @GetMapping("/IPaccess/getAllDevices")
    @ResponseBody
    public String getAllDevices(@RequestParam(required = false) Boolean available, RedirectAttributes redirectAttributes){
        StringBuilder deviceString = new StringBuilder();

        if(available != null && available){
            for (Device device : dataHandling.getAvailableDevices()) {
                deviceString.append(device.toString()).append("\n");
            }
            redirectAttributes.addFlashAttribute("message", deviceString.toString());
            return "redirect:/device.html";
        }

        for (Device device : dataHandling.getAllDevices()) {
            deviceString.append(device.toString()).append("\n");
        }
        redirectAttributes.addFlashAttribute("message", deviceString.toString());
        return "redirect:/device.html";
    }

    @PostMapping("/IPaccess/addDevice")
    public String addDevice(@RequestParam String deviceName, @RequestParam boolean availability, @RequestParam Integer renterID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.addDevice(deviceName, availability);
        redirectAttributes.addFlashAttribute("message", "Device added successfully. Device: " + device.toString());
        return "redirect:/device.html";
    }

    @GetMapping("/IPaccess/getDeviceByID")
    public String getDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.getDeviceByID(deviceID);
        if(device != null)
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

    //    ================================
    //    Loan endpoints
    //    ================================
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
        if(loan != null)
            redirectAttributes.addFlashAttribute("message", loan.toString());
        else
            redirectAttributes.addFlashAttribute("message", "Loan not found!");

        return "redirect:/loan.html";
    }

    @GetMapping("/IPaccess/deleteLoanByID")
    public String deleteLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributess) {
        dataHandling.deleteLoanByID(loanID);
        redirectAttributess.addFlashAttribute("message", "Loan deleted successfully. ID: " + loanID);

        return "redirect:/loan.html";
    }
}
