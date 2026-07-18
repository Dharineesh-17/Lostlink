import { useState, useEffect } from 'react';
import api from '../api/axios';
import { FileText, Clock, Package, AlertCircle } from 'lucide-react';

export default function MyClaims() {
  const [claims, setClaims] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchClaims(); }, []);

  const fetchClaims = async () => {
    try {
      const res = await api.get('/api/claims/my-claims');
      setClaims(res.data.data || []);
    } catch { setClaims([]); }
    finally { setLoading(false); }
  };

  const statusColor = (s) => {
    const m = { PENDING: 'badge-pending', APPROVED: 'badge-approved', REJECTED: 'badge-rejected' };
    return m[s] || 'badge';
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">My Claims</h1>
        <p className="text-gray-500">Track the status of your item claims.</p>
      </div>

      {loading ? (
        <div className="space-y-4">{[...Array(3)].map((_, i) => <div key={i} className="card animate-pulse h-28"></div>)}</div>
      ) : claims.length === 0 ? (
        <div className="text-center py-20">
          <FileText size={48} className="mx-auto text-gray-300 mb-4" />
          <h3 className="text-lg font-semibold text-gray-700 mb-1">No claims submitted</h3>
          <p className="text-gray-400">When you claim an item, it will appear here.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {claims.map((claim) => (
            <div key={claim.id} className="card-hover">
              <div className="flex items-start justify-between mb-3">
                <div>
                  <h3 className="font-bold text-gray-900">{claim.itemTitle}</h3>
                  <p className="text-sm text-gray-400">Claimed on {new Date(claim.claimDate).toLocaleDateString()}</p>
                </div>
                <span className={statusColor(claim.status)}>{claim.status}</span>
              </div>
              <p className="text-sm text-gray-600 bg-gray-50 rounded-xl p-3">{claim.reason}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}