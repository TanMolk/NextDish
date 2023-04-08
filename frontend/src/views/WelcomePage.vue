<!--
 @Name AppPage
 @Description The app page
 @author Wei
 @createDate 2023/03/12
-->
<template>
  <div class="welcome-page">
    <video-background
        src="./video/video.mp4"
        style="height: 100vh;"
    >
      <div class="title">
      <span>
      Newcastle Feed your stomach
      </span>
      </div>
      <button
          @click="jumpToApp()"
          class="button"
      >
        Start exploring
      </button>
      <p
          @click="jumpToApp(true)"
          class="never-show"
      >
        never show</p>
    </video-background>
  </div>
</template>

<script>
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";
import MsgBoxUtil from "@/utils/MsgBoxUtil";


export default {
  name: "WelcomePage",
  methods: {
    jumpToApp(neverShow) {
      //show loading
      this.$loading({
        fullscreen: true
      });

      let successCallback = (pos) => {
        //if success, jump to app
        if (neverShow) {
          StorageUtil.set(Constants.STORAGE_IS_USER_FIRST_USER_STAT, "false");
        }

        this.$router.push({path: "/app"});
      };

      let errorCallback = () => {
        MsgBoxUtil.alert(
            "Sorry",
            "If you don’t give this permission, we cannot provide all function for you.",
            "Refresh",
            () => {
              navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
            }
        )
      };

      //ger location permission
      navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
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
  top: 15%;
  left: calc(50% - 10em);

  height: 3em;
  width: 20em;
  text-align: center;

  color: #ffffff;
  font-size: 1.6em;
}

.button {;
  width: 9em;
  height: 2em;

  display: block;
  position: absolute;
  bottom: 15%;
  left: calc(50% - 4.5em);
  background-color: var(--application-normal-background-color);


  border: 0.1em black;
  border-radius: 2em;

  font-size: 20px;
  color: #FFFFFF;
}

.button:hover {
  background-color: #d2c0c0;
}

.button:focus {
  background-color: #b7abab;
}

.never-show {
  color: #FFFFFF;

  position: absolute;
  bottom: 3%;
  right: 3%;
}
</style>