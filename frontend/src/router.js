import { createRouter, createWebHistory } from 'vue-router';
import Home from './components/Home';
import Error from './components/Error';
import Articles from './components/Articles';
import Author from './components/Author';


const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        { path: '/', component: Home },
        { path: '/error', component: Error },
        { path: '/articles', component: Articles },
        { path: '/authors/:authorName', component: Author }
    ]
});

export default router;