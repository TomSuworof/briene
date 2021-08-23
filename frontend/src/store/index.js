import Vuex from "vuex";
import { auth } from "./auth.module";

const store = new Vuex.Store({
    modules: {
        auth,
    },
});

export default store;