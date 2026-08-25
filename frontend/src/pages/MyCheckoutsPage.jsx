import {useEffect, useState} from "react";
import axios from "axios";


function MyCheckoutsPage() {
    const [checkouts, setCheckouts] = useState([])

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

    return (
        <div>
            <h1>My Checkouts</h1>
            <ul>
                {checkouts.map(checkout => (
                    <li key={checkout.id}>
                        {checkout.itemName} — {checkout.productionName}
                        <button onClick={() => handleCheckIn(checkout.itemId)}>Check In</button>
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default MyCheckoutsPage
