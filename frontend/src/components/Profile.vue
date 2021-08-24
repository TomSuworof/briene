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
          <a href="" @click="logout">Logout</a>
        </div>
      </div>
    </div>
    <div>
      <p>
        <strong>Token:</strong>
        {{currentUser.accessToken}}
      </p>
      <p>
        <strong>Id:</strong>
        {{currentUser.id}}
      </p>
      <p>
        <strong>Email:</strong>
        {{currentUser.email}}
      </p>
      <strong>Authorities:</strong>
      <ul>
        <li v-for="role in currentUser.roles" :key="role">{{ role }}</li>
      </ul>
    </div>
<!--    <div class="updating-user-info">-->
<!--      <div class="updating-base-info">-->
<!--        <div>-->
<!--          <h2>Base information</h2>-->
<!--        </div>-->
<!--        <form method="post" modelAttribute="userForm" action="/personal_area/update_base_info">-->
<!--          <div>-->
<!--            <p>Username:</p>-->
<!--            <label>-->
<!--              <input type="text" name="username" th:value="${currentUser.getUsername()}" placeholder="Username" disabled>-->
<!--            </label>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p>Email:</p>-->
<!--            <label>-->
<!--              <input type="email" name="email" th:value="${currentUser.getEmail()}" placeholder="Email">-->
<!--            </label>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p>Enter current password to submit changes:</p>-->
<!--            <div>-->
<!--              <label>-->
<!--                <input type="password" name="password" placeholder="Current password">-->
<!--              </label>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <button type="submit">Submit</button>-->
<!--          </div>-->
<!--        </form>-->
<!--      </div>-->
<!--      <div class="updating-password">-->
<!--        <div>-->
<!--          <h2>Updating password</h2>-->
<!--        </div>-->
<!--        <form method="post" action="/personal_area/update_password">-->
<!--          <div>-->
<!--            <p>Enter old password:</p>-->
<!--            <div>-->
<!--              <label>-->
<!--                <input type="password" name="passwordOld" placeholder="Current password">-->
<!--              </label>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p>New password:</p>-->
<!--          </div>-->
<!--          <div class="setting-password">-->
<!--            <div>-->
<!--              <label>-->
<!--                <input id="psw" type="password" name="passwordNew" placeholder="New password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters">-->
<!--              </label>-->
<!--            </div>-->
<!--            <div class="container-for-message">-->
<!--              <div id="message" class="message">-->
<!--                <p class="must-contain">Password must contain the following:</p>-->
<!--                <p id="letter" class="invalid">A <b>lowercase</b> letter</p>-->
<!--                <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>-->
<!--                <p id="number" class="invalid">A <b>number</b></p>-->
<!--                <p id="length" class="invalid">Minimum <b>8 characters</b></p>-->
<!--              </div>-->
<!--            </div>-->
<!--            <div>-->
<!--              <label>-->
<!--                <input id="pswConfirm" type="password" name="passwordNewConfirm" placeholder="Confirm your new password" required>-->
<!--              </label>-->
<!--              <div id="password-matching" class="password-matching">-->
<!--                <p id="match" class="invalid">Passwords match</p>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <button type="submit" onclick="return checkFields()">Submit</button>-->
<!--          </div>-->
<!--        </form>-->
<!--      </div>-->
<!--    </div>-->
    <div class="articles-bookmarks">
      <div>
        <h2>Bookmarks</h2>
      </div>
      <div class="article-container" v-for="article in articles" v-bind:key="article.id">
        <h3 class="article-title">
          <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
        </h3>
      </div>
    </div>
<!--    <div class="articles-block">-->
<!--      <div>-->
<!--        <h2>Articles</h2>-->
<!--      </div>-->
<!--      <div class="reminder">-->
<!--        <p th:text="${articlesError}"></p>-->
<!--      </div>-->
<!--      <div class="articles-types">-->
<!--        <a href="/personal_area/articles?type=all">All</a>-->
<!--        <a href="/personal_area/articles?type=published">Published</a>-->
<!--        <a href="/personal_area/articles?type=drafts">Drafts</a>-->
<!--      </div>-->
<!--      <div th:each="article : ${articles}" class="article-container">-->
<!--        <a th:href="'/articles/' + ${article.getId()}"><h3 th:text="${article.getTitle()}" class="article-title"></h3></a>-->
<!--        <div class="article-action-buttons">-->
<!--          <p th:text="${article.getState()}"></p>-->
<!--          <a th:href="'/article_editor/' + ${article.getId()}">Edit</a>-->
<!--          <a th:href="'/personal_area/articles/' + ${article.getId()} + '/delete'">Delete</a>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
  </div>
</template>

<script>
export default {
  name: 'Profile',
  data() {
    return {
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
    logout() {
      this.$store.dispatch("auth/logout");
      this.$router.push('/login');
    }
  },
  beforeMount() {
    if (this.currentUser === null) {
      this.$router.push({ path: '/login' });
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

.updating-base-info {
  width: max-content;
  text-align: left;
  margin: 0 0 50pt;
}

.updating-password, .setting-password {
  width: max-content;
  text-align: left;
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

.article-action-buttons {
  display: inline-block;
}
</style>