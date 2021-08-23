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
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        { path: '/', component: Home },
        { path: '/terms_of_use', component: TermsOfUse },
        { path: '/error', component: Error },

        { path: '/login', component: Login},
        { path: '/register', component: Register},
        { path: '/profile', component: Profile},

        { path: '/articles', component: Articles },
        { path: '/articles/:articleId', component: Article },
        { path: '/article_editor', component: ArticleEditor },

        { path: '/authors/:authorName', component: Author }
    ]
});

export default router;