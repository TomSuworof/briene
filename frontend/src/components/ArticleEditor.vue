<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div class="row">
          <div class="title-wrapper">
            <input id="title" class="title" type="text" placeholder="Title" name="title" required/>
          </div>
          <div class="action-buttons">
            <input type="submit" name="action" value="Publish"/>
            <input type="submit" name="action" value="Save"/>
          </div>
        </div>
        <div class="editor">
          <vue-simplemde v-model="content" ref="markdownEditor"/>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import VueSimplemde from 'vue-simplemde'

export default {
  name: "ArticleEditor",
  components: {
    VueSimplemde
  },
  data() {
    return {
      content: '',
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {},
  created() {

  },
  beforeMount() {
    if (this.currentUser === null) {
      this.$router.push({path: '/login'});
    }
  }
}
</script>

<style scoped>

@import '~simplemde/dist/simplemde.min.css';

.row {
  padding: 0 10pt 0;
  justify-content: space-between;
}

input[type=text] {
  font-size: 20pt;
  outline: none;
  border: none;
}

.editor-page-content {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
}

</style>