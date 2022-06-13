<template>
  <div class="article-page-content">
    <div class="article-content-wrapper">
      <div class="article-about">
        <div class="article-author">
          <router-link :to="'/authors/' + article.author">{{ article.author }}</router-link>
        </div>
      </div>
      <div class="title">
        <div class="article-title">
          <h1 id="article-title">{{ article.title }}</h1>
        </div>
        <div class="bookmarking" v-if="article.title !== undefined">
          <div v-if="!inBookmarks" title="Add to bookmarks">
            <button @click="editBookmarks('add')">
              <img loading="eager" src="https://img.icons8.com/ios/35/000000/bookmark-ribbon--v1.png"
                   alt="Add to bookmarks"/>
            </button>
          </div>
          <div v-if="inBookmarks" title="Remove from bookmarks">
            <button @click="editBookmarks('remove')">
              <img loading="eager" src="https://img.icons8.com/ios-filled/35/000000/bookmark-ribbon.png"
                   alt="Remove from bookmarks"/>
            </button>
          </div>
        </div>
      </div>
      <div class="article-publication-date" v-if="article.publicationDate !== undefined">
        <time :datetime="htmlPublicationDate">{{ getFinePublicationDate(article.publicationDate) }}</time>
      </div>
      <div class="article-tags tags-container">
        <ul class="tags-list">
          <li class="tag-item" v-for="tag in article.tags">
            <a :href="'/tags/' + tag">{{ tag }}</a>
          </li>
        </ul>
      </div>
      <hr>
      <div v-if="loadingArticle">
        <ShimmerBlock/>
      </div>
      <div v-show="!loadingArticle" id="article-content">
        <v-md-editor :model-value="article.content" mode="preview"></v-md-editor>
      </div>
      <div class="control" id="control">
        <p id="quote">💬 Quote</p>
      </div>
    </div>
    <div class="suggestions-wrapper" v-if="nextArticle || prevArticle">
      <p><b>Read next</b></p>
      <div class="suggestions">
        <div class="next-article" v-if="nextArticle">
<!--          <p class="suggestion-name">Ctrl ←</p>-->
          <article-component
              v-bind:key="nextArticle.id"
              v-bind:article="nextArticle"
          ></article-component>
        </div>
        <div class="prev-article" v-if="prevArticle">
<!--          <p class="suggestion-name">Ctrl →</p>-->
          <article-component
              v-bind:key="prevArticle.id"
              v-bind:article="prevArticle"
          ></article-component>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import ArticlesService from "@/api/ArticlesService";
import BookmarksService from "@/api/BookmarksService";
import VMdEditor from '@kangc/v-md-editor';
import ShimmerBlock from "@/components/ShimmerBlock";
import ArticleComponent from "@/components/ArticleComponent";

