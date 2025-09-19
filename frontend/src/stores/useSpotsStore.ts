import { create } from 'zustand'
import { api } from '@/lib/api'

export type Spot = {
  id: number
  name: string
  latitude: number
  longitude: number
  type: 'CAFE' | 'COWORKING' | 'LIBRARY'
  wifiMbps?: number
  noiseLevel?: number
  outletAvailability?: number
}

type Filters = {
  minWifiSpeed: number
  noiseLevel: null | number
  minOutlets: number
}

function convertFiltersToParams(f: Filters) {
  const params: Record<string, any> = { minWifi: f.minWifiSpeed, minOutlets: f.minOutlets }
  if (typeof f.noiseLevel === 'number') {
    params.maxNoise = f.noiseLevel
  }
  return params
}

type Store = {
  spots: Spot[]
  filters: Filters
  loading: boolean
  userLocation: [number, number]
  fetchSpots: () => Promise<void>
  updateFilters: (nf: Partial<Filters>) => void
}

export const useSpotsStore = create<Store>((set, get) => ({
  spots: [],
  filters: { minWifiSpeed: 10, noiseLevel: null, minOutlets: 1 },
  loading: false,
  userLocation: [55.751244, 37.618423],
  async fetchSpots() {
    set({ loading: true })
    const params = convertFiltersToParams(get().filters)
    const res = await api.get('/spots', { params })
    // backend returns Spring Page<Place>
    const content = Array.isArray(res.data) ? res.data : res.data.content
    set({ spots: content ?? [], loading: false })
  },
  updateFilters(nf) {
    set((s) => ({ filters: { ...s.filters, ...nf } }))
    get().fetchSpots()
  },
}))


