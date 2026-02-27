import { createApp } from 'vue'
import './styles/oc.css'
import './styles/oc-utilities.css'
import './styles/global.css'
import { createRouter, createWebHistory } from 'vue-router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import Workplace from './views/Workplace.vue'
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
import Departments from './views/Departments.vue'
import Notices from './views/Notices.vue'
import Messages from './views/Messages.vue'
import { isAuthenticated } from './auth'

const routes = [
  { path: '/login', component: Login },
  { 
    path: '/dashboard', 
    component: Dashboard,
children: [
      { path: '', redirect: '/dashboard/workplace' },
      { path: 'workplace', component: Workplace },
      { path: 'overview', component: Overview },
      { path: 'reports', component: Reports },
      { path: 'charts', component: Charts },
      { path: 'departments', component: Departments },
      { path: 'users', component: Users },
      { path: 'roles', component: Roles },
      { path: 'logs', component: Logs },
      { path: 'projects', component: Projects },
      { path: 'tasks', component: Tasks },
      { path: 'calendar', component: Calendar },
      { path: 'documents', component: Documents },
      { path: 'wiki', component: Wiki },
      { path: 'notices', component: Notices },
      { path: 'messages', component: Messages },
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

const app = createApp(App)
app.use(router)
app.use(ElementPlus)

// Register all icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
