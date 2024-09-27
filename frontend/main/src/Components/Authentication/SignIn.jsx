import { React, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../../Assets/logo.png";
import SubmitButton from "../Others/SubmitButton";
import PasswordField from "../Others/PasswordField";
import InputField from "../Others/InputField";
import AuthService from "../../Services/Authentication/AuthService";
import { ProtectedAPI } from "../../Services/ProtectedAPI";
import { Link } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";
import { useAuth } from "../AuthProvider";

export default function SignIn() {

  const { handleSubmit, control, formState: { errors }} = useForm();

  // Use Auth
  const { authToken, handleLogin } = useAuth();

  const navigate = useNavigate();
  
  // Call handleLogin
  const onSubmit = async (data) => {
    try {
      handleLogin(data);
    } catch (error) {
      console.log(error);
      setError(error);
    }
  };

  // Display error message if user log in fails
  const [error, setError] = useState(null);

  return (
    <div className="flex flex-col justify-center items-center h-screen bg-gray-200">
      <div className="flex flex-col items-center bg-white p-8 rounded-lg shadow-lg w-[600px]">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h1 className="text-3xl font-semibold mb-10 text-center">Sign In</h1>
        </div>

        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-5">

            <Controller
              name="email"
              control={control}
              defaultValue=""
              rules={{ required: "Please fill this in!"}}
              render={({ field: { onChange, value } }) => (
                <InputField placeholder="Email" type="text" value={value} onChange={onChange} error={errors.email}/>
              )}
            />

            <Controller
              name="password"
              control={control}
              defaultValue=""
              rules={{ required: "Please fill this in!"}}
              render={({ field: { onChange, value } }) => (
                <PasswordField placeholder="Password" type="text" value={value} onChange={onChange} error={errors.password}/>
              )}
            />

            <SubmitButton onSubmit={handleSubmit}>Sign in</SubmitButton>

          </form>

          <p className="mt-3 text-center text-sm text-gray-500">
            Not a member?{" "}
            <Link
              to="/signup-options"
              className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
            >
              Sign up here!
            </Link>
          </p>

          {error && <h1 className="mt-5 text-center text-red-500 ">Login Failed. Username or password is incorrect.</h1>}
        </div>
      </div>
    </div>
  );
}
