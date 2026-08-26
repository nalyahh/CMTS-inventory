import {useEffect, useState} from "react";
import api from '../api'
import Navbar from '../components/Navbar'
import useCurrentUser from '../hooks/useCurrentUser'
import './MyCheckoutsPage.css'
import { formatDate } from '../dates.js'

function isOverdue(dateString) {
    const [year, month, day] = dateString.split('-')
    const due = new Date(year, month - 1, day)
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    return due < today
}

function MyCheckoutsPage() {
    const [checkouts, setCheckouts] = useState([])
    const { user, logout } = useCurrentUser()
    const [error, setError] = useState('')

    function loadCheckouts() {
        if (!localStorage.getItem('token')) return

        api.get('/checkouts/me')
            .then(response => {
                setCheckouts(response.data)
                setError('')
            })
            .catch(() => setError("Couldn't load your checkouts. Is the server running?"))
    }

    useEffect(() => {
        loadCheckouts()
    }, [])

    function handleCheckIn(checkoutId) {
        api.put(`/checkouts/${checkoutId}/checkin`)
            .then(() => loadCheckouts())
            .catch(error => {
                const message = error.response?.data?.message || 'Could not check that item in. Please try again.'
                setError(message)
            })
    }


    return (
        <div className="checkouts-page">
            <Navbar user={user} onLogout={logout} />
            <main className="checkouts">
                <h1>My Checkouts</h1>
                {error && <div className="error-banner">{error}</div>}
                {checkouts.length === 0 ? (
                    <p className="checkouts-empty">You don't have anything checked out right now.</p>
                ) : (
                    <div className="checkout-list">
                        {checkouts.map(checkout => (
                            <div className="checkout-card" key={checkout.id}>
                                <div className="checkout-info">
                                    <h3 className="checkout-item">{checkout.itemName}</h3>
                                    <div className="checkout-production">{checkout.productionName}</div>
                                    <div className={isOverdue(checkout.dueDate) ? 'checkout-due overdue' : 'checkout-due'}>
                                        Due: {formatDate(checkout.dueDate)}
                                    </div>                                </div>
                                <button className="checkin-btn" onClick={() => handleCheckIn(checkout.id)}>
                                    Check In
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    )
}

export default MyCheckoutsPage
