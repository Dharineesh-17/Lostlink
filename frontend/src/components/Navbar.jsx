import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Search, PlusCircle, LayoutDashboard, FileText, Shield, LogOut, Menu, X, Link2 } from 'lucide-react';
import { useState } from 'react';

export default function Navbar() {
  const { user, logout, hasAnyRole } = useAuth();
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);
  const isActive = (path) => location.pathname === path;

  const authLinks = [
    { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/my-items', label: 'My Items', icon: FileText },
    { to: '/my-claims', label: 'My Claims', icon: FileText },
  ];

  const adminLinks = [
    { to: '/admin/users', label: 'Manage Users', icon: Shield, role: 'ADMIN' },
    { to: '/security/review', label: 'Review Claims', icon: Shield, role: ['SECURITY', 'ADMIN'] },
  ].filter(l => hasAnyRole(...(Array.isArray(l.role) ? l.role : [l.role])));

  const navLink = (to, label, Icon) => (
    <Link
      key={to}
      to={to}
      className={isActive(to) ? 'nav-link-active' : 'nav-link'}
      onClick={() => setMobileOpen(false)}
    >
      <Icon size={18} className="inline mr-1.5 -mt-0.5" />
      {label}
    </Link>
  );

  return (
    <nav className="bg-white/80 backdrop-blur-lg border-b border-gray-100 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center gap-2.5">
            <div className="w-9 h-9 bg-gradient-to-br from-brand-600 to-brand-400 rounded-xl flex items-center justify-center shadow-md shadow-brand-600/20">
              <Link2 size={20} className="text-white" />
            </div>
            <span className="text-xl font-bold text-gray-900">
              Lost<span className="gradient-text">Link</span>
            </span>
          </Link>

          <div className="hidden md:flex items-center gap-1">
            {user && authLinks.map(l => navLink(l.to, l.label, l.icon))}
            {[
              { to: '/search', label: 'Search Items', icon: Search },
              { to: '/report', label: 'Report Item', icon: PlusCircle },
            ].map(l => navLink(l.to, l.label, l.icon))}
            {user && adminLinks.map(l => navLink(l.to, l.label, l.icon))}
          </div>

          <div className="hidden md:flex items-center gap-3">
            {user ? (
              <div className="flex items-center gap-3">
                <p className="text-sm font-semibold text-gray-900">{user.name || user.email}</p>
                <p className="text-xs text-gray-400">{user.role}</p>
                <button onClick={logout} className="btn-secondary !py-2 !px-3 !text-sm flex items-center gap-1.5">
                  <LogOut size={16} /> Logout
                </button>
              </div>
            ) : (
              <>
                <Link to="/login" className="btn-secondary !py-2 !text-sm">Log In</Link>
                <Link to="/register" className="btn-primary !py-2 !text-sm">Sign Up</Link>
              </>
            )}
          </div>

          <button className="md:hidden p-2" onClick={() => setMobileOpen(!mobileOpen)}>
            {mobileOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>

      {mobileOpen && (
        <div className="md:hidden bg-white border-t border-gray-100 animate-fade-in">
          <div className="px-4 py-3 space-y-1">
            {user && authLinks.map(l => navLink(l.to, l.label, l.icon))}
            {[{ to: '/search', label: 'Search Items', icon: Search }, { to: '/report', label: 'Report Item', icon: PlusCircle }].map(l => navLink(l.to, l.label, l.icon))}
            {user && adminLinks.map(l => navLink(l.to, l.label, l.icon))}
            <div className="pt-3 border-t border-gray-100 flex gap-2">
              {user ? (
                <button onClick={() => { logout(); setMobileOpen(false); }} className="btn-danger w-full !py-2 !text-sm">
                  <LogOut size={16} className="inline mr-1" /> Logout
                </button>
              ) : (
                <>
                  <Link to="/login" className="btn-secondary flex-1 !py-2 !text-sm text-center" onClick={() => setMobileOpen(false)}>Log In</Link>
                  <Link to="/register" className="btn-primary flex-1 !py-2 !text-sm text-center" onClick={() => setMobileOpen(false)}>Sign Up</Link>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </nav>
  );
}