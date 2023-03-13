<template>
  <div>
    <nut-tabs
        v-model="selectedTab"
        :background="'#9faab8'"
    >
      <template v-slot:titles>
        <div
            class="nut-tabs__titles-item tab-class"
            v-for="item in items"
            :key="item"
            @click="selectedTab = item"
        >{{ item }}
        </div>
      </template>
      <nut-tab-pane
          :pane-key="items[0]"
      >
        <div>
          {{ detail?.title }}
          <br>
          <br>
          Distance
          <br>
          <br>
          <button>Start Go</button>
        </div>
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[1]"
      >
        <div>
          <div style="background-color: #43bbad;width: 140px;height: 140px;margin: 33px"></div>
          <div style="background-color: #43bbad;width: 140px;height: 140px;margin: 33px"></div>
        </div>
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[2]"
      >
        123
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[3]"
      >
        <div>
          123
        </div>
      </nut-tab-pane>
    </nut-tabs>
  </div>
</template>

<script>

export default {
  name: "CuisineDetail",
  props: {
    detail: Object
  },
  data() {
    return {
      items: ['Location', 'Menu', 'Open Time', 'Reviews'],
      selectedTab: "Location"
    }
  },
}
</script>
<style scoped>
.tab-class {
  background-color: white;
  border-radius: 1em;

  margin: 5px 5px;
}
</style>