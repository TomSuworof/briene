import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home';
import TermsOfUse from "@/views/TermsOfUse";
import Error from '@/views/Error';

import Login from '@/views/Login';
import Register from '@/views/Register';
import Profile from '@/views/Profile';

import Articles from '@/views/Articles';
import Search from "@/views/Search";
import Article from '@/views/Article';
import ArticleEditor from "@/views/ArticleEditor";

import Admin from "@/views/Admin";

import Author from '@/views/Author';
import PasswordReset from "@/views/PasswordReset";

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        { path: '/', component: Home },
        { path: '/terms_of_use', component: TermsOfUse },
        { path: '/error', component: Error },

        { path: '/login', component: Login},
        { path: '/register', component: Register},
        { path: '/password_reset', component: PasswordReset },
        { path: '/profile', component: Profile, meta: { requiresAuth: true } },

        { path: '/articles', component: Articles },
        { path: '/search', component: Search },
        { path: '/articles/:articleId', component: Article },
        { path: '/article_editor', component: ArticleEditor, meta: { requiresAuth: true } },

        { path: '/admin', component: Admin, meta: { requiresAuth: true } },

        { path: '/authors/:authorName', component: Author },

        // otherwise, redirect to home
        { path: '/:pathMatch(.*)*', redirect: '/' }
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