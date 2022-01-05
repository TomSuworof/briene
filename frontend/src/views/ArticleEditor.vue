<template>
  <div class="editor-page-content">
    <!--    <div class="summary-wrapper" v-show="show_summary_wrapper">-->
    <!--      <textarea></textarea>-->
    <!--    </div>-->
    <form method="post">
      <div class="editor-wrapper">
        <div>
          <div class="title-wrapper">
            <input id="title" class="title" type="text" placeholder="Title" name="title" required v-model="title"/>
          </div>
          <div class="action-buttons">
            <div class="btn-container">
              <button class="btn btn-primary" id="publish" @click="handleButton('publish')" type="button" name="action"
                      value="Publish">
                <span>Publish</span>
              </button>
            </div>
            <div class="btn-container">
              <button class="btn btn-primary" id="save" @click="handleButton('save')" type="button" name="action"
                      value="Save">
                <span>Save</span>
              </button>
            </div>
          </div>
        </div>
        <div class="editor">
          <v-md-editor
              v-model="content"
              :disabled-menus="[]"
              @upload-image="handleUploadImage"
              height="400pt"
          />
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import VMdEditor from '@kangc/v-md-editor';
import ArticlesService from "@/api/ArticlesService";

export default {
  name: "ArticleEditor",
  components: {
    VMdEditor
  },
  data() {
    return {
      title: '',
      content: '',
      summary: '',
      // show_summary_wrapper: true
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
    },
    saveArticleToLocalStorage: function () {
      localStorage.setItem('recentArticle', JSON.stringify({
        'title': this.title,
        'summary': this.summary,
        'content': this.content
      }));
    },
    loadArticleFromLocalStorage: function () {
      let recentArticle = localStorage.getItem('recentArticle');

      if (recentArticle !== null) {
        let article = JSON.parse(recentArticle);
        this.title = article.title;
        this.summary = article.summary;
        this.content = article.content;
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

      let template = `<br><img id="base64image" src="${result}"  alt="Image from clipboard"/><br>`;
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
  margin: 0 5pt 5pt 0;
}

.title {
  width: 100%;
  font-size: 20pt;
  outline: transparent;
  border: transparent;
  background: transparent;
}

/*.summary-wrapper {*/
/*  position: absolute;*/
/*  top: 50%;*/
/*  left: 50%;*/
/*  width: 300pt;*/
/*  background: white;*/
/*  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;*/
/*  border-radius: 10px;*/
/*  margin: 0 0 10pt;*/
/*  padding: 10pt;*/
/*}*/
</style>