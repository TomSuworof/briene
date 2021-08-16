import { ComponentCustomProperties } from 'vue'
import { Store } from 'frontend/src/vuex'

declare module '@vue/runtime-core' {
    // declare your own store states
    interface State {
        count: number
    }

    // provide typings for `this.$store`
    interface ComponentCustomProperties {
        $store: Store<State>
    }
}