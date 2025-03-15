/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}" // Scans all HTML and TS files in src/
  ],
  theme: {
    extend: {
      colors: {
        'deep-charcoal': '#1A1A1A',      // Background
        'dark-slate': '#2D2D2D',         // Secondary Background
        'soft-white': '#E0E0E0',         // Primary Text
        'muted-gray': '#A0A0A0',         // Secondary Text
        'vibrant-purple': '#8E44AD',     // Accent 1 (Primary)
        'electric-blue': '#3498DB',      // Accent 2 (Secondary)
        'forest-green': '#27AE60',       // Success
        'crimson-red': '#E74C3C',        // Error/Warning
      },
    },
  },
  plugins: [],
}

