const handleSubmit = async (e) => {
  e.preventDefault();
//   console.log("form data: " + formData);
  // try {
  //     const response = await fetch('http://', {
  //         method: 'POST',
  //         headers: {
  //             'Content-Type': 'application/json'
  //         },
  //         body: JSON.stringify(formData)
  //     });

  //     const data = await response.json();
  //     console.log(data);
  // } catch (error) {
  //     console.log(error);
  // }
};

export default function EventCard({
  gender,
  weapon,
  date,
  startTime,
  endTime,
}) {
  return (
    <div className="bg-slate-100 border rounded-xl shadow-md hover:shadow-xl px-5 py-3 flex flex-col items-stretch min-w-60 min-h-60">
      <h1 className="text-center w-full text-xl">
        {weapon}({gender})
      </h1>

      <div className="mt-7 text-centered flex flex-col gap-2">
        <p className="text-justify text-lg">Date: {date}</p>
        <p className="text-justify text-lg">Start Time: {startTime}</p>
        <p className="text-justify text-lg">End Time: {endTime}</p>
        <button
          type="submit"
          onClick={handleSubmit}
          className="h-10 w-full justify-center rounded-md bg-indigo-600 mt-7 mb-2 text-lg font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
         >Sign Up</button>
      </div>
    </div>
  );
}
