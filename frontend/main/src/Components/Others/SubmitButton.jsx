export default function SubmitButton({children, onSubmit, ...props}) {
    return (
        <button 
            type="submit"
            onClick={onSubmit}
            className="h-12 w-full justify-center rounded-md bg-indigo-600 my-5 text-lg font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
            {children}
        </button>
    );
}