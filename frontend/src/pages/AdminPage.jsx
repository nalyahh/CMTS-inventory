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
}

const CATEGORY_VALUES = ['SET', 'PROPS', 'SOUND', 'LIGHTS', 'COSTUMES', 'HAIR_MAKEUP', 'INSTRUMENTS']

function resizeImage(file, maxWidth) {
    return new Promise((resolve, reject) => {
        const image = new Image()
        image.onload = () => {
            const scale = Math.min(1, maxWidth / image.width)
            const canvas = document.createElement('canvas')
            canvas.width = image.width * scale
            canvas.height = image.height * scale
            canvas.getContext('2d').drawImage(image, 0, 0, canvas.width, canvas.height)
            canvas.toBlob(blob => resolve(blob), 'image/jpeg', 0.85)
        }
        image.onerror = reject
        image.src = URL.createObjectURL(file)
    })
}

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
    const [photoFile, setPhotoFile] = useState(null)
    const [fileInputKey, setFileInputKey] = useState(0)
    const [previewUrl, setPreviewUrl] = useState('')

    function updateField(field, value) {
        setForm({ ...form, [field]: value })
    }

    async function handleAddItem(e) {
        e.preventDefault()
        try {
            const response = await api.post('/items', {
                ...form,
                quantity: Number(form.quantity),
                notes: form.notes || null,
                photoURL: null
            })

            if (photoFile) {
                const resized = await resizeImage(photoFile, 800)
                const formData = new FormData()
                formData.append('file', resized, 'photo.jpg')
                await api.post(`/items/${response.data.id}/photo`, formData)
            }

            setForm(emptyForm)
            setPhotoFile(null)
            setFileInputKey(fileInputKey + 1)
            loadItems()
        } catch (error) {
            setError(error.response?.data?.message || 'Could not add the item.')
        }
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

    useEffect(() => {
        if (!photoFile) {
            setPreviewUrl('')
            return
        }

        const url = URL.createObjectURL(photoFile)
        setPreviewUrl(url)

        return () => URL.revokeObjectURL(url)
    }, [photoFile])

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
                            <div className="file-field">
                                {previewUrl && <img className="file-preview" src={previewUrl} alt="Preview" />}
                                <label htmlFor="photo-input" className="file-button">Choose Photo</label>
                                <input
                                    id="photo-input"
                                    type="file"
                                    accept="image/*"
                                    key={fileInputKey}
                                    onChange={e => setPhotoFile(e.target.files[0])}
                                />
                                <span className="file-name">{photoFile ? photoFile.name : 'No photo chosen'}</span>
                            </div>
                            <button type="submit">Add Item</button>
                        </form>
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Location</th>
                                <th>Quantity</th>
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