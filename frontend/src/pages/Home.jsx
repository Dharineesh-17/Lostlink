import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Search, PlusCircle, Shield, Users, ArrowRight, MapPin, Clock, TrendingUp } from 'lucide-react';

export default function Home() {
  const { user } = useAuth();

  const features = [
    { icon: Search, title: 'Smart Search', desc: 'Search by keyword, category, or location. Find your lost item in seconds.' },
    { icon: PlusCircle, title: 'Easy Reporting', desc: 'Report lost or found items with detailed descriptions and images.' },
    { icon: Shield, title: 'Secure Claims', desc: 'Security-verified claim process ensures items reach their rightful owners.' },
    { icon: Users, title: 'Role-Based Access', desc: 'Students, security staff, and admins each get tailored dashboards.' },
  ];

  const stats = [
    { value: '500+', label: 'Items Reunited' },
    { value: '2,000+', label: 'Active Users' },
    { value: '50+', label: 'Daily Reports' },
    { value: '98%', label: 'Recovery Rate' },
  ];

  return (
    <div>
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-brand-950 via-brand-900 to-brand-800 text-white">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 left-10 w-72 h-72 bg-accent-500 rounded-full blur-3xl"></div>
          <div className="absolute bottom-20 right-10 w-96 h-96 bg-brand-400 rounded-full blur-3xl"></div>
        </div>
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 lg:py-32">
          <div className="max-w-3xl">
            <div className="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm rounded-full px-4 py-2 text-sm font-medium mb-6">
              <span className="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
              VSB Engineering College
            </div>
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold leading-tight mb-6">
              Lost something? <br />
              <span className="text-accent-400">We'll help you find it.</span>
            </h1>
            <p className="text-lg text-brand-200 mb-8 max-w-xl leading-relaxed">
              LostLink replaces the traditional notice board with a modern web platform. Report lost items, search for found ones, and claim what's yours — all in one place.
            </p>
            <div className="flex flex-wrap gap-4">
              {!user && (
                <Link to="/register" className="bg-accent-500 hover:bg-accent-600 text-brand-950 font-bold py-3.5 px-8 rounded-xl transition-all shadow-lg shadow-accent-500/25 hover:shadow-accent-500/40 flex items-center gap-2">
                  Get Started <ArrowRight size={20} />
                </Link>
              )}
              <Link to="/search" className="bg-white/10 hover:bg-white/20 backdrop-blur-sm font-semibold py-3.5 px-8 rounded-xl transition-all flex items-center gap-2">
                <Search size={20} /> Browse Items
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="bg-white border-b border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {stats.map((s, i) => (
              <div key={i} className="text-center">
                <p className="text-3xl font-extrabold gradient-text">{s.value}</p>
                <p className="text-sm text-gray-500 mt-1">{s.label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl font-bold text-gray-900 mb-3">How LostLink Works</h2>
            <p className="text-gray-500 max-w-2xl mx-auto">A streamlined process to report, search, and recover lost items on campus.</p>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {features.map((f, i) => (
              <div key={i} className="card-hover text-center">
                <div className="w-14 h-14 bg-brand-50 rounded-2xl flex items-center justify-center mx-auto mb-4">
                  <f.icon size={26} className="text-brand-600" />
                </div>
                <h3 className="font-bold text-gray-900 mb-2">{f.title}</h3>
                <p className="text-sm text-gray-500 leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-20 bg-white">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Ready to find what you lost?</h2>
          <p className="text-gray-500 mb-8">Join hundreds of students who have recovered their belongings through LostLink.</p>
          <Link to="/report" className="btn-primary inline-flex items-center gap-2 text-base">
            <PlusCircle size={20} /> Report an Item Now
          </Link>
        </div>
      </section>
    </div>
  );
}