import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import TournamentService from "../../Services/Tournament/TournamentService";

export default function Tournaments() {
  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
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
      <h1 className="my-10 ml-12 text-left text-2xl font-semibold">
        Search bar/filter etc
      </h1>

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
            {tournamentData.map((tournament, index) => (
              <tr key={tournament.id}>
                <td>
                  <a
                    href={`view-tournament/${tournament.id}`}
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
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
