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
        String phoneNumber = user.getPhoneNumber();
        String password = user.getPassword();

        dataHandling.addUser(firstName, lastName, zipCode, email, phoneNumber, password);
        redirectAttributes.addFlashAttribute("message", "User created successfully!");
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getUserByID")
    public String getUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByID(userID);
        redirectAttributes.addFlashAttribute("message", user.toString());
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getUserByName")
    public String getUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        User user = dataHandling.getUserByName(firstName, lastName);
        redirectAttributes.addFlashAttribute("message", user.toString());
        return "redirect:/";
    }

    @PostMapping("/IPaccess/deleteUserByID")
    public String deleteUserByID(@RequestParam int userID, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByID(userID);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. ID: " + userID);
        return "redirect:/";
    }

    @PostMapping("/IPaccess/deleteUserByName")
    public String deleteUserByName(@RequestParam String firstName, @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        dataHandling.deleteUserByName(firstName, lastName);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully. User: " + firstName + " " + lastName);
        return "redirect:/";
    }

    //    ================================
    //    Device endpoints
    //    ================================
    @GetMapping("/IPaccess/getAllDevices")
    @ResponseBody
    public String getAllDevices(@RequestParam(required = false) Boolean available, RedirectAttributes redirectAttributes){
        StringBuilder deviceString = new StringBuilder();

        // This looks confusing so to clarify, query is based on availability
        // If Boolean available = True, the request is for all *available* devices
        // If it is False, the request is for all devices
        if(available != null && available){
            for (Device device : dataHandling.getAvailableDevices()) {
                deviceString.append(device.toString()).append("\n");
            }
            redirectAttributes.addFlashAttribute("message", deviceString.toString());
            return "redirect:/";
        }

        for (Device device : dataHandling.getAllDevices()) {
            deviceString.append(device.toString()).append("\n");
        }
        redirectAttributes.addFlashAttribute("message", deviceString.toString());
        return "redirect:/";
    }

    @PostMapping("/IPaccess/addDevice")
    public String addDevice(@RequestParam ApplicationEnums.DeviceType deviceType, @RequestParam boolean availability, @RequestParam int renterID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.addDevice(deviceType, availability, renterID);
        redirectAttributes.addFlashAttribute("message", "Device added successfully. Device: " + device.toString());
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getDeviceByID")
    public String getDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        Device device = dataHandling.getDeviceByID(deviceID);
        redirectAttributes.addFlashAttribute("message", device.toString());
        return "redirect:/";
    }

    @PostMapping("/IPaccess/removeDeviceByID")
    public String removeDeviceByID(@RequestParam int deviceID, RedirectAttributes redirectAttributes) {
        dataHandling.removeDeviceByID(deviceID);
        redirectAttributes.addFlashAttribute("message", "Device removed successfully. ID: " + deviceID);
        return "redirect:/";
    }

    //    ================================
    //    Loan endpoints
    //    ================================
    @PostMapping("/IPaccess/addLoan")
    public String addLoan(@RequestParam int userID, @RequestParam int deviceID, @RequestParam ApplicationEnums.LoanStatus loanStatus, RedirectAttributes redirectAttributes) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(14);

        Loan loan = dataHandling.addLoan(userID, deviceID, start, end, loanStatus);

        redirectAttributes.addFlashAttribute("message", loan.toString());
        return "redirect:/";
    }

    @GetMapping("/IPaccess/getLoanByID")
    public String getLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributes) {
        Loan loan = dataHandling.getLoanByID(loanID);
        redirectAttributes.addFlashAttribute("message", loan.toString());

        return "redirect:/";
    }

    @GetMapping("/IPaccess/deleteLoanByID")
    public String deleteLoanByID(@RequestParam int loanID, RedirectAttributes redirectAttributess) {
        dataHandling.deleteLoanByID(loanID);
        redirectAttributess.addFlashAttribute("message", "Loan deleted successfully. ID: " + loanID);

        return "redirect:/";
    }

}
