/** @type {import('tailwindcss').Config} */
module.exports = {
  daisyui: {
    themes: [
      {
        mytheme: {
          "primary": "#0690DB",
          "secondary": "#058ED9",
          "accent": "#DB504A",
          // "neutral": "#D3D4D9",
          "neutral": "#000000",
          "neutral-content": "#00120B",
          "base-100": "#FFF9FB",
        },
      },
      "dark",
      "cupcake",
    ],
    theme: "mytheme",
  },
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    "./node_modules/flowbite/**/*.js",
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin'),
    require('daisyui'),
  ]
}
