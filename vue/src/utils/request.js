import axios from 'axios';
import { ElMessage } from 'element-plus'
import router from '@/router/index.js'

// 1. 创建实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 环境变量配置
  timeout: 30000, // 超时时间
});

// 2. 请求拦截器
//可以自请求发送前请求做一些处理
request.interceptors.request.use((config) => {
    config.headers['Content-Type'] = `application/json;charset=utf-8`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  });

// 3. 响应拦截器
//可以在接口相应后统一处理结果
request.interceptors.response.use(
  response => {
    let res = response.data;
    //兼容服务端返回的字符串数据
    if (typeof res == 'string') {
      res = res ? JSON.parse(res) : res
    }
    return res;
  },
  (error) => {
    // 对 HTTP 错误状态码做统一处理
    if (error.response === 404) {
        ElMessage.error('未找到请求接口')
    } else if (error.response.status === 500) {
        ElMessage.error('系统异常，查看后端报错')
    } else {
        console.error(error.message)
    }
    return Promise.reject(error);
  }
);

export default request