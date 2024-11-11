import { useState } from "react";
import InputField from "../Others/InputField";
import { useForm, Controller } from "react-hook-form";
import validator from "validator";
import SubmitButton from "../Others/SubmitButton";
import AuthService from "../../Services/Authentication/AuthService";

export default function ForgotPassword() {
  const { control, handleSubmit, formState: { errors } } = useForm();
  const [error, setError] = useState();

  //Send form data to server
  const onSubmit = async (data) => {
    try {
        const response = await AuthService.forgetPassword(data.email);
        console.log("Response: " + response);
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError("An error has occured, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("An error has occured, please try again later.");
      }
    }
  };

  return (
    <div className="flex flex-col justify-center items-center bg-white h-screen">

      <div className="flex flex-col items-center bg-white p-8 rounded-lg shadow-lg w-[600px]">
        <h1 className="text-3xl font-semibold mb-6 text-center">
          Change Password
        </h1>

        <form className="flex flex-col w-96 gap-5" onSubmit={handleSubmit(onSubmit)}>
          <Controller
            name="email"
            control={control}
            defaultValue=""
            rules={{
              required: "Please fill this in!",
              validate: (value) => validator.isEmail(value) || "Please enter a valid email!",
            }}
            render={({ field: { onChange, value } }) => (
              <InputField
                placeholder="Email"
                type="text"
                value={value}
                onChange={onChange}
                error={errors.email}
              />
            )}
          />
          <SubmitButton onSubmit={handleSubmit}>
            Send Confirmation Email
          </SubmitButton>
        </form>
        {error && <h2 className="text-red-500 text-center mt-4"> {error} </h2>}
      </div>
    </div>
  );
}
