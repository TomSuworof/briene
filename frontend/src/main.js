import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import Keycloak from "keycloak-js";

createApp(App).use(router).mount('#app')

// Параметры для подключения к Keycloak
let initOptions = {
    url: 'http://localhost:8080/auth', // Адрес Keycloak
    realm: 'keycloak-demo', // Имя нашего realm в Keycloak
    clientId: 'app-vue', // Идентификатор клиента в Keycloak

    // Перенаправлять неавторизованных пользователей на страницу входа
    onLoad: 'login-required'
}

// Создать Keycloak JS Adapter
let keycloak = Keycloak(initOptions);

// Инициализировать Keycloak JS Adapter
keycloak.init({ onLoad: initOptions.onLoad }).then((auth) => {
    if (!auth) {
        // Если пользователь не авторизован - перезагрузить страницу
        window.location.reload();
    } else {
        Vue.$log.info("Authenticated");

        // Если авторизован - инициализировать приложение Vue
        new Vue({
            el: '#app',
            render: h => h(App, { props: { keycloak: keycloak } })
        })
    }

    // Пытаемся обновить токен каждые 6 секунд
    setInterval(() => {
        // Обновляем токен, если срок его действия истекает в течении 70 секунд
        keycloak.updateToken(70).then((refreshed) => {
            if (refreshed) {
                Vue.$log.info('Token refreshed' + refreshed);
            } else {
                Vue.$log.warn('Token not refreshed, valid for '
                    + Math.round(keycloak.tokenParsed.exp
                        + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
            }
        }).catch(() => {
            Vue.$log.error('Failed to refresh token');
        });
    }, 6000)

}).catch(() => {
    Vue.$log.error("Authenticated Failed");
});