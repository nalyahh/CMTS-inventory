import { Link } from 'react-router-dom'
import cmtsLogo from '../assets/cmts-logo.png'
import './Navbar.css'

function Navbar({ user, onLogout }) {
    return (
        <nav className="navbar">
            <div className="navbar-lights">
                {Array.from({ length: 40 }).map((_, i) => (
                    <span key={i} className="light" />
                ))}
            </div>
            <img src={cmtsLogo} alt="CMTS logo" className="navbar-logo" />
            <div className="navbar-title">
                Columbia Musical Theatre Society
                <br />
                Inventory and Checkout
            </div>
            <div className="navbar-links">
                <Link to="/">Item Catalog</Link>
                <Link to="/checkouts">My Checkouts</Link>
                {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
            </div>
            <div className="navbar-user">
                {user ? (
                    <>
                        Hi, {user.name.split(' ')[0]}
                        <span className="avatar">{user.name.charAt(0)}</span>
                        <button className="logout-btn" onClick={onLogout}>Log out</button>
                    </>
                ) : (
                    <Link className="navbar-signin" to="/login">Sign In</Link>
                )}
            </div>
        </nav>
    )
}

export default Navbar