import router from '@/router';
import axios from 'axios'

// 创建一个新的axios对象
const request=axios.create({
    baseURL:process.env.VUE_APP_BASEURL,
    timeout:30000
})

// request拦截器
// 可以在请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密

request.interceptors.request.use(config=>{
    config.headers['Content-Type']='application/json;charset=utf-8';
    let user=JSON.parse(localStorage.getItem("userData")||'{}')
    config.headers['token']=user.token//设置请求头
    return config;
},
error=>{
    console.error('request error:'+error)
    return Promise.reject(error)
});

// response拦截器
// 可在接口响应后统一处理结果
request.interceptors.response.use(
    response=>{
        let res=response.data
// 之前每次拿数据都是res.data.data，这里将res.data封装起来
        // 兼容服务器端返回的是字符串数据（我们springboot返回的是json数据，不碍事，但是可能其他的服务器端返回的是字符串）
        if(typeof res === 'string'){
            res=res?JSON.parse(res) :res
        }
        // code==401表示用户未登录就访问接口，跳转至登录页面
        if(res.code === '401'){
            router.push('/login')
        }
        return res;
    },
    error=>{
        console.error('response error' +error)
        return Promise.reject(error)
    }
)
export default request;