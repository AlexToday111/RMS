import { useSpotsStore } from '@/stores/useSpotsStore'

const SpotFilters = () => {
  const { filters, updateFilters } = useSpotsStore()
  return (
    <div className="bg-white p-4 rounded-lg border space-y-3">
      <div className="font-semibold">Фильтры</div>
      <div className="space-y-1">
        <label className="block text-sm">Скорость Wi‑Fi (Мбит/с)</label>
        <input type="range" min={0} max={300} value={filters.minWifiSpeed}
               onChange={(e) => updateFilters({ minWifiSpeed: Number(e.target.value) })} />
        <div className="text-xs text-gray-600">от {filters.minWifiSpeed}</div>
      </div>
      <div className="space-y-1">
        <label className="block text-sm">Макс. уровень шума</label>
        <select value={filters.noiseLevel ?? ''}
                onChange={(e) => updateFilters({ noiseLevel: e.target.value ? Number(e.target.value) : null })}
                className="border rounded px-2 py-1">
          <option value="">Любой</option>
          <option value="30">Тихий (≤30)</option>
          <option value="50">Умеренный (≤50)</option>
          <option value="70">Шумный (≤70)</option>
        </select>
      </div>
      <div className="space-y-1">
        <label className="block text-sm">Мин. розеток</label>
        <input type="number" min={0} max={100} value={filters.minOutlets}
               onChange={(e) => updateFilters({ minOutlets: Number(e.target.value) })}
               className="border rounded px-2 py-1 w-24" />
      </div>
    </div>
  )
}

export default SpotFilters


