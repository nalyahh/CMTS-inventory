import './ItemsPage.css'
import {useState, useEffect} from "react";
import api, { API_BASE } from '../api'
import { useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import useCurrentUser from '../hooks/useCurrentUser'
import { CATEGORIES, CATEGORY_LABELS, CATEGORY_STYLES, quantityStyle } from '../itemStyles'

const FILTER_PILLS = [{ value: 'ALL', label: 'All' }, ...CATEGORIES]

function ItemsPage() {
    const [items, setItems] = useState([])
    const { user, logout } = useCurrentUser()
    const[searchQuery, setSearchQuery] = useState('')
    const[selectedCategory, setSelectedCategory] = useState('ALL')
    const[checkoutItem, setCheckoutItem] = useState(null)
    const[productions, setProductions] = useState([])
    const [productionSearch, setProductionSearch] = useState('')
    const navigate = useNavigate()
    const [checkoutError, setCheckoutError] = useState('')
    const [error, setError] = useState('')

    function closeModal() {
        setCheckoutItem(null)
        setProductionSearch('')
        setCheckoutError('')
    }

    function handleCheckout(productionId) {
        api.post('/checkouts', {
            itemId: checkoutItem.id,
            userId: user.id,
            productionId: productionId
        })
            .then(() => {
                closeModal()
                return api.get('/items')
            })
            .then(response => setItems(response.data))
            .catch(error => {
                const message = error.response?.data?.message || 'Something went wrong. Please try again.'
                setCheckoutError(message)
            })    }

    useEffect(() => {
        api.get('/items')
            .then(response => {
                setItems(response.data)
                setError('')
            })
            .catch(() => setError("Couldn't load items. Is the server running?"))
    }, [])

    useEffect(() => {
        if (!user || !checkoutItem) return

        api.get('/productions')
            .then(response => setProductions(response.data.filter(production => !production.archived)))
            .catch(() => setCheckoutError('Could not load the list of shows.'))
    }, [checkoutItem, user])

    const visibleItems = items
        .filter(item => {
            const matchesCategory = selectedCategory === 'ALL' || item.category === selectedCategory
            const matchesSearch = item.name.toLowerCase().includes(searchQuery.toLowerCase())
            return matchesCategory && matchesSearch
        })
        .sort((a, b) => a.name.localeCompare(b.name))
    return (
        <div className="items-page">
            <Navbar user={user} onLogout={logout} />
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
                {error && <div className="error-banner">{error}</div>}
                <div className="category-pills">
                    {FILTER_PILLS.map(category => <button
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
                                {item.photoURL && <img src={`${API_BASE}${item.photoURL}`} alt={item.name} />}
                            <span className="item-badge category-badge" style={CATEGORY_STYLES[item.category]}>
                                {CATEGORY_LABELS[item.category]}
                                </span>
                                <span className="item-badge quantity-badge" style={quantityStyle(item.availableQuantity, item.quantity)}>{item.availableQuantity} of {item.quantity}</span>
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
                                    onClick={() => user ? setCheckoutItem(item) : navigate('/login')}
                                >
                                    {item.availableQuantity === 0 ? 'All Checked Out' : 'Check Out'}
                                </button>
                            </div>
                        </div>
                    ))}
                    {checkoutItem && (
                        <div className="modal-backdrop" onClick={closeModal}>
                            <div className="modal" onClick={e => e.stopPropagation()}>
                                <h2>Check out {checkoutItem.name}</h2>
                                <p>Which production is this for?</p>
                                {checkoutError && <div className="error-banner">{checkoutError}</div>}
                                <input
                                    type="text"
                                    className="production-input"
                                    placeholder="Type a show name..."
                                    value={productionSearch}
                                    onChange={e => setProductionSearch(e.target.value)}
                                    autoFocus
                                />
                                <div className="modal-productions">
                                    {productions
                                        .filter(p => p.name.toLowerCase().includes(productionSearch.toLowerCase()))
                                        .map(production => (
                                            <button
                                                key={production.id}
                                                className="production-option"
                                                onClick={() => handleCheckout(production.id)}
                                            >
                                                {production.name}
                                            </button>
                                        ))}
                                </div>
                                {productions.length === 0 && <p>There are no active shows yet.</p>}
                                <button className="modal-cancel" onClick={closeModal}>Cancel</button>
                            </div>
                        </div>
                    )}
                </div>
            </main>
        </div>
    )
}

export default ItemsPage