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
        style="height: var(--doc-height);"
    >
      <div class="title"
           @click="clickTitle"
      >
      <span>
      Newcastle Feed your stomach
      </span>
      </div>
      <button
          @click="login()"
          class="button"
          style="margin-bottom: 3em"
      >
        Login
      </button>

      <button
          @click="jumpToApp()"
          class="button"
      >
        Start exploring
      </button>

      <p
          @click="jumpToApp(true)"
          class="right-bottom-button"
      >
        never show</p>
    </video-background>
  </div>
</template>

<script>
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";
import MsgBoxUtil from "@/utils/MsgBoxUtil";
import UserModal from "@/components/UserModal.vue";
import UserUtil from "@/utils/UserUtil";


export default {
  name: "WelcomePage",
  components: {UserModal},
  data() {
    return {
      clickTime: 0,
    }
  },
  methods: {
    jumpToApp(neverShow) {
      //show loading
      this.$loading({
        fullscreen: true
      });

      let successCallback = (pos) => {
        //if success, jump to app
        if (neverShow) {
          StorageUtil.set(Constants.STORAGE_IF_NEVER_SHOW, "true");
        }

        this.$router.push({path: "/app"});
      };

      let errorCallback = () => {
        MsgBoxUtil.alert(
            "Sorry",
            "If you don’t give this permission, we cannot provide all function for you.",
            "Refresh",
            () => {

              let newHref = location.href

              if (!this.$route.query['permission']) {
                newHref += "?" + "permission=true"
              }

              if (neverShow) {
                if (!this.$route.query['neverShow']) {
                  newHref += "&neverShow=true"
                }
              }

              location.href = newHref;
            }
        )
      };

      //get location permission
      navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
    },
    clickTitle() {
      this.clickTime++;
      if (this.clickTime === 3) {
        new window.VConsole();
      }
    },
    login() {
      UserUtil.show();
    }
  },
  beforeMount() {
    //check if the first time
    if (StorageUtil.get(Constants.STORAGE_IF_NEVER_SHOW)) {
      this.$router.push({path: "/app"});
      return;
    }

    if (this.$route.query['permission']) {
      this.jumpToApp(this.$route.query['neverShow']);
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
</style>