import Vue from 'vue'
import VueRouter from 'vue-router'

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../components/home'),
    children: [{
      path: "/",
      name: "index",
      component: () => import('../components/index')
    }, {
      path: "/category",
      name: "category",
      component: () => import('../components/category')
    }, {
      path: "/tag",
      name: "tag",
      component: () => import('../components/tag')
    }, {
      path: "/article",
      name: "article",
      component: () => import('../components/article')
    }, {
      path: "/weiYan",
      name: "weiYan",
      component: () => import('../components/weiYan')
    }, {
      path: "/message",
      name: "message",
      component: () => import('../components/message')
    }, {
      path: "/friend",
      name: "friend",
      component: () => import('../components/friend')
    }, {
      path: "/about",
      name: "about",
      component: () => import('../components/about')
    }, {
      path: "/user",
      name: "user",
      component: () => import('../components/user')
    }, {
      path: "/letter",
      name: "letter",
      component: () => import('../components/letter')
    },{
      path: "/archive",
      name: "archive",
      component: () => import('../components/archive')
    }]
  }
]

const router = new VueRouter({
  mode: "history",
  base: process.env.NODE_ENV === 'production' ? 'jw_vue_blog/':'/',
  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    return {x: 0, y: 0}
  }
})

router.beforeEach((to, from, next) => {

  next();

})

export default router
