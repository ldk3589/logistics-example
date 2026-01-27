import request from '../api/request';

/**
 * 注册
 * @param {Object} data - 包含 username, password, role
 */
export const registerApi = (data) => {
  return request.post('/auth/register', data);
};

/**
 * 登录
 * @param {Object} data - 包含 username, password
 * @returns {Promise<string>} 返回 JWT Token 字符串
 */
export const loginApi = (data) => {
  return request.post('/auth/login', data);
};