import { useEffect } from 'react'
import MapView from '@/components/MapView'
import SpotFilters from '@/components/SpotFilters'
import { useSpotsStore } from '@/stores/useSpotsStore'

const HomePage = () => {
  const fetchSpots = useSpotsStore((s) => s.fetchSpots)
  useEffect(() => { fetchSpots() }, [fetchSpots])
  return (
    <div className="h-screen grid grid-cols-1 md:grid-cols-4">
      <div className="p-4 border-r md:col-span-1">
        <SpotFilters />
      </div>
      <div className="md:col-span-3">
        <MapView />
      </div>
    </div>
  )
}

export default HomePage


