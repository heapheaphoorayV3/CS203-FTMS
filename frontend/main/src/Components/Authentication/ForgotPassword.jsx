import { useState } from "react";
import InputField from "../Others/InputField";
import SubmitButton from "../Others/SubmitButton";
import AuthService from "../../Services/Authentication/AuthService";

export default function ForgotPassword() {
  // Save form data
  const [formData, setFormData] = useState({
    email: "",
  });
  const [error, setError] = useState();

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
    console.log("form data: " + formData.email);
    try {
        const response = await AuthService.forgetPassword(formData.email);
        console.log("Response: " + response);
    } catch (error) {
        console.log("Error: " + error);
        setError(error.response.data);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center bg-white h-screen">

      <div className="flex flex-col items-center bg-white p-8 rounded-lg shadow-lg w-[600px]">
        <h1 className="text-3xl font-semibold mb-6 text-center">
          Change Password
        </h1>

        <form className="flex flex-col w-96 gap-5">
          <InputField
            name="email"
            placeholder="Recovery Email"
            type="text"
            value={formData.email}
            onChange={handleValueChange}
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
