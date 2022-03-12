<template>
  <div class="admin-dashboard">
    <div class="admin-dashboard-users accordion">
      <div class="admin-dashboard-title accordion__intro">
        <h1>Users</h1>
        <hr/>
      </div>
      <div class="accordion__content">
        <table class="admin-dashboard-table table">
          <tr>
            <th class="user-column">ID</th>
            <th class="user-column">Username</th>
            <th class="user-column">Email</th>
            <th class="user-column">Roles</th>
            <th class="user-column-actions">Actions</th>
          </tr>
          <tr class="user-row" v-for="user in users"  v-bind:key="user.id">
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
                  <button @click="editUser('make_admin', user.id)" class="btn btn-primary btn-block">Make admin</button>
                </div>
                <div class="user-action" v-show="user.roles.map(role => role.name).includes('ROLE_USER') ||
                    user.roles.map(role => role.name).includes('ROLE_ADMIN')">
                  <button @click="editUser('delete', user.id)" class="btn btn-danger btn-block">Delete (block)</button>
                </div>
                <div class="user-action" v-show="user.roles.map(role => role.name).includes('ROLE_BLOCKED')">
                  <button @click="editUser('make_user', user.id)" class="btn btn-success btn-block">Make user</button>
                </div>
              </div>
            </td>
          </tr>
        </table>
      </div>
    </div>
    <div class="admin-dashboard-articles accordion">
      <div class="accordion__intro">
        <h1>Articles</h1>
        <hr/>
      </div>
      <div class="accordion__content">
        <div class="articles-types">
          <a href="#" class="article-type" @click="getAllArticles('all')">All</a>
          <a href="#" class="article-type" @click="getAllArticles('published')">Published</a>
          <a href="#" class="article-type" @click="getAllArticles('drafts')">Drafts</a>
        </div>
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
  </div>
</template>

<script>
import ArticleComponent from "@/components/ArticleComponent";
import AdminService from "@/api/AdminService";
import ArticlesService from "@/api/ArticlesService";
import * as accordion from "@/assets/js/accordion";

export default {
  name: "Admin",
  components: {
    ArticleComponent
  },
  data() {
    return {
      users: [],
      articles: [],
      actions: [
        { function: this.editArticle, icon: '🖊', message: 'Edit' },
        { function: this.removeArticle, icon: '❌', message: 'Remove' },
      ]
    };
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
    },
  },
  methods: {
    getAllUsers: function () {
      AdminService.getAllUsers()
          .then(response => {
            this.users = response.data;
          }).catch(err => {
            console.log(err);
            this.users = [];
      });
    },
    getAllArticles: function(state) {
      AdminService.getAllArticles(state)
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
    editUser: function(action, id) {
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
  },
  created() {
    if (this.isAdmin) {
      this.getAllUsers();
      this.getAllArticles('all');
    } else {
      this.$router.push('/error');
    }
    this.$nextTick(accordion.setupAccordion);
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
  padding: 5pt 0;
}

.article-type {
  margin: 0 20pt 0 0;
}
</style>