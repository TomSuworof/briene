<template>
  <div class="profile-page-content" v-if="currentUser !== null">
    <div class="header row">
      <div>
        <h1>Profile</h1>
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
    <div v-if="showUserData" class="show-user-basic-data">
      <div class="user-data-action-edit">
        <a @click="editUserData" href="#">🖊 Edit my info</a>
      </div>
      <div class="user-data">
        <p>
          <strong>Username: </strong>{{ currentUser.username }}
        </p>
        <p>
          <strong>Email: </strong>{{ currentUser.email }}
        </p>
        <p>
          <strong>Bio: </strong>
        </p>
        <div class="bio">
          <p>{{ bio }}</p>
        </div>
      </div>
    </div>
    <div v-if="showEditUserData" class="edit-user-data">
      <div class="edit-user-basic-data">
        <h2>Basic information</h2>
        <div class="user-data">
          <form method="post">
            <div>
              <label for="username"><strong>Username: </strong></label>
              <input id="username" type="text" disabled v-model="currentUser.username"/>
            </div>
            <div>
              <label for="email"><strong>Email: </strong></label>
              <input id="email" type="email" v-model="email"/>
            </div>
            <div>
              <label><strong>Bio: </strong></label>
              <div>
                <textarea id="bio" v-model="bio"></textarea>
              </div>
            </div>
            <div>
              <div>
                <label for="password">Enter current password to submit changes: </label>
              </div>
              <input id="password" type="password" v-model="password" required placeholder="Current password">
            </div>
            <div>
              <input @click="submitChangesBaseInfo" type="button" value="Submit">
            </div>
          </form>
        </div>
      </div>
      <div class="edit-user-password">
        <h2>Updating password</h2>
        <div class="user-data">
          <form method="post">
            <div>
              <input type="password" name="passwordOld" v-model="password" required placeholder="Current password"/>
            </div>
            <div class="setting-password">
              <div>
                <input id="psw" type="password" v-model="passwordNew" placeholder="New password" required
                       pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                       title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters">
              </div>
              <div class="container-for-message">
                <div id="message" class="message">
                  <p class="must-contain">Password must contain the following:</p>
                  <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
                  <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
                  <p id="number" class="invalid">A <b>number</b></p>
                  <p id="length" class="invalid">Minimum <b>8 characters</b></p>
                </div>
              </div>
              <div>
                <input id="pswConfirm" type="password" v-model="passwordNewConfirm"
                       placeholder="Confirm your new password" required>
                <div id="password-matching" class="password-matching">
                  <p id="match" class="invalid">Passwords match</p>
                </div>
              </div>
            </div>
            <div>
              <button @click="submitChangesPassword">Submit</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <div class="articles-bookmarks">
      <div>
        <h2>Bookmarks</h2>
      </div>
      <div v-if="bookmarks.length > 0">
        <article-container
            v-for="article in bookmarks"
            v-bind:key="article.id"
            v-bind:article="article"
            v-bind:actions="actionsForBookmarks"
        ></article-container>
      </div>
      <div v-else-if="bookmarks.length === 0">
        <p>No bookmarks</p>
      </div>
    </div>
    <div class="articles-block">
      <div>
        <h2>My articles</h2>
      </div>
      <div class="articles-types">
        <a href="#" class="article-type" @click="getMyArticles('all')">All</a>
        <a href="#" class="article-type" @click="getMyArticles('published')">Published</a>
        <a href="#" class="article-type" @click="getMyArticles('drafts')">Drafts</a>
      </div>
      <div v-if="articles.length > 0">
        <article-container
            v-for="article in articles"
            v-bind:key="article.id"
            v-bind:article="article"
            v-bind:actions="actionsForMyArticles"
        ></article-container>
      </div>
      <div v-else-if="articles.length === 0">
        <p>No articles</p>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleContainer from "@/components/ArticleContainer";
