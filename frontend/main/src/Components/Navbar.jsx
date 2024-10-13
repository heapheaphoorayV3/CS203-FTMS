import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../Assets/logosvg.svg";
import jacpic from "../Assets/jackinpic.jpg";
import NavbarButton from "./Others/NavbarButton";
import SubmitButton from "./Others/SubmitButton";
import FencerService from "../Services/Fencer/FencerService";
import OrganiserService from "../Services/Organiser/OrganiserService";

const Navbar = () => {
  const [isUserDropdownOpen, setIsUserDropdownOpen] = useState(false);
  const isLoggedIn = sessionStorage.getItem("token");
  const userType = sessionStorage.getItem("userType");

  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        let response = null;
        if(userType === "F"){
          response = await FencerService.getProfile()
        }
        else if(userType === "O"){
          response = await OrganiserService.getProfile();
        }
        {/* else if(userType === "A"){
          response = await AdminService.getProfile();
           */}
        setUserData(response.data);

      } catch (error) {
        console.error("Error fetching user data:", error);
      }
    };
    fetchData();
  }, [sessionStorage.getItem("userType")]);

  const toggleUserDropdown = () => {
    setIsUserDropdownOpen(!isUserDropdownOpen);
  };

  const navigate = useNavigate();

  const handleSubmit = (e, path) => {
    e.preventDefault();
    navigate(path);
  };

  const handleLogout = () => {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("userType");
    navigate("/signin");
  };

  return (
    <>
      {isLoggedIn ? (
        <nav className="h-full w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
          <div className="w-full flex items-center justify-between mx-auto p-2.5">
            <a href="/" className="flex items-center">
              <img src={logo} className="pl-3 h-8" alt="Logo" />
            </a>

            <div className="relative ml-auto">
              <button
                onClick={toggleUserDropdown}
                type="button"
                className="flex text-sm bg-gray-800 rounded-full focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-600"
                aria-expanded={isUserDropdownOpen}
              >
                <span className="sr-only">Open user menu</span>
                <img
                  className="w-8 h-8 rounded-full"
                  src={jacpic}
                  alt="user photo"
                />
              </button>

              <div
                className={`${isUserDropdownOpen ? "block" : "hidden"
                  } absolute right-0 mt-50 w-48 bg-white rounded-lg shadow-lg z-50 dark:bg-gray-700`}
              >
                <div className="px-4 py-3">
                  {userData ? (
                    <>
                      <span className="block text-sm text-gray-900 dark:text-white">
                        {userData.name}
                      </span>
                      <span className="block text-sm text-gray-500 truncate dark:text-gray-400">
                        {userData.email}
                      </span>
                    </>
                  ) : (
                    <span className="block text-sm text-gray-500 truncate dark:text-gray-400">
                      Loading...
                    </span>
                  )}
                </div>
                <ul className="py-2">
                  <li>
                    <a
                      onClick={handleLogout}
                      className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-200 dark:hover:text-white"
                    >
                      Sign out
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </nav>
      ) : (
        <nav className="fixed top-0 z-50 w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
          <div className="w-full flex items-center justify-between mx-auto py-0.5 px-2.5">
            <a href="/" className="flex items-center">
              <img src={logo} className="pl-3 h-8" alt="Logo" />
            </a>
            <div className="relative ml-auto flex space-x-4">
              <NavbarButton
                onSubmit={(e) => handleSubmit(e, "/signup-options")}
              >
                Sign up
              </NavbarButton>
              <NavbarButton onSubmit={(e) => handleSubmit(e, "/signin")}>
                Sign in
              </NavbarButton>
            </div>
          </div>
        </nav>
      )}
    </>
  );
};

export default Navbar;