export default {
  name: "Article",
  components: {
    ShimmerBlock,
    VMdEditor,
    ArticleComponent
  },
  data() {
    return {
      inBookmarks: false,
      loadingArticle: false,
      article: '',

      nextArticle: undefined,
      prevArticle: undefined,
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    htmlPublicationDate: function () {
      return this.article.publicationDate;
    }
  },
  methods: {
    getFinePublicationDate: function (publicationDate) {
      return moment(Date.parse(publicationDate)).format("DD.MM.YYYY HH:mm");
    },
    makeQuote: function (citedText) {
      let citedArticleUrl = window.location.href;
      let citedArticleTitle = document.getElementById("article-title").innerText;

      if (citedArticleUrl.endsWith('/')) {
        citedArticleUrl = citedArticleUrl.slice(0, -1);
      }

      let quote = `
>${citedText}
>
><a target="_blank" href="${citedArticleUrl}/#:~:text=${encodeURIComponent(citedText)}"><i>- ${citedArticleTitle}</i></a></p>
`;

      navigator.clipboard.writeText(quote).then(() => alert('Quote copied to clipboard'));
    },
    editBookmarks: function (action) {
      if (this.currentUser === null) {
        this.$router.push('/login');
      }

      BookmarksService.editBookmark(this.$route.params.articleId, action)
          .then(response => {
            console.log(response);
            this.inBookmarks = !this.inBookmarks;
          }).catch(err => {
        console.log(err);
      });
    },
    loadIsInBookmarks: function (articleId) {
      BookmarksService.isArticleInBookmarksOfUser(articleId)
          .then(response => {
            this.inBookmarks = response.data;
          }).catch(err => {
        console.log(err);
        this.inBookmarks = false;
      });
    },
    loadArticleContent: function (articleId) {
      this.loadingArticle = true;
      ArticlesService.getArticleForRender(articleId)
          .then(response => {
            this.article = response.data;
            this.loadingArticle = false;
            // this.article.content = xss.process(VMdEditor.vMdParser.themeConfig.markdownParser.render(this.article.content));
            document.title = this.article.title;
          })
          .catch(err => {
            console.log(err);
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
    getNextArticle: function (articleId) {
      ArticlesService.getNextArticle(articleId)
          .then(response => {
            this.nextArticle = response.data;
          })
          .catch(err => {
            console.log(err)
          })
    },
    getPrevArticle: function (articleId) {
      ArticlesService.getPrevArticle(articleId)
          .then(response => {
            this.prevArticle = response.data;
          })
          .catch(err => {
            console.log(err)
          })
    },
    addShortcutsForSuggestions: function () {

    }
  },
  created() {
    let articleId = this.$route.params.articleId;
    this.loadIsInBookmarks(articleId);
    this.loadArticleContent(articleId);
    this.getNextArticle(articleId);
    this.getPrevArticle(articleId);
  },
  mounted() {
    let articleContent = document.getElementById('article-content');
    let control = document.getElementById('control');

    let selectedString = '';

    articleContent.addEventListener('mouseup', () => {
      let selectedText = document.getSelection();

      if (!selectedText.toString()) {
        control.style.display = 'none';
        return;
      }

      let rect = selectedText.getRangeAt(0).getBoundingClientRect();

      control.style.top = `calc(${rect.top}px - 40px)`;
      control.style.left = `calc(${rect.left}px + ${rect.width}px / 2 - 45px)`;
      control.style.display = 'block';

      selectedString = selectedText.toString();
    });

    let makeQuoteFunction = this.makeQuote; // should be outside eventListener, because of 'this' belongs to 'control'

    control.addEventListener('mouseup', function () {
      makeQuoteFunction(selectedString);
      control.style.display = 'none';
    });

    this.addShortcutsForSuggestions();
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>

hr {
  max-width: unset !important;
}

@media screen and (max-width: 720px) {
  .article-page-content {
  }
}

@media screen and (min-width: 721px) {
  .bookmarking {
    padding: 12pt 7pt 0 0;
  }

  .article-page-content {
    padding: 0 60pt 10pt 60pt;
  }
}

button {
  background: white;
  display: block;
  border: none;
  outline: none;
}

.bookmarking, .article-title {
  display: inline-block;
}

.bookmarking {
  position: relative;
  float: right;
}

.article-title {
  overflow-wrap: break-word;
}

#article-title {
  font-weight: bold;
}

.article-about {
  justify-content: space-between;
}

.control {
  text-align: center;
  border-radius: 9px;
  display: none;
  position: absolute;
  height: 35px;
  width: 100px;
  background: #DDD;
}

.control:hover {
  cursor: pointer;
}

.article-publication-date {
  font-size: 12px;
  padding-right: 7pt;
}

.article-publication-date, .tags-container {
  display: inline-block;
}

.tags-list {
  padding-left: 0;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
  display: inline-block;
  position: relative;
  margin: 0 10pt 0 0;
}

.tag-item > a, .article-publication-date {
  color: #666;
}

.article-content-wrapper {
  padding-bottom: 60pt;
}

.suggestions {
  display: flex;
}

.next-article, .prev-article {
  float: left;
  margin-right: 40pt;
}

.suggestion-name {
  font-size: 12px;
}
</style>