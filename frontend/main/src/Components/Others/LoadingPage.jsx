export default function LoadingPage() {
    return (
        <div className="flex flex-col justify-center items-center h-full w-full">
            <div className="animate-spin rounded-full h-32 w-32 border-t-2 border-b-2 border-blue-500"></div>
            <h1 className="text-2xl text-blue-600 mt-4">Loading<span className="dot-ellipsis"></span></h1>
            <style jsx="true">{`
                @keyframes dot-ellipsis {
                    0% { content: ''; }
                    25% { content: '.'; }
                    50% { content: '..'; }
                    75% { content: '...'; }
                    100% { content: ''; }
                }
                .dot-ellipsis::after {
                    content: '';
                    animation: dot-ellipsis 1s steps(4, end) infinite;
                }
            `}</style>
        </div>
    )
}