# MediShare Frontend

This is the React frontend for the MediShare medicine donation platform.

## Getting Started

### Prerequisites
- Node.js (version 14 or higher)
- npm or yarn

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000` and proxy API requests to `http://localhost:8081`.

### Features

- Browse available medicines
- User registration and login
- Medicine donation form
- Role-based access (donor, receiver, ngo, admin)

### Available Scripts

- `npm start` - Runs the app in development mode
- `npm run build` - Builds the app for production
- `npm test` - Runs tests
- `npm run eject` - Ejects from Create React App (irreversible)

### API Integration

The frontend communicates with the backend API running on `http://localhost:8081`. All API calls are proxied through the development server to avoid CORS issues.

### Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── App.js          # Main application component
│   ├── App.css         # Global styles
│   └── index.js        # Entry point
├── package.json        # Dependencies and scripts
└── README.md
```