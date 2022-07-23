<template>
  <div class="profile-page-content" v-show="currentUser !== null">
    <div class="avatar-wrapper" v-if="!avatarString">
      <img src="@/assets/images/avatar-empty.webp" alt="Avatar"/>
    </div>
    <div class="avatar-wrapper" v-if="avatarString">
      <img :src="avatarString" alt="Avatar"/>
    </div>
    <div class="header row">
      <div>
        <h1 id="username">{{ currentUser.username }}</h1>
      </div>
      <div class="profile-header-buttons">
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
        <button class="button button-outline" @click="editUserData">
          <span class="button-icon"><img loading="eager" src="https://img.icons8.com/material/24/000000/edit--v1.png" alt="Edit profile info"/></span>
          <span class="button-message">Edit my info</span>
        </button>
      </div>
      <div class="user-data">
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
              <label><strong>Email:</strong></label>
              <Field id="email" name="email" type="email" class="form-control" v-model="email"/>
              <ErrorMessage name="email" class="error-feedback"/>
            </div>
            <div class="form-group">
              <label><strong>Avatar:</strong></label>
              <Field name="avatar" type="file" class="form-control">
                <input id="avatar" type="file" accept="image/*" @change="setAvatarString" class="form-control"/>
              </Field>
              <ErrorMessage name="avatar" class="error-feedback"/>
            </div>
            <div>
              <label><strong>Bio:</strong></label>
              <div>
                <textarea id="bio" v-model="bio" maxlength="255"></textarea>
              </div>
            </div>
            <div class="form-group">
              <label>Enter current password to submit changes: </label>
              <Field id="pswCurrent" type="password" name="password" class="form-control" placeholder="Password" v-model="password"/>
            </div>
            <div class="form-group">
              <button class="button button-primary" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                <span>Save</span>
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
              <Field id="pswOld" type="password" name="passwordOld" class="form-control" placeholder="Password" v-model="currentPassword"/>
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
              <button class="button button-primary">
                <span>Save</span>
              </button>
            </div>
          </Form>
        </div>
      </div>
    </div>
    <div class="articles-bookmarks accordion">
      <div class="accordion__intro bookmarks-title">
        <h2>Bookmarks</h2>
        <hr align="left">
      </div>
      <div v-if="loadingBookmarks">
        <ShimmerBlock/>
      </div>
      <div v-if="!loadingBookmarks" class="accordion__content">
        <div v-show="bookmarks.length > 0">
          <article-component
              v-for="article in bookmarks"
              :key="article.id"
              :article="article"
              :actions="actionsForBookmarks"
          ></article-component>
        </div>
        <div v-show="bookmarks.length === 0">
          <p>No bookmarks</p>
        </div>
      </div>
    </div>
    <div class="articles-block accordion">
      <div class="accordion__intro">
        <h2>My articles</h2>
        <hr align="left">
      </div>
      <div v-if="loadingArticles">
        <ShimmerBlock/>
      </div>
      <div v-if="!loadingArticles" class="accordion__content">
        <div class="articles-types">
          <button v-bind:class="getButtonClass('all')"  @click="resetArticlesState('all')">All</button>
          <button v-bind:class="getButtonClass('published')" @click="resetArticlesState('published')">Published</button>
          <button v-bind:class="getButtonClass('drafts')" @click="resetArticlesState('drafts')">Drafts</button>
        </div>
        <div class="articles">
          <div v-show="articles.length > 0">
            <article-component
                v-for="article in articles"
                :key="article.id"
                :article="article"
                :actions="actionsForMyArticles"
                :state="article.state"
            ></article-component>
          </div>
          <div v-show="articles.length === 0">
            <p>No articles</p>
          </div>
        </div>
        <div class="navigation-buttons">
          <div class="navigation-button-prev" title="Previous">
            <button class="navigation-button" @click="getPreviousArticlesPage" :disabled="!hasArticlesBefore">◀</button>
          </div>
          <div class="navigation-button-next" title="Next">
            <button class="navigation-button" @click="getNextArticlesPage" :disabled="!hasArticlesAfter">▶</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleComponent from "@/components/ArticleComponent";
import ArticlesService from "@/api/ArticlesService";
import BookmarksService from "@/api/BookmarksService";
import * as pswChecker from '@/assets/js/password_check'
import UserService from "@/api/UserService";
import AuthorsService from "@/api/AuthorsService";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import * as accordion from "@/assets/js/accordion";
import ShimmerBlock from "@/components/ShimmerBlock";
import AvatarsService from "@/api/AvatarsService";

