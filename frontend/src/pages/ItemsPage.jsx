import './ItemsPage.css'
import {useState, useEffect} from "react";
import axios from "axios";

const CATEGORY_LABELS = {
    SOUND: 'Sound',
    LIGHTS: 'Lights',
    PROPS: 'Props',
    COSTUMES: 'Costumes',
    SET: 'Set',
    HAIR_MAKEUP: 'Hair and Makeup',
    INSTRUMENTS: 'Instruments',
}

function quantityClass(available, total) {
    if (available === 0) return 'quantity-badge qty-red'
    if (available < total) return 'quantity-badge qty-yellow'
    return 'quantity-badge qty-green'
}

function ItemsPage() {
    const [items, setItems] = useState([])
    const[searchQuery, setSearchQuery] = useState('')

    useEffect(() =>  {
        axios.get('http://localhost:8080/api/v1/items').then(response => setItems(response.data)).catch(error => console.error('Failed to load items:', error))}, [])

    const visibleItems = items.filter(item => item.name.toLowerCase().includes(searchQuery.toLowerCase()))
    return (
        <div className="items-page">
            <nav className="navbar">
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
                <div className="navbar-user">Hi, Nalyah</div>
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
                <div className="item-grid">
                    {visibleItems.map(item => (
                        <div className="item-card" key={item.id}>
                            <div className="item-photo">
                                <span className="item-badge category-badge">{CATEGORY_LABELS[item.category]}</span>
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