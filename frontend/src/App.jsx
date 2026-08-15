import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ItemsPage from './pages/ItemsPage'

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ItemsPage />} />
        </Routes>
      </BrowserRouter>
  )
}

export default App