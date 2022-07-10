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
    <div class="read-next-wrapper" v-if="nextArticle || prevArticle">
      <p><b>Read next</b></p>
      <div class="suggestions">
        <div class="next-article" v-if="nextArticle">
          <p class="suggestion-name">Ctrl ←</p>
          <article-component
              v-bind:key="nextArticle.id"
              v-bind:article="nextArticle"
          ></article-component>
        </div>
        <div class="prev-article" v-if="prevArticle">
          <p class="suggestion-name">Ctrl →</p>
          <article-component
              v-bind:key="prevArticle.id"
              v-bind:article="prevArticle"
          ></article-component>
        </div>
      </div>
    </div>
    <div class="suggestions-wrapper" v-if="suggestedArticles.length > 0">
      <p><b>Read more</b></p>
      <div class="suggestions">
        <article-component
            v-for="article in suggestedArticles"
            v-bind:key="article.id"
            v-bind:article="article"
        ></article-component>
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

      suggestedArticles: []
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

      BookmarksService.editBookmark(this.$route.params.articleUrl, action)
          .then(response => {
            console.log(response);
            this.inBookmarks = !this.inBookmarks;
          }).catch(err => {
        console.log(err);
      });
    },
    loadIsInBookmarks: function (articleUrl) {
      BookmarksService.isArticleInBookmarksOfUser(articleUrl)
          .then(response => {
            this.inBookmarks = response.data;
          }).catch(err => {
            console.log(err);
            this.inBookmarks = false;
          });
    },
    loadArticleContent: function (articleUrl) {
      this.loadingArticle = true;
      ArticlesService.getArticleForRender(articleUrl)
          .then(response => {
            this.article = response.data;
            this.loadingArticle = false;
            // this.article.content = xss.process(VMdEditor.vMdParser.themeConfig.markdownParser.render(this.article.content));
            this.loadMeta();
          })
          .catch(err => {
            console.log(err);
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
    getNextArticle: function (articleUrl) {
      ArticlesService.getNextArticle(articleUrl)
          .then(response => {
            this.nextArticle = response.data;
            this.addShortcutsForNextArticle(response.data.url);
          })
          .catch(err => {
            console.log(err);
          })
    },
    getPrevArticle: function (articleUrl) {
      ArticlesService.getPrevArticle(articleUrl)
          .then(response => {
            this.prevArticle = response.data;
            this.addShortcutsForPrevArticle(response.data.url);
          })
          .catch(err => {
            console.log(err);
          })
    },
    addShortcutsForNextArticle: function (articleUrl) {
      this.addShortcutsForSuggestion('ArrowLeft', articleUrl);
    },
    addShortcutsForPrevArticle: function (articleUrl) {
      this.addShortcutsForSuggestion('ArrowRight', articleUrl);
    },
    addShortcutsForSuggestion: function (key, articleUrl) {
      document.onkeydown = function (e) {
        if (e.ctrlKey && e.key === key) {
          window.location.href = window.location.origin + '/articles/' + articleUrl;
        }
      }
    },
    getSuggestedArticles: function (articleUrl) {
      ArticlesService.getSuggestedArticles(articleUrl)
          .then(response => {
            this.suggestedArticles = response.data;
          })
          .catch(err => {
            console.log(err);
          })
    },
    loadMeta: function () {
      document.title = this.article.title;

      const head = document.head;

      let metaOgUrl = document.createElement('meta');
      metaOgUrl.setAttribute('property', 'og:url');
      metaOgUrl.content = window.location.origin + window.location.pathname;
      head.appendChild(metaOgUrl);

      let metaOgTitle = document.createElement('meta');
      metaOgTitle.setAttribute('property', 'og:title');
      metaOgTitle.content = this.article.title;
      head.appendChild(metaOgTitle);

      let metaOgDescription = document.createElement('meta');
      metaOgDescription.setAttribute('property', 'og:description');
      metaOgDescription.content = this.article.summary;
      head.appendChild(metaOgDescription);

      let metaOgSiteName = document.createElement('meta');
      metaOgSiteName.setAttribute('property', 'og:site_name');
      metaOgSiteName.content = 'Briene';
      head.appendChild(metaOgSiteName);

      let metaDescription = document.createElement('meta');
      metaDescription.name = 'description'
      metaDescription.content = this.article.summary;
      head.appendChild(metaDescription);

      let metaKeywords = document.createElement('meta')
      metaKeywords.name = 'keywords';
      metaKeywords.content = this.article.tags.join(', ');
      head.appendChild(metaKeywords);

      let metaAuthor = document.createElement('meta')
      metaAuthor.name = 'author';
      metaAuthor.content = this.article.author;
      head.appendChild(metaAuthor);

      let metaRobots = document.createElement('meta')
      metaRobots.name = 'robots';
      metaRobots.content = 'index,follow'
      head.appendChild(metaRobots);

      // useMeta({
      //     'og:url': document.URL
      //     // { property: 'og:image', content: this.aws_url + '/users/' + this.userData.profileurl + '-main.jpg' }
      //   }
      // });
    },
    clearMeta: function () {
      document.title = 'Briene';
      document.querySelector("[property='og:url']").remove();
      document.querySelector("[property='og:title']").remove();
      document.querySelector("[property='og:description']").remove();
      document.querySelector("[property='og:site_name']").remove();
      document.querySelector("[name='description']").remove();
      document.querySelector("[name='keywords']").remove();
      document.querySelector("[name='author']").remove();
      document.querySelector("[name='robots']").remove();
    }
  },
  created() {
    let articleUrl = this.$route.params.articleUrl;
    this.loadIsInBookmarks(articleUrl);
    this.loadArticleContent(articleUrl);
    this.getNextArticle(articleUrl);
    this.getPrevArticle(articleUrl);
    this.getSuggestedArticles(articleUrl);
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
  },
  beforeUnmount() {
    this.clearMeta();
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