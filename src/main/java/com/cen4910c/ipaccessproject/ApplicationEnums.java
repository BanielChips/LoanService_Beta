package com.cen4910c.ipaccessproject;

public class ApplicationEnums {
    public enum LoanStatus {
        ACTIVE, OVERDUE, REVIEW
    }

    public enum DeviceType {
        LAPTOP, TABLET, PHONE, HOTSPOT
    }

    public enum DeviceStatus {
        ACTIVE, INACTIVE, LOANED, DAMAGED
    }

    public enum UserRole {
        USER, ADMIN;
    }

    public enum AlertType {
        NEW_OVERDUE_DEVICE,
        ANNOUNCEMENT,
        DEVICE_REPAIR_STATUS_UPDATE,
        NEW_DEVICE_REQUEST,
        DEVICE_RETURNED,
        LOAN_APPROVAL_REQUIRED,
        SYSTEM_MAINTENANCE,
        URGENT_ACTION_REQUIRED,
        NOTIFICATION
    }

    public enum AlertPriority {
        HIGH, MEDIUM, LOW
    }

/*
    TODO:
        User Management:
        UPDATE_USER
        LIST_ALL_USERS
        |
        Device Management:
        UPDATE_DEVICE
        LIST_ALL_DEVICES
        MARK_DEVICE_AS_DAMAGED
        REPAIR_DEVICE
        ASSIGN_DEVICE_TO_USER
        |
        Loan Management:
        EXTEND_LOAN
        ASSIGN_LOAN_STATUS
        CANCEL_LOAN
        LIST_ALL_LOANS
        RETURN_DEVICE
        |
        Admin Specific:
        CREATE_ADMIN_USER
        REMOVE_ADMIN_USER
        VIEW_ALL_ADMINS
 */

//    ================================
//    Admin Commands - Users, Devices, and Loans
//    ================================
    public enum AdminCommandsUser {
            ADD_USER("/IPaccess/addUser"),
            GET_USER_BY_ID("/IPaccess/getUserByID"),
            GET_USER_BY_NAME("/IPaccess/getUserByName"),
            DELETE_USER_BY_ID("/IPaccess/deleteUserByID"),
            DELETE_USER_BY_NAME("/IPaccess/deleteUserByName");

            private final String endpoint;

            AdminCommandsUser(String endpoint) {
                this.endpoint = endpoint;
            }
            public String getEndpoint() {
                return endpoint;
            }
    }

    public enum AdminCommandsDevice {

        GET_ALL_DEVICES("/IPaccess/getAllDevices"),
        ADD_DEVICE("/IPaccess/addDevice"),
        GET_DEVICE_BY_ID("/IPaccess/getDeviceByID"),
        REMOVE_DEVICE_BY_ID("/IPaccess/removeDeviceByID");

        private final String endpoint;

        AdminCommandsDevice(String endpoint) {
            this.endpoint = endpoint;
        }
        public String getEndpoint() {
            return endpoint;
        }
    }

    public enum AdminCommandsLoan {

        ADD_LOAN("/IPaccess/addLoan"),
        GET_LOAN_BY_ID("/IPaccess/getLoanByID"),
        DELETE_LOAN_BY_ID("/IPaccess/deleteLoanByID");

        private final String endpoint;

        AdminCommandsLoan(String endpoint) {
            this.endpoint = endpoint;
        }
        public String getEndpoint() {
            return endpoint;
        }
    }
//    ================================
//    End Admin commands
//    ================================

    // TODO: Admin commands Alert
}