export default {
  name: 'Profile',
  components: {
    ShimmerBlock,
    ArticleComponent,
    ArticleContainer: ArticleComponent,
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
          .max(40, "Must be maximum 40 characters!")
    });

    return {
      loadingBookmarks: false,
      bookmarks: [],

      loadingArticles: true,
      articlesState: 'all',
      articles: [],
      hasArticlesBefore: false,
      hasArticlesAfter: false,
      articlesLimit: 5,
      articlesOffset: 0,

      avatarString: undefined,
      avatarFile: undefined,
      email: undefined,
      bio: undefined,
      password: undefined,

      currentPassword: undefined,
      passwordNew: undefined,
      passwordNewConfirm: undefined,

      loading: false,

      showUserData: true,

      actionsForBookmarks: [
        { function: this.removeFromBookmarks, icon: '<img loading="eager" src="https://img.icons8.com/material/24/000000/trash--v1.png" alt="Delete"/>', message: 'Remove from bookmarks' }
      ],

      actionsForMyArticles: [
        { function: this.shareArticle, icon: '<img loading="eager" src="https://img.icons8.com/material/24/000000/link--v1.png"/>', message: 'Share' },
        { function: this.editArticle, icon: '<img loading="eager" src="https://img.icons8.com/material-outlined/24/000000/edit--v1.png" alt="Edit"/>', message: 'Edit' },
        { function: this.removeArticle, icon: '<img loading="eager" src="https://img.icons8.com/material/24/000000/trash--v1.png" alt="Delete"/>', message: 'Remove' },
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
    toBase64: function (file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
      });
    },
    setAvatarString: async function ({ target: { files }}) {
      this.avatarString = await this.toBase64(files[0]);
    },
    submitChangesBaseInfo: function () {
      this.showUserData = !this.showUserData;
      UserService.editUser(this.currentUser.id, this.password, {
        avatar: this.avatarString,
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
        UserService.editUser(this.currentUser.id, this.currentPassword, {
          passwordNew: this.passwordNew
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
    getMyAvatar: function () {
      AvatarsService.getUserAvatar(this.currentUser.username)
          .then(response => {
            this.avatarString = response.data;
          })
          .catch(err => {
            console.log(err);
          });
    },
    getMyBio: function() {
      AuthorsService.getAuthorData(this.currentUser.username, 1, 0)
          .then(response => {
            this.bio = response.data.bio;
          })
          .catch(err => {
            console.log(err);
          });
    },
    getMyBookmarks: function () {
      this.loadingBookmarks = true;
      BookmarksService.getBookmarksOfUser()
          .then(response => {
            this.bookmarks = response.data;
            this.loadingBookmarks = false;
          })
          .catch(err => {
            console.log(err);
            this.bookmarks = [];
          });
    },

    resetArticlesState: function (state) {
      this.articlesState = state;
      this.articlesOffset = 0;
      this.getArticlesPaginated(this.articlesState, this.articlesLimit, this.articlesOffset);
    },
    getPreviousArticlesPage: function () {
      this.articlesOffset -= this.articlesLimit;
      this.getArticlesPaginated(this.articlesState, this.articlesLimit, this.articlesOffset);
    },
    getNextArticlesPage: function () {
      this.articlesOffset += this.articlesLimit;
      this.getArticlesPaginated(this.articlesState, this.articlesLimit, this.articlesOffset);
    },
    getArticlesPaginated: function (state, limit, offset) {
      this.loadingArticles = true;
      ArticlesService.getMyArticlesPaginated(state, limit, offset)
          .then(response => {
            this.articles = response.data.entities.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first
            this.loadingArticles = false;
            this.hasArticlesBefore = response.data.hasBefore;
            this.hasArticlesAfter = response.data.hasAfter;
          })
          .catch(e => {
            console.log(e);
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
            this.resetArticlesState('all');
          })
          .catch(err => {
            console.log(err);
          });
    },
    shareArticle: function (articleId) {
      let link = window.location.origin + '/articles/share/' + articleId;
      navigator.clipboard.writeText(link).then(() => alert('Link copied to clipboard'));
    },
    getButtonClass(buttonTitle) {
      if (buttonTitle === this.articlesState) {
        return 'article-type button button-primary';
      } else {
        return 'article-type button button-outline';
      }
    }
  },
  created() {
    this.$nextTick(pswChecker.passwordChecking);
    this.$nextTick(accordion.setupAccordion);
  },
  mounted() {
    this.email = this.currentUser.email;
    this.getMyAvatar();
    this.getMyBio();
    this.getMyBookmarks();
    this.getArticlesPaginated('all', this.articlesLimit, this.articlesOffset);
  },
};
</script>

<style scoped>
@import "../assets/css/accordion_style.css";
@import "../assets/css/password_checking_style.css";

@media screen and (max-width: 720px) {
  .avatar-wrapper > img {
    object-fit: cover;
    border-radius: 50%;
    width: 60pt;
    height: 60pt;
  }
}


@media screen and (min-width: 720px) {
  .avatar-wrapper > img {
    object-fit: cover;
    border-radius: 50%;
    width: 120pt;
    height: 120pt;
  }
}

.header {
  justify-content: space-between;
  margin: 10pt 0 10pt;
}

#username {
  font-weight: bold;
}

.header-admin-button, .header-logout-button {
  margin: 0 10pt 0;
  display: inline-block;
}

.form-group {
  max-width: 300pt;
}

textarea {
  padding: 5pt;
  border-radius: 9px;
  width: 100%;
  max-width: 300pt;
  height: 100pt;
  resize: none;
}

.form-control#avatar {
  height: fit-content;
}

.bio {
  white-space: pre-line;
  padding: 0 0 5pt;
  max-width: 700pt;
}

.bookmarks-title {
  padding: 0 0 10pt 0;
}

.articles-types {
  padding: 5pt 0 20pt 0;
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