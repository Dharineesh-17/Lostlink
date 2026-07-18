import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';
import { Search, Filter, MapPin, Clock, Tag, ChevronRight, Package } from 'lucide-react';

export default function SearchItems() {
  const [items, setItems] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);

  useEffect(() => {
    fetchItems();
  }, [page, status]);

  const fetchItems = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams({ page: String(page), size: '12' });
      if (keyword) params.append('keyword', keyword);
      if (status) params.append('status', status);
      const res = await api.get(`/api/items/search?${params}`);
      setItems(res.data.data?.content || []);
    } catch {
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchItems();
  };

  const statusColor = (s) => {
    const m = { LOST: 'badge-lost', FOUND: 'badge-found', CLAIMED: 'badge-claimed', RETURNED: 'badge-returned' };
    return m[s] || 'badge';
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Search Items</h1>
        <p className="text-gray-500">Browse all reported lost and found items on campus.</p>
      </div>

      {/* Search Bar */}
      <form onSubmit={handleSearch} className="flex flex-col sm:flex-row gap-3 mb-6">
        <div className="relative flex-1">
          <Search size={20} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
          <input type="text" className="input-field !pl-12" placeholder="Search by title, description, category, or location..."
            value={keyword} onChange={e => setKeyword(e.target.value)} />
        </div>
        <select className="input-field !w-auto sm:w-40" value={status} onChange={e => setStatus(e.target.value)}>
          <option value="">All Status</option>
          <option value="LOST">Lost</option>
          <option value="FOUND">Found</option>
          <option value="CLAIMED">Claimed</option>
          <option value="RETURNED">Returned</option>
        </select>
        <button type="submit" className="btn-primary">Search</button>
      </form>

      {/* Results */}
      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
            <div key={i} className="card animate-pulse">
              <div className="h-40 bg-gray-200 rounded-xl mb-4"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div className="h-3 bg-gray-100 rounded w-1/2"></div>
            </div>
          ))}
        </div>
      ) : items.length === 0 ? (
        <div className="text-center py-20">
          <Package size={48} className="mx-auto text-gray-300 mb-4" />
          <h3 className="text-lg font-semibold text-gray-700 mb-1">No items found</h3>
          <p className="text-gray-400">Try adjusting your search or filter criteria.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {items.map((item) => (
            <Link key={item.id} to={`/items/${item.id}`} className="card-hover block group">
              {item.imageUrl ? (
                <img src={item.imageUrl} alt={item.title} className="w-full h-40 object-cover rounded-xl mb-4" />
              ) : (
                <div className="w-full h-40 bg-gradient-to-br from-brand-50 to-brand-100 rounded-xl mb-4 flex items-center justify-center">
                  <Package size={40} className="text-brand-300" />
                </div>
              )}
              <div className="flex items-start justify-between mb-2">
                <h3 className="font-bold text-gray-900 group-hover:text-brand-600 transition-colors line-clamp-1">{item.title}</h3>
                <span className={statusColor(item.status)}>{item.status}</span>
              </div>
              <p className="text-sm text-gray-500 line-clamp-2 mb-3">{item.description || 'No description provided'}</p>
              <div className="flex items-center justify-between text-xs text-gray-400">
                <span className="flex items-center gap-1"><Tag size={14} />{item.category}</span>
                <span className="flex items-center gap-1"><MapPin size={14} />{item.location}</span>
              </div>
            </Link>
          ))}
        </div>
      )}

      {/* Pagination */}
      <div className="flex justify-center gap-3 mt-8">
        <button onClick={() => setPage(Math.max(0, page - 1))} disabled={page === 0}
          className="btn-secondary !py-2 !px-4 !text-sm disabled:opacity-40">Previous</button>
        <span className="py-2 px-4 text-sm text-gray-500">Page {page + 1}</span>
        <button onClick={() => setPage(page + 1)} disabled={items.length < 12}
          className="btn-secondary !py-2 !px-4 !text-sm disabled:opacity-40">Next</button>
      </div>
    </div>
  );
}