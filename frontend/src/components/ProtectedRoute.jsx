import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ children, requiredRole, requiredAnyRole }) {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-4 border-brand-600 border-t-transparent"></div>
      </div>
    );
  }

  if (!user) return <Navigate to="/login" replace />;
  if (requiredRole && user.role !== requiredRole) return <Navigate to="/dashboard" replace />;
  if (requiredAnyRole && !requiredAnyRole.includes(user.role)) return <Navigate to="/dashboard" replace />;

  return children;
}