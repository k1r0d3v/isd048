package es.udc.ws.app.model.util.validation;

import es.udc.ws.util.exceptions.InputValidationException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.regex.Pattern;

public class PropertyValidator
{
    private PropertyValidator() {}

    public static void validateMajorEqualsThan(String first, String second, double major, double that)
            throws InputValidationException {
        if (major < that)
            throw new InputValidationException("Invalid " + first +
                    " value (it must be greater or equals than " + second + "): " + first + ">=" + second);
    }

    public static void validateLong(String propertyName,
                                    Long value, int lowerValidLimit, int upperValidLimit)
            throws InputValidationException {

        if ( (value == null) || (value < lowerValidLimit) || (value > upperValidLimit) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " + value);
        }

    }

    public static void validateNotNegativeLong(String propertyName,
                                               Long longValue) throws InputValidationException {

        if (longValue == null || longValue < 0) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0): " +	longValue);
        }

    }

    public static void validateFloat(String propertyName,
                                      Float doubleValue, float lowerValidLimit, float upperValidLimit)
            throws InputValidationException {

        if (doubleValue == null || (doubleValue < lowerValidLimit) ||
                (doubleValue > upperValidLimit)) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be gtrater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " +
                    doubleValue);
        }

    }

    public static void validateDouble(String propertyName,
                                      Double doubleValue, double lowerValidLimit, double upperValidLimit)
            throws InputValidationException {

        if (doubleValue == null || (doubleValue < lowerValidLimit) ||
                (doubleValue > upperValidLimit)) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be gtrater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " +
                    doubleValue);
        }

    }

    public static void validateMandatoryString(String propertyName,
                                               String stringValue) throws InputValidationException {

        if ( (stringValue == null) || (stringValue.length() == 0) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it cannot be null neither empty): " +
                    stringValue);
        }

    }

    public static void validateAfterDate(String propertyName,
                                        Calendar date,
                                        Calendar after) throws InputValidationException {

        if ( (date == null) || date.before(after) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be after " + after + " ): " +
                    date);
        }

    }

    public static void validateBeforeDate(String propertyName,
                                         Calendar date,
                                         Calendar before) throws InputValidationException {

        if ( (date == null) || date.after(before) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be before " + before + " ): " +
                    date);
        }

    }

    public static void validateFutureDate(String propertyName,
                                        Calendar propertyValue) throws InputValidationException {

        Calendar now = Calendar.getInstance();
        if ( (propertyValue == null) || (propertyValue.before(now)) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be a future date): " +
                    propertyValue);
        }

    }

    public static void validateCreditCard(String propertyValue)
            throws InputValidationException {

        boolean validCreditCard = true;
        if ( (propertyValue != null) && (propertyValue.length() == 16) ) {
            try {
                new BigInteger(propertyValue);
            } catch (NumberFormatException e) {
                validCreditCard = false;
            }
        } else {
            validCreditCard = false;
        }
        if (!validCreditCard) {
            throw new InputValidationException("Invalid credit card number" +
                    " (it should be a sequence of 16 numeric digits): " +
                    propertyValue);
        }

    }

    public static void validateEMail(String email)
            throws InputValidationException {
        if (email == null)
            throw new InputValidationException("Email should not be null");

        String[] ab = email.split("@");
        if (ab.length != 2) throw new InputValidationException("Invalid email format, expected one @");
        String[] cd = ab[1].split("\\.");
        if (cd.length != 2) throw new InputValidationException("Invalid email format, expected one domain dot");
    }
}
