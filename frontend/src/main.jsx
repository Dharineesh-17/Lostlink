import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { Toaster } from 'react-hot-toast'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <App />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: { borderRadius: '12px', background: '#1e1b4b', color: '#fff', fontSize: '14px' },
        }}
      />
    </BrowserRouter>
  </React.StrictMode>
)