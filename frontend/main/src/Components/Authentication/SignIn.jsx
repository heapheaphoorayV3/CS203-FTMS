import { React, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../../Assets/logo.png";
import SubmitButton from "../Others/SubmitButton";
import PasswordField from "../Others/PasswordField";
import InputField from "../Others/InputField";
import AuthService from "../../Services/Authentication/AuthService";
import { ProtectedAPI } from "../../Services/ProtectedAPI";
import { Link } from "react-router-dom";

export default function SignIn() {
  // Save form data
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  // Handle Input field change
  const handleValueChange = (e) => {
    // Ensure that the name attribute is set for the input field
    if (!e.target.name) {
      return;
    }

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  //Send form data to server
  const handleSubmit = async (e) => {
    e.preventDefault();
    let credentials = {
      email: formData.email,
      password: formData.password,
    };
    console.log("credentials => " + credentials);
    AuthService.loginUser(credentials)
      .then((res) => {
        if (res.data) {
          console.log("data");
          console.log(res.data);
          localStorage.setItem("token", res.data.token);
          localStorage.setItem("userType", res.data.userType);
          ProtectedAPI.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${localStorage.getItem("token")}`;

          if (
            res.data.userType == "F" ||
            res.data.userType == "A" ||
            res.data.userType == "O"
          ) {
            console.log("login success");
            navigate("/dashboard");
          } else {
            console.log("login failed");
          }
        }
      })
      .catch((error) => {
        console.error("Error logging in:", error);
        if (error.response) {
          if (error.response.status === 403) {
            setErrorMessage("Incorrect email or password. Please try again.");
          } else {
            setErrorMessage("An error occurred. Please try again later.");
          }
        }
      });
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen bg-gray-200">
      <div className="flex flex-col items-center bg-white p-8 rounded-lg shadow-lg w-[600px]">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h1 className="text-3xl font-semibold mb-10 text-center">Sign In</h1>
        </div>

        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <form action="#" method="POST" className="flex flex-col gap-5">
            <InputField
              name="email"
              placeholder="Email"
              type="text"
              value={formData.email}
              onChange={handleValueChange}
            />
            <PasswordField
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleValueChange}
            />
            <SubmitButton onSubmit={handleSubmit}>Sign in</SubmitButton>
            {errorMessage && <p className="text-red-500">{errorMessage}</p>}
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
        </div>
      </div>
    </div>
  );
}
