<template>
  <button
      style="border: none;background: transparent;"
      @click="this.openState = true"
  >
    <el-image
        src="/favorite.png"
    />
    <el-drawer
        size="85%"
        v-model="openState"
        title="Favorite"
        direction="ltr"
    >
      <div>
        <ul
            style="padding: 0;margin-top: 0;"
        >
          <li
              style="list-style: none;margin-top: 1px;"
              v-for="item in items"
          >
            <CuisineListItem
                :info="item"
            />
          </li>
        </ul>
      </div>
    </el-drawer>
  </button>
</template>

<script>
import CuisineListItem from "@/components/CuisineListItem.vue";

export default {
  name: "FavoriteCuisine",
  components: {CuisineListItem},
  methods: {},
  data() {
    return {
      openState: false,
      items:[]

    }
  }
}
</script>

<style scoped>

</style>