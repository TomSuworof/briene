<template>
  <div>
    <div class="header-buttons row">
      <div class="main-link">
        <router-link to="/">Main</router-link>
      </div>
      <div class="header-buttons-right row">
        <div class="header-button-editor">
          <router-link to="/article_editor">New article</router-link>
        </div>
        <div class="header-button-profile">
          <router-link to="/profile">Profile</router-link>
        </div>
      </div>
    </div>
    <div class="content">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
.header-buttons {
  margin: 10pt;
  justify-content: space-between;
}

.header-button-editor, .header-button-profile {
  margin: 0 10pt 0;
}

.content {
  padding: 3% 10% 0;
}
</style>