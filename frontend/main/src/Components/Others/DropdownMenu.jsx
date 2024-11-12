import { useState, useEffect, useRef } from "react";
import { EllipsisVerticalIcon } from "@heroicons/react/20/solid";

const DropdownMenu = ({ entity, updateEntity, deleteEntity }) => {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef(null);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    const handleClickOutside = (event) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
            setIsOpen(false);
        }
    };

    useEffect(() => {
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const handleUpdateEntity = () => {
        updateEntity();
        setIsOpen(false);
    };

    const handleDeleteEntity = () => {
        deleteEntity();
        setIsOpen(false);
    };

    return (
        <div className="relative inline-block text-left" ref={dropdownRef}>
            <button
                id="dropdownDividerButton"
                onClick={toggleDropdown}
                className="p-2 focus:outline-none hover:bg-gray-300 rounded"
                type="button"
            >
                <EllipsisVerticalIcon className="h-5 w-5 text-black" />
            </button>

            {/* Dropdown Menu */}
            <div
                id="dropdownDivider"
                className={`absolute right-0 mt-2 z-10 ${isOpen ? 'block' : 'hidden'} bg-white divide-y divide-gray-100 rounded-lg shadow-md border border-gray-200  w-40 dark:bg-gray-700`}
            >
                <ul
                    className="py-2 text-sm text-gray-700 dark:text-gray-200"
                    aria-labelledby="dropdownDividerButton"
                >
                    <li>
                        <button
                            className="w-full text-center px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                            onClick={handleUpdateEntity}
                        >
                            Update {entity}
                        </button>
                    </li>
                    <li>
                        <button
                            className="w-full text-center px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                            onClick={handleDeleteEntity}
                        >
                            Delete {entity}
                        </button>
                    </li>                  
                </ul>
            </div>
        </div>
    );
};

export default DropdownMenu;