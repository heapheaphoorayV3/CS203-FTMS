import React, { useState } from "react";

export default function DobField({ name, placeholder, value, error, onChange, ...props }) {

    // Manage State of dob field (type date or text so tha we can specify our placeholder)
    const [inputType, setInputType] = useState("text");

    let classes = null;
    if (error) {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline border-red-500";
    } else {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
    }

    return (
        <div>
            <input
                name="dateOfBirth"
                placeholder="Date of Birth"
                type={inputType}
                onFocus={() => setInputType("date")}
                onBlur={() => setInputType("text")}
                value={value}
                onChange={onChange}
                className={classes}
            />
            {error && <p className="text-red-500 text-s italic">{error.message}</p>}
        </div>
        
    );
}