import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/components/Home';
import TermsOfUse from "@/components/TermsOfUse";
import Error from '@/components/Error';

import Login from '@/components/Login';
import Register from '@/components/Register';
import Profile from '@/components/Profile';

import Articles from '@/components/Articles';
import Article from '@/components/Article';
import ArticleEditor from "@/components/ArticleEditor";

import Author from '@/components/Author';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Home },
        { path: '/terms_of_use', component: TermsOfUse },
        { path: '/error', component: Error },

        { path: '/login', component: Login},
        { path: '/register', component: Register},
        { path: '/profile', component: Profile, meta: {requiresAuth: true} },

        { path: '/articles', component: Articles },
        { path: '/articles/:articleId', component: Article },
        { path: '/article_editor', component: ArticleEditor, meta: {requiresAuth: true} },

        { path: '/authors/:authorName', component: Author }
    ]
});

router.beforeEach((to, from, next) => {
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    const loggedIn = localStorage.getItem('user');

    // trying to access a restricted page + not logged in
    // redirect to login page

    if (requiresAuth && !loggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router;