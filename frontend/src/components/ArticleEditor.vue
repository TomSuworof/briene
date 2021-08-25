<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div class="row">
          <div class="title-wrapper">
            <input id="title" class="title" type="text" placeholder="Title" name="title" required v-model="title"/>
          </div>
          <div class="action-buttons">
            <input id="publish" @click="handleButton('publish')" type="button" name="action" value="Publish"/>
            <input id="save" @click="handleButton('save')" type="button" name="action" value="Save"/>
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
import ArticlesService from "@/services/ArticlesService";

export default {
  name: "ArticleEditor",
  components: {
    VueSimplemde
  },
  data() {
    return {
      title: '',
      content: '',
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    contentNotEmpty: function() {
      return this.content !== '';
    },
    titleNotEmpty: function() {
      return this.title !== '';
    },
    formIsValid: function() {
      return this.contentNotEmpty() && this.titleNotEmpty();
    },
    showWarningEmpty: function() {
       alert('Fields can not be empty');
    },
    showWarningArticleExists: function() {
      alert('Such article already exists. Try to make different or remove old one');
    },
    handleButton: function(action) {
      if (this.formIsValid()) {
        ArticlesService.loadArticle(this.title, this.content, action, this.currentUser.token)
            .then(() => {
              this.$router.push('/articles');
            })
            .catch(err => {
              console.log(err);
              this.showWarningArticleExists();
            });
      } else {
        this.showWarningEmpty();
      }
    },
  },
  created() {
    if (this.currentUser === null) {
      this.$router.replace('/login');
    }
  },
  mounted() {
    if (this.$route.query.articleId !== undefined) {
      // if there is a parameter with id - it is a request for editing existing article
      let requestedArticleId = this.$route.query.articleId;

      ArticlesService.getRawArticle(requestedArticleId, this.currentUser ? this.currentUser.token : undefined)
          .then((response) => {
            this.title = response.data.title;
            this.content = response.data.content;
          }).catch(err => {
            console.log(err);
            this.$router.replace('/error'); // redirecting to '/error'
          });
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