import { useState, useEffect } from 'react';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';
import { Package, Users, FileText, AlertCircle, TrendingUp, Shield, Eye } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const endpoint = user?.role === 'ADMIN' ? '/api/dashboard/admin'
      : user?.role === 'SECURITY' ? '/api/dashboard/security'
      : '/api/dashboard/student';
    
    api.get(endpoint)
      .then(res => setStats(res.data.data))
      .catch(() => setStats(null))
      .finally(() => setLoading(false));
  }, [user]);

  const chartData = stats ? [
    { name: 'Lost', value: stats.totalLostItems || 0, color: '#ef4444' },
    { name: 'Found', value: stats.totalFoundItems || 0, color: '#10b981' },
    { name: 'Claimed', value: stats.totalClaimedItems || 0, color: '#f59e0b' },
    { name: 'Returned', value: stats.totalReturnedItems || 0, color: '#6366f1' },
  ].filter(d => d.value > 0) : [];

  const statCards = stats ? [
    ...(user?.role === 'ADMIN' ? [{ label: 'Total Users', value: stats.totalUsers, icon: Users, color: 'text-blue-600 bg-blue-50' }] : []),
    { label: 'Lost Items', value: stats.totalLostItems, icon: Package, color: 'text-red-600 bg-red-50' },
    { label: 'Found Items', value: stats.totalFoundItems, icon: Eye, color: 'text-emerald-600 bg-emerald-50' },
    { label: 'Pending Claims', value: stats.totalPendingClaims, icon: AlertCircle, color: 'text-amber-600 bg-amber-50' },
    ...(user?.role === 'ADMIN' ? [
      { label: 'Claimed', value: stats.totalClaimedItems, icon: FileText, color: 'text-brand-600 bg-brand-50' },
      { label: 'Returned', value: stats.totalReturnedItems, icon: TrendingUp, color: 'text-purple-600 bg-purple-50' },
    ] : []),
  ] : [];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">
          {user?.role === 'ADMIN' ? 'Admin' : user?.role === 'SECURITY' ? 'Security' : 'Student'} Dashboard
        </h1>
        <p className="text-gray-500">
          {user?.role === 'ADMIN' ? 'System-wide overview and management.'
           : user?.role === 'SECURITY' ? 'Review pending claims and verify items.'
           : 'Track your reported items and claims.'}
        </p>
      </div>

      {loading ? (
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">{[...Array(4)].map((_, i) => <div key={i} className="stat-card animate-pulse h-28"></div>)}</div>
      ) : (
        <>
          {/* Stat Cards */}
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
            {statCards.map((s, i) => (
              <div key={i} className="stat-card hover:shadow-md transition-shadow">
                <div className="flex items-center justify-between mb-3">
                  <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${s.color}`}>
                    <s.icon size={20} />
                  </div>
                </div>
                <p className="text-2xl font-bold text-gray-900">{s.value}</p>
                <p className="text-sm text-gray-500">{s.label}</p>
              </div>
            ))}
          </div>

          {/* Chart */}
          {chartData.length > 0 && (
            <div className="card mb-8">
              <h3 className="font-bold text-gray-900 mb-6">Items Overview</h3>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={chartData} cx="50%" cy="50%" innerRadius={60} outerRadius={100} paddingAngle={4} dataKey="value" label={({ name, value }) => `${name}: ${value}`}>
                    {chartData.map((entry, i) => <Cell key={i} fill={entry.color} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
              <div className="flex justify-center gap-6 mt-2">
                {chartData.map((d, i) => (
                  <div key={i} className="flex items-center gap-2 text-sm text-gray-600">
                    <span className="w-3 h-3 rounded-full" style={{ background: d.color }}></span>
                    {d.name}
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Quick Actions */}
          <div className="card">
            <h3 className="font-bold text-gray-900 mb-4">Quick Actions</h3>
            <div className="flex flex-wrap gap-3">
              <Link to="/report" className="btn-primary !text-sm">Report an Item</Link>
              <Link to="/search" className="btn-secondary !text-sm">Search Items</Link>
              <Link to="/my-items" className="btn-secondary !text-sm">My Items</Link>
              <Link to="/my-claims" className="btn-secondary !text-sm">My Claims</Link>
            </div>
          </div>
        </>
      )}
    </div>
  );
}