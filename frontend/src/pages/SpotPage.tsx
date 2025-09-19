import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api } from '@/lib/api'
import ReviewForm from '@/components/ReviewForm'

type Review = { id: number; rating: number; comment: string }
type Spot = { id: number; name: string; type: string; latitude: number; longitude: number }

const SpotPage = () => {
  const { id } = useParams()
  const [spot, setSpot] = useState<Spot | null>(null)
  const [reviews, setReviews] = useState<Review[]>([])
  useEffect(() => {
    if (!id) return
    api.get(`/spots/${id}`).then((r) => setSpot(r.data))
    api.get(`/spots/${id}/reviews`).then((r) => setReviews(r.data))
  }, [id])
  if (!spot) return <div className="p-4">Загрузка...</div>
  return (
    <div className="max-w-3xl mx-auto p-4 space-y-4">
      <div>
        <div className="text-2xl font-semibold">{spot.name}</div>
        <div className="text-gray-600">{spot.type}</div>
      </div>
      <div>
        <div className="font-semibold mb-2">Отзывы</div>
        <div className="space-y-2">
          {reviews.map((r) => (
            <div key={r.id} className="border rounded p-2">
              <div className="text-sm text-gray-600">Оценка: {r.rating}</div>
              <div>{r.comment}</div>
            </div>
          ))}
        </div>
      </div>
      <div>
        <div className="font-semibold mb-2">Оставить отзыв</div>
        <ReviewForm spotId={Number(id)} />
      </div>
    </div>
  )
}

export default SpotPage


