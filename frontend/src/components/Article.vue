<template>
  <div class="article-page-content">
    <div class="article-about row">
      <div class="article-author">
        <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
      </div>
      <div class="article-publication-date">
        <p>{{ finePublicationDate }}</p>
      </div>
    </div>
    <div class="title-bookmark row">
      <div class="article-title">
        <h1 id="article-title">{{  article.title }}</h1>
      </div>
      <div class="bookmarking">
        <div v-if="!inBookmarks" title="Add to bookmarks">
          <button @click="editBookmarks('add')">❤</button>
        </div>
        <div v-if="inBookmarks" title="Remove from bookmarks">
          <button @click="editBookmarks('remove')">💔</button>
        </div>
      </div>
    </div>
    <div v-html="article.content" class="article-content" id="article-content"></div>
    <div class="control" id="control">
      <p id="quote">💬 Quote</p>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import ArticlesService from "@/services/ArticlesService";
import BookmarksService from "@/services/BookmarksService";

export default {
  name: "Article",
  data() {
    return {
      inBookmarks: false,
      article: ''
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    finePublicationDate: function() {
      return moment(Date.parse(this.article.publicationDate)).format("DD.MM.YYYY HH:mm")
    }
  },
  created() {
    let articleId = this.$route.params.articleId;

    BookmarksService.isArticleInBookmarksOfUser(articleId)
        .then(response => {
          this.inBookmarks = response.data;
        }).catch(err => {
          console.log(err);
          this.inBookmarks = false;
        });

    ArticlesService.getArticleById(articleId)
        .then(response => {
          this.article = response.data;
          document.title = this.article.title;
        })
        .catch(err => {
          console.log(err);
          this.$router.replace('/error'); // redirecting to '/error'
        });
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
  methods: {
    makeQuote: function(citedText) {
      let citedArticleUrl = window.location.href;
      let citedArticleTitle = document.getElementById("article-title").innerText;

      if (citedArticleUrl.endsWith('/')) {
        citedArticleUrl = citedArticleUrl.slice(0, -1);
      }

      let quote = `
<figure>
  <blockquote>
    <q>${citedText}</q>
  </blockquote>
  <p>- <a target="_blank" href="${citedArticleUrl}/#:~:text=${encodeURIComponent(citedText)}"><i>${citedArticleTitle}</i></a></p>
</figure>`;

      console.log(quote);

      navigator.clipboard.writeText(quote).then(() => console.log('Quote copied to clipboard'));
    },
    editBookmarks: function(action) {
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
    }
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>
.article-author {
  margin: 0 0 10pt;
}

button {
  background: white;
  display: block;
  border: none;
  outline: none;
}

.title-bookmark {
  padding: 0 10pt 0;
  vertical-align: center;
  justify-content: space-between;
}

.bookmarking, .article-title {
  display: inline-block;
}

.article-title {
  overflow-wrap: break-word;
}

.article-about {
  padding: 0 10pt 0;
  justify-content: space-between;
}

.control {
  text-align: center;
  border-radius: 10px;
  display: none;
  position: absolute;
  height: 35px;
  width: 100px;
  background: #DDD;
}

.control:hover {
  cursor: pointer;
}

.article-page-content {
  background: white;
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 10px;
  margin: 0 0 10pt;
  padding: 20pt 20pt 10pt 40pt;
}
</style>