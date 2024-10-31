import { useState } from "react";
import InputField from "../Others/InputField";
import logo from "../../Assets/logo white bg.png";
import SubmitButton from "../Others/SubmitButton";

export default function ForgotPassword() {
  // Save form data
  const [formData, setFormData] = useState({
    email: "",
  });

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
    console.log("form data: " + formData);
    // try {
    //     const response = await fetch('http://', {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         body: JSON.stringify(formData)
    //     });

    //     const data = await response.json();
    //     console.log(data);
    // } catch (error) {
    //     console.log(error);
    // }
  };

  return (
    <div className="flex flex-col justify-center items-center bg-white h-screen">
      <img src={logo} alt="OnlyFence" className="h-50 mx-auto mb-10" />

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
      </div>
    </div>
  );
}
