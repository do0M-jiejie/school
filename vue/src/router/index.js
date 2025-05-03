import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/admin',
      name: 'admin',
      redirect:'/admin/user_manager',
      component: () => import('@/views/admin/Manager.vue'),
      children: [
        { path: 'test', name: 'test', meta: { title: '测试' }, component: () => import('@/views/admin/Test.vue') },
        { path: 'data', name: 'data', meta: { title: '数据展示' }, component: () => import('@/views/admin/Data.vue') },
        { path: 'userManager', name: 'userManager', meta: { title: '用户' }, component: () => import('@/views/admin/UserManager.vue') },
        { path: 'coachManager', name: 'coachManager', meta: { title: '教练' }, component: () => import('@/views/admin/CoachManager.vue') },
        { path: 'courseManager', name: 'courseManager', meta: { title: '课程' }, component: () => import('@/views/admin/CourseManager.vue') },
        { path: 'equipManager', name: 'equipManager', meta: { title: '器材' }, component: () => import('@/views/admin/EquipManager.vue') },
      ],
    },
    {
      path: '/user',
      name: 'user',
      redirect:'/user/homepage',
      component: () => import('@/views/user/Home.vue'),
      children: [
        { path: 'homepage', name: 'homepage', meta: { title: '首页' }, component: () => import('@/views/user/HomePage.vue') },
        { path: 'userinfo/:id', name: 'userInfo', meta: { title: '个人主页' }, component: () => import('@/views/user/UserInfo.vue') },
        { path: 'coaches', name: 'coaches', meta: { title: '金牌教练' }, component: () => import('@/views/user/Coaches.vue') },
        { path: 'course', name: 'course', meta: { title: '健身课程' }, component: () => import('@/views/user/Course.vue') },
        { path: 'equipment', name: 'equipment', meta: { title: '健身器材' }, component: () => import('@/views/user/Equipment.vue') },
        { path: 'courseDetails/:id', name: 'courseDetails', meta: { title: '课程详情' }, component: () => import('@/views/user/CourseDetails.vue') },
        { path: 'coachDetails/:id', name: 'coachDetails', meta: { title: '教练详情' }, component: () => import('@/views/user/CoachDetails.vue') },
        { path: 'orderList/:id', name: 'orderList', meta: { title: '订单详情' }, component: () => import('@/views/user/OrderList.vue') },
        { path: 'cartDetails', name: 'cartDetails', meta: { title: '购物车详情' }, component: () => import('@/views/user/CartDetails.vue') },
        { path: 'userAppointment', name: 'userAppointment', meta: { title: '预约详情' }, component: () => import('@/views/user/UserAppointment.vue') },
      ],
    },
    {
      path: '/coach',
      name: 'coach',
      component: () => import('@/views/coach/CoachManage.vue'),
      children:[
        {path: 'home', name: 'home', meta: { title: '系统首页' }, component: () => import('@/views/coach/Home.vue')},
        {path: 'appointmentDetail', name: 'appointmentDetail', meta: { title: '预约管理' }, component: () => import('@/views/coach/AppointmentDetail.vue')},
        {path: 'myCourse', name: 'myCourse', meta: { title: '我的课程' }, component: () => import('@/views/coach/MyCourse.vue')}
      ]
    },
    { path: '/login', name: 'login', meta: { title: '登录' }, component: () => import('@/views/Login.vue') },
    { path: '/register', name: 'register', meta: { title: '注册' }, component: () => import('@/views/Register.vue') },
    { path: '/404', name: 'NotFound', meta: { title: '404找不到页面' }, component: () => import('../views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' },
  ],
})

// from:将要进行跳转的当前route对象（跳转前的一些操作）
// to:跳转后route对象（跳转后的一些操作）
// next:调用该方法后才能进入下一个钩子

// GOOD
router.beforeEach(async (to, from, next) => {
  const publicPages = ['login', 'register', 'NotFound']
  const authRequired = !publicPages.includes(to.name)
  const loggedIn = localStorage.getItem('userInfo')

  if (authRequired && !loggedIn) {
    return next({ name: 'login' })
  }
  next()
})


export default router;
