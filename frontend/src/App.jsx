import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ItemsPage from './pages/ItemsPage'
import LoginPage from './pages/LoginPage'
import MyCheckoutsPage from "./pages/MyCheckoutsPage.jsx";

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ItemsPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/checkouts" element={<MyCheckoutsPage />} />
        </Routes>
      </BrowserRouter>
  )
}

export default App