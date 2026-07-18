import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import { MapPin, Clock, Tag, User, ArrowLeft, Send, Package, Calendar } from 'lucide-react';
import toast from 'react-hot-toast';

export default function ItemDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [item, setItem] = useState(null);
  const [claims, setClaims] = useState([]);
  const [reason, setReason] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchItem();
    fetchClaims();
  }, [id]);

  const fetchItem = async () => {
    try {
      const res = await api.get(`/api/items/${id}`);
      setItem(res.data.data);
    } catch { toast.error('Item not found'); navigate('/search'); }
    finally { setLoading(false); }
  };

  const fetchClaims = async () => {
    try {
      const res = await api.get(`/api/claims/item/${id}`);
      setClaims(res.data.data || []);
    } catch { setClaims([]); }
  };

  const submitClaim = async (e) => {
    e.preventDefault();
    if (reason.length < 10) return toast.error('Please provide at least 10 characters explaining why this is your item');
    setSubmitting(true);
    try {
      await api.post('/api/claims', { itemId: Number(id), reason });
      toast.success('Claim submitted! Security will review it.');
      setReason('');
      fetchClaims();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Claim failed');
    } finally { setSubmitting(false); }
  };

  const statusColor = (s) => {
    const m = { LOST: 'badge-lost', FOUND: 'badge-found', CLAIMED: 'badge-claimed', RETURNED: 'badge-returned' };
    return m[s] || 'badge';
  };

  if (loading) return <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-4 border-brand-600 border-t-transparent"></div></div>;
  if (!item) return null;

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-gray-500 hover:text-brand-600 mb-6 text-sm font-medium transition-colors">
        <ArrowLeft size={18} /> Back
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-6">
          <div className="card">
            {item.imageUrl ? (
              <img src={item.imageUrl} alt={item.title} className="w-full h-64 object-cover rounded-xl mb-6" />
            ) : (
              <div className="w-full h-64 bg-gradient-to-br from-brand-50 to-brand-100 rounded-xl mb-6 flex items-center justify-center">
                <Package size={64} className="text-brand-200" />
              </div>
            )}
            <div className="flex items-start justify-between mb-3">
              <h1 className="text-2xl font-bold text-gray-900">{item.title}</h1>
              <span className={statusColor(item.status)}>{item.status}</span>
            </div>
            <p className="text-gray-600 leading-relaxed mb-6">{item.description || 'No description provided.'}</p>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <Tag size={16} className="text-brand-400" /> {item.category}
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <MapPin size={16} className="text-brand-400" /> {item.location}
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <Calendar size={16} className="text-brand-400" /> {item.dateLost ? new Date(item.dateLost).toLocaleDateString() : 'N/A'}
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-500">
                <User size={16} className="text-brand-400" /> {item.reportedByName}
              </div>
            </div>
          </div>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Claim Form */}
          {user && item.status !== 'RETURNED' && (
            <div className="card">
              <h3 className="font-bold text-gray-900 mb-4 flex items-center gap-2"><Send size={18} className="text-brand-600" /> Claim This Item</h3>
              <form onSubmit={submitClaim} className="space-y-3">
                <textarea className="input-field !h-24 resize-none text-sm" placeholder="Explain why this item belongs to you (e.g., 'I lost a blue backpack with my laptop on Jan 15 near the library...')"
                  value={reason} onChange={e => setReason(e.target.value)} />
                <button type="submit" disabled={submitting} className="btn-primary w-full !text-sm flex items-center justify-center gap-2">
                  {submitting ? <div className="animate-spin rounded-full h-4 w-4 border-2 border-white border-t-transparent" /> : <><Send size={16} /> Submit Claim</>}
                </button>
              </form>
            </div>
          )}

          {/* Claims on this item */}
          <div className="card">
            <h3 className="font-bold text-gray-900 mb-4">Claims ({claims.length})</h3>
            {claims.length === 0 ? (
              <p className="text-sm text-gray-400">No claims submitted yet.</p>
            ) : (
              <div className="space-y-3">
                {claims.map((c) => (
                  <div key={c.id} className="bg-gray-50 rounded-xl p-3">
                    <div className="flex items-center justify-between mb-1">
                      <span className="text-sm font-medium text-gray-700">{c.studentName}</span>
                      <span className={c.status === 'PENDING' ? 'badge-pending' : c.status === 'APPROVED' ? 'badge-approved' : 'badge-rejected'}>{c.status}</span>
                    </div>
                    <p className="text-xs text-gray-500 line-clamp-2">{c.reason}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}