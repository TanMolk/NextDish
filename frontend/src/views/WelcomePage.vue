<!--
 @Name AppPage
 @Description The app page
 @author Wei
 @createDate 2023/03/12
-->
<template>
  <div class="welcome-page">
    <div class="title">
      <span>
      Newcastle Feed your stomach
      </span>
    </div>
    <button @click="jumpToApp()"
            class="button">
      Start exploring
    </button>
  </div>
</template>

<script>
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";

export default {
  name: "WelcomePage",
  methods: {
    jumpToApp() {
      //change router
      this.$router.push({path: "/app"});
    }
  },
  beforeCreate() {
    //check if the first time
    if (StorageUtil.get(Constants.STORAGE_IS_USER_FIRST_USER_STAT)) {
      this.$router.push({path: "/app"});
      return;
    }

    //change background
    document.querySelector("body").setAttribute('style', 'background:#a0aab6')
  }
}
</script>

<style scoped>
.welcome-page {

}

.title {

  position: absolute;
  top: 30%;
  left: calc(50% - 10em);

  line-height: 3em;
  height: 3em;
  width: 20em;
  background-color: #d7dbdf;
  border-radius: 2em;
  text-align: center;

  font-weight: bolder;
}

.button {;
  width: 10em;
  height: 2em;

  position: absolute;
  bottom: 20%;
  left: calc(50% - 5em);

  display: block;

  border: 0.1em black;
  border-radius: 1em;

  font-weight: bolder;
}

.button:hover {
  background-color: #d2c0c0;
}

.button:focus {
  background-color: #b7abab;
}
</style>