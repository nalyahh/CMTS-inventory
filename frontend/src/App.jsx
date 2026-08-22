import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ItemsPage from './pages/ItemsPage'
import LoginPage from './pages/LoginPage'

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ItemsPage />} />
            <Route path="/login" element={<LoginPage />} />
        </Routes>
      </BrowserRouter>
  )
}

export default App