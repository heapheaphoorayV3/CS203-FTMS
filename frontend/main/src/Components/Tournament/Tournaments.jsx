import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import TournamentService from "../../Services/Tournament/TournamentService";

export default function Tournaments() {

  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  

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
            {/* row 1 */}
            <tr>
              <td><a href="view-tournament" className="underline hover:text-accent">Jackin's Arena</a></td>
              <td>Location</td>
              <td>Dates</td>
              <td>Status</td>
            </tr>
            {/* row 2 */}
            <tr>
              <td>Tournament</td>
              <td>Location</td>
              <td>Dates</td>
              <td>Status</td>
            </tr>
            {/* row 3 */}
            <tr>
              <td>Tournament</td>
              <td>Location</td>
              <td>Dates</td>
              <td>Status</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
