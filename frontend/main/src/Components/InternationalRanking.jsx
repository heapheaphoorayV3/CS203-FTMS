import { useState, useEffect } from "react";
import Pagination from "./Others/Pagination";

export default function InternationalRanking() {
  const [rankingData, setRankingData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

//   if (loading) {
//     return <div className="mt-10">Loading...</div>; // Show loading state
//   }

//   if (error) {
//     return <div className="mt-10">{error}</div>; // Show error message if any
//   }

  return (
    <div className="row-span-2 col-start-2 bg-gray-300 h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        International Ranking
      </h1>
      <h1 className="my-10 ml-12 text-left text-2xl font-semibold">
        Search bar/filter etc
      </h1>

      <div className="ml-12 mr-8 overflow-x-auto">
        <table className="table text-lg">
          {/* head */}
          <thead className="text-lg">
            <tr>
              <th>Rank</th>
              <th className="w-1/2">Name</th>
              <th>Country</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>Name</td>
              <td>SG</td>
            </tr>
            <tr>
              <td>2</td>
              <td>Name</td>
              <td>SG</td>
            </tr>
            <tr>
              <td>3</td>
              <td>Name</td>
              <td>SG</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
