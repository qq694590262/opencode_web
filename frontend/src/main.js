import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import Overview from './views/Overview.vue'
import Reports from './views/Reports.vue'
import Charts from './views/Charts.vue'
import Users from './views/Users.vue'
import Roles from './views/Roles.vue'
import Logs from './views/Logs.vue'
import Projects from './views/Projects.vue'
import Tasks from './views/Tasks.vue'
import Calendar from './views/Calendar.vue'
import Documents from './views/Documents.vue'
import Wiki from './views/Wiki.vue'
import Profile from './views/Profile.vue'
import Prefs from './views/Prefs.vue'
import System from './views/System.vue'
import { isAuthenticated } from './auth'

const routes = [
  { path: '/login', component: Login },
  { 
    path: '/dashboard', 
    component: Dashboard,
    children: [
      { path: '', redirect: '/dashboard/overview' },
      { path: 'overview', component: Overview },
      { path: 'reports', component: Reports },
      { path: 'charts', component: Charts },
      { path: 'users', component: Users },
      { path: 'roles', component: Roles },
      { path: 'logs', component: Logs },
      { path: 'projects', component: Projects },
      { path: 'tasks', component: Tasks },
      { path: 'calendar', component: Calendar },
      { path: 'documents', component: Documents },
      { path: 'wiki', component: Wiki },
      { path: 'profile', component: Profile },
      { path: 'prefs', component: Prefs },
      { path: 'system', component: System }
    ]
  },
  { path: '/', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') return next()
  if (isAuthenticated()) return next()
  next('/login')
})

createApp(App).use(router).mount('#app')
