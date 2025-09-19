import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import { useSpotsStore } from '@/stores/useSpotsStore'
import { Link } from 'react-router-dom'

const MapView = () => {
  const { spots, userLocation } = useSpotsStore()
  return (
    <div className="h-full w-full">
      <MapContainer center={userLocation} zoom={13} className="h-full w-full">
        <TileLayer url={import.meta.env.VITE_MAP_TILE_URL ?? 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'} />
        {spots.map((s) => (
          <Marker key={s.id} position={[s.latitude, s.longitude]}>
            <Popup>
              <div className="space-y-1">
                <div className="font-medium">{s.name}</div>
                <div className="text-sm text-gray-600">{s.type}</div>
                <Link to={`/spots/${s.id}`} className="text-blue-600 text-sm">Подробнее</Link>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  )
}

export default MapView


