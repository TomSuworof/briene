import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    mode: history,
    routes: [
        {
            path: '/',
            alias: '/articles',
            name: 'articles',
            component: () => import('./components/Articles')
        },
        {
            path: '/authors/:authorName',
            name: 'author',
            component: () => import('./components/Author')
        }
    ],
});