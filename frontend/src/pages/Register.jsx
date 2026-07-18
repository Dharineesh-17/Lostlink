import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import { UserPlus, Eye, EyeOff, Link2 } from 'lucide-react';
import toast from 'react-hot-toast';

export default function Register() {
  const { register } = useAuth();
  const [form, setForm] = useState({ name: '', email: '', password: '', confirmPassword: '' });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.name || !form.email || !form.password || !form.confirmPassword) return toast.error('All fields are required');
    if (form.password !== form.confirmPassword) return toast.error('Passwords do not match');
    if (form.password.length < 8) return toast.error('Password must be at least 8 characters');
    setLoading(true);
    try {
      await register(form.name, form.email, form.password);
      window.location.href = '/login';
    } catch (err) {
      toast.error(err.response?.data?.message || 'Registration failed');
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
          <h1 className="text-2xl font-bold text-gray-900">Create an account</h1>
          <p className="text-gray-500 text-sm mt-1">Join LostLink to report and find items</p>
        </div>

        <form onSubmit={handleSubmit} className="card space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Full Name</label>
            <input type="text" className="input-field" placeholder="Your full name" value={form.name}
              onChange={e => setForm({ ...form, name: e.target.value })} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Email</label>
            <input type="email" className="input-field" placeholder="you@vsb.edu.in" value={form.email}
              onChange={e => setForm({ ...form, email: e.target.value })} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Password</label>
            <div className="relative">
              <input type={showPass ? 'text' : 'password'} className="input-field !pr-11" placeholder="Min 8 chars, uppercase, lowercase, digit, special"
                value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
              <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                onClick={() => setShowPass(!showPass)}>
                {showPass ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Confirm Password</label>
            <input type="password" className="input-field" placeholder="Re-enter your password"
              value={form.confirmPassword} onChange={e => setForm({ ...form, confirmPassword: e.target.value })} />
          </div>
          <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2">
            {loading ? <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent" /> : <><UserPlus size={18} /> Create Account</>}
          </button>
          <p className="text-center text-sm text-gray-500">
            Already have an account? <Link to="/login" className="text-brand-600 font-semibold hover:underline">Sign in</Link>
          </p>
        </form>
      </div>
    </div>
  );
}