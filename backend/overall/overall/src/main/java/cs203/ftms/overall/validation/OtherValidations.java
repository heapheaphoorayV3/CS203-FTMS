package cs203.ftms.overall.validation;

import java.time.LocalDate;
import java.time.Year;

import org.springframework.cglib.core.Local;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;

public class OtherValidations {

    public static void validEventDate(Event e, Tournament t) throws MethodArgumentNotValidException {
        LocalDate tournamentStartDate = t.getStartDate();
        LocalDate tournamentEndDate = t.getEndDate();
        LocalDate eventDate = e.getDate();

        if (!(eventDate.isBefore(tournamentEndDate.plusDays(1)) && eventDate.isAfter(tournamentStartDate.minusDays(1)))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(e.getDate(), "eventDate");
        
            bindingResult.addError(new FieldError(
            "eventDate", "eventDate", "Event date must be within tournament period."));

            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public static void validTournamentSignUpEndDate(Tournament t) throws MethodArgumentNotValidException {
        LocalDate eventStartDate = t.getStartDate();
        LocalDate signUpEndDate = t.getSignupEndDate();

        if (!signUpEndDate.isBefore(eventStartDate.minusDays(1))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(t, "tournament");
        
            bindingResult.addError(new FieldError(
            "tournament", "signUpEndDate", "Sign-up end date must be at least one day before the event start date."));

            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public static void validTournamentSignUpEndDate(LocalDate eventStartDate, LocalDate signUpEndDate) throws MethodArgumentNotValidException {
        if (!signUpEndDate.isBefore(eventStartDate.minusDays(1))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(signUpEndDate, "signUpEndDate");
            bindingResult.addError(new FieldError("signUpEndDate", "signUpEndDate", "Sign-up end date must be at least one day before the event start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public static void validDebutYear(Fencer f, int year) throws MethodArgumentNotValidException {
        int dobyear = f.getDateOfBirth().getYear();
        int currentyear = Year.now().getValue();
        if ((year < dobyear + 8) || year > currentyear) {
            BindingResult bindingResult = new BeanPropertyBindingResult(year, "debutYear");
            bindingResult.addError(new FieldError("debutYear", "debutYear", "The debut year must be at least 8 years after the birth year."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public static void validTournamentDates(Tournament t) throws MethodArgumentNotValidException {
        LocalDate startDate = t.getStartDate();
        LocalDate endDate = t.getEndDate();
        if (startDate.isAfter(endDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(startDate, "startDate");
            bindingResult.addError(new FieldError("startDate", "startDate", "The end date cannot be before the start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public static void validTournamentDates(LocalDate startDate, LocalDate endDate) throws MethodArgumentNotValidException {
        if (startDate.isAfter(endDate)) {
            BindingResult bindingResult = new BeanPropertyBindingResult(startDate, "startDate");
            bindingResult.addError(new FieldError("startDate", "startDate", "The end date cannot be before the start date."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

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

    public static void validUpdateEventDate(LocalDate newDate, Tournament t) throws MethodArgumentNotValidException {
        LocalDate tournamentStartDate = t.getStartDate();
        LocalDate tournamentEndDate = t.getEndDate();
        if (!(newDate.isBefore(tournamentEndDate.plusDays(1)) && newDate.isAfter(tournamentStartDate.minusDays(1)))) {
            BindingResult bindingResult = new BeanPropertyBindingResult(newDate, "eventDate");
            bindingResult.addError(new FieldError("eventDate", "eventDate", "Event date must be within tournament period."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

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
