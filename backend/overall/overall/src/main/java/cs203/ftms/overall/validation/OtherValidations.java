package cs203.ftms.overall.validation;

import java.time.LocalDate;
import java.time.Year;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;

/**
 * A utility class for validating various aspects of tournament events and fencer data.
 */
public class OtherValidations {

    /**
     * Validates that the event date is within the tournament's start and end dates.
     *
     * @param e The event to validate.
     * @param t The tournament containing the event.
     * @throws MethodArgumentNotValidException if the event date is outside the tournament period.
     */
    public static void validEventDate(Event e, Tournament t) throws MethodArgumentNotValidException {
        LocalDate tournamentStartDate = t.getStartDate();
        LocalDate tournamentEndDate = t.getEndDate();
        LocalDate eventDate = e.getDate();

        if (!(eventDate.isBefore(tournamentEndDate.plusDays(1)) && eventDate.isAfter(tournamentStartDate.minusDays(1)))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(e.getDate(), "eventDate");
            bindingResult.addError(new FieldError("eventDate", "eventDate", "Event date must be within tournament period."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that the tournament sign-up end date is before the start date of the event.
     *
     * @param t The tournament to validate.
     * @throws MethodArgumentNotValidException if the sign-up end date is not at least one day before the event start date.
     */
    public static void validTournamentSignUpEndDate(Tournament t) throws MethodArgumentNotValidException {
        LocalDate eventStartDate = t.getStartDate();
        LocalDate signUpEndDate = t.getSignupEndDate();

        if (!signUpEndDate.isBefore(eventStartDate.minusDays(1))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(t, "tournament");
            bindingResult.addError(new FieldError("tournament", "signUpEndDate", "Sign-up end date must be at least one day before the event start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that a given tournament sign-up end date is before the specified event start date.
     *
     * @param eventStartDate The start date of the event.
     * @param signUpEndDate  The end date for sign-ups.
     * @throws MethodArgumentNotValidException if the sign-up end date is not at least one day before the event start date.
     */
    public static void validTournamentSignUpEndDate(LocalDate eventStartDate, LocalDate signUpEndDate) throws MethodArgumentNotValidException {
        if (!signUpEndDate.isBefore(eventStartDate.minusDays(1))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(signUpEndDate, "signUpEndDate");
            bindingResult.addError(new FieldError("signUpEndDate", "signUpEndDate", "Sign-up end date must be at least one day before the event start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that the debut year of a fencer is at least 8 years after their birth year and does not exceed the current year.
     *
     * @param f    The fencer whose debut year is to be validated.
     * @param year The debut year to validate.
     * @throws MethodArgumentNotValidException if the debut year is less than 8 years after the birth year or greater than the current year.
     */
    public static void validDebutYear(Fencer f, int year) throws MethodArgumentNotValidException {
        int dobyear = f.getDateOfBirth().getYear();
        int currentyear = Year.now().getValue();
        if ((year < dobyear + 8) || year > currentyear) {
            BindingResult bindingResult = new BeanPropertyBindingResult(year, "debutYear");
            bindingResult.addError(new FieldError("debutYear", "debutYear", "The debut year must be at least 8 years after the birth year."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that the tournament's start date is not after its end date.
     *
     * @param t The tournament to validate.
     * @throws MethodArgumentNotValidException if the start date is after the end date.
     */
    public static void validTournamentDates(Tournament t) throws MethodArgumentNotValidException {
        LocalDate startDate = t.getStartDate();
        LocalDate endDate = t.getEndDate();
        if (startDate.isAfter(endDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(startDate, "startDate");
            bindingResult.addError(new FieldError("startDate", "startDate", "The end date cannot be before the start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that the start date is not after the end date.
     *
     * @param startDate The start date of the tournament or event.
     * @param endDate   The end date of the tournament or event.
     * @throws MethodArgumentNotValidException if the start date is after the end date.
     */
    public static void validTournamentDates(LocalDate startDate, LocalDate endDate) throws MethodArgumentNotValidException {
        if (startDate.isAfter(endDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(startDate, "startDate");
            bindingResult.addError(new FieldError("startDate", "startDate", "The end date cannot be before the start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that a poule score is an integer between 0 and 5.
     *
     * @param scoreStr The score to validate as a string.
     * @return The validated score as an integer.
     * @throws MethodArgumentNotValidException if the score is not an integer or is out of the range 0-5.
     */
    public static int validPoulePoint(String scoreStr) throws MethodArgumentNotValidException {
        int score = -1;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException ex) {
            BindingResult bindingResult = new BeanPropertyBindingResult(scoreStr, "Poule Score");
            bindingResult.addError(new FieldError("pouleScore", "pouleScore", "The poule score must be an integer within 0 to 5."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (score < 0 || score > 5) {
            BindingResult bindingResult = new BeanPropertyBindingResult(scoreStr, "Poule Score");
            bindingResult.addError(new FieldError("pouleScore", "pouleScore", "The poule score must be an integer within 0 to 5."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        return score;
    }

    /**
     * Validates that an updated event date is within the tournament's start and end dates.
     *
     * @param newDate The new date for the event.
     * @param t       The tournament to which the event belongs.
     * @throws MethodArgumentNotValidException if the new date is outside the tournament period.
     */
    public static void validUpdateEventDate(LocalDate newDate, Tournament t) throws MethodArgumentNotValidException {
        LocalDate tournamentStartDate = t.getStartDate();
        LocalDate tournamentEndDate = t.getEndDate();
        if (!(newDate.isBefore(tournamentEndDate.plusDays(1)) && newDate.isAfter(tournamentStartDate.minusDays(1)))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(newDate, "eventDate");
            bindingResult.addError(new FieldError("eventDate", "eventDate", "Event date must be within tournament period."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    /**
     * Validates that an updated tournament start and end date is consistent with existing events.
     *
     * @param e           The event to check against.
     * @param newStartDate The proposed new start date.
     * @param newEndDate   The proposed new end date.
     * @throws MethodArgumentNotValidException if the new start date is after any existing event dates or if the new end date is before any existing event dates.
     */
    public static void validUpdateTournamentDate(Event e, LocalDate newStartDate, LocalDate newEndDate) throws MethodArgumentNotValidException {
        LocalDate eventDate = e.getDate();
        if (eventDate.isBefore(newStartDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(newStartDate, "startDate");
            bindingResult.addError(new FieldError("startDate", "startDate", "New tournament start date is invalid as there are existing events before this date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (eventDate.isAfter(newEndDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(newEndDate, "endDate");
            bindingResult.addError(new FieldError("endDate", "endDate", "New tournament end date is invalid as there are existing events after this date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }
}
