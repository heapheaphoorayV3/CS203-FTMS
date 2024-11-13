import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import PasswordField from "../Others/PasswordField";
import SubmitButton from "../Others/SubmitButton";
import validator from "validator";
import FencerService from "../../Services/Fencer/FencerService";
import OrganiserService from "../../Services/Organiser/OrganiserService";
import AdminService from "../../Services/Admin/AdminService";
import { XCircleIcon } from "@heroicons/react/16/solid";

export default function ChangePassword({ isOpen, onClose }) {
  const userType = sessionStorage.getItem("userType");

  // Save form data
  const [formData, setFormData] = useState({
    oldPassword: "",
    newPassword: "",
    confirmNewPassword: "",
  });

  const {
    handleSubmit,
    control,
    watch,
    formState: { errors },
  } = useForm();
  const [isError, setError] = useState(false);

  // Watch the password field (to see if confirm password and password matches)
  const newPassword = watch("newPassword");

  //Send form data to server
  const onSubmit = async (data) => {

    formData.oldPassword = data.oldPassword;
    formData.newPassword = data.newPassword;
    formData.confirmNewPassword = data.confirmNewPassword;

    try {
      if (userType === "F") {
        await FencerService.changePassword(formData);
      } else if (userType === "O") {
        await OrganiserService.changePassword(formData);
      } else if (userType === "A") {
        await AdminService.changePassword(formData);
      }
      onClose(); // Close the popup after submission
    } catch (error) {
      setError(true);
      console.log(error);
    }
  };

  if (!isOpen) return null;

  return (
    // Overlay
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      {/* Popup content */}
      <div className="bg-white p-8 rounded-lg shadow-lg w-[600px] relative">
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
        >
          <XCircleIcon className="w-6 h-6" />
        </button>

        <h1 className="text-3xl font-semibold my-6 text-center">
          Change Password
        </h1>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-col w-full max-w-md mx-auto gap-5"
        >
          <Controller
            name="oldPassword"
            control={control}
            defaultValue=""
            rules={{
              required: "Old password is required!",
            }}
            render={({ field: { onChange, value } }) => (
              <PasswordField
                name="oldPassword"
                placeholder="Old Password"
                value={value}
                onChange={onChange}
                error={errors.oldPassword}
              />
            )}
          />
          <hr className="border border-black w-full" />
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
                placeholder="New Password"
                type="text"
                value={value}
                onChange={onChange}
                error={errors.newPassword}
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
                value === newPassword || "Passwords do not match!",
            }}
            render={({ field: { onChange, value } }) => (
              <PasswordField
                placeholder="Confirm New Password"
                type="text"
                value={value}
                onChange={onChange}
                error={errors.confirmNewPassword}
              />
            )}
          />
          <SubmitButton onSubmit={handleSubmit}>Confirm</SubmitButton>
        </form>
        {isError && (
          <h1 className="text-xl font-semibold text-center text-red-500">
            Original password is incorrect!
          </h1>
        )}
      </div>
    </div>
  );
}
