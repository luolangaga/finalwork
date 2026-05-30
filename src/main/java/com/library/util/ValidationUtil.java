package com.library.util;

import com.library.model.entity.LibraryResource;
import com.library.model.entity.Borrower;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^1[3-9]\\d{9}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidResource(LibraryResource resource) {
        return resource != null
                && resource.getTitle() != null
                && !resource.getTitle().isEmpty();
    }

    public static boolean isValidBorrower(Borrower borrower) {
        return borrower != null
                && borrower.getName() != null
                && !borrower.getName().isEmpty()
                && isValidEmail(borrower.getEmail());
    }

    public static String validateBorrowOperation(
            String borrowerId, String resourceId) {
        if (borrowerId == null || borrowerId.isEmpty())
            return "借阅者编号不能为空";
        if (resourceId == null || resourceId.isEmpty())
            return "资源编号不能为空";
        return null;
    }
}
