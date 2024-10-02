import React, { useState, useMemo } from "react";
import Select from "react-select";
import countryList from "react-select-country-list";

export default function CountrySelector({ value, onChange, error}) {
  const options = useMemo(() => countryList().getData(), []);

  let customStyles = {
    control: (provided) => ({
      ...provided,
      backgroundColor: 'white',
      borderColor: '#6b7280',
      minHeight: '50px',
      height: '50px',
      boxShadow: '0 2px 1px rgba(0, 0, 0, 0.05)',
      '&:hover': {
        borderColor: '#6b7280'
      }
    }),
    valueContainer: (provided) => ({
      ...provided,
      height: '40px',
      padding: '0 6px'
    }),
    input: (provided) => ({
      ...provided,
      margin: '0px'
    }),
    indicatorSeparator: () => ({
      display: 'none'
    }),
    indicatorsContainer: (provided) => ({
      ...provided,
      height: '40px'
    }),
    placeholder: (provided) => ({
      ...provided,
      color: '#6b7280', // Custom placeholder color
      fontSize: '16px',  // Custom placeholder font size
      paddingLeft: '5px' // Move placeholder text to the right
    }),
    dropdownIndicator: (provided) => ({
      ...provided,
      paddingTop: '8px', // Adjust this value to move the arrow downwards
      paddingBottom: '0.5px' // Adjust this value to move the arrow downwards
    })
  };

  // Error Styles --> please change to be more efficient --> just change the borderColor
  if (error) {
    customStyles = {
      control: (provided) => ({
        ...provided,
        backgroundColor: 'white',
        borderColor: '#e53e3e',
        minHeight: '50px',
        height: '50px',
        boxShadow: '0 2px 1px rgba(0, 0, 0, 0.05)',
        '&:hover': {
          borderColor: '#e53e3e'
        }
      }),
      valueContainer: (provided) => ({
        ...provided,
        height: '40px',
        padding: '0 6px'
      }),
      input: (provided) => ({
        ...provided,
        margin: '0px'
      }),
      indicatorSeparator: () => ({
        display: 'none'
      }),
      indicatorsContainer: (provided) => ({
        ...provided,
        height: '40px'
      }),
      placeholder: (provided) => ({
        ...provided,
        color: '#6b7280', // Custom placeholder color
        fontSize: '16px',  // Custom placeholder font size
        paddingLeft: '5px' // Move placeholder text to the right
      }),
      dropdownIndicator: (provided) => ({
        ...provided,
        paddingTop: '8px', // Adjust this value to move the arrow downwards
        paddingBottom: '0.5px' // Adjust this value to move the arrow downwards
      })
    };
  }

  return (
    <div>
      <Select 
        options={options} 
        value={value} 
        onChange={onChange} 
        styles={customStyles} 
        placeholder="Country" 
      />
      {error && <p className="text-red-500 text-s italic">{error.message}</p>}
    </div>
    
  );
  
}

