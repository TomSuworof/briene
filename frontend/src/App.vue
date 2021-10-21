<template>
  <div class="whole-page">
    <div class="header-buttons row">
      <div class="main-link">
        <router-link to="/">Main</router-link>
      </div>
      <div class="header-buttons-right">
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
.whole-page {
  background: #FAFAFA;
}

.row {
  width: 100%;
}

.header-buttons {
  padding: 10pt;
  justify-content: space-between;
}

.main-link, .header-button-editor, .header-button-profile {
  display: inline-block;
  margin: 0 10pt 0;
}

.content {
  padding: 3% 10% 0;
}
</style>