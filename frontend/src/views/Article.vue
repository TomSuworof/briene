<template>
  <div class="article-page-content">
    <div class="article-about row">
      <div class="article-author">
        <router-link :to="'/authors/' + article.author">{{ article.author }}</router-link>
      </div>
      <div class="article-publication-date">
        <time :datetime="htmlPublicationDate">{{ finePublicationDate }}</time>
      </div>
    </div>
    <div class="title">
      <div class="article-title">
        <h1 id="article-title">{{ article.title }}</h1>
      </div>
      <div class="bookmarking">
        <div v-if="!inBookmarks" title="Add to bookmarks">
          <button @click="editBookmarks('add')">
            <img loading="eager" src="https://img.icons8.com/ios/35/000000/bookmark-ribbon--v1.png"
                 alt="Add to bookmarks icon"/>
          </button>
        </div>
        <div v-if="inBookmarks" title="Remove from bookmarks">
          <button @click="editBookmarks('remove')">
            <img loading="eager" src="https://img.icons8.com/ios-filled/35/000000/bookmark-ribbon.png"
                 alt="Remove from bookmarks icon"/>
          </button>
        </div>
      </div>
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
</template>

<script>
import moment from 'moment';
import ArticlesService from "@/api/ArticlesService";
import BookmarksService from "@/api/BookmarksService";
import VMdEditor from '@kangc/v-md-editor';
import ShimmerBlock from "@/components/ShimmerBlock";

export default {
  name: "Article",
  components: {
    ShimmerBlock,
    VMdEditor
  },
  data() {
    return {
      inBookmarks: false,
      loadingArticle: false,
      article: ''
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    finePublicationDate: function () {
      return moment(Date.parse(this.article.publicationDate)).format("DD.MM.YYYY HH:mm");
    },
    htmlPublicationDate: function () {
      return this.article.publicationDate;
    }
  },
  methods: {
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
    }
  },
  created() {
    let articleId = this.$route.params.articleId;
    this.loadIsInBookmarks(articleId);
    this.loadArticleContent(articleId);
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
    document.title = 'Briene';
  }
}
</script>

<style scoped>

hr {
  max-width: unset !important;
}

@media screen and (max-width: 720px) {
  /* comes into effect for screens less than 720 pixels (inclusive) */
  .article-about {
    padding: 0 0 0 12pt;
  }

  .article-page-content {
  }
}

@media screen and (min-width: 721px) {
  /* comes into effect for screens larger than or equal to 481 pixels */
  .bookmarking {
    padding: 12pt 7pt 0 0;
  }

  .article-about {
    padding: 0 0 0 12pt;
  }

  .article-page-content {
    background: white;
    box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
    border-radius: 9px;
    margin: 0 0 10pt;
    padding: 30pt 60pt 10pt 60pt;
  }
}

:deep(#article-content) img, iframe {
  box-shadow: rgba(0, 0, 0, 0.5) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
}

.article-author {
  margin: 0 0 10pt;
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

.tags-container {
  padding: 5pt 0 0 0;
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

</style>