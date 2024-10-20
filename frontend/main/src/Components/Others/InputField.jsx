export default function InputField({placeholder, name, type, value, onChange, error, ...props}) {

    let classes = null;
    if (error) {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline border-red-500";
    } else {
        classes = "h-12 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
    }

    return (
        <div>
            <input
                name={name}
                type={type} 
                value={value}
                placeholder={placeholder}
                onChange={onChange} 
                className={classes}
            />
            {error && <p className="text-red-500 text-s italic">{error.message}</p>}
        </div>
        
        
    );
}