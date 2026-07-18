import { useState, useEffect } from 'react';
import api from '../api/axios';
import { Users, Shield, Trash2, Search } from 'lucide-react';
import toast from 'react-hot-toast';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchUsers(); }, []);

  const fetchUsers = async () => {
    try {
      const res = await api.get('/api/admin/users');
      setUsers(res.data.data || []);
    } catch { setUsers([]); }
    finally { setLoading(false); }
  };

  const updateRole = async (userId, newRole) => {
    try {
      await api.put(`/api/admin/users/${userId}/role?role=${newRole}`);
      toast.success('Role updated');
      fetchUsers();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to update role');
    }
  };

  const deleteUser = async (userId) => {
    if (!confirm('Are you sure you want to delete this user?')) return;
    try {
      await api.delete(`/api/admin/users/${userId}`);
      toast.success('User deleted');
      fetchUsers();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Delete failed');
    }
  };

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">User Management</h1>
        <p className="text-gray-500">Manage users and their roles.</p>
      </div>

      {loading ? (
        <div className="space-y-3">{[...Array(5)].map((_, i) => <div key={i} className="card animate-pulse h-16"></div>)}</div>
      ) : (
        <div className="card !p-0 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr>
                  <th className="text-left px-6 py-3.5 text-xs font-semibold text-gray-500 uppercase">Name</th>
                  <th className="text-left px-6 py-3.5 text-xs font-semibold text-gray-500 uppercase">Email</th>
                  <th className="text-left px-6 py-3.5 text-xs font-semibold text-gray-500 uppercase">Role</th>
                  <th className="text-left px-6 py-3.5 text-xs font-semibold text-gray-500 uppercase">Joined</th>
                  <th className="text-right px-6 py-3.5 text-xs font-semibold text-gray-500 uppercase">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {users.map((u) => (
                  <tr key={u.id} className="hover:bg-gray-50/50 transition-colors">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{u.name}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">{u.email}</td>
                    <td className="px-6 py-4">
                      <select value={u.role} onChange={e => updateRole(u.id, e.target.value)}
                        className="text-xs font-semibold border border-gray-200 rounded-lg px-2.5 py-1.5 focus:outline-none focus:ring-2 focus:ring-brand-500">
                        <option value="STUDENT">STUDENT</option>
                        <option value="SECURITY">SECURITY</option>
                        <option value="ADMIN">ADMIN</option>
                      </select>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-400">{new Date(u.createdAt).toLocaleDateString()}</td>
                    <td className="px-6 py-4 text-right">
                      <button onClick={() => deleteUser(u.id)} className="text-red-400 hover:text-red-600 transition-colors p-1.5 rounded-lg hover:bg-red-50">
                        <Trash2 size={16} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {users.length === 0 && (
            <div className="text-center py-12 text-gray-400 text-sm">No users found.</div>
          )}
        </div>
      )}
    </div>
  );
}