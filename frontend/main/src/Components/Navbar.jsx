import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../Assets/logosvg.svg";
import logoutIcon from "../Assets/logout.png";
import NavbarButton from "./Others/NavbarButton";
import FencerService from "../Services/Fencer/FencerService";
import OrganiserService from "../Services/Organiser/OrganiserService";
import AdminService from "../Services/Admin/AdminService";
import ChangePassword from "./Authentication/ChangePassword";

const Navbar = () => {
  const [isUserDropdownOpen, setIsUserDropdownOpen] = useState(false);
  const isLoggedIn = sessionStorage.getItem("token");
  const userType = sessionStorage.getItem("userType");
  const [isPasswordChange, setIsPasswordChange] = useState(false);
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!isLoggedIn) return;
        let response = null;
        if (userType === "F") {
          response = await FencerService.getProfile()
        }
        else if (userType === "O") {
          response = await OrganiserService.getProfile();
        }
        else if (userType === "A") {
          response = await AdminService.getProfile();
        }
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
    sessionStorage.removeItem("refreshToken");
    setIsUserDropdownOpen(!isUserDropdownOpen);
    navigate("/signin");
  };

  const handlePasswordChange = () => {
    setIsPasswordChange(true);
    setIsUserDropdownOpen(!isUserDropdownOpen);
  }

  // Determine the dashboard link based on userType
  let dashboardLink = '/';

  if (userType === 'F') {
    dashboardLink = '/fencer-dashboard';
  } else if (userType === 'O') {
    dashboardLink = '/organiser-dashboard';
  } else if (userType === 'A') {
    dashboardLink = '/admin-dashboard';
  }

  return (
    <>
      {isLoggedIn ? (
        <nav className="h-full w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
          <div className="w-full flex items-center justify-between mx-auto p-2.5">
            {/* Clicking the logo will redirect to the respective user dashboards */}
            <a href={dashboardLink} className="flex items-center">
              <img src={logo} className="pl-3 h-8" alt="Logo" />
            </a>
            <div className="relative ml-auto">
              <button
                onClick={toggleUserDropdown}
                type="button"
                className="flex justify-center items-center text-sm rounded-full focus:ring-2 focus:ring-gray-300 dark:focus:ring-gray-600"
                aria-expanded={isUserDropdownOpen}
              >
                <span className="sr-only">Open user menu</span>
                {/* <UserCircleIcon className="h-8 w-8 text-black" /> */}
                <img src={logoutIcon} className="h-6 w-6" alt="Logout Icon" />
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
                <ul className="pt-2">
                  <li>
                    <a
                      onClick={handlePasswordChange}
                      className="block w-full text-left px-4 py-2 text-sm cursor-pointer text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-200 dark:hover:text-white"
                    >
                      Change Password
                    </a>
                  </li>
                </ul>
                <ul className="pb-2">
                  <li>
                    <a
                      onClick={handleLogout}
                      className="block w-full text-left px-4 py-2 text-sm cursor-pointer text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-200 dark:hover:text-white"
                    >
                      Sign out
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          {isPasswordChange &&
            <ChangePassword
              isOpen={isPasswordChange}
              onClose={() => setIsPasswordChange(false)}
            />}

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
