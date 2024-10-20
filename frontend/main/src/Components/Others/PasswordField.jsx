import { useState } from "react";
import { EyeIcon, EyeSlashIcon } from "@heroicons/react/24/solid";

export default function PasswordField({placeholder, name, value, onChange, error, ...props}) {
    
    const [passwordShown, setPasswordShown] = useState(false);
    const type = passwordShown ? "text" : "password";
    const Icon = passwordShown ? EyeSlashIcon : EyeIcon;

    let classes = null;
    if (error) {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline border-red-500";
    } else {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
    }

    return (
        <div>
            <div className="flex">
                <input
                    name={name}
                    type={type} 
                    value={value}
                    placeholder={placeholder}
                    onChange={onChange}
                    className={classes} 
                />
                <span className="flex justify-around items-center mr-[-20px]">
                    <Icon 
                        onClick={() => setPasswordShown(() => !passwordShown)} 
                        className="w-5 h-5 text-gray-400 relative right-7" 
                    />
                </span>
            </div>
            {error && <p className="text-red-500 text-s italic">{error.message}</p>}
        </div>
        

    );
}