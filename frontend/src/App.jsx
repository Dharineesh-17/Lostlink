import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import SearchItems from './pages/SearchItems';
import ReportItem from './pages/ReportItem';
import MyItems from './pages/MyItems';
import MyClaims from './pages/MyClaims';
import ItemDetail from './pages/ItemDetail';
import Dashboard from './pages/Dashboard';
import AdminUsers from './pages/AdminUsers';
import SecurityReview from './pages/SecurityReview';
import ProtectedRoute from './components/ProtectedRoute';

function AppRoutes() {
  const { user } = useAuth();
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />
      <main className="flex-1">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={user ? <Navigate to="/dashboard" /> : <Login />} />
          <Route path="/register" element={user ? <Navigate to="/dashboard" /> : <Register />} />
          <Route path="/search" element={<SearchItems />} />
          <Route path="/items/:id" element={<ItemDetail />} />
          <Route path="/report" element={<ProtectedRoute><ReportItem /></ProtectedRoute>} />
          <Route path="/my-items" element={<ProtectedRoute><MyItems /></ProtectedRoute>} />
          <Route path="/my-claims" element={<ProtectedRoute><MyClaims /></ProtectedRoute>} />
          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/admin/users" element={<ProtectedRoute requiredRole="ADMIN"><AdminUsers /></ProtectedRoute>} />
          <Route path="/security/review" element={<ProtectedRoute requiredAnyRole={["SECURITY", "ADMIN"]}><SecurityReview /></ProtectedRoute>} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}