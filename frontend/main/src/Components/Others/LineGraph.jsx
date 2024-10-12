import React from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const LineGraph = ({ data, options, height }) => {

  // Ensure options are set for responsiveness
  const responsiveOptions = {
    ...options,
    responsive: true,
    maintainAspectRatio: true,
  };
  
  return (
    <div>
      <Line data={data} options={responsiveOptions} height={height} />
    </div>
  );
};

export default LineGraph;