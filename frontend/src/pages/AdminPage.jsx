import Navbar from '../components/Navbar'
import useCurrentUser from '../hooks/useCurrentUser'
import './AdminPage.css'
import { useState, useEffect } from 'react'
import api from '../api'

const emptyForm = {
    name: '',
    location: '',
    category: 'PROPS',
    quantity: 1,
    notes: '',
    photoURL: ''
}

const CATEGORY_VALUES = ['SET', 'PROPS', 'SOUND', 'LIGHTS', 'COSTUMES', 'HAIR_MAKEUP', 'INSTRUMENTS']

function AdminPage() {
    const { user, loading, logout } = useCurrentUser()
    const isAdmin = user?.role === 'ADMIN'
    const [items, setItems] = useState([])
    const [error, setError] = useState('')
    const [form, setForm] = useState(emptyForm)
    const locations = [...new Set(items.map(item => item.location))].sort()
    const [showLocations, setShowLocations] = useState(false)
    const locationMatches = locations.filter(location =>
        location.toLowerCase().includes(form.location.toLowerCase()) && location !== form.location
    )

    function updateField(field, value) {
        setForm({ ...form, [field]: value })
    }

    function handleAddItem(e) {
        e.preventDefault()
        api.post('/items', {
            ...form,
            quantity: Number(form.quantity),
            notes: form.notes || null,
            photoURL: form.photoURL || null
        })
            .then(() => {
                setForm(emptyForm)
                loadItems()
            })
            .catch(error => setError(error.response?.data?.message || 'Could not add the item.'))
    }
    function loadItems() {
        api.get('/items')
            .then(response => {
                setItems(response.data)
                setError('')
            })
            .catch(() => setError("Couldn't load items."))
    }

    useEffect(() => {
        loadItems()
    }, [])
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
                        {error && <div className="error-banner">{error}</div>}
                        <h2>Items</h2>
                        <h2>Add an item</h2>
                        <form className="admin-form" onSubmit={handleAddItem}>
                            <input
                                placeholder="Name"
                                value={form.name}
                                onChange={e => updateField('name', e.target.value)}
                                required
                            />
                            <div className="location-field">
                                <input
                                    placeholder="Location"
                                    value={form.location}
                                    onChange={e => updateField('location', e.target.value)}
                                    onFocus={() => setShowLocations(true)}
                                    onBlur={() => setTimeout(() => setShowLocations(false), 150)}
                                    required
                                />
                                {showLocations && locationMatches.length > 0 && (
                                    <ul className="location-options">
                                        {locationMatches.map(location => (
                                            <li key={location}>
                                                <button
                                                    type="button"
                                                    onClick={() => {
                                                        updateField('location', location)
                                                        setShowLocations(false)
                                                    }}
                                                >
                                                    {location}
                                                </button>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                            <select value={form.category} onChange={e => updateField('category', e.target.value)}>
                                {CATEGORY_VALUES.map(value => (
                                    <option key={value} value={value}>{value}</option>
                                ))}
                            </select>
                            <input
                                type="number"
                                min="1"
                                placeholder="Quantity"
                                value={form.quantity}
                                onChange={e => updateField('quantity', e.target.value)}
                                required
                            />
                            <input
                                placeholder="Notes (optional)"
                                value={form.notes}
                                onChange={e => updateField('notes', e.target.value)}
                            />
                            <input
                                placeholder="Photo URL (optional)"
                                value={form.photoURL}
                                onChange={e => updateField('photoURL', e.target.value)}
                            />
                            <button type="submit">Add Item</button>
                        </form>
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Location</th>
                                <th>Qty</th>
                                <th>Available</th>
                            </tr>
                            </thead>
                            <tbody>
                            {items.map(item => (
                                <tr key={item.id}>
                                    <td>{item.name}</td>
                                    <td>{item.category}</td>
                                    <td>{item.location}</td>
                                    <td>{item.quantity}</td>
                                    <td>{item.availableQuantity}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </>
                )}
            </main>
        </div>
    )
}

export default AdminPage