import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";
import PasswordField from "../Others/PasswordField";
import SubmitButton from "../Others/SubmitButton";
import validator from "validator";
import AuthService from "../../Services/Authentication/AuthService";
import { XCircleIcon } from "@heroicons/react/16/solid";

export default function ResetPassword(props) {
  const { token } = useParams();
  const navigate = useNavigate();

  // Save form data
  const [formData, setFormData] = useState({
    newPassword: "",
    confirmNewPassword: "",
  });

  const {
    handleSubmit,
    control,
    watch,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    
    const form = {
      newPassword: data.newPassword,
    };
    console.log("Data: ", form);
    try {
      await AuthService.resetPassword(form, token);
      navigate("/signin"); // Navigate to the login page after successful password reset
    } catch (error) {
      console.log("Error resetting password:", error);
    }
  };

  return (
    <div className="h-full flex flex-col justify-center items-center bg-white">
      <div className="flex flex-col items-center bg-white border p-8 rounded-lg shadow-lg w-[600px]">
        <h1 className="text-3xl font-semibold my-6 text-center">
          Change Password
        </h1>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-col w-full max-w-md mx-auto gap-5"
        >
          <Controller
            name="newPassword"
            control={control}
            defaultValue=""
            rules={{
              required: "Please fill this in!",
              validate: (value) =>
                validator.isStrongPassword(value) ||
                "Password should be at least 8 characters, and contain at least one lowercase character, uppercase character, number, and symbol!",
            }}
            render={({ field: { onChange, value } }) => (
              <PasswordField
                value={value}
                placeholder="New Password"
                onChange={(e) => {
                  onChange(e);
                  setFormData((prev) => ({
                    ...prev,
                    newPassword: e.target.value,
                  }));
                }}
              />
            )}
          />
          <Controller
            name="confirmNewPassword"
            control={control}
            defaultValue=""
            rules={{
              required: "Please fill this in!",
              validate: (value) =>
                value === watch("newPassword") || "Passwords do not match!",
            }}
            render={({ field: { onChange, value } }) => (
              <PasswordField
                value={value}
                placeholder="Confirm New Password"
                onChange={(e) => {
                  onChange(e);
                  setFormData((prev) => ({
                    ...prev,
                    confirmNewPassword: e.target.value,
                  }));
                }}
              />
            )}
          />
          <SubmitButton type="submit">Submit</SubmitButton>
        </form>
      </div>
    </div>
  );
}