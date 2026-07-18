import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { PlusCircle, X } from 'lucide-react';
import toast from 'react-hot-toast';

export default function ReportItem() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ title: '', description: '', category: '', location: '', dateLost: '', imageUrl: '' });
  const [loading, setLoading] = useState(false);

  const categories = ['Electronics', 'Documents', 'Keys', 'Wallet/Purse', 'Clothing', 'Books', 'Bags', 'Accessories', 'Other'];

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.title || !form.category || !form.location) {
      return toast.error('Title, category, and location are required');
    }
    setLoading(true);
    try {
      await api.post('/api/items', {
        ...form,
        dateLost: form.dateLost ? new Date(form.dateLost).toISOString() : null,
      });
      toast.success('Item reported successfully!');
      navigate('/my-items');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to report item');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Report an Item</h1>
        <p className="text-gray-500">Fill in the details of the lost or found item.</p>
      </div>

      <form onSubmit={handleSubmit} className="card space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Title *</label>
          <input type="text" className="input-field" placeholder="e.g., Blue Backpack with Laptop"
            value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Description</label>
          <textarea className="input-field !h-28 resize-none" placeholder="Describe the item in detail — brand, color, distinguishing features..."
            value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} />
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Category *</label>
            <select className="input-field" value={form.category} onChange={e => setForm({ ...form, category: e.target.value })}>
              <option value="">Select a category</option>
              {categories.map(c => <option key={c} value={c}>{c}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Location *</label>
            <input type="text" className="input-field" placeholder="e.g., Main Library 2nd Floor"
              value={form.location} onChange={e => setForm({ ...form, location: e.target.value })} />
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Date Lost/Found</label>
            <input type="datetime-local" className="input-field"
              value={form.dateLost} onChange={e => setForm({ ...form, dateLost: e.target.value })} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Image URL (optional)</label>
            <input type="url" className="input-field" placeholder="https://..."
              value={form.imageUrl} onChange={e => setForm({ ...form, imageUrl: e.target.value })} />
          </div>
        </div>

        <div className="flex gap-3 pt-2">
          <button type="submit" disabled={loading} className="btn-primary flex items-center gap-2">
            {loading ? <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent" /> : <><PlusCircle size={18} /> Submit Report</>}
          </button>
          <button type="button" onClick={() => navigate(-1)} className="btn-secondary">Cancel</button>
        </div>
      </form>
    </div>
  );
}