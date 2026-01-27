import request from '../api/request';

export const getOrderListApi = () => request.get('/orders')
export const createOrderApi = data => request.post('/orders', data)
export const deleteOrderApi = id => request.delete(`/orders/${id}`)
export const updateOrderStatusApi = (id, status) =>
  request.put(`/orders/${id}/status?status=${status}`)

export const assignOrderApi = (orderId, adminL2Id) =>
  request.put(`/orders/${orderId}/assign`, { adminL2Id })

export const getAdminL2ListApi = () =>
  request.get('/orders/admin-l2-list')
