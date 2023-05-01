

// 编辑页面反查详情接口
const queryriderById = (id) => {
  return $axios({
    url: `/rider/${id}`,
    method: 'get'
  })
}
const getSellerList = (params) => {
  return $axios({
    url: '/rider/list',
    method: 'get',
    params
  })
}
const changeSeller = (params) => {
  return $axios({
    url: '/rider',
    method: 'post',
    params
  })
}
// 删除当前列的接口
const delerider = (id) => {
  return $axios({
    url: '/rider',
    method: 'delete',
    params: { id }
  })
}
const riderBelong = (params) => {
  return $axios({
    url: '/rider/belong',
    method: 'get',
    params
  })
}
// 修改接口
const editrider = (params) => {
  return $axios({
    url: '/rider',
    method: 'put',
    data: { ...params }
  })
}
const getRiderPage = (params) => {
  return $axios({
    url: '/rider/page',
    method: 'get',
    params
  })
}

// 新增接口
const addrider = (params) => {
  return $axios({
    url: '/rider',
    method: 'post',
    data: { ...params }
  })
}