import ArticlesService from "@/services/ArticlesService";
import BookmarksService from "@/services/BookmarksService";
import * as pswChecker from '../../static/js/password_check'
import UserService from "@/services/UserService";
import AuthorsService from "@/services/AuthorsService";

export default {
  name: 'Profile',
  components: {
    ArticleContainer
  },
  data() {
    return {
      bookmarks: [],
      articles: [],

      password: undefined,
      email: undefined,
      bio: undefined,
      passwordNew: undefined,
      passwordNewConfirm: undefined,

      showUserData: true,

      actionsForBookmarks: [
        {function: this.removeFromBookmarks, message: '❌ Remove from bookmarks'}
      ],

      actionsForMyArticles: [
        {function: this.editArticle, message: '🖊 Edit'},
        {function: this.removeArticle, message: '❌ Remove'},
      ],
    }
  },
  computed: {
    showEditUserData() {
      return !this.showUserData;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdmin() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_ADMIN');
      }
      return false;
    },
  },
  methods: {
    logout: function () {
      this.$store.dispatch("auth/logout");
      this.$router.push('/login');
    },
    editUserData: function () {
      this.showUserData = !this.showUserData;
      this.$nextTick(pswChecker.passwordChecking); // waits for rendering
    },
    submitChangesBaseInfo: function () {
      this.showUserData = !this.showUserData;
      UserService.editUser(this.currentUser.id, this.password, {
        email: this.email,
        bio: this.bio,
      })
          .then(response => {
            console.log(response);
            this.logout();
          })
          .catch(err => {
            console.log(err);
          });
    },
    submitChangesPassword: function () {
      this.showUserData = !this.showUserData;

      if (pswChecker.checkFields()) {
        UserService.editUser(this.currentUser.id, this.password, {
          passwordNew: this.passwordNew,
        })
            .then(response => {
              console.log(response);
              this.logout();
            })
            .catch(err => {
              console.log(err);
            });
      }
    },
    getMyBio: function() {
      AuthorsService.getAuthorData(this.currentUser.username)
          .then(response => {
            this.bio = response.data.bio;
          })
          .catch(err => {
            console.log(err);
          });
    },
    getMyBookmarks: function () {
      BookmarksService.getBookmarksOfUser()
          .then(response => {
            this.bookmarks = response.data;
          })
          .catch(err => {
            console.log(err);
            this.bookmarks = [];
          });
    },
    getMyArticles: function (state) {
      ArticlesService.getMyArticles(state)
          .then(response => {
            this.articles = response.data;
          })
          .catch(err => {
            console.log(err);
            this.articles = [];
          });
    },
    removeFromBookmarks: function (articleId) {
      BookmarksService.editBookmark(articleId, 'remove')
          .then(() => {
            this.getMyBookmarks();
          })
          .catch(err => {
            console.log(err);
          });
    },
    editArticle: function (articleId) {
      this.$router.push(`/article_editor?articleId=${articleId}`);
    },
    removeArticle: function (articleId) {
      ArticlesService.delete(articleId)
          .then(() => {
            this.getMyArticles('all');
          })
          .catch(err => {
            console.log(err);
          });
    },
  },
  created() {
    this.email = this.currentUser.email;
    this.getMyBio();
    this.getMyBookmarks();
    this.getMyArticles('all');
  },
};
</script>

<style scoped>
@import "../../static/css/password_checking_style.css";

.header {
  justify-content: space-between;
  margin: 0 0 50pt;
}

.header-admin-button, .header-logout-button {
  margin: 0 10pt 0;
  display: inline-block;
}

#bio {
  width: 100%;
  height: 100pt;
}

.article-type {
  margin: 0 20pt 0 0;
}

.articles-types {
  padding: 5pt 0;
}

.show-user-basic-data,
.edit-user-data,
.edit-user-basic-data,
.edit-user-password,
.articles-bookmarks,
.articles-block {
  margin: 50pt 0 0;
}
</style>