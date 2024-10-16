import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import validator from "validator";
import PasswordField from "../Others/PasswordField";
import InputField from "../Others/InputField";
import SubmitButton from "../Others/SubmitButton";
import CountrySelector from "../Others/CountrySelector";
import AuthService from "../../Services/Authentication/AuthService";

export default function SignUpOrganiser() {
  const { handleSubmit, control, watch, formState: { errors } } = useForm();
  const [isSignUp, setSignUp] = useState(false);
  const [isError, setError] = useState(false);

  // Watch the password field (to see if confirm password and password matches)
  const password = watch("password");

  const onSubmit = async (data) => {

    // Separate comfirmPassword from data before sending to backend
    const { confirmPassword, ...formData } = data;

    // Country object is returned from the CountrySelector component --> consist of label and value (need only label for form)
    const country = data.country.label;
    formData.country = country;

    console.log(formData);

    try {
      await AuthService.createOrganiser(formData);
      console.log("Sign up successful!")
      setSignUp(true);
    } catch (error) {
      setError(true);
      console.log(error);
    }

  };

  return (
    <div className="flex flex-col h-full justify-center items-center bg-gray-200 relative">
      <div className="flex flex-col my-12 items-center bg-white p-8 rounded-lg shadow-lg w-[600px] relative">

        {!isSignUp && (
          <>
            <h1 className="text-3xl font-semibold mb-10 text-center">
              Sign up for an Organiser account
            </h1>
            <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col w-96 gap-5">
              <Controller
                name="name"
                control={control}
                defaultValue=""
                rules={{ required: "Please fill this in!" }}
                render={({ field: { onChange, value } }) => (
                  <InputField placeholder="Name" type="text" value={value} onChange={onChange} error={errors.name} />
                )}
              />
              <Controller
                name="email"
                control={control}
                defaultValue=""
                rules={{
                  required: "Please fill this in!",
                  validate: value => validator.isEmail(value) || "Invalid email"
                }}
                render={({ field: { onChange, value } }) => (
                  <InputField placeholder="Email" type="text" value={value} onChange={onChange} error={errors.email} />
                )}
              />
              <Controller
                name="country"
                control={control}
                defaultValue=""
                rules={{ required: "Please fill this in!" }}
                render={({ field: { onChange, value } }) => (
                  <CountrySelector value={value} onChange={onChange} error={errors.country} />
                )}
              />
              <Controller
                name="contactNo"
                control={control}
                defaultValue=""
                rules={{
                  required: "Please fill this in!",
                  validate: value => validator.isMobilePhone(value) || "Please enter a valid phone number!"
                }}
                render={({ field: { onChange, value } }) => (
                  <InputField placeholder="Contact Number" type="text" value={value} onChange={onChange} error={errors.contactNo} />
                )}
              />
              <Controller
                name="password"
                control={control}
                defaultValue=""
                rules={{
                  required: "Please fill this in!",
                  validate: value => validator.isStrongPassword(value) || "Password should be at least 8 characters, and contain at least one lowercase character, uppercase character, number, and symbol!"
                }}
                render={({ field: { onChange, value } }) => (
                  <PasswordField placeholder="Password" type="text" value={value} onChange={onChange} error={errors.password} />
                )}
              />
              <Controller
                name="confirmPassword"
                control={control}
                defaultValue=""
                rules={{
                  required: "Please fill this in!",
                  validate: value => value === password || "Passwords do not match!"
                }}
                render={({ field: { onChange, value } }) => (
                  <PasswordField placeholder="Confirm Password" type="text" value={value} onChange={onChange} error={errors.confirmPassword} />
                )}
              />

              <SubmitButton onSubmit={handleSubmit}>Sign up</SubmitButton>
            </form>
          </>
        )}

        {isError && (
          <h1 className="text-xl font-semibold text-center text-red-500">
            An error has occurred. Email may have already been used!
          </h1>
        )}

        {isSignUp && (
          <div>
            <h1 className="text-2xl font-semibold text-center text-green-400">
              Sign up successful!
            </h1>
            <br />
            <h1 className="text-md text-center">
              Please wait for us to verify your organiser account. An email will be sent to you once your account has been verified!
            </h1>
          </div>
        )}

      </div>
    </div>
  );
}
