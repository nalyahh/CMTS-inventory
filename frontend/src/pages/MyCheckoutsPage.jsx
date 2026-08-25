import {useEffect, useState} from "react";
import axios from "axios";
import Navbar from '../components/Navbar'
import './MyCheckoutsPage.css'


function formatDate(dateString) {
    const [year, month, day] = dateString.split('-')
    const date = new Date(year, month - 1, day)
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

function MyCheckoutsPage() {
    const [checkouts, setCheckouts] = useState([])
    const [user, setUser] = useState(null)

    function loadCheckouts() {
        const token = localStorage.getItem('token')
        if (!token) return

        axios.get('http://localhost:8080/api/v1/checkouts/me', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setCheckouts(response.data))
            .catch(error => console.error('Failed to load checkouts:', error))
    }

    useEffect(() => {
        loadCheckouts()
    }, [])

    function handleCheckIn(itemId) {
        const token = localStorage.getItem('token')
        axios.put(`http://localhost:8080/api/v1/checkouts/items/${itemId}/checkin`, null, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(() => loadCheckouts())
            .catch(error => console.error('Check in failed:', error))
    }

    function handleLogout() {
        localStorage.removeItem('token')
        setUser(null)
    }


    useEffect(() => {
        const token = localStorage.getItem('token')
        if (!token) return

        axios.get('http://localhost:8080/api/v1/users/me', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setUser(response.data))
            .catch(() => localStorage.removeItem('token'))
    }, [])

    return (
        <div className="checkouts-page">
            <Navbar user={user} onLogout={handleLogout} />
            <main className="checkouts">
                <h1>My Checkouts</h1>

                {checkouts.length === 0 ? (
                    <p className="checkouts-empty">You don't have anything checked out right now.</p>
                ) : (
                    <div className="checkout-list">
                        {checkouts.map(checkout => (
                            <div className="checkout-card" key={checkout.id}>
                                <div className="checkout-info">
                                    <h3 className="checkout-item">{checkout.itemName}</h3>
                                    <div className="checkout-production">{checkout.productionName}</div>
                                    <div className="checkout-due">Due {formatDate(checkout.dueDate)}</div>
                                </div>
                                <button className="checkin-btn" onClick={() => handleCheckIn(checkout.itemId)}>
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
