import { useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
import './LoginPage.css'

function LoginPage() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate()

    function handleSubmit(e) {
        e.preventDefault()
        axios.post('http://localhost:8080/api/v1/auth', { email: email, password: password })
            .then(response => {
                localStorage.setItem('token', response.data.token)
                navigate('/')
            })
            .catch(() => setError('Invalid email or password'))
    }

    return (
        <div className="login-page">
            <form className="login-card" onSubmit={handleSubmit}>
                <h1>Sign In</h1>
                {error && <p className="login-error">{error}</p>}
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                />
                <button type="submit">Sign In</button>
            </form>
        </div>
    )
}

export default LoginPage