import { useState, useEffect } from 'react'
import api from '../../api'
import { formatDate } from '../../dates'
function ProductionsAdmin() {

    const emptyForm = {
        name: '',
        startDate: '',
        endDate: ''
    }

    const [productions, setProductions] = useState([])
    const [error, setError] = useState('')
    const [form, setForm] = useState(emptyForm)
    const [editingId, setEditingId] = useState(null)
    const sortedProductions = [...productions].sort((a, b) => b.startDate.localeCompare(a.startDate))

    function updateField(field, value) {
        setForm({ ...form, [field]: value })
    }

    function startEdit(production) {
        setEditingId(production.id)
        setForm({
            name: production.name,
            startDate: production.startDate,
            endDate: production.endDate
        })
        setError('')
        window.scrollTo({ top: 0, behavior: 'smooth' })
    }

    function cancelEdit() {
        setEditingId(null)
        setForm(emptyForm)
    }

    function loadProductions() {
        api.get('/productions')
            .then(response => {
                setProductions(response.data)
                setError('')
            })
            .catch(() => setError("Couldn't load productions."))
    }

    useEffect(() => {
        loadProductions()
    }, [])

    function handleSubmit(e) {
        e.preventDefault()

        const request = editingId
            ? api.put(`/productions/${editingId}`, form)
            : api.post('/productions', form)

        request
            .then(() => {
                cancelEdit()
                loadProductions()
            })
            .catch(error => setError(error.response?.data?.message || 'Could not save the production.'))
    }

    function handleArchive(production) {
        const action = production.archived ? 'unarchive' : 'archive'
        api.patch(`/productions/${production.id}/${action}`)
            .then(() => loadProductions())
            .catch(error => setError(error.response?.data?.message || 'Could not update the production.'))
    }

    function handleDelete(production) {
        if (!window.confirm(`Delete ${production.name}? This cannot be undone.`)) return

        api.delete(`/productions/${production.id}`)
            .then(() => loadProductions())
            .catch(error => setError(error.response?.data?.message || 'Could not delete the production.'))
    }

    return (
        <>
            {error && <div className="error-banner">{error}</div>}
            <h2>{editingId ? 'Edit production' : 'Add a production'}</h2>
            <form className="admin-form" onSubmit={handleSubmit}>
                <input
                    placeholder="Name"
                    value={form.name}
                    onChange={e => updateField('name', e.target.value)}
                    required
                />
                <label className="field-label">
                    Start date:
                    <input
                        type="date"
                        value={form.startDate}
                        onChange={e => updateField('startDate', e.target.value)}
                        required
                    />
                </label>
                <label className="field-label">
                    End date:
                    <input
                        type="date"
                        value={form.endDate}
                        onChange={e => updateField('endDate', e.target.value)}
                        required
                    />
                </label>
                <button type="submit">{editingId ? 'Save Changes' : 'Add Production'}</button>
                {editingId && (
                    <button type="button" className="row-btn" onClick={cancelEdit}>Cancel</button>
                )}
            </form>
            <table className="admin-table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {sortedProductions.map(production =>
                    <tr key={production.id} className={production.archived ? 'archived-row' : ''}>
                        <td>{production.name}</td>
                        <td>{formatDate(production.startDate)}</td>
                        <td>{formatDate(production.endDate)}</td>
                        <td className="row-actions">
                            <button className="row-btn" onClick={() => startEdit(production)}>Edit</button>
                            <button className="row-btn" onClick={() => handleArchive(production)}>
                                {production.archived ? 'Restore' : 'Archive'}
                            </button>
                            <button className="row-btn danger" onClick={() => handleDelete(production)}>Delete</button>
                        </td>

                    </tr>)}
                </tbody>
            </table>
        </>
    )
}

export default ProductionsAdmin