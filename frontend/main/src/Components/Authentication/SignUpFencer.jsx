import { useNavigate } from "react-router-dom";
import validator from "validator";
import { useForm, Controller } from "react-hook-form";
import AuthService from "../../Services/Authentication/AuthService";
import CountrySelector from "../Others/CountrySelector";
import DobField from "../Others/DobField";
import InputField from "../Others/InputField";
import PasswordField from "../Others/PasswordField";
import SubmitButton from "../Others/SubmitButton";

export default function SignUpFencer() {
  const { handleSubmit, control, watch, formState: { errors }} = useForm();

  // Watch the password field (to see if confirm password and password matches)
  const password = watch("password");

  const navigate = useNavigate();

  const onSubmit = async (data) => {
    console.log(data);

    // Separate comfirmPassword from data before sending to backend
    const { confirmPassword, ...formData} = data;

    // Country object is returned from the CountrySelector component --> consist of label and value (need only label for form)
    const country = data.country.label;
    formData.country = country;

    console.log(formData);
    
    try {
      await AuthService.createFencer(formData).then(() => {
        navigate("/dashboard");
      });
    } catch (error) {
      console.log(error);
    }

  };

  return (
    <div className="flex flex-col h-full justify-center items-center bg-gray-200 relative">
      <div className="flex flex-col my-12 items-center bg-white p-8 rounded-lg shadow-lg w-[600px] relative">

        <h1 className="text-3xl font-semibold mb-10 text-center">
          Sign up for a Fencer account
        </h1>
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col w-96 gap-5">
          <Controller
            name="firstName"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!" }}
            render={({ field: { onChange, value } }) => (
              <InputField placeholder="First Name" type="text" value={value} onChange={onChange} error={errors.firstName}/>
            )}
          />
          <Controller
            name="lastName"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!" }}
            render={({ field: { onChange, value } }) => (
              <InputField placeholder="Last Name" type="text" value={value} onChange={onChange} error={errors.lastName}/>
            )}
          />
          <Controller
            name="dateOfBirth"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!",
                    validate: value => {
                      const selectedDate = new Date(value);
                      const today = new Date();
                      today.setHours(0, 0, 0, 0); // Set time to midnight to compare only dates
                      return selectedDate <= today || "Please enter a valid date of birth!";
                    }
             }}
            render={({ field: { onChange, value } }) => (
              <DobField placeholder="Last Name" type="text" value={value} onChange={onChange} error={errors.dateOfBirth}/>
            )}
          />
          <Controller
            name="email"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!",
                     validate: value => validator.isEmail(value) || "Invalid email"
             }}
            render={({ field: { onChange, value } }) => (
              <InputField placeholder="Email" type="text" value={value} onChange={onChange} error={errors.email}/>
            )}
          />
          <Controller
            name="country"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!" }}
            render={({ field: { onChange, value } }) => (
              <CountrySelector value={value} onChange={onChange} error={errors.country}/>
            )}
          />
          <Controller
            name="contactNo"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!",
                      validate: value => validator.isMobilePhone(value) || "Please enter a valid phone number!"
             }}
            render={({ field: { onChange, value } }) => (
              <InputField placeholder="Contact Number" type="text" value={value} onChange={onChange} error={errors.contactNo}/>
            )}
          />
          <Controller
            name="password"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!",
                     validate: value => validator.isStrongPassword(value) || "Password should be at least 8 characters, and contain at least one lowercase character, uppercase character, number, and symbol!"
             }}
            render={({ field: { onChange, value } }) => (
              <PasswordField placeholder="Password" type="text" value={value} onChange={onChange} error={errors.password}/>
            )}
          />
          <Controller
            name="confirmPassword"
            control={control}
            defaultValue=""
            rules={{ required: "Please fill this in!",
                     validate: value => value === password || "Passwords do not match!"
             }}
            render={({ field: { onChange, value } }) => (
              <PasswordField placeholder="Confirm Password" type="text" value={value} onChange={onChange} error={errors.confirmPassword}/>
            )}
          />

          <SubmitButton onSubmit={handleSubmit}>Sign up</SubmitButton>
        </form>
      </div>
    </div>
  );
}