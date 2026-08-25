import { useState, useEffect } from 'react'
import api from '../api'

function useCurrentUser() {
    const [user, setUser] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        if (!localStorage.getItem('token')) {
            setLoading(false)
            return
        }

        api.get('/users/me')
            .then(response => setUser(response.data))
            .catch(() => localStorage.removeItem('token'))
            .finally(() => setLoading(false))
    }, [])

    function logout() {
        localStorage.removeItem('token')
        setUser(null)
    }

    return { user, loading, logout }
}

export default useCurrentUser