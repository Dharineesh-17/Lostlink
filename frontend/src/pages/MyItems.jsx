import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import { Package, MapPin, Tag, Trash2, Edit, PlusCircle, Clock } from 'lucide-react';
import toast from 'react-hot-toast';

export default function MyItems() {
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchMyItems(); }, []);

  const fetchMyItems = async () => {
    try {
      const res = await api.get('/api/items/my-items');
      setItems(res.data.data?.content || res.data.data || []);
    } catch { setItems([]); }
    finally { setLoading(false); }
  };

  const deleteItem = async (id) => {
    if (!confirm('Are you sure you want to delete this item?')) return;
    try {
      await api.delete(`/api/items/${id}`);
      toast.success('Item deleted');
      fetchMyItems();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Delete failed');
    }
  };

  const statusColor = (s) => {
    const m = { LOST: 'badge-lost', FOUND: 'badge-found', CLAIMED: 'badge-claimed', RETURNED: 'badge-returned' };
    return m[s] || 'badge';
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">My Reported Items</h1>
          <p className="text-gray-500">Items you have reported as lost or found.</p>
        </div>
        <Link to="/report" className="btn-primary flex items-center gap-2">
          <PlusCircle size={18} /> Report New
        </Link>
      </div>

      {loading ? (
        <div className="space-y-4">{[...Array(3)].map((_, i) => <div key={i} className="card animate-pulse h-24"></div>)}</div>
      ) : items.length === 0 ? (
        <div className="text-center py-20">
          <Package size={48} className="mx-auto text-gray-300 mb-4" />
          <h3 className="text-lg font-semibold text-gray-700 mb-1">No items reported yet</h3>
          <p className="text-gray-400 mb-4">Start by reporting a lost or found item.</p>
          <Link to="/report" className="btn-primary inline-flex items-center gap-2"><PlusCircle size={18} /> Report Item</Link>
        </div>
      ) : (
        <div className="space-y-4">
          {items.map((item) => (
            <div key={item.id} className="card-hover flex flex-col sm:flex-row sm:items-center gap-4">
              <div className="w-full sm:w-20 h-20 bg-gradient-to-br from-brand-50 to-brand-100 rounded-xl flex-shrink-0 flex items-center justify-center">
                {item.imageUrl ? <img src={item.imageUrl} className="w-full h-full object-cover rounded-xl" /> : <Package size={32} className="text-brand-300" />}
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-start justify-between gap-2">
                  <Link to={`/items/${item.id}`} className="font-bold text-gray-900 hover:text-brand-600 transition-colors truncate">{item.title}</Link>
                  <span className={statusColor(item.status)}>{item.status}</span>
                </div>
                <p className="text-sm text-gray-500 truncate mt-0.5">{item.description || 'No description'}</p>
                <div className="flex items-center gap-4 mt-2 text-xs text-gray-400">
                  <span className="flex items-center gap-1"><Tag size={14} />{item.category}</span>
                  <span className="flex items-center gap-1"><MapPin size={14} />{item.location}</span>
                  <span className="flex items-center gap-1"><Clock size={14} />{new Date(item.createdAt).toLocaleDateString()}</span>
                </div>
              </div>
              <div className="flex gap-2 sm:flex-shrink-0">
                <Link to={`/report?edit=${item.id}`} className="btn-secondary !py-2 !px-3 !text-sm"><Edit size={16} /></Link>
                <button onClick={() => deleteItem(item.id)} className="btn-danger !py-2 !px-3 !text-sm"><Trash2 size={16} /></button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}