<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div>
          <div class="title-wrapper">
            <input id="title" class="article-header" type="text" placeholder="Title" name="title" required v-model="title"
                   autofocus/>
          </div>
        </div>
        <div class="tag-wrapper">
          <ul class="tags-list">
            <li class="tag-item">
              <b>Tags:</b>
            </li>
            <li class="tag-item" v-for="tag in tags">
              <div class="tag-item-name">{{ tag }}</div>
              <div class="tag-item-close-button">
                <button @click="removeTag(tag)" class="fa fa-close" title="Remove tag" type="button"/>
              </div>
            </li>
            <li class="tag-item" v-if="showTagInput">
              <form @submit.prevent="addTag" autocomplete="off">
                <input name="tagValue" class="add-tag-input" list="tagsList"/>
                <datalist id="tagsList">
                  <option v-for="suggestedTag in suggestedTags">{{ suggestedTag }}</option>
                </datalist>
              </form>
            </li>
            <li class="tag-item" v-if="!showTagInput">
              <button @click="openAddTagInput()" class="fa fa-plus" title="Add tag"></button>
            </li>
          </ul>
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
      <div class="article-summary-text">
        <b>Summary</b>
        <p>Max {{ maxLength }} characters. This summary will be seen under title in article card</p>
        <button class="button button-outline" id="magic-summary" @click="makeMagicSummary()" type="button"
                name="magic-summary"
                value="magic-summary">
          <span>Make magic summary</span>
        </button>
      </div>
      <div class="article-summary-wrapper">
        <textarea v-model="summary" :maxlength="maxLength"></textarea>
      </div>
      <div class="article-url-text">
        <b>Custom URL</b>
        <p>Max {{ 140 }} characters</p>
      </div>
      <div class="article-url-wrapper">
        <input class="article-url" type="text" maxlength="140" placeholder="Custom URL" required v-model="url"/>
      </div>
      <div class="action-buttons">
        <div class="btn-container">
          <button class="button button-outline" id="save" @click="handleButton('save')" type="button"
                  name="action"
                  value="save">
            <span v-show="loadingSave" class="spinner-border spinner-border-sm"></span>
            <span>Save as draft</span>
          </button>
        </div>
        <div class="btn-container">
          <button class="button button-primary" id="publish" @click="handleButton('publish')" type="button"
                  name="action"
                  value="publish">
            <span v-show="loadingPublish" class="spinner-border spinner-border-sm"></span>
            <span>Publish</span>
          </button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import VMdEditor from '@kangc/v-md-editor';
import ArticlesService from "@/api/ArticlesService";
import TagService from "@/api/TagService";

