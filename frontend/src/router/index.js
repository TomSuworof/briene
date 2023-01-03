import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home';
import TermsOfUse from "@/views/TermsOfUse";
import Error from '@/views/Error';

import Login from '@/views/Login';
import Register from '@/views/Register';
import Profile from '@/views/Profile';

import Search from "@/views/Search";
import Article from '@/views/Article';
import ArticleEditor from "@/views/ArticleEditor";

import Admin from "@/views/Admin";

import Author from '@/views/Author';
import PasswordReset from "@/views/PasswordReset";
import Tag from "@/views/Tag";
import ArticleShare from "@/views/ArticleShare";

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        { path: '/', component: Home, meta: { requiresAuth: false } },
        { path: '/terms_of_use', component: TermsOfUse, meta: { requiresAuth: false } },
        { path: '/error', component: Error, meta: { requiresAuth: false } },

        { path: '/login', component: Login, meta: { requiresAuth: false } },
        { path: '/register', component: Register, meta: { requiresAuth: false } },
        { path: '/password_reset', component: PasswordReset, meta: { requiresAuth: false } },
        { path: '/profile', component: Profile, meta: { requiresAuth: true } },

        { path: '/search', component: Search , meta: { requiresAuth: false }},
        { path: '/articles/share/:articleId', component: ArticleShare, meta: { requiresAuth: false } },
        { path: '/articles/:articleUrl', component: Article, meta: { requiresAuth: false } },
        { path: '/article_editor', component: ArticleEditor, meta: { requiresAuth: true } },

        { path: '/admin', component: Admin, meta: { requiresAuth: true } },

        { path: '/authors/:authorName', component: Author, meta: { requiresAuth: false } },
        { path: '/tags/:tag', component: Tag, meta: { requiresAuth: false } },

        // otherwise, redirect to home
        { path: '/:pathMatch(.*)*', redirect: '/' }
    ]
});

router.beforeEach((to, from, next) => {
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const loggedIn = localStorage.getItem('user') != null;

    // trying to access a restricted page + not logged in
    // redirect to login page

    if (requiresAuth && !loggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router;