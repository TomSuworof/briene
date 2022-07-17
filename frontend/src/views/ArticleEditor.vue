<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div>
          <div class="title-wrapper">
            <input id="title" class="title" type="text" placeholder="Title" name="title" required v-model="title" autofocus/>
          </div>
          <div class="action-buttons">
            <div class="btn-container">
              <button class="button button-outline" id="save" @click="handleButton('save')" type="button"
                      name="action"
                      value="save">
                <span>Save as draft</span>
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
        <div class="tag-wrapper">
          <ul class="tags-list">
            <li class="tag-item">
              <b>Tags:</b>
            </li>
            <li class="tag-item" v-for="tag in tags">
              <div class="tag-item-name">{{ tag }}</div>
              <div class="tag-item-close-button">
                <button @click="removeTag(tag)" class="fa fa-close" title="Remove tag"></button>
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
    </form>
    <article-summary-modal/>
  </div>
</template>

<script>
import VMdEditor from '@kangc/v-md-editor';
import ArticlesService from "@/api/ArticlesService";
import { openModal, container } from "jenesius-vue-modal";
import ArticleSummaryModal from "@/components/ArticleSummaryModal"
import TagService from "@/api/TagService";
import articleSummaryModal from "../components/ArticleSummaryModal";

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
      url: '',
      tags: [],

      showTagInput: false,
      suggestedTags: []
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  methods: {
    handleButton: function (action) {
      openModal(ArticleSummaryModal, {
        title: this.title,
        content: this.content,
        summary: this.summary,
        tags: this.tags,
        url: this.url,
        action: action,
      })
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
            this.url = response.data.url;
          }).catch(err => {
        console.log(err);
        this.$router.replace('/error'); // redirecting to '/error'
      });
    } else {
      this.loadArticleFromLocalStorage();
    }

    TagService.getTagsWithExclusion(this.tags)
        .then(response => this.suggestedTags = response.data)
        .catch(err => console.log(err));
  },
  beforeUnmount() {
    this.saveArticleToLocalStorage();
  }
};
</script>

<style scoped>

.action-buttons {
  float: right;
}

.title-wrapper, .action-buttons {
  display: inline-block;
}

.title-wrapper {
  width: 75%;
}

.btn-container {
  display: inline-block;
  /*margin: 0 5pt 5pt 0;*/
}

.title {
  width: 100%;
  font-size: 20pt;
  font-weight: bold;
  background: transparent;
  outline: 0;
  border-width: 0 0 1px;
  border-color: #CCC;
}

.tags-list {
  padding-top: 10pt;
  padding-left: 0;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
  display: inline-block;
  position: relative;
  margin: 0 10pt 0 0;
  color: #666;
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
  padding: 2pt;
  text-align: center;
  height: 1.7em;
  width: 1.7em;
  border-radius: 20px !important;
  background: none;
  border: 1px solid #AAA;
  color: #AAA;
}

.v-md-editor {
  margin: 10pt 0;
  border-radius: 9px !important;
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px !important;
}
</style>