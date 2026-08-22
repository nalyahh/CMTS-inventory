import './ItemsPage.css'
import {useState, useEffect} from "react";
import axios from "axios";
import cmtsLogo from '../assets/cmts-logo.png'

const CATEGORY_LABELS = {
    SOUND: 'Sound',
    LIGHTS: 'Lights',
    PROPS: 'Props',
    COSTUMES: 'Costumes',
    SET: 'Set',
    HAIR_MAKEUP: 'Hair and Makeup',
    INSTRUMENTS: 'Instruments',
}

const CATEGORIES = [
    { value: 'ALL', label: 'All' },
    { value: 'SET', label: 'Set' },
    { value: 'PROPS', label: 'Props' },
    { value: 'SOUND', label: 'Sound' },
    { value: 'LIGHTS', label: 'Lights' },
    { value: 'COSTUMES', label: 'Costumes' },
    { value: 'HAIR_MAKEUP', label: 'Hair and Makeup' },
    { value: 'INSTRUMENTS', label: 'Instruments' },
]

const CATEGORY_STYLES = {
    SOUND: { background: '#E1F2FD', color: '#6DB2F3' },
    LIGHTS: { background: '#FDF7E1', color: '#F2CD56' },
    PROPS: { background: '#E8FDE1', color: '#97EA92' },
    COSTUMES: { background: '#E7E1FD', color: '#8E6DF3' },
    SET: { background: '#FDE1E1', color: '#F3786D' },
    HAIR_MAKEUP: { background: '#FDE1FF', color: '#EA98F2' },
    INSTRUMENTS: { background: '#E1FDF7', color: '#6DF3EC' },
}
function quantityClass(available, total) {
    if (available === 0) return 'quantity-badge qty-red'
    if (available < total) return 'quantity-badge qty-yellow'
    return 'quantity-badge qty-green'
}

function ItemsPage() {
    const [items, setItems] = useState([])
    const [user, setUser] = useState(null)
    const[searchQuery, setSearchQuery] = useState('')
    const[selectedCategory, setSelectedCategory] = useState('ALL')

    useEffect(() =>  {
        axios.get('http://localhost:8080/api/v1/items').then(response => setItems(response.data)).catch(error => console.error('Failed to load items:', error))}, [])

    useEffect(() => {
        const token = localStorage.getItem('token')
        if (!token) return

        axios.get('http://localhost:8080/api/v1/users/me', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setUser(response.data))
            .catch(() => localStorage.removeItem('token'))
    }, [])

    const visibleItems = items.filter(item => {
        const matchesCategory = selectedCategory === 'ALL' || item.category === selectedCategory
        const matchesSearch = item.name.toLowerCase().includes(searchQuery.toLowerCase())
        return matchesCategory && matchesSearch
    })
    return (
        <div className="items-page">
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
                    <a href="/">Item Catalog</a>
                    <a href="/checkouts">My Checkouts</a>
                    <a href="/admin">Admin</a>
                </div>
                <div className="navbar-user">
                    {user ? (
                        <div className="navbar-user">
                            Hi, {user.name.split(' ')[0]}
                            <span className="avatar">{user.name.charAt(0)}</span>
                        </div>
                    ) : (
                        <a className="navbar-signin" href="/login">Sign In</a>
                    )}
                </div>
            </nav>
            <main className="catalog">
                <div className="catalog-header">
                    <h1>Item Catalog</h1>
                    <input
                        type="text"
                        className="search-bar"
                        placeholder="Search items..."
                        value={searchQuery}
                        onChange={e => setSearchQuery(e.target.value)}
                    />
                </div>

                <div className="category-pills">
                    {CATEGORIES.map(category => <button
                        key={category.value}
                        className={selectedCategory === category.value ? 'pill active' : 'pill'}
                        onClick={() => setSelectedCategory(category.value)}
                        >
                            {category.label}
                        </button>
                        )}

                </div>
                <div className="item-grid">
                    {visibleItems.map(item => (
                        <div className="item-card" key={item.id}>
                            <div className="item-photo">
                                <span className="item-badge category-badge" style={CATEGORY_STYLES[item.category]}>
                                    {CATEGORY_LABELS[item.category]}
                                </span>
                                <span className={`item-badge ${quantityClass(item.availableQuantity, item.quantity)}`}>{item.availableQuantity} of {item.quantity}</span>
                            </div>
                            <div className="item-body">
                                <h3 className="item-name">{item.name}</h3>
                                <div className="item-location">
                                    <svg className="location-icon" viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
                                        <path d="M12 3l9 8h-3v9h-4v-6h-4v6H6v-9H3l9-8z" />
                                    </svg>
                                    {item.location}
                                </div>
                                {item.notes && <div className="item-notes">{item.notes}</div>}
                                <button
                                    className={item.availableQuantity === 0 ? 'checkout-btn sold-out' : 'checkout-btn'}
                                    disabled={item.availableQuantity === 0}
                                >
                                    {item.availableQuantity === 0 ? 'All Checked Out' : 'Check Out'}
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </main>
        </div>
    )
}

export default ItemsPage