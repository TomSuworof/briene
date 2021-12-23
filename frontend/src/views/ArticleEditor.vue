<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div>
          <div class="title-wrapper">
            <input id="title" class="title" type="text" placeholder="Title" name="title" required v-model="title"/>
          </div>
          <div class="action-buttons">
            <div class="btn-container">
              <button class="btn btn-primary" id="publish" @click="handleButton('publish')" type="button" name="action" value="Publish">Publish</button>
            </div>
            <div class="btn-container">
              <button class="btn btn-primary" id="save" @click="handleButton('save')" type="button" name="action" value="Save">Save</button>
            </div>
          </div>
        </div>
        <div class="editor">
          <vue-simplemde
              :configs="configs"
              v-model="content"
              ref="markdownEditor"
              :highlight="true"
          />
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import VueSimplemde from 'vue-simplemde'
import ArticlesService from "@/api/ArticlesService";

export default {
  name: "ArticleEditor",
  components: {
    VueSimplemde
  },
  data() {
    return {
      title: '',
      content: '',
      summary: '',
      configs: {
        spellChecker: false, // disable spell checker
      },
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    contentNotEmpty: function () {
      return this.content !== '';
    },
    titleNotEmpty: function () {
      return this.title !== '';
    },
    formIsValid: function () {
      return this.contentNotEmpty() && this.titleNotEmpty();
    },
    showWarningEmpty: function () {
      alert('Fields can not be empty');
    },
    showWarningArticleExists: function () {
      alert('Such article already exists. Try to make different or remove old one');
    },
    handleButton: function (action) {
      if (this.formIsValid()) {
        let maxLength = 255

        this.summary = prompt(`Enter summary for article (max ${maxLength} characters):`, this.summary);
        if (this.summary.length > maxLength) {
          alert(`Summary should be less than ${maxLength} characters`);
        } else {
          this.uploadArticle(action);
        }
      } else {
        this.showWarningEmpty();
      }
    },
    uploadArticle: function (action) {
      ArticlesService.uploadArticle(this.title, this.content, this.summary, action)
          .then(() => {
            this.$router.push('/articles');
          })
          .catch(err => {
            console.log(err);
            this.showWarningArticleExists();
          });
    }
  },
  created() {
    if (this.$route.query.articleId !== undefined) {
      // if there is a parameter with id - it is a request for editing existing article
      let requestedArticleId = this.$route.query.articleId;

      ArticlesService.getRawArticle(requestedArticleId)
          .then((response) => {
            this.title = response.data.title;
            this.content = response.data.content;
            this.summary = response.data.summary;
          }).catch(err => {
        console.log(err);
        this.$router.replace('/error'); // redirecting to '/error'
      });
    }
  }
};
</script>

<style scoped>

@import '~simplemde/dist/simplemde.min.css';
@import '~github-markdown-css';

.title-wrapper, .action-buttons {
  display: inline-block;
}

.title-wrapper {
  width: 85%;
}

.btn-container {
  display: inline-block;
  margin: 0 5pt 5pt 0;
}

.title {
  width: 100%;
  font-size: 20pt;
  outline: transparent;
  border: transparent;
  background: transparent;
}

.editor-page-content {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
}

</style>