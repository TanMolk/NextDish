<template>
  <div
      class="li-wrapper"
  >
    <div class="header">
      <span class="title">more details</span>
      <button
          class="exit"
          @click="clickX()"
      >X
      </button>
    </div>

    <div class="list-wrapper">
      <InfiniteList
          v-show="infoShowState"
          :width="'100%'"
          :height="'100%'"
          :data="items"
          :itemSize="100"
          v-slot="{item,index}"
      >
        <CuisineListItem
            @cuisine-item-clicked="changeInfoWindow"
        />
      </InfiniteList>
      <CuisineDetail
          class="cuisine-detail"
          v-show="!infoShowState"
      />
    </div>
  </div>
</template>

<script>
import InfiniteList from 'vue3-infinite-list';
import CuisineListItem from "@/components/CuisineListItem.vue";
import CuisineDetail from "@/components/CuisineDetail.vue";

export default {
  name: "CuisineList",
  components: {CuisineDetail, CuisineListItem, InfiniteList},
  data() {
    return {
      infoShowState: true,
      items: [{
        'title': '无缝滚动第一行无缝滚动第一行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第二行无缝滚动第二行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第三行无缝滚动第三行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第四行无缝滚动第四行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第五行无缝滚动第五行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第六行无缝滚动第六行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第七行无缝滚动第七行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第八行无缝滚动第八行',
        'date': '2017-12-16'
      }, {
        'title': '无缝滚动第九行无缝滚动第九行',
        'date': '2017-12-16'
      }]
    }
  },
  methods: {
    clickX() {
      this.$emit("click-X")
    },
    changeInfoWindow() {
      this.infoShowState = !this.infoShowState;
    }
  }
}
</script>

<style scoped>
.cuisine-detail{
  max-width: 100%;
}

.li-wrapper {
  display: grid;
  grid-template-rows: 10% 90%;


  position: absolute;
  width: 100vw;
  height: 50vh;
  left: calc(50% - 50vw);
  bottom: 0;

  background-color: #9faab8;
  border-radius: 2em 2em 0 0;

  text-align: center;
}

.header {
  grid-row: 1;

  line-height: 5vh;
}

.list-wrapper {
  grid-row: 2;
}

.title {
  margin: 20px auto;

  font-size: 1.5em;
  font-weight: bolder;
}

.exit {
  position: absolute;
  right: 5%;
  top: 2.5%;

  border: none;

  background-color: transparent;
}

@media screen and (min-width: 480px) {
  .li-wrapper {
    width: 480px;

    left: calc(50% - 240px);
  }
}
</style>