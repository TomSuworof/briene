<template>
  <div>
    <div class="header-main-link">
      <router-link to="/">Main</router-link>
    </div>
    <div class="content">
      <router-view>
      </router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
.header-main-link {
  margin: 10px;
}

.content {
  padding: 3% 10% 0;
}
</style>