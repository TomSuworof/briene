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
    <div class="title">
      <div class="article-title">
        <h1 id="article-title">{{ article.title }}</h1>
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
    <div class="article-tags tags-container">
      <ul class="tags-list">
        <li class="tag-item" v-for="tag in article.tags">
          <div>
            <a :href="'/tags/' + tag">#{{ tag }}</a>
          </div>
        </li>
      </ul>
    </div>
    <hr/>
    <div id="article-content">
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

export default {
  name: "Article",
  components: {
    VMdEditor
  },
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
    finePublicationDate: function () {
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

    ArticlesService.getArticleForRender(articleId)
        .then(response => {
          this.article = response.data;
          // this.article.content = xss.process(VMdEditor.vMdParser.themeConfig.markdownParser.render(this.article.content));
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
    makeQuote: function (citedText) {
      let citedArticleUrl = window.location.href;
      let citedArticleTitle = document.getElementById("article-title").innerText;

      if (citedArticleUrl.endsWith('/')) {
        citedArticleUrl = citedArticleUrl.slice(0, -1);
      }

      let quote = `
<figure>
  <blockquote>
    ${citedText}
  </blockquote>
  <p>- <a target="_blank" href="${citedArticleUrl}/#:~:text=${encodeURIComponent(citedText)}"><i>${citedArticleTitle}</i></a></p>
</figure>`;

      navigator.clipboard.writeText(quote).then(() => console.log('Quote copied to clipboard'));
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
    }
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>

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
    padding: 20pt 20pt 10pt 40pt;
  }
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
  display: inline-block;
  margin: 0 5pt 5pt 0;
  padding: 5pt;
  border-radius: 9px;
  border: 1px solid;
  position: relative;
}
</style>