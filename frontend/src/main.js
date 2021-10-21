import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import setupInterceptors from "@/api/setupInterceptors";

setupInterceptors(store, router);

createApp(App)
    .use(router)
    .use(store)
    .mount("#app");