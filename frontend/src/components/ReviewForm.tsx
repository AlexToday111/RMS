import { useForm } from 'react-hook-form'
import { api } from '@/lib/api'

type ReviewFormData = {
  rating: number
  comment: string
}

const ReviewForm = ({ spotId }: { spotId: number }) => {
  const { register, handleSubmit, reset } = useForm<ReviewFormData>()
  const onSubmit = async (data: ReviewFormData) => {
    await api.post(`/spots/${spotId}/reviews`, data)
    reset()
  }
  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-3">
      <div>
        <label className="block text-sm">Оценка (1-5)</label>
        <input type="number" min={1} max={5} {...register('rating', { valueAsNumber: true })}
               className="border rounded px-2 py-1 w-24" />
      </div>
      <div>
        <label className="block text-sm">Комментарий</label>
        <textarea {...register('comment')} className="border rounded w-full p-2" rows={3} />
      </div>
      <button type="submit" className="bg-blue-600 text-white px-3 py-1 rounded">Отправить</button>
    </form>
  )
}

export default ReviewForm


