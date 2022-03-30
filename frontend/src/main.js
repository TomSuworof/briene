import { createApp } from "vue";
import BootstrapVue3 from 'bootstrap-vue-3';
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import setupInterceptors from "@/api/setupInterceptors";

setupInterceptors(store, router);

import hljs from 'highlight.js';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import createKatexPlugin from '@kangc/v-md-editor/lib/plugins/katex/cdn';
import createMermaidPlugin from '@kangc/v-md-editor/lib/plugins/mermaid/cdn';
import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css';
import enUS from '@kangc/v-md-editor/lib/lang/en-US';

VMdEditor
    .use(githubTheme, { Hljs: hljs })
    .use(createKatexPlugin())
    .use(createMermaidPlugin());

VMdEditor.lang.use('en-US', enUS);

VMdEditor.xss.extend({
    whiteList: {
        source: [],
        iframe: ['src', 'width', 'height', 'title', 'frameBorder', 'allow', 'allowFullScreen']
    },
});

createApp(App)
    .use(VMdEditor)
    .use(router)
    .use(store)
    .use(BootstrapVue3)
    .mount("#app");