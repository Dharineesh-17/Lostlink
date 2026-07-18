import { Link2, Github, Mail } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="bg-brand-950 text-white mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div>
            <div className="flex items-center gap-2.5 mb-4">
              <div className="w-9 h-9 bg-gradient-to-br from-brand-400 to-brand-300 rounded-xl flex items-center justify-center">
                <Link2 size={20} className="text-brand-950" />
              </div>
              <span className="text-xl font-bold">LostLink</span>
            </div>
            <p className="text-brand-300 text-sm leading-relaxed">
              A modern campus lost-and-found management system. Report, search, and claim lost items with ease.
            </p>
          </div>
          <div>
            <h4 className="font-semibold text-sm uppercase tracking-wider text-brand-300 mb-4">Quick Links</h4>
            <ul className="space-y-2 text-sm text-brand-200">
              <li><a href="/search" className="hover:text-white transition-colors">Search Items</a></li>
              <li><a href="/report" className="hover:text-white transition-colors">Report an Item</a></li>
              <li><a href="/dashboard" className="hover:text-white transition-colors">Dashboard</a></li>
            </ul>
          </div>
          <div>
            <h4 className="font-semibold text-sm uppercase tracking-wider text-brand-300 mb-4">Connect</h4>
            <div className="flex gap-3">
              <a href="#" className="w-10 h-10 bg-brand-900 hover:bg-brand-800 rounded-xl flex items-center justify-center transition-colors">
                <Github size={18} />
              </a>
              <a href="#" className="w-10 h-10 bg-brand-900 hover:bg-brand-800 rounded-xl flex items-center justify-center transition-colors">
                <Mail size={18} />
              </a>
            </div>
          </div>
        </div>
        <div className="border-t border-brand-900 mt-8 pt-8 text-center text-brand-400 text-sm">
          Built with Spring Boot 3 + React. LostLink &copy; {new Date().getFullYear()}
        </div>
      </div>
    </footer>
  );
}