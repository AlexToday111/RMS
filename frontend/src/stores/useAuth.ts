import { api } from '@/lib/api'

export type LoginData = { email: string; password: string }
export type RegisterData = { email: string; password: string }

export const auth = {
  async login(data: LoginData) {
    const res = await api.post('/auth/signin', data)
    localStorage.setItem('token', res.data.token)
  },
  async register(data: RegisterData) {
    const res = await api.post('/auth/signup', data)
    localStorage.setItem('token', res.data.token)
  },
  logout() {
    localStorage.removeItem('token')
  },
}


