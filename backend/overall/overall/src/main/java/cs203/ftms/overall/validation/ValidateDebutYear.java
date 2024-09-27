// package cs203.ftms.overall.validation;

// import java.util.Map;
// import org.springframework.beans.factory.annotation.Autowired;
// import cs203.ftms.overall.service.fencer.FencerService;
// import jakarta.validation.ConstraintValidator;
// import jakarta.validation.ConstraintValidatorContext;
// import cs203.ftms.overall.dto.EditFencerProfileDTO;
// import cs203.ftms.overall.exception.InvalidDebutYearException;


// public class ValidateDebutYear implements ConstraintValidator<ValidDebutYear, Map<String, Integer>> {
//     private static final int MIN_YEARS_AFTER_BIRTH = 8;

//     @Autowired
//     private FencerService fencerService;  // Inject the service
    
//     public ValidateDebutYear (FencerService fencerService){
//         this.fencerService = fencerService;
//     }
//     @Override
//     public void initialize(ValidDebutYear constraintAnnotation) {
//         // No initialization needed
//     }

//     @Override
//     public boolean isValid(Map<String, Integer> map, ConstraintValidatorContext context) {
//         if (map.getValue() == null) {
//             throw new InvalidDebutYearException("Debut year must be provided.");
//         }

//         // Fetch the birthYear using the userId
//         Integer birthYear = fencerService.getBirthYearByUserId(dto.getUserId());

//         if (birthYear == null) {
//             throw new InvalidDebutYearException("Birth year not found for the user.");
//         }

//         // Validate that debutYear is at least 8 years after birthYear
//         if (dto.getDebutYear() < birthYear + MIN_YEARS_AFTER_BIRTH) {
//             throw new InvalidDebutYearException("The debut year must be at least 8 years after the birth year.");
//         }

//         return true;
//     }
// }