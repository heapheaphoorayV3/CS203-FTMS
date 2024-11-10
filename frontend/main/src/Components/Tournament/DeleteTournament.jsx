import { XCircleIcon } from "@heroicons/react/20/solid";
import { useState } from "react";
import TournamentService from "../../Services/Tournament/TournamentService";

const DeleteTournament = ({ id, closeDeletePopUp }) => {
    const [error, setError] = useState(false);

    const handleDeleteTournament = async () => {
        try {
            console.log("ID: " + id);
            await TournamentService.deleteTournament(id);
            closeDeletePopUp();
        } catch (error) {
            console.error(error);
            setError(error);
        }
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center" style={{ zIndex: 3 }}>
            <div className="bg-white rounded-lg w-1/3 min-w-[300px] flex flex-col justify-center pt-6 py-12 lg:px-8">
                <button
                    className="ml-auto w-5 text-gray-300 hover:text-gray-800 focus:outline-none"
                    aria-label="Close"
                    onClick={() => closeDeletePopUp()}
                >
                    <XCircleIcon />
                </button>
                <div className="flex flex-col justify-center items-center gap-5">
                    <h1 className="text-xl font-semibold text-center mt-5">Are you sure you want to delete this tournament?</h1>
                    <button
                        className="w-1/5 bg-red-500 text-white font-semibold py-2 rounded-md hover:bg-red-600"
                        onClick={handleDeleteTournament}
                    >
                        Delete
                    </button>
                    {error && <p className="text-red-500 text-sm text-center">An unexpected error has occured. Please try again later!</p>}
                </div>
            </div>
        </div>
    )
}

export default DeleteTournament;