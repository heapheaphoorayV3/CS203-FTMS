import { React, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../../Assets/logo.png";
import SubmitButton from "../Others/SubmitButton";
import PasswordField from "../Others/PasswordField";
import InputField from "../Others/InputField";
import AuthService from "../../Services/Authentication/AuthService";
import { ProtectedAPI } from "../../Services/ProtectedAPI";
import { Link } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";

export default function SignIn() {

  const { handleSubmit, control, formState: { errors }} = useForm();

  // Login Error Message if wrong password/username
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // Handle Input field change
  const handleValueChange = (e) => {
    // Ensure that the name attribute is set for the input field
    if (!e.target.name) {
      return;
    }
  }
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = useForm();

  // Login Error Message if wrong password/username
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // Call handleLogin
  const onSubmit = async (data) => {
    try {
      // Get response from server
      const response = await AuthService.loginUser(data);
      const { token, userType } = response.data;

      sessionStorage.setItem("token", token);
      sessionStorage.setItem("userType", userType);

      if (!token) {
        setError(true);
      } else {
        setError(false);

        // Route to correct dashboard based on userType
        if (userType === "F") {
          console.log("Redirecting to dashboard");
          setTimeout(() => {
            navigate("/fencer-dashboard");
            window.location.reload();
          }, 100);
          // navigate("/fencer-dashboard");
        }

        if (userType === "O") {
          console.log("Redirecting to organiser dashboard");

          setTimeout(() => {
            navigate("/organiser-dashboard");
            window.location.reload();
          }, 100);
          // navigate("/organiser-dashboard");
        }

        if (userType === "A") {
          console.log("Redirecting to admin dashboard");
          navigate("/admin-dashboard");
        }
      }
    } catch (error) {
      console.log("Failed Login");
    }

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };
  };

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
<<<<<<< Updated upstream
            {errorMessage && <p className="text-red-500">{errorMessage}</p>}
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

          {error && (
            <h1 className="mt-5 text-center text-red-500 ">
              Login Failed. Username or password is incorrect.
            </h1>
          )}
>>>>>>> Stashed changes

          {error && <h1 className="mt-5 text-center text-red-500 ">Login Failed. Username or password is incorrect.</h1>}
        </div>
      </div>
    </div>
  );
}
