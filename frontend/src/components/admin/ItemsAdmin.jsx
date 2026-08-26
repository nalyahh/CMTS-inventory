import { useState, useEffect } from 'react'
import api from '../../api'
import { CATEGORIES, CATEGORY_LABELS, CATEGORY_STYLES, quantityStyle } from '../../itemStyles'

const emptyForm = {
    name: '',
    location: '',
    category: 'PROPS',
    quantity: 1,
    notes: '',
}


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

function ItemsAdmin() {
    const [items, setItems] = useState([])
    const [error, setError] = useState('')
    const [form, setForm] = useState(emptyForm)
    const locations = [...new Set(items.map(item => item.location))].sort()
    const sortedItems = [...items].sort((a, b) => a.name.localeCompare(b.name))
    const [showLocations, setShowLocations] = useState(false)
    const locationMatches = locations.filter(location =>
        location.toLowerCase().includes(form.location.toLowerCase()) && location !== form.location
    )
    const [photoFile, setPhotoFile] = useState(null)
    const [fileInputKey, setFileInputKey] = useState(0)
    const [editingId, setEditingId] = useState(null)
    const [previewUrl, setPreviewUrl] = useState('')

    function updateField(field, value) {
        setForm({ ...form, [field]: value })
    }

    function startEdit(item) {
        setForm({
            name: item.name,
            location: item.location,
            category: item.category,
            quantity: item.quantity,
            notes: item.notes || ''
        })
        setEditingId(item.id)
        setPhotoFile(null)
        setFileInputKey(fileInputKey + 1)
        setError('')
        window.scrollTo({ top: 0, behavior: 'smooth' })
    }

    function cancelEdit() {
        setForm(emptyForm)
        setEditingId(null)
        setPhotoFile(null)
        setFileInputKey(fileInputKey + 1)
    }

    async function handleSubmit(e) {
        e.preventDefault()
        try {
            const body = {
                ...form,
                quantity: Number(form.quantity),
                notes: form.notes || null,
                photoURL: null
            }

            const response = editingId
                ? await api.put(`/items/${editingId}`, body)
                : await api.post('/items', body)

            if (photoFile) {
                const resized = await resizeImage(photoFile, 800)
                const formData = new FormData()
                formData.append('file', resized, 'photo.jpg')
                await api.post(`/items/${response.data.id}/photo`, formData)
            }

            cancelEdit()
            loadItems()
        } catch (error) {
            setError(error.response?.data?.message || 'Could not save the item.')
        }
    }

    function loadItems() {
        api.get('/items?includeArchived=true')
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

    function handleRetire(item) {
        const action = item.archived ? 'unarchive' : 'archive'
        api.patch(`/items/${item.id}/${action}`)
            .then(() => loadItems())
            .catch(error => setError(error.response?.data?.message || 'Could not update the item.'))
    }

    function handleDelete(item) {
        if (!window.confirm(`Delete ${item.name}? This cannot be undone.`)) return

        api.delete(`/items/${item.id}`)
            .then(() => loadItems())
            .catch(error => setError(error.response?.data?.message || 'Could not delete the item.'))
    }

    return (
        <>
                    {error && <div className="error-banner">{error}</div>}
                    <h2>{editingId ? 'Edit item' : 'Add an item'}</h2>
                    <form className="admin-form" onSubmit={handleSubmit}>
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
                            {CATEGORIES.map(category => (
                                <option key={category.value} value={category.value}>{category.label}</option>
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
                        <button type="submit">{editingId ? 'Save Changes' : 'Add Item'}</button>
                        {editingId && (
                            <button type="button" className="row-btn" onClick={cancelEdit}>Cancel</button>
                        )}
                    </form>
                    <table className="admin-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Location</th>
                            <th>Available</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {sortedItems.map(item => (
                            <tr key={item.id} className={item.archived ? 'archived-row' : ''}>
                                <td>{item.name}</td>
                                <td>
                                <span className="table-pill" style={CATEGORY_STYLES[item.category]}>
                                    {CATEGORY_LABELS[item.category]}
                                </span>
                                </td>
                                <td>{item.location}</td>
                                <td>
                                <span className="table-pill" style={quantityStyle(item.availableQuantity, item.quantity)}>
                                    {item.availableQuantity} of {item.quantity}
                                </span>
                                </td>
                                <td className="row-actions">
                                    <button className="row-btn" onClick={() => startEdit(item)}>Edit</button>
                                    <button className="row-btn" onClick={() => handleRetire(item)}>
                                        {item.archived ? 'Restore' : 'Retire'}
                                    </button>
                                    <button className="row-btn danger" onClick={() => handleDelete(item)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
        </>
    )
}

export default ItemsAdmin
