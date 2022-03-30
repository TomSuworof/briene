<template>
  <div class="admin-dashboard">
    <div class="admin-dashboard-users accordion">
      <div class="admin-dashboard-title accordion__intro">
        <h1>Users</h1>
        <hr align="left">
      </div>
      <div class="accordion__content">
        <div class="users">
          <table class="admin-dashboard-table table">
          <tr>
            <th class="user-column">ID</th>
            <th class="user-column">Username</th>
            <th class="user-column">Email</th>
            <th class="user-column">Roles</th>
            <th class="user-column-actions">Actions</th>
          </tr>
          <tr class="user-row" v-if="loadingUsers">
            <ShimmerBlock/>
          </tr>
          <tr class="user-row" v-if="!loadingUsers" v-for="user in users" v-bind:key="user.id">
            <td class="user-column">{{ user.id }}</td>
            <td class="user-column">{{ user.username }}</td>
            <td class="user-column">{{ user.email }}</td>
            <td class="user-column">
              <ul v-for="role in user.roles" v-bind:key="role.id">
                <li>{{ role.name }}</li>
              </ul>
            </td>
            <td class="user-column-actions">
              <div class="user-actions">
                <div class="user-action">
                  <button @click="editUser('make_admin', user.id)" class="button button-primary">Make admin</button>
                </div>
                <div class="user-action" v-show="user.roles.map(role => role.name).includes('ROLE_USER') ||
                    user.roles.map(role => role.name).includes('ROLE_ADMIN')">
                  <button @click="editUser('delete', user.id)" class="button button-danger">Delete (block)</button>
                </div>
                <div class="user-action" v-show="user.roles.map(role => role.name).includes('ROLE_BLOCKED')">
                  <button @click="editUser('make_user', user.id)" class="button button-success">Make user</button>
                </div>
              </div>
            </td>
          </tr>
        </table>
        </div>
        <div class="user-navigation-buttons">
          <div class="navigation-button-prev" title="Previous">
            <button class="navigation-button" @click="getPreviousUsersPage" :disabled="!hasUsersBefore">◀</button>
          </div>
          <div class="navigation-button-next" title="Next">
            <button class="navigation-button" @click="getNextUsersPage" :disabled="!hasUsersAfter">▶</button>
          </div>
        </div>
      </div>
    </div>
    <div class="admin-dashboard-articles accordion">
      <div class="accordion__intro">
        <h1>Articles</h1>
        <hr align="left">
      </div>
      <div class="accordion__content">
        <div class="articles-types">
          <button v-bind:class="getButtonClass('all')" @click="resetArticlesState('all')">All</button>
          <button v-bind:class="getButtonClass('published')" @click="resetArticlesState('published')">Published</button>
          <button v-bind:class="getButtonClass('drafts')" @click="resetArticlesState('drafts')">Drafts</button>
        </div>
        <div class="articles">
          <div v-if="loadingArticles">
            <ShimmerBlock/>
          </div>
          <div v-if="!loadingArticles">
            <div v-if="articles.length > 0">
              <article-component
                  v-for="article in articles"
                  :key="article.id"
                  :article="article"
                  :actions="actions"
                  :state="article.state"
              ></article-component>
            </div>
            <div v-else-if="articles.length === 0">
              <p>No articles</p>
            </div>
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
import ShimmerBlock from "@/components/ShimmerBlock";
import AdminService from "@/api/AdminService";
import ArticlesService from "@/api/ArticlesService";
import * as accordion from "@/assets/js/accordion";

export default {
  name: "Admin",
  components: {
    ArticleComponent,
    ShimmerBlock,
  },
  data() {
    return {
      loadingUsers: true,
      users: [],
      hasUsersBefore: false,
      hasUsersAfter: false,
      usersLimit: 5,
      usersOffset: 0,

      loadingArticles: true,
      articlesState: 'all',
      articles: [],
      hasArticlesBefore: false,
      hasArticlesAfter: false,
      articlesLimit: 5,
      articlesOffset: 0,

      actions: [
        {
          function: this.editArticle,
          icon: '<img loading="eager" src="https://img.icons8.com/material-outlined/24/000000/edit--v1.png" alt="Edit"/>',
          message: 'Edit'
        },
        {
          function: this.removeArticle,
          icon: '<img loading="eager" src="https://img.icons8.com/material/24/000000/trash--v1.png" alt="Delete"/>',
          message: 'Remove'
        },
      ]
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdmin() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
      }
      return false;
    },
  },
  methods: {
    getPreviousUsersPage: function () {
      this.usersOffset -= this.usersLimit;
      this.getUsersPaginated(this.usersLimit, this.usersOffset);
    },
    getNextUsersPage: function () {
      this.usersOffset += this.usersLimit;
      this.getUsersPaginated(this.usersLimit, this.usersOffset);
    },
    getUsersPaginated: function (limit, offset) {
      this.loadingUsers = true;
      AdminService.getUsersPaginated(limit, offset)
          .then(response => {
            this.users = response.data.entities;
            this.hasUsersBefore = response.data.hasBefore;
            this.hasUsersAfter = response.data.hasAfter;
            this.loadingUsers = false;
          })
          .catch(err => {
            console.log(err);
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
      AdminService.getArticlesPaginated(state, limit, offset)
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

    editUser: function (action, id) {
      AdminService.editUser(action, id)
          .then(response => {
            console.log(response);
            // fetch all users after changing some data
            this.getAllUsers();
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
            this.getAllArticles('all');
          })
          .catch(err => {
            console.log(err);
          });
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
    if (this.isAdmin) {
      this.getUsersPaginated(this.usersLimit, this.usersOffset);
      this.getArticlesPaginated(this.articlesState, this.articlesLimit, this.articlesOffset);
      this.$nextTick(accordion.setupAccordion);
    } else {
      this.$router.push('/error');
    }
  }
}
</script>

<style scoped>
@import "../assets/css/accordion_style.css";

@media (max-width: 720px) {
  table {
    display: block;
    max-width: fit-content;
    margin: 0 auto;
    overflow-x: auto;
    white-space: nowrap;
  }
}

.user-action {
  float: left;
  margin: 1%;
  display: inline;
}

.admin-dashboard-articles {
  padding: 50pt 0 0;
}

.articles-types {
  padding: 5pt 0 20pt 0;
}

.article-type {
  margin: 0 20pt 0 0;
}

</style>