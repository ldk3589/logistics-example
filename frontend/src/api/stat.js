import request from '../api/request';

/**
 * 获取统计数据
 * @param {string} period - WEEK / MONTH / YEAR / HISTORY
 */
export const getOrderStatApi = (period) => {
  return request.get('/stats/orders', {
    params: { period }
  });
};

/**
 * 获取订单量趋势数据 (新增，用于图表)
 * @param {string} period - WEEK / MONTH / YEAR
 * @returns {Promise<{ dates: string[], counts: number[], amounts: number[] }>}
 */
export const getOrderTrendApi = (period) => {
  // 假设后端有一个新的接口，如 /stats/orders/trend
  // 如果没有，你需要让后端增加一个，或者在现有接口返回更多数据
  return request.get('/stats/orders/trend', {
    params: { period }
  });
};