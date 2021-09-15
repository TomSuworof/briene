<template>
  <div class="admin-dashboard">
    <div class="admin-dashboard-users">
      <div class="admin-dashboard-title">
        <h1>Users</h1>
      </div>
      <table class="admin-dashboard-table">
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
                <button @click="editUser('delete', user.id)" type="submit" class="simple-red-button">Delete (block)</button>
              </div>
              <div class="user-action">
                <button @click="editUser('make_admin', user.id)" type="submit" class="simple-red-button">Make admin</button>
              </div>
              <div class="user-action">
                <button @click="editUser('make_user', user.id)" type="submit" class="simple-red-button">Make user</button>
              </div>
            </div>
          </td>
        </tr>
      </table>
    </div>
    <div class="admin-dashboard-articles">
      <div>
        <h2>Articles</h2>
      </div>
      <div class="articles-types">
        <a href="#" class="article-type" @click="getAllArticles('all')">All</a>
        <a href="#" class="article-type" @click="getAllArticles('published')">Published</a>
        <a href="#" class="article-type" @click="getAllArticles('drafts')">Drafts</a>
      </div>
      <div v-if="articles.length > 0">
        <div class="article-container" v-for="article in articles" v-bind:key="article.id">
          <div class="article-about row">
            <div class="article-author">
              <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
            </div>
            <div class="article-publication-date">
              <p>{{ getFinePublicationDate(article) }}</p>
            </div>
          </div>
          <div class="article-title-container">
            <h3 class="article-title">
              <router-link v-bind:to="'/articles/' + article.id">{{ article.title }}</router-link>
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
          <div class="article-summary">
            <p>{{ article.summary }}</p>
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
import AdminService from "@/services/AdminService";
import ArticlesService from "@/services/ArticlesService";
import moment from "moment";

export default {
  name: "Admin",
  data() {
    return {
      users: [],
      articles: []
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
            this.articles = response.data;
          })
          .catch(err => {
            console.log(err);
            this.articles = [];
          });
    },
    getFinePublicationDate: function(article) {
      return moment(Date.parse(article.publicationDate)).format("DD.MM.YYYY HH:mm")
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
  }
}
</script>

<style scoped>
.admin-dashboard-table {
  align-self: center;
  border-collapse: collapse;
  width: 100%;
}

.user-row {
  border-top: thin solid;
  padding: 2% 0 0;
}

.user-column {
  text-align: center;
  border-right: thin solid;
}

.user-column-actions {
  text-align: center;
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

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-container {
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 10px;
  margin: 0 0 10pt;
  padding: 10pt;
}

.article-about {
  padding: 0 15pt 0;
  justify-content: space-between;
}

.article-author, .article-publication-date {
  display: inline-block;
}

.article-publication-date {
  color: #999;
}

.article-type {
  margin: 0 20pt 0 0;
}

.article-action-container, .article-title-container, .article-action-edit, .article-action-remove {
  display: inline-block;
  margin: 0 20pt 0 0;
}

.article-container {
  margin: 0 0 10pt;
}
</style>