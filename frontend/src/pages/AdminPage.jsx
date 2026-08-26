import { useState } from 'react'
import Navbar from '../components/Navbar'
import useCurrentUser from '../hooks/useCurrentUser'
import ItemsAdmin from '../components/admin/ItemsAdmin'
import './AdminPage.css'
import ProductionsAdmin from "../components/admin/ProductionsAdmin.jsx";

const TABS = [
    { value: 'items', label: 'Items' },
    { value: 'productions', label: 'Productions' },
    { value: 'users', label: 'Users' },
]

function AdminPage() {
    const { user, loading, logout } = useCurrentUser()
    const isAdmin = user?.role === 'ADMIN'
    const [activeTab, setActiveTab] = useState('items')

    return (
        <div className="admin-page">
            <Navbar user={user} onLogout={logout} />
            <main className="admin">
                <h1>Admin</h1>

                {loading && <p>Loading...</p>}

                {!loading && !isAdmin && (
                    <div className="error-banner">You need an admin account to view this page.</div>
                )}

                {!loading && isAdmin && (
                    <>
                        <div className="admin-tabs">
                            {TABS.map(tab => (
                                <button
                                    key={tab.value}
                                    className={activeTab === tab.value ? 'tab active' : 'tab'}
                                    onClick={() => setActiveTab(tab.value)}
                                >
                                    {tab.label}
                                </button>
                            ))}
                        </div>

                        {activeTab === 'items' && <ItemsAdmin />}
                        {activeTab === 'productions' && <ProductionsAdmin />}
                        {activeTab === 'users' && <p>Users coming later.</p>}
                    </>
                )}
            </main>
        </div>
    )
}

export default AdminPage