export default {
  name: "ArticleEditor",
  components: {
    VMdEditor,
  },
  data() {
    return {
      id: '',
      title: '',
      content: '',
      summary: '',
      url: '',
      tags: [],
      action: '',

      showTagInput: false,
      suggestedTags: [],

      maxLength: 500,

      loadingSave: false,
      loadingPublish: false
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  methods: {
    contentNotEmpty: function () {
      return this.content !== '';
    },
    titleNotEmpty: function () {
      return this.title !== '';
    },
    urlNotEmpty: function () {
      return this.url !== '';
    },
    formIsValid: function () {
      return this.contentNotEmpty() && this.titleNotEmpty() && this.urlNotEmpty();
    },
    showWarningEmpty: function () {
      alert('Fields can not be empty');
    },
    showWarningArticleExists: function () {
      alert('Such article already exists. Try to make different title or URL or remove old one');
    },
    handleButton: function (action) {
      if (this.formIsValid()) {
        if (this.summary !== null && this.summary.length > this.maxLength) {
          alert(`Summary should be less than ${this.maxLength} characters`);
        } else {
          this.uploadArticle(action);
        }
      } else {
        this.showWarningEmpty();
      }
    },
    uploadArticle: function (action) {
      if (action === 'publish') {
        this.loadingPublish = true;
      } else if (action === 'save') {
        this.loadingSave = true;
      }
      ArticlesService.uploadArticle(this.id, this.title, this.content, this.summary, action, this.tags, this.url)
          .then(() => {
            if (action === 'publish') {
              this.loadingPublish = false;
              this.$router.push('/');
            } else if (action === 'save') {
              this.loadingSave = false;
              alert('Article was saved');
            }
          })
          .catch(() => {
            this.loadingPublish = false;
            this.loadingSave = false;
            this.showWarningArticleExists();
          });
    },
    saveArticleToLocalStorage: function () {
      localStorage.setItem('recentArticle', JSON.stringify({
        'title': this.title,
        'summary': this.summary,
        'content': this.content,
        'tags': this.tags,
        'url': this.url,
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
        this.url = article.url;
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
      let cursorPos = document.getElementsByTagName('textarea')[0].selectionStart;

      this.content = this.content.substring(0, cursorPos) + template + this.content.substring(cursorPos);
    },
    loadTags: function () {
      TagService.getTagsWithExclusion(this.tags)
          .then(response => this.suggestedTags = response.data)
          .catch(err => console.log(err));
    },
    openAddTagInput: function () {
      this.showTagInput = true;
    },
    addTag: function (submitEvent) {
      let newTagName = submitEvent.target.elements.tagValue.value;
      this.suggestedTags.splice(this.suggestedTags.indexOf(newTagName), 1);
      this.tags.push(newTagName);
      this.showTagInput = false;
    },
    removeTag: function (tagName) {
      this.tags.splice(this.tags.indexOf(tagName), 1);
    },
    getSummaryFromContent: function (content) {
      let maxAvailContent = content.substring(0, this.maxLength);
      let reversedContent = maxAvailContent.split('').reverse().join('');
      let indexOfLastDot = maxAvailContent.length - reversedContent.indexOf('.', 1)

      return maxAvailContent.substring(0, indexOfLastDot);
    },
    makeMagicSummary: function () {
      this.summary = this.getSummaryFromContent(this.content);
    }
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
      this.id = this.$route.query.articleId;

      ArticlesService.getArticleRaw(this.id)
          .then(response => {
            this.title = response.data.title;
            this.content = response.data.content;
            this.summary = response.data.summary;
            this.tags = response.data.tags;
            this.url = response.data.url;
          })
          .catch(() => {
            this.$router.replace('/error'); // redirecting to '/error'
          });
    } else {
      this.loadArticleFromLocalStorage();
    }

    this.loadTags();

    if (this.url === undefined || this.url === '') {
      this.url = this.title;
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
  width: 75%;
}

.article-header {
  color: var(--text-color);
  width: 100%;
  font-size: 20pt;
  font-weight: bold;
  background: transparent;
  outline: 0;
  border-width: 0 0 1px;
  border-color: var(--background-color-contrast);
}

.tag-wrapper {
  padding-top: 10pt;
}

.tags-list {
  padding-left: 0;
  padding-bottom: 5pt;
  margin: 0;
  display: flex;
  gap: 7pt;
  list-style-type: none;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
}

.tag-item-name, .tag-item-close-button {
  display: inline-block;
}

.tag-item-name {
  padding-right: 3pt;
}

.fa-plus {
  color: #666 !important;
  border: 1px solid #666 !important;
}

.fa-close, .fa-plus {
  text-align: center;
  height: 1.9em;
  background: none;
  border: 1px solid #AAA;
  color: #AAA;
}

.v-md-editor {
  background-color: var(--background-color-primary);
  color: var(--text-color);
  padding: 1.5pt;
  margin: 10pt 0;
  border-radius: 9px !important;
  box-shadow: var(--shadow);
}

.article-summary-text {
  color: var(--text-color);
  margin-top: 20pt;
  margin-bottom: 10pt;
}

.article-summary-wrapper > textarea {
  background-color: var(--background-color-primary);
  color: var(--text-color);
  padding: 5pt;
  border-radius: 9px;
  width: 100%;
  height: 200pt;
  resize: none;
}

.article-url-text {
  color: var(--text-color);
  margin-top: 20pt;
}

.article-url-wrapper {
  position: relative;
  border: 1px solid #777;
  border-radius: 9px;
  padding: 5pt;
}

.article-url {
  color: var(--text-color);
  width: 100%;
  background: transparent;
  outline: 0;
  border: none;
}

.action-buttons {
  margin-top: 20pt;
}

</style>