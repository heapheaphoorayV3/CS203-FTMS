export default function ViewCard({className, heading, children}) {

    let classes = "bg-white border border-white rounded-xl px-5 py-3 shadow-md hover:shadow-xl"

    if(className){
        classes += " " + className;
    }

    return (
        <div className={classes}>
            <div className="flex flex-col items-stretch">
                <h1 className="text-center w-full text-2xl">{heading}</h1>
                <hr className="self-center mt-2 mb-4 h-[2px] w-[90%] bg-black"/>
                <div className="mt-2 flex flex-grow flex-col text-center text-lg"> 
                    {children}
                </div>
            </div>
        </div>
    );
}