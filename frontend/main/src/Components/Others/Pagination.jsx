import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/16/solid";
import { useState } from "react";

// PaginationButton component with dynamic button size
function PaginationButton({ element, onClick, isActive, buttonSize, isVisible, ...props }) {
    return (
        <li>
            <button
                onClick={onClick}
                className={`flex items-center justify-center ${buttonSize} leading-tight text-gray-500 
                ${isActive ? 'bg-gray-700 text-white' : 'bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700'} 
                ${isVisible ? '' : 'invisible'} ${props.extraStyling}`} 
            >
                {element}
            </button>

        </li>
    );
}

// Main Pagination component
export default function Pagination({ totalPages, buttonSize = 'px-3 h-8', currentPage, onPageChange }) {
    const [visibleStartPage, setVisibleStartPage] = useState(1); // Track the start of visible range

    const visiblePageButtons = 5; // Limit to showing 5 page buttons at a time

    if (totalPages < 2) return null; // Don't render if there are less than 2 pages

    // Handle clicking of page numbers
    const handlePageClick = (page) => {
        onPageChange(page);
    };

    // Handle previous button click (decrease the page, if possible)
    const handlePrevious = () => {
        if (currentPage > 1) {
            onPageChange(currentPage - 1);
            if (currentPage === visibleStartPage && visibleStartPage > 1) {
                setVisibleStartPage(visibleStartPage - 1); // Shift the range backward
            }
        }
    };

    // Handle next button click (increment the page, if possible)
    const handleNext = () => {
        if (currentPage < totalPages) {
            onPageChange(currentPage + 1);
            if (currentPage === visibleStartPage + visiblePageButtons - 1 && currentPage < totalPages) {
                setVisibleStartPage(visibleStartPage + 1); // Shift the range forward
            }
        }
    };

    // Generate range of visible page numbers based on current start page
    const getVisiblePageNumbers = () => {
        let endPage = Math.min(visibleStartPage + visiblePageButtons - 1, totalPages); // Calculate end page
        const pageNumbers = [];
        for (let i = visibleStartPage; i <= endPage; i++) {
            pageNumbers.push(i);
        }
        return pageNumbers;
    };

    return (
        <nav>
            <ul className="flex items-center -space-x-px text-sm">
                {/* Previous Button - Always present but invisible if currentPage is 1 */}
                <PaginationButton
                    element={<ChevronLeftIcon className="h-5" />}
                    onClick={handlePrevious}
                    buttonSize={buttonSize}
                    isActive={false}
                    isVisible={currentPage > 1} // Control visibility
                    extraStyling="rounded-l-md"
                />

                {/* Dynamic Page Number Buttons */}
                {getVisiblePageNumbers().map((number) => (
                    <PaginationButton
                        key={number}
                        element={number}
                        onClick={() => handlePageClick(number)}
                        buttonSize={buttonSize}
                        isActive={currentPage === number}
                        isVisible={true} // Always visible
                    />
                ))}

                {/* Next Button - Always present but invisible if currentPage is at the last page */}
                <PaginationButton
                    element={<ChevronRightIcon className="h-5" />}
                    onClick={handleNext}
                    buttonSize={buttonSize}
                    isActive={false}
                    isVisible={currentPage < totalPages} // Control visibility
                    extraStyling="rounded-r-md"
                />
            </ul>
        </nav>
    );
}
