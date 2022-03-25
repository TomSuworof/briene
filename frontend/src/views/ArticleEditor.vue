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
              <button class="button button-outline" id="save" @click="handleButton('save')" type="button"
                      name="action"
                      value="save">
                <span>Save</span>
              </button>
            </div>
            <div class="btn-container">
              <button class="button button-primary" id="publish" @click="handleButton('publish')" type="button"
                      name="action"
                      value="publish">
                <span>Publish</span>
              </button>
            </div>
          </div>
        </div>
        <div class="editor">
          <v-md-editor
              v-model="content"
              :disabled-menus="[]"
              @upload-image="handleUploadImage"
              height="367pt"
          />
        </div>
      </div>
    </form>
    <article-summary-modal/>
  </div>
</template>

<script>
import VMdEditor from '@kangc/v-md-editor';
import ArticlesService from "@/api/ArticlesService";
import { openModal, container } from "jenesius-vue-modal";
import ArticleSummaryModal from "@/components/ArticleSummaryModal"

export default {
  name: "ArticleEditor",
  components: {
    VMdEditor,
    ArticleSummaryModal: container
  },
  data() {
    return {
      title: '',
      content: '',
      summary: '',
      tags: [],
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    currentAvailableHeight() {
      return window.screen.height * 0.6;
    }
  },
  methods: {
    handleButton: function (action) {
      openModal(ArticleSummaryModal, {
        title: this.title,
        content: this.content,
        summary: this.summary,
        tags: this.tags,
        action: action,
      })
    },
    saveArticleToLocalStorage: function () {
      localStorage.setItem('recentArticle', JSON.stringify({
        'title': this.title,
        'summary': this.summary,
        'content': this.content,
        'tags': this.tags,
      }));
    },
    loadArticleFromLocalStorage: function () {
      let recentArticle = localStorage.getItem('recentArticle');

      if (recentArticle !== null) {
        let article = JSON.parse(recentArticle);
        this.title = article.title;
        this.summary = article.summary;
        this.content = article.content;
        this.tags = article.tags;
      }
    },
    toBase64: function (file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
      });
    },
    handleUploadImage: async function (event, insertImage, files) {

      let result = await this.toBase64(files[0]);

      let template = `<br><img loading="lazy" id="base64image" src="${result}"  alt="Image from clipboard"/><br>`;
      this.content += template;
    },
  },
  created() {
    if (this.currentUser === undefined) {
      this.$router.push('/login');
    }

    window.onblur = () => {
      this.saveArticleToLocalStorage();
    };

    if (this.$route.query.articleId !== undefined) {
      // if there is a parameter with id - it is a request for editing existing article
      let requestedArticleId = this.$route.query.articleId;

      ArticlesService.getArticleRaw(requestedArticleId)
          .then((response) => {
            this.title = response.data.title;
            this.content = response.data.content;
            this.summary = response.data.summary;
            this.tags = response.data.tags;
          }).catch(err => {
        console.log(err);
        this.$router.replace('/error'); // redirecting to '/error'
      });
    } else {
      this.loadArticleFromLocalStorage();
    }
  },
  beforeUnmount() {
    this.saveArticleToLocalStorage();
  }
};
</script>

<style scoped>

.title-wrapper, .action-buttons {
  display: inline-block;
}

.title-wrapper {
  width: 85%;
}

.btn-container {
  display: inline-block;
  /*margin: 0 5pt 5pt 0;*/
}

.title {
  width: 100%;
  font-size: 20pt;
  font-weight: bold;
  outline: transparent;
  border: transparent;
  background: transparent;
}

.v-md-editor {
  border-radius: 9px !important;
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px !important;
}
</style>