import { useState, useEffect } from "react";
import TournamentService from "../../Services/Tournament/TournamentService";
import SearchBar from "../Others/SearchBar";

export default function Tournaments() {
  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [InputSearch, setInputSearch] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await TournamentService.getAllTournaments();
        const sortedTournaments = response.data.sort((a, b) => {
          const dateA = new Date(a.startDate);
          const dateB = new Date(b.startDate);
          return dateA - dateB;
        });
        setTournamentData(sortedTournaments);
      } catch (error) {
        if (error.response) {
          console.log("Error response data: ", error.response.data);
          setError(error.response.data);
        } else if (error.request) {
          // The request was made but no response was received
          console.log("Error request: ", error.request);
          setError("Tournament Data has failed to load, please try again later.");
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log("Unknown Error: " + error);
          setError("Tournament Data has failed to load, please try again later.");
        }
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

  function handleSearch(e) {
    setInputSearch(e.target.value);
  }

  const filteredTournamentData = tournamentData?.filter((tournament) => {
    return tournament.name.toLowerCase().includes(InputSearch.toLowerCase());
  });

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  return (
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        Tournaments
      </h1>
      <div className="w-full max-w-sm min-w-[200px] ml-12 pb-8">
      <SearchBar
          value={InputSearch}
          onChange={handleSearch}
          placeholder="Search Tournaments by Name..."
        />
      </div>
      <div className="ml-12 mr-8 overflow-x-auto">
        <table className="table text-lg border-collapse">
          {/* head */}
          <thead className="text-lg text-primary">
            <tr className="border-b border-gray-300">
              <th>Tournament Name</th>
              <th>Location</th>
              <th className="text-center">Dates</th>
              <th className="text-center">Status</th>
            </tr>
          </thead>
          <tbody>
            {tournamentData.length > 0 ? (
              filteredTournamentData.map((tournament) => (
                <tr
                  key={tournament.id}
                  className="border-b border-gray-300 hover:bg-gray-100"
                >
                  <td>
                    <a
                      href={`tournaments/${tournament.id}`}
                      className="underline hover:text-primary"
                    >
                      {tournament.name}
                    </a>
                  </td>
                  <td>{tournament.location}</td>
                  <td className="text-center">
                    {formatDateRange(tournament.startDate, tournament.endDate)}
                  </td>
                  <td className="text-center">
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
