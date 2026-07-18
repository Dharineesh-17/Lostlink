import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import { LogIn, Eye, EyeOff, Link2 } from 'lucide-react';
import toast from 'react-hot-toast';

export default function Login() {
  const { login } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.email || !form.password) return toast.error('Please fill in all fields');
    setLoading(true);
    try {
      await login(form.email, form.password);
      window.location.href = '/dashboard';
    } catch (err) {
      toast.error(err.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-200px)] flex items-center justify-center py-12 px-4">
      <div className="w-full max-w-md animate-slide-up">
        <div className="text-center mb-8">
          <div className="w-14 h-14 bg-gradient-to-br from-brand-600 to-brand-400 rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg shadow-brand-600/20">
            <Link2 size={28} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900">Welcome back</h1>
          <p className="text-gray-500 text-sm mt-1">Sign in to your LostLink account</p>
        </div>

        <form onSubmit={handleSubmit} className="card space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Email</label>
            <input type="email" className="input-field" placeholder="you@vsb.edu.in" value={form.email}
              onChange={e => setForm({ ...form, email: e.target.value })} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Password</label>
            <div className="relative">
              <input type={showPass ? 'text' : 'password'} className="input-field !pr-11" placeholder="Enter your password"
                value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
              <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                onClick={() => setShowPass(!showPass)}>
                {showPass ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>
          <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2">
            {loading ? <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent" /> : <><LogIn size={18} /> Sign In</>}
          </button>
          <p className="text-center text-sm text-gray-500">
            Don't have an account? <Link to="/register" className="text-brand-600 font-semibold hover:underline">Sign up</Link>
          </p>
        </form>
      </div>
    </div>
  );
}