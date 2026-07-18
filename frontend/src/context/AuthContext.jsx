import { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/axios';
import toast from 'react-hot-toast';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = localStorage.getItem('user');
    const token = localStorage.getItem('accessToken');
    if (stored && token) {
      setUser(JSON.parse(stored));
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const res = await api.post('/api/auth/login', { email, password });
    const { accessToken, userId, email: userEmail, role } = res.data.data;
    const userData = { id: userId, email: userEmail, role };
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
    toast.success('Welcome back!');
    return userData;
  };

  const register = async (name, email, password) => {
    const res = await api.post('/api/auth/register', { name, email, password, confirmPassword: password });
    toast.success('Registration successful! Please login.');
    return res.data.data;
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    setUser(null);
    toast.success('Logged out');
  };

  const hasRole = (role) => user?.role === role;
  const hasAnyRole = (...roles) => roles.includes(user?.role);

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading, hasRole, hasAnyRole }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};