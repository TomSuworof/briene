<template>
  <div class="personal-area-page-content" v-if="currentUser !== null">
    <div class="header row">
      <div>
        <h1>Personal area</h1>
      </div>
      <div class="header-buttons">
        <div v-if="isAdmin" class="header-admin-button">
          <router-link to="/admin">Admin</router-link>
        </div>
        <div class="header-logout-button">
          <a href="#" @click="logout">Logout</a>
        </div>
      </div>
    </div>
    <div class="user-data-action-edit">
      <a @click="editUserData" href="#">🖊 Edit my info</a>
    </div>
    <div class="user-data">
      <p>
        <strong>Username:</strong>
        {{currentUser.username}}
      </p>
      <p>
        <strong>Email:</strong>
        {{currentUser.email}}
      </p>
    </div>
    <div class="articles-bookmarks">
      <div>
        <h2>Bookmarks</h2>
      </div>
      <div v-if="bookmarks.length > 0">
        <div class="article-container" v-for="article in bookmarks" v-bind:key="article.id">
          <div class="article-title-container">
            <h3 class="article-title">
              <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
            </h3>
          </div>
          <div class="article-action-container">
            <a @click="removeFromBookmarks(article.id)" href="#">❌ Remove from bookmarks</a>
          </div>
        </div>
      </div>
      <div v-else-if="bookmarks.length === 0">
        <p>No bookmarks</p>
      </div>
    </div>
    <div class="articles-block">
      <div>
        <h2>Articles</h2>
      </div>
      <div class="articles-types">
        <a href="#" class="article-type" @click="getMyArticles('all')">All</a>
        <a href="#" class="article-type" @click="getMyArticles('published')">Published</a>
        <a href="#" class="article-type" @click="getMyArticles('drafts')">Drafts</a>
      </div>
      <div v-if="articles.length > 0">
        <div class="article-container" v-for="article in articles" v-bind:key="article.id">
          <div class="article-title-container">
            <h3 class="article-title">
              <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
            </h3>
          </div>
          <div class="article-action-container">
            <div class="article-action-edit">
              <a @click="editArticle(article.id)" href="#">🖊 Edit</a>
            </div>
            <div class="article-action-remove">
              <a @click="removeArticle(article.id)" href="#">❌ Remove</a>
            </div>
          </div>
        </div>
      </div>
      <div v-else-if="articles.length === 0">
        <p>No articles</p>
      </div>
    </div>
  </div>
</template>

<script>
import ArticlesService from "@/services/ArticlesService";
import BookmarksService from "@/services/BookmarksService";

export default {
  name: 'Profile',
  data() {
    return {
      bookmarks: [],
      articles: [],
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdmin() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_ADMIN');
      }
      return false;
    }
  },
  methods: {
    logout: function() {
      this.$store.dispatch("auth/logout");
      this.$router.push('/login');
    },
    editUserData: function() {
      alert('Not implemented');
    },
    getMyBookmarks: function() {
      BookmarksService.getBookmarksOfUser(this.currentUser.token)
          .then(response => {
            this.bookmarks = response.data;
          })
          .catch(err => {
            console.log(err);
            this.bookmarks = [];
          });
    },
    getMyArticles: function(state) {
      ArticlesService.getMyArticles(state, this.currentUser.token)
          .then(response => {
            this.articles = response.data;
          })
          .catch(err => {
            console.log(err);
            this.articles = [];
          });
    },
    removeFromBookmarks: function(articleId) {
      BookmarksService.editBookmark(articleId, 'remove', this.currentUser ? this.currentUser.token : undefined)
          .then(() => {
            this.getMyBookmarks();
          })
          .catch(err => {
          console.log(err);
      });
    },
    editArticle: function(articleId) {
      this.$router.push(`/article_editor?articleId=${articleId}`);
    },
    removeArticle: function (articleId) {
      ArticlesService.delete(articleId, this.currentUser.token)
          .then(() => {
            this.getMyArticles('all');
          })
          .catch(err => {
            console.log(err);
          });
    }
  },
  created() {
    this.getMyBookmarks();
    this.getMyArticles('all');
  },
  beforeMount() {
    if (this.currentUser === null) {
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.header {
  justify-content: space-between;
  margin: 0 0 50pt;
}

.header-admin-button, .header-logout-button {
  margin: 0 10pt 0;
  display: inline-block;
}

.article-type {
  margin: 0 20pt 0 0;
}

.article-action-container, .article-title-container, .article-action-edit, .article-action-remove {
  display: inline-block;
  margin: 0 20pt 0 0;
}

.articles-bookmarks, .articles-block {
  margin: 50pt 0 0;
}

.article-container {
  margin: 0 0 10pt;
}

.article-title {
  overflow-wrap: break-word;
}
</style>