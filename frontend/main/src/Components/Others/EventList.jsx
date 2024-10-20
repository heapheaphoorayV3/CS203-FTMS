import EventCard from './EventCard';

export default function EventList({ className, events }) {

    // Dynamic Output for EventList
    let List = null;
    let ListOfEvents = null;

    if (events.length === 0) {  
        List =  <h1 className="text-center w-full text-2xl mt-10"> No Events Available Yet</h1>;

    } else {
        ListOfEvents = events.map((eventObject, index) => (
            <EventCard
                key={`${eventObject.weapon}-${eventObject.gender}`}
                gender={eventObject.gender}
                weapon={eventObject.weapon}
                date={eventObject.date}
                startTime={eventObject.startTime}
                endTime={eventObject.endTime}
            />
        ));

        List = (
            <div className="self-center my-2 pb-5 max-w-full flex content-center gap-10 overflow-x-scroll">
                {ListOfEvents}
            </div> 
        );
    }

    let classes = "bg-white border border-white rounded-md px-5 py-3";

    if(className){
        classes += " " + className;
    }

	return (
        <div className={classes}>
            <div className="flex flex-col items-stretch">
                <h2 className="text-center w-full text-2xl">Event List</h2>
                <hr className="self-center mt-2 mb-4 h-[2px] w-[90%] bg-black" />
                {List}
            </div>
        </div>
		
	);
}