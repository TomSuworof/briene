import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/components/Home';
import TermsOfUse from "@/components/TermsOfUse";
import Error from '@/components/Error';



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

        { path: '/articles', component: Articles },
        { path: '/articles/:articleId', component: Article },
        { path: '/article_editor', component: ArticleEditor },

        { path: '/authors/:authorName', component: Author }
    ]
});

export default router;