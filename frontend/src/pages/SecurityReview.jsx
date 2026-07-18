import { useState, useEffect } from 'react';
import api from '../api/axios';
import { Shield, CheckCircle, XCircle, FileText, User, Clock } from 'lucide-react';
import toast from 'react-hot-toast';

export default function SecurityReview() {
  const [claims, setClaims] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchPending(); }, []);

  const fetchPending = async () => {
    try {
      const res = await api.get('/api/claims/pending');
      setClaims(res.data.data || []);
    } catch { setClaims([]); }
    finally { setLoading(false); }
  };

  const handleAction = async (claimId, status) => {
    const remark = status === 'REJECTED' ? prompt('Enter reason for rejection:') : '';
    if (status === 'REJECTED' && !remark) return toast.error('Rejection reason is required');
    try {
      await api.put(`/api/claims/review/${claimId}`, { status, remark });
      toast.success(`Claim ${status.toLowerCase()}`);
      fetchPending();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Action failed');
    }
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Claim Review</h1>
        <p className="text-gray-500">Review and process pending item claims.</p>
      </div>

      {loading ? (
        <div className="space-y-4">{[...Array(3)].map((_, i) => <div key={i} className="card animate-pulse h-36"></div>)}</div>
      ) : claims.length === 0 ? (
        <div className="text-center py-20">
          <Shield size={48} className="mx-auto text-gray-300 mb-4" />
          <h3 className="text-lg font-semibold text-gray-700">No pending claims</h3>
          <p className="text-gray-400">All caught up! No claims need review.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {claims.map((claim) => (
            <div key={claim.id} className="card">
              <div className="flex flex-col lg:flex-row lg:items-start gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-3">
                    <span className="badge-pending">{claim.status}</span>
                    <span className="text-sm text-gray-400 flex items-center gap-1"><Clock size={14} />{new Date(claim.claimDate).toLocaleString()}</span>
                  </div>
                  <h3 className="font-bold text-gray-900 mb-1">Item: {claim.itemTitle}</h3>
                  <p className="text-sm text-gray-500 mb-3 flex items-center gap-1"><User size={14} /> Claimed by: <strong>{claim.studentName}</strong></p>
                  <div className="bg-gray-50 rounded-xl p-4">
                    <p className="text-xs font-semibold text-gray-400 uppercase mb-1">Claim Reason</p>
                    <p className="text-sm text-gray-700">{claim.reason}</p>
                  </div>
                </div>
                <div className="flex lg:flex-col gap-2 lg:flex-shrink-0">
                  <button onClick={() => handleAction(claim.id, 'APPROVED')}
                    className="flex-1 lg:flex-none flex items-center justify-center gap-2 bg-emerald-500 hover:bg-emerald-600 text-white font-semibold py-2.5 px-5 rounded-xl transition-all">
                    <CheckCircle size={18} /> Approve
                  </button>
                  <button onClick={() => handleAction(claim.id, 'REJECTED')}
                    className="flex-1 lg:flex-none flex items-center justify-center gap-2 bg-red-500 hover:bg-red-600 text-white font-semibold py-2.5 px-5 rounded-xl transition-all">
                    <XCircle size={18} /> Reject
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}