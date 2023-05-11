<!--
 @Name App
 @Description the components for user dialog
 @author Wei Tan
 @createDate 2023/03/12
-->
<script setup>
import UserModal from "@/components/UserModal.vue";
</script>

<template>
  <RouterView></RouterView>
  <UserModal
      ref="userDialog"
  />
</template>

<script>


import UserUtil from "@/utils/UserUtil";

export default {
  mounted() {
    UserUtil.setUserDialog(this.$refs.userDialog);
  }
}
</script>

<style scoped>
</style>