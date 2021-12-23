<template>
  <div class="profile-page-content" v-show="currentUser !== null">
    <div class="header row">
      <div>
        <h1>Profile</h1>
      </div>
      <div class="header-buttons">
        <div v-show="isAdmin" class="header-admin-button">
          <router-link to="/admin">Admin</router-link>
        </div>
        <div class="header-logout-button">
          <a href="#" @click="logout">Logout</a>
        </div>
      </div>
    </div>
    <div v-show="showUserData" class="show-user-basic-data">
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
    <div v-show="showEditUserData" class="edit-user-data">
      <div class="edit-user-basic-data">
        <h2>Basic information</h2>
        <div class="user-data">
          <Form @submit="submitChangesBaseInfo" method="post">
            <div class="form-group">
              <label><strong>Username:</strong></label>
              <Field id="username" name="username" type="text" class="form-control" :disabled="true" v-model="currentUser.username"/>
              <ErrorMessage name="username" class="error-feedback"/>
            </div>
            <div class="form-group">
              <label><strong>Email:</strong></label>
              <Field id="email" name="email" type="email" class="form-control" v-model="email"/>
              <ErrorMessage name="email" class="error-feedback"/>
            </div>
            <div>
              <label><strong>Bio:</strong></label>
              <div>
                <textarea id="bio" v-model="bio"></textarea>
              </div>
            </div>
            <div class="form-group">
              <label>Enter current password to submit changes: </label>
              <Field id="pswOld" type="password" name="password" class="form-control" placeholder="Password" v-model="password"/>
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-block" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                Save
              </button>
            </div>
          </Form>
        </div>
      </div>
      <div class="edit-user-password">
        <h2>Updating password</h2>
        <div class="user-data">
          <Form @submit="submitChangesPassword" method="post">
            <div class="form-group">
              <label>Password</label>
              <Field id="pswOld" type="password" name="passwordOld" class="form-control" placeholder="Password" v-model="password"/>
            </div>
            <div class="setting-password">
              <div class="form-group">
                <label>New password</label>
                <Field id="psw" type="password" name="password" class="form-control" placeholder="New password" required
                       pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                       title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
                       v-model="passwordNew"/>
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
              <div class="form-group">
                <Field id="pswConfirm" type="password" name="passwordConfirm" class="form-control"
                       placeholder="Confirm your password" required v-model="passwordNewConfirm"/>
                <div id="password-matching" class="password-matching">
                  <p id="match" class="invalid">Passwords match</p>
                </div>
              </div>
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-block">Save</button>
            </div>
          </Form>
        </div>
      </div>
    </div>
    <div class="articles-bookmarks">
      <div class="accordion">
        <h2>Bookmarks</h2>
        <hr/>
      </div>
      <div class="panel">
        <div v-show="bookmarks.length > 0">
          <article-container
              v-for="article in bookmarks"
              :key="article.id"
              :article="article"
              :actions="actionsForBookmarks"
          ></article-container>
        </div>
        <div v-show="bookmarks.length === 0">
          <p>No bookmarks</p>
        </div>
      </div>
    </div>
    <div class="articles-block">
      <div class="accordion">
        <h2>My articles</h2>
        <hr/>
      </div>
      <div class="panel">
        <div class="articles-types">
          <a href="#" class="article-type" @click="getMyArticles('all')">All</a>
          <a href="#" class="article-type" @click="getMyArticles('published')">Published</a>
          <a href="#" class="article-type" @click="getMyArticles('drafts')">Drafts</a>
        </div>
        <div v-show="articles.length > 0">
          <article-container
              v-for="article in articles"
              :key="article.id"
              :article="article"
              :actions="actionsForMyArticles"
              :state="article.state"
          ></article-container>
        </div>
        <div v-show="articles.length === 0">
          <p>No articles</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleContainer from "@/components/ArticleContainer";
import ArticlesService from "@/api/ArticlesService";
import BookmarksService from "@/api/BookmarksService";
import * as pswChecker from '@/assets/js/password_check'
import UserService from "@/api/UserService";
import AuthorsService from "@/api/AuthorsService";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import * as accordion from "@/assets/js/accordion";

export default {
  name: 'Profile',
  components: {
    ArticleContainer,
    Form,
    Field,
    ErrorMessage
  },
  data() {
    const schema = yup.object().shape({
      username: yup
          .string()
          .required("Username is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      email: yup
          .string()
          .required("Email is required!")
          .email("Email is invalid!")
          .max(50, "Must be maximum 50 characters!"),
      password: yup
          .string()
          .required("Password is required!")
          .min(8, "Must be at least 8 characters!")
          .max(40, "Must be maximum 40 characters!"),
      agreement: yup
          .bool()
          .oneOf([true])
          .required("You must agree to terms of use")
    });

    return {
      bookmarks: [],
      articles: [],

      password: undefined,
      email: undefined,
      bio: undefined,
      passwordNew: undefined,
      passwordNewConfirm: undefined,
      loading: false,

      showUserData: true,

      actionsForBookmarks: [
        {function: this.removeFromBookmarks, message: '❌ Remove from bookmarks'}
      ],

      actionsForMyArticles: [
        {function: this.editArticle, message: '🖊 Edit'},
        {function: this.removeArticle, message: '❌ Remove'},
      ],

      schema,
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
      this.loading = true;
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
            this.articles = response.data.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first;
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
    this.$nextTick(pswChecker.passwordChecking);
    this.$nextTick(accordion.setupAccordion);
  },
  mounted() {
    this.email = this.currentUser.email;
    this.getMyBio();
    this.getMyBookmarks();
    this.getMyArticles('all');
  },
};
</script>

<style scoped>
@import "../assets/css/accordion_style.css";
@import "../assets/css/password_checking_style.css";

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

.bio {
  white-space: pre-line;
  padding: 0 0 5pt;
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