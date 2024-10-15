import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import TournamentService from "../../Services/Tournament/TournamentService";

export default function Tournaments() {
  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      console.log(sessionStorage.getItem("userType"));
      try {
        const response = await TournamentService.getAllTournaments();
        setTournamentData(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
        setError("Failed to load data.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  console.log(tournamentData);

  const formatDateRange = (start, end) => {
    const startDate = new Date(start).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    const endDate = new Date(end).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return `${startDate} - ${endDate}`;
  };

  const getTournamentStatus = (start, end) => {
    const currentDate = new Date();
    const startDate = new Date(start);
    const endDate = new Date(end);

    if (currentDate < startDate) {
      return "Upcoming";
    } else if (currentDate >= startDate && currentDate <= endDate) {
      return "Ongoing";
    } else {
      return "Past";
    }
  };

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  return (
    <div className="row-span-2 col-start-2 bg-gray-300 h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        Tournaments
      </h1>
      {/* <h1 className="my-10 ml-12 text-left text-2xl font-semibold">
        Search bar/filter etc
      </h1> */}
      <div class="w-full max-w-sm min-w-[200px] ml-12 pb-8">
        <div class="relative">
          <input
            class="w-full bg-slate-100 placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md pl-3 pr-28 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
            placeholder="Tournament Name..."
          />
          <button
            class="absolute top-1 right-1 flex items-center rounded bg-slate-800 py-1 px-2.5 border border-transparent text-center text-sm text-white transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
            type="button"
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4 mr-2">
              <path fill-rule="evenodd" d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z" clip-rule="evenodd" />
            </svg>

            Search
          </button>
        </div>
      </div>
      <div className="ml-12 mr-8 overflow-x-auto">
        <table className="table text-lg">
          {/* head */}
          <thead className="text-lg">
            <tr>
              <th>Tournament Name</th>
              <th>Location</th>
              <th>Dates</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {tournamentData.length > 0 ? (
              tournamentData.map((tournament) => (
                <tr key={tournament.id}>
                  <td>
                    <a
                      href={`tournaments/${tournament.id}`}
                      className="underline hover:text-accent"
                    >
                      {tournament.name}
                    </a>
                  </td>
                  <td>{tournament.location}</td>
                  <td>
                    {formatDateRange(tournament.startDate, tournament.endDate)}
                  </td>
                  <td>
                    {getTournamentStatus(
                      tournament.startDate,
                      tournament.endDate
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="text-center">
                  No tournaments available.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
