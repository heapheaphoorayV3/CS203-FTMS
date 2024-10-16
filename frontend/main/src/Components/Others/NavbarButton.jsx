export default function NavbarButton({children, onSubmit, ...props}) {
    return (
        <button 
            type="submit"
            onClick={onSubmit}
            className="h-8 min-w-[80px] justify-center rounded-md bg-indigo-600 my-2 text-sm font-semibold leading-6 text-white hover:bg-indigo-500"
        >
            {children}
        </button>
    );
}